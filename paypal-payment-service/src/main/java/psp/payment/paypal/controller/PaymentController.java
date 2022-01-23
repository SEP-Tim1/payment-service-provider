package psp.payment.paypal.controller;

import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import psp.payment.paypal.client.PaymentRequestClient;
import psp.payment.paypal.dto.PaymentRequestDTO;
import psp.payment.paypal.exceptions.AlreadyProcessedException;
import psp.payment.paypal.exceptions.NotFoundException;
import psp.payment.paypal.service.PaymentService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    private PaymentService service;
    @Autowired
    private PaymentRequestClient paymentRequestClient;

    @PostMapping("{requestId}")
    public String pay(HttpServletRequest r, @PathVariable long requestId) throws NotFoundException, PayPalRESTException, AlreadyProcessedException {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        return service.pay(r, request);
    }

    @PostMapping("execute/{paymentId}/{payerId}")
    public String executePayment(HttpServletRequest r, @PathVariable String paymentId, @PathVariable String payerId) throws NotFoundException {
        return service.executePayment(r, paymentId, payerId);
    }

    @GetMapping("{requestId}")
    public boolean isEnabledForRequest(@PathVariable long requestId) {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        return service.isEnabledForRequest(requestId, request.getStoreId());
    }

    @PostMapping("execute/sub/{requestId}/{token}")
    public String executeAgreement(HttpServletRequest r, @PathVariable long requestId, @PathVariable String token) {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        return service.executeAgreement(r, request, token);
    }

    @PutMapping("cancel/{requestId}")
    public String cancelPayment(HttpServletRequest r, @PathVariable long requestId) throws NotFoundException {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        return service.cancelPayment(r, request);
    }

    @PutMapping("cancel/sub/{requestId}")
    public String cancelAgreement(HttpServletRequest r, @PathVariable long requestId) throws NotFoundException {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        return service.cancelAgreement(r, request);
    }
}
