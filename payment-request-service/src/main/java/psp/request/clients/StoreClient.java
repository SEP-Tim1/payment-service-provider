package psp.request.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("store-service")
public interface StoreClient {

    @GetMapping("store/id/{apiToken}")
    long getIdByApiToken(@PathVariable String apiToken);
}
