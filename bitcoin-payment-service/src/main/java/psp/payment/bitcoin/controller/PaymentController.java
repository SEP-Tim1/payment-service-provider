package psp.payment.bitcoin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import psp.payment.bitcoin.client.PaymentRequestClient;
import psp.payment.bitcoin.dto.ChargeStatusDTO;
import psp.payment.bitcoin.dto.PaymentRequestDTO;
import psp.payment.bitcoin.exceptions.NotFoundException;
import psp.payment.bitcoin.service.PaymentService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    private PaymentRequestClient paymentRequestClient;
    @Autowired
    private PaymentService paymentService;

    @GetMapping("{requestId}")
    public boolean isEnabledForRequest(@PathVariable long requestId) {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        if (!request.getBillingCycle().equals("ONE_TIME")) {
            return false;
        }
        return paymentService.isEnabledForRequest(requestId, request.getStoreId());
    }

    @PostMapping("{requestId}")
    public String createCharge(HttpServletRequest r, @PathVariable long requestId) throws NotFoundException {
        PaymentRequestDTO request = paymentRequestClient.getById(requestId);
        return paymentService.createCharge(r, request);
    }

    @PostMapping(value = "status/{requestId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void processChargeStatus(HttpServletRequest request, @PathVariable long requestId, ChargeStatusDTO status) {
        //TODO: verify
        paymentService.processChargeStatus(request, status, requestId);
    }
}
