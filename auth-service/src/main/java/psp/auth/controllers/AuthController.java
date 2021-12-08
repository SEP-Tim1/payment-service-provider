package psp.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psp.auth.dto.UserDTO;
import psp.auth.exceptions.NotUniqueException;
import psp.auth.services.UserService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService service;

    @PostMapping("register")
    public void registerMerchant(@RequestBody UserDTO dto) throws NotUniqueException, NoSuchAlgorithmException, InvalidKeySpecException {
        service.registerMerchant(dto);
    }
}
