package psp.payment.bitcoin.service.opennode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import psp.payment.bitcoin.client.OpenNodeClient;
import psp.payment.bitcoin.client.PaymentRequestClient;
import psp.payment.bitcoin.dto.*;
import psp.payment.bitcoin.exceptions.AlreadyProcessedException;
import psp.payment.bitcoin.exceptions.NotFoundException;
import psp.payment.bitcoin.service.PaymentService;
import psp.payment.bitcoin.service.SubscriptionService;
import psp.payment.bitcoin.service.TransactionService;

@Service
public class OpenNodePaymentService implements PaymentService {

    @Autowired
    private OpenNodeClient openNodeClient;
    @Autowired
    private PaymentRequestClient paymentRequestClient;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private TransactionService transactionService;
    @Value("${service.opennode.checkout}")
    private String openNodeCheckoutUrl;
    @Value("${service.ngrok.url}")
    private String callbackUrl;

    @Override
    public String createCharge(PaymentRequestDTO request) throws NotFoundException {
        try {
            if (paymentRequestClient.isProcessed(request.getId())) {
                throw new AlreadyProcessedException("Payment request has already been processed");
            }
            if (transactionService.exists(request.getId())) {
                throw new AlreadyProcessedException("Payment request is already being processed");
            }
        } catch (Exception e) {
            throw new NotFoundException("Request not found");
        }
        OpenNodeRequestDTO onRequest = new OpenNodeRequestDTO(
                request.getAmount(),
                request.getCurrency().toString(),
                Long.toString(request.getMerchantOrderId()),
                callbackUrl + "bitcoin/payment/status/" + request.getId(),
                request.getSuccessUrl()
        );
        String apiKey = subscriptionService.getApiKey(request.getStoreId());
        OpenNodeResponseDTO response = openNodeClient.createCharge(onRequest, apiKey);
        return openNodeCheckoutUrl + response.getData().getId();
    }

    @Override
    public void processChargeStatus(ChargeStatusDTO chargeStatus, long requestId) {
        transactionService.save(requestId, Long.parseLong(chargeStatus.getOrder_id()), chargeStatus.getStatus());
        PaymentStatusDTO status = convertFromOpenNodeStatus(chargeStatus.getStatus());
        if (status != null) {
            PaymentOutcomeDTO outcome = new PaymentOutcomeDTO(status, null);
            notifyPaymentRequestService(requestId, outcome);
        }
    }

    @Override
    public boolean isEnabledForRequest(long requestId, long storeId) {
        try {
            if (paymentRequestClient.isProcessed(requestId)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return subscriptionService.exists(storeId) && !transactionService.exists(requestId);
    }

    private PaymentStatusDTO convertFromOpenNodeStatus(String openNodeStatus) {
        PaymentStatusDTO status = null;
        switch (openNodeStatus) {
            case "paid": {
                status = PaymentStatusDTO.SUCCESS;
                break;
            }
            case "expired": {
                status = PaymentStatusDTO.ERROR;
                break;
            }
            case "refunded": {
                status = PaymentStatusDTO.FAILURE;
                break;
            }
        }
        return status;
    }

    private void notifyPaymentRequestService(long requestId, PaymentOutcomeDTO outcome) {
        paymentRequestClient.setPaymentRequestOutcome(requestId, outcome);
    }
}
