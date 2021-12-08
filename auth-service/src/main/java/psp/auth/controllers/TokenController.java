package psp.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import psp.auth.exceptions.UnauthenticatedException;
import psp.auth.model.Role;
import psp.auth.model.User;
import psp.auth.services.TokenService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("token")
public class TokenController {

    @Autowired
    private TokenService service;

    @GetMapping("id")
    long getUserId(HttpServletRequest request) throws UnauthenticatedException {
        User user = service.getUser(request);
        return user.getId();
    }

    @PostMapping("role")
    boolean hasRoles(HttpServletRequest request, @RequestBody List<Role> roles) throws UnauthenticatedException {
        User user = service.getUser(request);
        return roles.contains(user.getRole());
    }
}
