package psp.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.auth.dto.UserDTO;
import psp.auth.exceptions.InvalidArgumentException;
import psp.auth.exceptions.NotFoundException;
import psp.auth.exceptions.NotUniqueException;
import psp.auth.model.Role;
import psp.auth.model.User;
import psp.auth.repositories.UserRepository;
import psp.auth.security.PasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    public void registerMerchant(UserDTO dto) throws NotUniqueException, NoSuchAlgorithmException, InvalidKeySpecException {
        if(repository.existsByUsername(dto.getUsername())) {
            throw new NotUniqueException("User with provided username already exists!");
        }
        User user = new User(dto.getUsername(), PasswordEncoder.encode(dto.getPassword()), Role.MERCHANT);
        repository.save(user);
    }

    public String logIn(UserDTO dto) throws NotFoundException, InvalidArgumentException {
        if (!repository.existsByUsername(dto.getUsername())) {
            throw new NotFoundException("User with provided username does not exist");
        }
        User user = repository.findByUsername(dto.getUsername());
        if (!PasswordEncoder.equals(dto.getPassword(), user.getPassword())) {
            throw new InvalidArgumentException("Password incorrect");
        }
        return tokenService.generateToken(user);
    }
}
