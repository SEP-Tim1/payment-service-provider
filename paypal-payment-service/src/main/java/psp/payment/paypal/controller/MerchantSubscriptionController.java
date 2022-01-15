package psp.payment.paypal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import psp.payment.paypal.client.AuthClient;
import psp.payment.paypal.client.StoreClient;
import psp.payment.paypal.dto.MerchantSubscriptionDTO;
import psp.payment.paypal.exceptions.NotFoundException;
import psp.payment.paypal.exceptions.NotUniqueException;
import psp.payment.paypal.exceptions.UnauthenticatedException;
import psp.payment.paypal.exceptions.UnauthorizedException;
import psp.payment.paypal.service.MerchantSubscriptionService;

import java.util.Arrays;

@RestController
@RequestMapping("merchant/sub")
@Slf4j
public class MerchantSubscriptionController {

    @Autowired
    private MerchantSubscriptionService service;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private StoreClient storeClient;

    @PostMapping
    public void create(@RequestBody MerchantSubscriptionDTO dto, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException, NotUniqueException {
        if (token == null) {
            log.warn("Unauthenticated user made an attempt to create paypal payment subscription");
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn("Unauthorized user made an attempt to create paypal payment subscription");
            throw new UnauthorizedException("You don't have a permission to create paypal payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        service.create(dto, storeId);
    }

    @DeleteMapping
    public void delete(@RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException, NotFoundException {
        if (token == null) {
            log.warn("Unauthenticated user made an attempt to delete paypal payment subscription");
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn("Unauthorized user made an attempt to delete paypal payment subscription");
            throw new UnauthorizedException("You don't have a permission to delete paypal payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        service.delete(storeId);
    }

    @GetMapping
    public boolean isSubscribed(@RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn("Unauthenticated user made an attempt to check paypal payment subscription");
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn("Unauthorized user made an attempt to check paypal payment subscription");
            throw new UnauthorizedException("You don't have a permission to check paypal payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        return service.isSubscribed(storeId);
    }
}
