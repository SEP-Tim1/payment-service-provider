package psp.auth.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import psp.auth.dto.UserDTO;
import psp.auth.exceptions.AccountBlockedException;
import psp.auth.exceptions.InvalidArgumentException;
import psp.auth.exceptions.NotFoundException;
import psp.auth.exceptions.NotUniqueException;
import psp.auth.model.BlockedAccount;
import psp.auth.model.FailedLoginAttempt;
import psp.auth.model.Role;
import psp.auth.model.User;
import psp.auth.repositories.BlockedAccountRepository;
import psp.auth.repositories.LoginAttemptRepository;
import psp.auth.repositories.UserRepository;
import psp.auth.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private LoginAttemptRepository loginAttemptRepository;
    @Autowired
    private BlockedAccountRepository blockedAccountRepository;
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

    public String logIn(HttpServletRequest request, UserDTO dto) throws NotFoundException, InvalidArgumentException, AccountBlockedException {
        if (!repository.existsByUsername(dto.getUsername())) {
            log.warn(loggerUtil.getLogMessage(request, "Login with unknown username attempted"));
            throw new NotFoundException("User with provided username does not exist");
        }
        User user = repository.findByUsername(dto.getUsername());
        if (isBlocked(user)) {
            log.info(loggerUtil.getLogMessage(request, "User (id=" + user.getId() + ") login attempt. Account blocked."));
            throw new AccountBlockedException("Your account is currently blocked");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            failedLoginAttempt(request, user);
            log.warn(loggerUtil.getLogMessage(request, "Login with incorrect password attempted"));
            throw new InvalidArgumentException("Password incorrect");
        }
        String token = tokenService.generateToken(user);
        log.info(loggerUtil.getLogMessage(request, "User (id=" + user.getId() + ") logged in"));
        return token;
    }

    private boolean isBlocked(User user) {
        return blockedAccountRepository.findAllByUserId(user.getId()).stream()
                .filter(b -> b.getTimestamp().isAfter(LocalDateTime.now().minusDays(1)))
                .count() > 0;
    }

    private void failedLoginAttempt(HttpServletRequest request, User user) {
        FailedLoginAttempt loginAttempt = new FailedLoginAttempt();
        loginAttempt.setUser(user);
        loginAttempt.setTimestamp(LocalDateTime.now());
        loginAttemptRepository.save(loginAttempt);
        tryBlock(request, user);
    }

    private void tryBlock(HttpServletRequest request, User user) {
        if (loginAttemptRepository.findAllByUserId(user.getId()).stream()
                .filter(la -> la.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(10)))
                .count() >= 3) {
            BlockedAccount blockedAccount = new BlockedAccount();
            blockedAccount.setUser(user);
            blockedAccount.setTimestamp(LocalDateTime.now());
            blockedAccountRepository.save(blockedAccount);
            log.warn(loggerUtil.getLogMessage(request, "User (id=" + user.getId() + ") has been blocked for 24h"));
        }
    }
}
