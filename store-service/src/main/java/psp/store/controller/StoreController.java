package psp.store.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import psp.store.client.AuthClient;
import psp.store.exceptions.NotFoundException;
import psp.store.exceptions.UnauthenticatedException;
import psp.store.exceptions.UnauthorizedException;
import psp.store.model.Store;
import psp.store.services.StoreService;
import psp.store.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("store")
public class StoreController {

    @Autowired
    private StoreService service;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private LoggerUtil loggerUtil;

    @PostMapping("{name}")
    public void create(HttpServletRequest request,  @PathVariable String name, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to create a store"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to create a store"));
            throw new UnauthorizedException("You don't have a permission to create a store");
        }
        long userId = authClient.getUserId(token);
        this.service.create(request, name, userId);
    }

    @GetMapping
    public Store get(HttpServletRequest request, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthenticated user made an attempt to fetch store's info"));
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn(loggerUtil.getLogMessage(request, "Unauthorized user made an attempt to fetch store's info"));
            throw new UnauthorizedException("You don't have a permission to view store info");
        }
        long userId = authClient.getUserId(token);
        return service.getByUserId(userId);
    }

    @GetMapping("id/{apiToken}")
    public long getIdByApiToken(HttpServletRequest request, @PathVariable String apiToken) throws NotFoundException {
        return service.getIdByApiToken(request, apiToken);
    }

    @GetMapping("token/{id}")
    public String getApiTokenById(HttpServletRequest request, @PathVariable long id) throws NotFoundException {
        return service.getApiTokenById(request, id);
    }

    @GetMapping("{userId}")
    public long getIdByUserId(HttpServletRequest request, @PathVariable long userId) throws NotFoundException {
        return service.getIdByUserId(request, userId);
    }
}
