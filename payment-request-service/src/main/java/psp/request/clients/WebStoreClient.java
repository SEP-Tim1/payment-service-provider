package psp.request.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import psp.request.dtos.PaymentOutcomeDTO;

import java.net.URI;

@FeignClient(name = "a", url="a")
public interface WebStoreClient {
    @PostMapping
    void process(URI baseUri, @RequestBody PaymentOutcomeDTO dto);
}
