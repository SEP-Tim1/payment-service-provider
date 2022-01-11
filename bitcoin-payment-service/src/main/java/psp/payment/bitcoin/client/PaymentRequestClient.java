package psp.payment.bitcoin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import psp.payment.bitcoin.dto.PaymentOutcomeDTO;
import psp.payment.bitcoin.dto.PaymentRequestDTO;

@FeignClient("payment-request-service")
public interface PaymentRequestClient {

    @GetMapping("request/{id}")
    PaymentRequestDTO getById(@PathVariable long id);

    @PostMapping("request/outcome/{id}")
    void setPaymentRequestOutcome(@PathVariable long id, @RequestBody PaymentOutcomeDTO outcome);
}
