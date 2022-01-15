package psp.payment.paypal.service.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import psp.payment.paypal.client.PaymentRequestClient;
import psp.payment.paypal.dto.MerchantSubscriptionDTO;
import psp.payment.paypal.dto.PaymentOutcomeDTO;
import psp.payment.paypal.dto.PaymentRequestDTO;
import psp.payment.paypal.dto.PaymentStatusDTO;
import psp.payment.paypal.exceptions.AlreadyProcessedException;
import psp.payment.paypal.exceptions.NotFoundException;
import psp.payment.paypal.model.PayPalTransaction;
import psp.payment.paypal.service.MerchantSubscriptionService;
import psp.payment.paypal.service.PaymentService;
import psp.payment.paypal.service.TransactionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class PayPalPaymentService implements PaymentService {

    @Autowired
    private MerchantSubscriptionService subscriptionService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PaymentRequestClient paymentRequestClient;
    @Value("${service.front.url}")
    private String executePaymentUrl;


    @Override
    public String pay(PaymentRequestDTO request) throws NotFoundException, AlreadyProcessedException, PayPalRESTException {
        MerchantSubscriptionDTO subscription = subscriptionService.get(request.getStoreId());
        if (subscription == null) {
            log.warn("PayPal payment attempt for request (id=" + request.getId() + ") and store (id=" + request.getStoreId() + "). Subscription could not be found");
            throw new NotFoundException("Subscription to PayPal services could not be found");
        }
        if (paymentRequestClient.isProcessed(request.getId())) {
            log.warn("PayPal payment attempt for request (id=" + request.getId() + ") and store (id=" + request.getStoreId() + "). Request already processed");
            throw new AlreadyProcessedException("Payment request has already been processed");
        }
        System.out.println(subscription.getClientId() + "   " + subscription.getClientSecret());
        APIContext context = new APIContext(subscription.getClientId(), subscription.getClientSecret(), "sandbox");
        System.out.println(subscription.getClientId() + "   " + subscription.getClientSecret());
        return pay(request, context);
    }

    @Override
    public String executePayment(String paypalPaymentId, String payerId) throws NotFoundException {
        PayPalTransaction transaction = transactionService.findByPayPalPaymentId(paypalPaymentId);
        if (transaction == null) {
            throw new NotFoundException("Transaction could not be found");
        }
        System.out.println(payerId);
        System.out.println(paypalPaymentId);
        PaymentRequestDTO request = paymentRequestClient.getById(transaction.getRequestId());
        MerchantSubscriptionDTO subscription = subscriptionService.get(request.getStoreId());
        Payment payment = new Payment();
        payment.setId(paypalPaymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return executePayment(payment, paymentExecution, request, subscription, transaction);
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
        return subscriptionService.isSubscribed(storeId);
    }

    private String pay(PaymentRequestDTO request, APIContext context) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(request.getCurrency().toString());
        amount.setTotal(String.format("%.2f", request.getAmount()));
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(request.getFailureUrl());
        redirectUrls.setReturnUrl(executePaymentUrl);
        payment.setRedirectUrls(redirectUrls);
        payment = payment.create(context);
        transactionService.save(request.getId(), payment.getId(), payment.getState());
        return getRedirectUrl(payment);
    }

    private String getRedirectUrl(Payment payment) {
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return link.getHref();
            }
        }
        return null;
    }

    private String executePayment(Payment payment, PaymentExecution paymentExecution, PaymentRequestDTO request, MerchantSubscriptionDTO subscription, PayPalTransaction transaction) {
        OAuthTokenCredential oAuthTokenCredential = new OAuthTokenCredential(
                subscription.getClientId(),
                subscription.getClientSecret(),
                getConfigMap()
                );
        boolean successful = true;
        try {
            APIContext context = new APIContext(oAuthTokenCredential.getAccessToken());
            context.setMode("sandbox");
            payment = payment.execute(context, paymentExecution);
            if (!payment.getState().equals("approved")) {
                successful = false;
            }
            transaction = transactionService.save(transaction.getRequestId(), transaction.getPaypalPaymentId(), payment.getState());
        } catch (Exception e) {
            successful = false;
        }
        if (successful) {
            paymentSuccessful(transaction);
            return request.getSuccessUrl();
        }
        paymentFailed(transaction, payment.getFailureReason());
        return request.getFailureUrl();
    }

    private void paymentSuccessful(PayPalTransaction transaction) {
        PaymentOutcomeDTO outcome = new PaymentOutcomeDTO(PaymentStatusDTO.SUCCESS, null);
        notifyPaymentRequestService(transaction.getRequestId(), outcome);
    }
    private void paymentFailed(PayPalTransaction transaction, String errorMessage) {
        PaymentOutcomeDTO outcome = new PaymentOutcomeDTO(PaymentStatusDTO.FAILURE, errorMessage);
        notifyPaymentRequestService(transaction.getRequestId(), outcome);
    }

    private void notifyPaymentRequestService(long requestId, PaymentOutcomeDTO outcome) {
        paymentRequestClient.setPaymentRequestOutcome(requestId, outcome);
    }

    private HashMap<String, String> getConfigMap() {
        HashMap<String, String> config = new HashMap<>();
        config.put("mode", "sandbox");
        return config;
    }
}
