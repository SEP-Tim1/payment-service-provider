package psp.payment.qrcode.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import psp.payment.qrcode.dtos.InvoiceDTO;
import psp.payment.qrcode.dtos.InvoiceResponseDTO;
import psp.payment.qrcode.dtos.MerchantCredentialsDTO;

@FeignClient(url= "${service.bank2.url}", name = "second-bank")
public interface Bank2Client {
    @PostMapping("account/validate")
    void validate(@RequestBody MerchantCredentialsDTO dto);

    @PostMapping("invoice/generate")
    InvoiceResponseDTO generate(@RequestBody InvoiceDTO dto);
}
