package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.user;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.LoginRequest;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.LoginResponse;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.exceptions.TechnicalException;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Role;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.repository.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            var user = userRepository.findByUsername(loginRequest.username());

            if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
                throw new BadCredentialsException("User or password is invalid!");
            }

            var now = Instant.now();
            var expiresIn = 300L;
            var scopes = user.get().getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(" "));

            var claims = JwtClaimsSet.builder()
                    .issuer("myBackend")
                    .subject(user.get().getUserId().toString())
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiresIn))
                    .claim("scope", scopes)
                    .build();

            var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));

        } catch (Exception e) {
            throw new TechnicalException("002", "Failed to generate token: " + e.getMessage());
        }
    }
}
