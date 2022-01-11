package psp.payment.bitcoin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psp.payment.bitcoin.client.PaymentRequestClient;
import psp.payment.bitcoin.dto.PaymentRequestDTO;
import psp.payment.bitcoin.service.SubscriptionService;

@RestController
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    private PaymentRequestClient paymentRequestClient;
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("{requestId}")
    public boolean isEnabledForRequest(@PathVariable long requestId) {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        return subscriptionService.exists(request.getStoreId());
    }
}
