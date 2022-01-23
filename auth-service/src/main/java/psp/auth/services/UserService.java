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
import psp.auth.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private LoggerUtil loggerUtil;

    public void registerMerchant(HttpServletRequest request, UserDTO dto) throws NotUniqueException {
        if(repository.existsByUsername(dto.getUsername())) {
            log.warn(loggerUtil.getLogMessage(request, "Registration with taken username attempted"));
            throw new NotUniqueException("User with provided username already exists!");
        }
        User user = new User(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), Role.MERCHANT);
        user = repository.save(user);
        log.info(loggerUtil.getLogMessage(request, "User (id=" + user.getId() + ") created"));
    }

    public String logIn(HttpServletRequest request, UserDTO dto) throws NotFoundException, InvalidArgumentException {
        if (!repository.existsByUsername(dto.getUsername())) {
            log.warn(loggerUtil.getLogMessage(request, "Login with unknown username attempted"));
            throw new NotFoundException("User with provided username does not exist");
        }
        User user = repository.findByUsername(dto.getUsername());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.warn(loggerUtil.getLogMessage(request, "Login with incorrect password attempted"));
            throw new InvalidArgumentException("Password incorrect");
        }
        String token = tokenService.generateToken(user);
        log.info(loggerUtil.getLogMessage(request, "User (id=" + user.getId() + ") logged in"));
        return token;
    }
}
