package psp.payment.paypal.controller;

import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import psp.payment.paypal.client.PaymentRequestClient;
import psp.payment.paypal.dto.PaymentRequestDTO;
import psp.payment.paypal.exceptions.AlreadyProcessedException;
import psp.payment.paypal.exceptions.NotFoundException;
import psp.payment.paypal.service.PaymentService;

@RestController
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    private PaymentService service;
    @Autowired
    private PaymentRequestClient paymentRequestClient;

    @PostMapping("{requestId}")
    public String pay(@PathVariable long requestId) throws NotFoundException, PayPalRESTException, AlreadyProcessedException {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        return service.pay(request);
    }

    @PostMapping("execute/{paymentId}/{payerId}")
    public String executePayment(@PathVariable String paymentId, @PathVariable String payerId) throws NotFoundException {
        return service.executePayment(paymentId, payerId);
    }

    @GetMapping("{requestId}")
    public boolean isEnabledForRequest(@PathVariable long requestId) {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        return service.isEnabledForRequest(requestId, request.getStoreId());
    }
}
