package ru.endienasg.foxworld.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.endienasg.foxworld.models.Role;
import ru.endienasg.foxworld.models.User;
import ru.endienasg.foxworld.models.requests.AuthRequest;
import ru.endienasg.foxworld.models.requests.RegistryRequest;
import ru.endienasg.foxworld.models.responses.AuthResponse;
import ru.endienasg.foxworld.repositories.IUserRepository;
import ru.endienasg.foxworld.security.jwt.JwtTokenService;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AuthService {
    private JwtTokenService jwtTokenService = new JwtTokenService();


    @Async
    public CompletableFuture<?> Registry(RegistryRequest request, IUserRepository repository , PasswordEncoder passwordEncoder) {
        return CompletableFuture.supplyAsync(() -> {
            User userToSave = new User();
            userToSave.setRole(Role.USER);
            userToSave.setPassword(passwordEncoder.encode(request.getPassword()));
            userToSave.setUsername(request.getUsername());
            userToSave.setEmail(request.getEmail());
            userToSave.setUuid(UUID.randomUUID().toString());
            log.info("user with nickname: " + userToSave.getUsername() + " was been registry");
            try {
                User user = repository.save(userToSave);
                return user;
            } catch (Exception e) {
                return "Произошла ошибка при сохранении этого пользователя";
            }
        });
    }

    @Async
    public CompletableFuture<?> Auth(AuthRequest request, IUserRepository repository , PasswordEncoder passwordEncoder) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                User userFromDb = repository.findByUsername(request.getUsername());
                User user = userFromDb;
                log.warn(user.getUsername());
                log.warn(request.getUsername());
                String psw = user.getPassword();
                if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
                    AuthResponse response = new AuthResponse();
                    response.setUsername(user.getUsername());
                    response.setUuid(user.getUuid());
                    response.setToken(jwtTokenService.generateToken(user));
                    return response;
                } else {
                    return "Логин или пароль оказали неверными";
                }
            } catch (Exception e) {
                return e;
            }
        });
    }
}
