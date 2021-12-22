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

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("store")
public class StoreController {

    @Autowired
    private StoreService service;
    @Autowired
    private AuthClient authClient;

    @PostMapping("{name}")
    public void create(@PathVariable String name, @RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn("Unauthenticated user made an attempt to create a store");
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn("Unauthorized user made an attempt to create a store");
            throw new UnauthorizedException("You don't have a permission to create a store");
        }
        long userId = authClient.getUserId(token);
        this.service.create(name, userId);
    }

    @GetMapping
    public Store get(@RequestHeader("Authorization") String token) throws UnauthenticatedException, UnauthorizedException {
        if (token == null) {
            log.warn("Unauthenticated user made an attempt to fetch store's info");
            throw new UnauthenticatedException("You are not logged in");
        }
        if(!authClient.hasRoles(token, Arrays.asList("MERCHANT"))) {
            log.warn("Unauthorized user made an attempt to fetch store's info");
            throw new UnauthorizedException("You don't have a permission to view store info");
        }
        long userId = authClient.getUserId(token);
        return service.getByUserId(userId);
    }

    @GetMapping("id/{apiToken}")
    public long getIdByApiToken(@PathVariable String apiToken) throws NotFoundException {
        return service.getIdByApiToken(apiToken);
    }

    @GetMapping("token/{id}")
    public String getApiTokenById(@PathVariable long id) throws NotFoundException {
        return service.getApiTokenById(id);
    }
}
