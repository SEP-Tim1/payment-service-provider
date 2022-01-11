package psp.payment.bitcoin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("auth-service")
public interface AuthClient {

    @PostMapping("token/role")
    boolean hasRoles(@RequestHeader("Authorization") String token, @RequestBody List<String> roles);

    @GetMapping("token/id")
    long getUserId(@RequestHeader("Authorization") String token);
}
