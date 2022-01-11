package psp.payment.bitcoin.service.opennode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import psp.payment.bitcoin.client.OpenNodeClient;
import psp.payment.bitcoin.client.PaymentRequestClient;
import psp.payment.bitcoin.dto.*;
import psp.payment.bitcoin.exceptions.NotFoundException;
import psp.payment.bitcoin.service.PaymentService;
import psp.payment.bitcoin.service.SubscriptionService;

@Service
public class OpenNodePaymentService implements PaymentService {

    @Autowired
    private OpenNodeClient openNodeClient;
    @Autowired
    private PaymentRequestClient paymentRequestClient;
    @Autowired
    private SubscriptionService subscriptionService;
    @Value("${opennode.checkout}")
    private String openNodeCheckoutUrl;
    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public String createCharge(PaymentRequestDTO request) throws NotFoundException {
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
        PaymentStatusDTO status = convertFromOpenNodeStatus(chargeStatus.getStatus());
        System.out.println(chargeStatus);
        if (status != null) {
            PaymentOutcomeDTO outcome = new PaymentOutcomeDTO(status, null);
            //TODO: kreiraj transakciju ako je status = success
            notifyPaymentRequestService(requestId, outcome);
        }
    }

    private PaymentStatusDTO convertFromOpenNodeStatus(String openNodeStatus) {
        PaymentStatusDTO status = null;
        switch (openNodeStatus) {
            case "paid":
            case "processing": {
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
