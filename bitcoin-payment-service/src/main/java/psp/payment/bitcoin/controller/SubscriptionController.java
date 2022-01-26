package psp.payment.bitcoin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import psp.payment.bitcoin.client.AuthClient;
import psp.payment.bitcoin.client.StoreClient;
import psp.payment.bitcoin.exceptions.NotFoundException;
import psp.payment.bitcoin.exceptions.UnauthenticatedException;
import psp.payment.bitcoin.exceptions.UnauthorizedException;
import psp.payment.bitcoin.service.SubscriptionService;
import psp.payment.bitcoin.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Autowired
    private LoggerUtil loggerUtil;

    @PostMapping("{apiKey}")
    public void create(HttpServletRequest request,  @PathVariable String apiKey, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException, MethodArgumentNotValidException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to create bitcoin payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to create bitcoin payment subscription"));
            throw new UnauthorizedException("You don't have a permission to create bitcoin payment subscription");
        }
        Pattern pattern = Pattern.compile("^[A-Za-z0-9 -_]+$");
        Matcher matcher = pattern.matcher(apiKey);
        if (!matcher.find()) {
            throw new MethodArgumentNotValidException(null, null);
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        service.create(request, storeId, apiKey);
    }

    @DeleteMapping
    public void delete(HttpServletRequest request, @RequestHeader("Authorization") String token) throws UnauthorizedException, UnauthenticatedException, NotFoundException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to delete bitcoin payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to delete bitcoin payment subscription"));
            throw new UnauthorizedException("You don't have a permission to delete bitcoin payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        service.delete(request, storeId);
    }

    @GetMapping("{storeId}")
    public boolean exists(HttpServletRequest request, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to check bitcoin payment subscription"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to check bitcoin payment subscription"));
            throw new UnauthorizedException("You don't have a permission to check bitcoin payment subscription");
        }
        long userId = authClient.getUserId(token);
        long storeId = storeClient.getIdByUserId(userId);
        return service.exists(storeId);
    }
}
