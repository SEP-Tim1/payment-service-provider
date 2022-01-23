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
import psp.payment.paypal.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    @Autowired
    private LoggerUtil loggerUtil;

    @PostMapping
    public void create(HttpServletRequest request, @RequestBody @Valid MerchantSubscriptionDTO dto, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException, NotUniqueException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to create paypal payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to create paypal payment subscription"));
            throw new UnauthorizedException("You don't have a permission to create paypal payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        service.create(request, dto, storeId);
    }

    @DeleteMapping
    public void delete(HttpServletRequest request, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException, NotFoundException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to delete paypal payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to delete paypal payment subscription"));
            throw new UnauthorizedException("You don't have a permission to delete paypal payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        service.delete(request, storeId);
    }

    @GetMapping
    public boolean isSubscribed(HttpServletRequest request, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to check paypal payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to check paypal payment subscription"));
            throw new UnauthorizedException("You don't have a permission to check paypal payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        return service.isSubscribed(storeId);
    }
}
