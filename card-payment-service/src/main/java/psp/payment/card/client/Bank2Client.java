package psp.payment.card.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import psp.payment.card.dtos.MerchantCredentialsDTO;

@FeignClient(url= "localhost:8062", name = "second-bank")
public interface Bank2Client {
    @PostMapping("account/validate")
    void validate(@RequestBody MerchantCredentialsDTO dto);
}
