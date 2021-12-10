package psp.payment.card.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import psp.payment.card.dtos.PaymentRequest;

@FeignClient("payment-request-service")
public interface Client {

    @GetMapping("request/{id}")
    PaymentRequest getById(@PathVariable long id);
}
