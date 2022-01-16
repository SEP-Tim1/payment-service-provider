package psp.payment.paypal.service.paypal;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
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
import psp.payment.paypal.model.PayPalAgreement;
import psp.payment.paypal.model.PayPalTransaction;
import psp.payment.paypal.service.AgreementService;
import psp.payment.paypal.service.MerchantSubscriptionService;
import psp.payment.paypal.service.PaymentService;
import psp.payment.paypal.service.TransactionService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class PayPalPaymentService implements PaymentService {

    @Autowired
    private MerchantSubscriptionService subscriptionService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AgreementService agreementService;
    @Autowired
    private PaymentRequestClient paymentRequestClient;
    @Value("${service.front.url}")
    private String frontUrl;


    @Override
    public String pay(PaymentRequestDTO request) throws NotFoundException, AlreadyProcessedException {
        MerchantSubscriptionDTO subscription = subscriptionService.get(request.getStoreId());
        if (subscription == null) {
            log.warn("PayPal payment attempt for request (id=" + request.getId() + ") and store (id=" + request.getStoreId() + "). Subscription could not be found");
            throw new NotFoundException("Subscription to PayPal services could not be found");
        }
        if (paymentRequestClient.isProcessed(request.getId())) {
            log.warn("PayPal payment attempt for request (id=" + request.getId() + ") and store (id=" + request.getStoreId() + "). Request already processed");
            throw new AlreadyProcessedException("Payment request has already been processed");
        }
        APIContext context = new APIContext(subscription.getClientId(), subscription.getClientSecret(), "sandbox");
        if (request.getBillingCycle().equals("ONE_TIME")) {
            try {
                return pay(request, context);
            } catch (PayPalRESTException e) {
                paymentFailed(request.getId(), "Payment could not be created");
                return request.getErrorUrl();
            }
        }
        return paySubscription(request, context);
    }

    @Override
    public String executePayment(String paypalPaymentId, String payerId) throws NotFoundException {
        PayPalTransaction transaction = transactionService.findByPayPalPaymentId(paypalPaymentId);
        if (transaction == null) {
            throw new NotFoundException("Transaction could not be found");
        }
        PaymentRequestDTO request = paymentRequestClient.getById(transaction.getRequestId());
        MerchantSubscriptionDTO subscription = subscriptionService.get(request.getStoreId());
        Payment payment = new Payment();
        payment.setId(paypalPaymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return executePayment(payment, paymentExecution, request, subscription, transaction);
    }

    @Override
    public String cancelPayment(PaymentRequestDTO request) throws NotFoundException {
        PayPalTransaction transaction = transactionService.findByRequestId(request.getId());
        if (transaction == null) {
            throw new NotFoundException("Transaction could not be found");
        }
        transaction.setStatus("cancelled");
        transactionService.save(transaction.getRequestId(), transaction.getPaypalPaymentId(), transaction.getStatus());
        paymentFailed(request.getId(), "cancelled");
        return request.getFailureUrl();
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

    @Override
    public String executeAgreement(PaymentRequestDTO request, String token) {
        PayPalAgreement a = agreementService.findByRequestId(request.getId());
        if (a == null) {
            paymentFailed(request.getId(), "Agreement could not be found");
            return request.getErrorUrl();
        }
        Agreement agreement =  new Agreement();
        MerchantSubscriptionDTO subscription = subscriptionService.get(request.getStoreId());
        APIContext context = new APIContext(subscription.getClientId(), subscription.getClientSecret(), "sandbox");
        try {
            agreement = agreement.execute(context, token);
            a.setStatus(agreement.getState());
            agreementService.save(a);
            paymentSuccessful(request.getId());
            return request.getSuccessUrl();
        }
        catch (PayPalRESTException e) {
            a.setStatus("failed");
            agreementService.save(a);
            paymentFailed(request.getId(), "Agreement could not be executed");
            return request.getFailureUrl();
        }
    }

    @Override
    public String cancelAgreement(PaymentRequestDTO request) throws NotFoundException {
        PayPalAgreement agreement = agreementService.findByRequestId(request.getId());
        if (agreement == null) {
            throw new NotFoundException("Agreement could not be found");
        }
        agreement.setStatus("cancelled");
        agreementService.save(agreement);
        paymentFailed(request.getId(), "cancelled");
        return request.getFailureUrl();
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
        redirectUrls.setCancelUrl(frontUrl + "paypal/cancel/" + request.getId());
        redirectUrls.setReturnUrl(frontUrl + "paypal/execute");
        payment.setRedirectUrls(redirectUrls);
        payment = payment.create(context);
        transactionService.save(request.getId(), payment.getId(), payment.getState());
        return getRedirectUrl(payment.getLinks());
    }

    private String paySubscription(PaymentRequestDTO request, APIContext context) {
        try {
            String billingPlanId = createBillingPlan(request, context);
            if (billingPlanId != null) {
                String redirectUrl = createBillingAgreement(billingPlanId, context, request);
                if (redirectUrl == null) {
                    paymentFailed(request.getId(), "Billing agreement could not be created");
                    return request.getErrorUrl();
                }
                return redirectUrl;
            }
            paymentFailed(request.getId(), "Billing agreement could not be created");
            return request.getErrorUrl();
        } catch (PayPalRESTException e) {
            paymentFailed(request.getId(), "Billing agreement could not be created");
            return request.getErrorUrl();
        }
    }

    private String createBillingPlan(PaymentRequestDTO request, APIContext context) throws PayPalRESTException {
        Currency currency = new Currency();
        currency.setCurrency(request.getCurrency().toString());

        double price = request.getAmount();
        price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        currency.setValue(String.valueOf(price));

        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setType("REGULAR");
        paymentDefinition.setFrequency(request.getBillingCycle());
        paymentDefinition.setFrequencyInterval("1");
        paymentDefinition.setCycles("0");
        paymentDefinition.setAmount(currency);
        paymentDefinition.setName("payment definition name");
        List<PaymentDefinition> paymentDefinitionList = List.of(paymentDefinition);

        MerchantPreferences merchantPreferences = new MerchantPreferences(
                frontUrl + "paypal/cancel/sub/" + request.getId(),
                frontUrl + "paypal/execute/sub/" + request.getId());
        merchantPreferences.setAutoBillAmount("YES");
        merchantPreferences.setInitialFailAmountAction("CONTINUE");

        Plan plan = new Plan();
        plan.setType("INFINITE");
        plan.setState("ACTIVE");
        plan.setPaymentDefinitions(paymentDefinitionList);
        plan.setMerchantPreferences(merchantPreferences);
        plan.setName("some name");
        plan.setDescription("some description");

        plan = plan.create(context);

        List<Patch> patches = new ArrayList<>();
        Map<String, String> value = new HashMap<>();
        value.put("state", "ACTIVE");
        Patch patch = new Patch();
        patch.setPath("/");
        patch.setValue(value);
        patch.setOp("replace");
        patches.add(patch);
        plan.update(context, patches);

        return plan.getId();
    }

    private String createBillingAgreement(String billingPlanId, APIContext context, PaymentRequestDTO request) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, 2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = sdf.format(c.getTime());

        Plan plan = new Plan();
        plan.setId(billingPlanId);
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        Agreement agreement = new Agreement();
        agreement.setStartDate(formattedDate);
        agreement.setPlan(plan);
        agreement.setPayer(payer);
        agreement.setName("name");
        agreement.setDescription("description");

        try {
            agreement = agreement.create(context);
            if(agreement != null) {
                PayPalAgreement a = new PayPalAgreement(request.getId(), billingPlanId, agreement.getId(), agreement.getState());
                agreementService.save(a);
                return getRedirectUrl(agreement.getLinks());
            }
            return null;
        } catch (MalformedURLException | UnsupportedEncodingException | PayPalRESTException e) {
            return null;
        }
    }

    private String getRedirectUrl(List<Links> links) {
        for (Links link : links) {
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
            paymentSuccessful(transaction.getRequestId());
            return request.getSuccessUrl();
        }
        paymentFailed(transaction.getRequestId(), payment.getFailureReason());
        return request.getFailureUrl();
    }

    private void paymentSuccessful(long requestId) {
        PaymentOutcomeDTO outcome = new PaymentOutcomeDTO(PaymentStatusDTO.SUCCESS, null);
        notifyPaymentRequestService(requestId, outcome);
    }
    private void paymentFailed(long requestId, String errorMessage) {
        PaymentOutcomeDTO outcome = new PaymentOutcomeDTO(PaymentStatusDTO.FAILURE, errorMessage);
        notifyPaymentRequestService(requestId, outcome);
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
