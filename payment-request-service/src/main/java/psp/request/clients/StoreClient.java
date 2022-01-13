package psp.request.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("${service.store.name}")
public interface StoreClient {

    @GetMapping("store/id/{apiToken}")
    long getIdByApiToken(@PathVariable String apiToken);

    @GetMapping("store/token/{id}")
    String getApiTokenById(@PathVariable long id);
}
