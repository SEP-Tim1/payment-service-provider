package psp.auth.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import psp.auth.dto.UserDTO;
import psp.auth.exceptions.InvalidArgumentException;
import psp.auth.exceptions.NotFoundException;
import psp.auth.exceptions.NotUniqueException;
import psp.auth.model.Role;
import psp.auth.model.User;
import psp.auth.repositories.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerMerchant(UserDTO dto) throws NotUniqueException, NoSuchAlgorithmException, InvalidKeySpecException {
        if(repository.existsByUsername(dto.getUsername())) {
            log.warn("Registration with taken username attempted");
            throw new NotUniqueException("User with provided username already exists!");
        }
        User user = new User(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), Role.MERCHANT);
        user = repository.save(user);
        log.info("User (id=" + user.getId() + ") created");
    }

    public String logIn(UserDTO dto) throws NotFoundException, InvalidArgumentException {
        if (!repository.existsByUsername(dto.getUsername())) {
            log.warn("Login with unknown username attempted");
            throw new NotFoundException("User with provided username does not exist");
        }
        User user = repository.findByUsername(dto.getUsername());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.warn("Login with incorrect password attempted");
            throw new InvalidArgumentException("Password incorrect");
        }
        String token = tokenService.generateToken(user);
        log.info("User (id=" + user.getId() + ") logged in");
        return token;
    }
}
