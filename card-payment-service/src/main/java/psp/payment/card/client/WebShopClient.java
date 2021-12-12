package psp.payment.card.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import psp.payment.card.dtos.PaymentResponseDTO;

@FeignClient(url= "localhost:8050", name = "web-shop-back")
public interface WebShopClient {
    @PostMapping("purchase/bank-payment-response")
    void bankPaymentResponse(@RequestBody PaymentResponseDTO dto);
}
