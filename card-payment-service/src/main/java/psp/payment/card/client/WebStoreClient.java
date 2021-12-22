package psp.payment.card.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import psp.payment.card.dtos.PaymentResponseDTO;

import java.net.URI;

@FeignClient(name = "a", url="a")
public interface WebStoreClient {
    @PostMapping
    void process(URI baseUri, @RequestBody PaymentResponseDTO dto);
}
