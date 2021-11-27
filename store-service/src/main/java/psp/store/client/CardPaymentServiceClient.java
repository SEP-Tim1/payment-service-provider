package psp.store.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("card-payment-service")
public interface CardPaymentServiceClient {
    @GetMapping("/hello/{name}")
    String hello(@PathVariable String name);
}
