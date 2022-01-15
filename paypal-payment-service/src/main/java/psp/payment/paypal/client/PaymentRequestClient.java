package psp.payment.paypal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import psp.payment.paypal.dto.PaymentOutcomeDTO;
import psp.payment.paypal.dto.PaymentRequestDTO;

@FeignClient("${service.payment-request.name}")
public interface PaymentRequestClient {

    @GetMapping("request/{id}")
    PaymentRequestDTO getById(@PathVariable long id);

    @PostMapping("request/outcome/{id}")
    void setPaymentRequestOutcome(@PathVariable long id, @RequestBody PaymentOutcomeDTO outcome);

    @GetMapping("request/processed/{id}")
    boolean isProcessed(@PathVariable long id);
}
