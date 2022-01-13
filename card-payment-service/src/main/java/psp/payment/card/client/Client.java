package psp.payment.card.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import psp.payment.card.dtos.PaymentOutcomeDTO;
import psp.payment.card.dtos.PaymentRequest;
import psp.payment.card.dtos.PaymentResponseDTO;

@FeignClient("${service.payment-request.name}")
public interface Client {

    @GetMapping("request/{id}")
    PaymentRequest getById(@PathVariable long id);

    @PostMapping("request/outcome/{id}")
    void setPaymentRequestOutcome(@PathVariable long id, @RequestBody PaymentOutcomeDTO outcome);

    @GetMapping("request/processed/{id}")
    boolean isProcessed(@PathVariable long id);
}
