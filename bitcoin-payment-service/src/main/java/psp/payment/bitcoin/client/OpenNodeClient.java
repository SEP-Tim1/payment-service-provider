package psp.payment.bitcoin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import psp.payment.bitcoin.dto.OpenNodeRequestDTO;
import psp.payment.bitcoin.dto.OpenNodeResponseDTO;

@FeignClient(url="${service.opennode.url}", name="opennode")
public interface OpenNodeClient {

    @PostMapping("charges")
    OpenNodeResponseDTO createCharge(@RequestBody OpenNodeRequestDTO request, @RequestHeader("Authorization") String apiKey);
}
