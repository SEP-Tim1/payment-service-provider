package psp.payment.bitcoin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import psp.payment.bitcoin.client.AuthClient;
import psp.payment.bitcoin.client.StoreClient;
import psp.payment.bitcoin.exceptions.NotFoundException;
import psp.payment.bitcoin.exceptions.UnauthenticatedException;
import psp.payment.bitcoin.exceptions.UnauthorizedException;
import psp.payment.bitcoin.service.SubscriptionService;

import java.util.Arrays;

@RestController
@RequestMapping("sub")
@Slf4j
public class SubscriptionController {

    @Autowired
    private AuthClient authClient;
    @Autowired
    private StoreClient storeClient;
    @Autowired
    private SubscriptionService service;

    @PostMapping("{apiKey}")
    public void create(@PathVariable String apiKey, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn("Unauthenticated user made an attempt to create bitcoin payment subscription");
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn("Unauthorized user made an attempt to create bitcoin payment subscription");
            throw new UnauthorizedException("You don't have a permission to create bitcoin payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        service.create(storeId, apiKey);
    }

    @DeleteMapping
    public void delete(@RequestHeader("Authorization") String token) throws UnauthorizedException, UnauthenticatedException, NotFoundException {
        if (token == null) {
            log.warn("Unauthenticated user made an attempt to delete bitcoin payment subscription");
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn("Unauthorized user made an attempt to delete bitcoin payment subscription");
            throw new UnauthorizedException("You don't have a permission to delete bitcoin payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        service.delete(storeId);
    }

    @GetMapping("{storeId}")
    public boolean exists(@PathVariable long storeId, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn("Unauthenticated user made an attempt to check bitcoin payment subscription");
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn("Unauthorized user made an attempt to check bitcoin payment subscription");
            throw new UnauthorizedException("You don't have a permission to check bitcoin payment subscription");
        }
        return service.exists(storeId);
    }
}
