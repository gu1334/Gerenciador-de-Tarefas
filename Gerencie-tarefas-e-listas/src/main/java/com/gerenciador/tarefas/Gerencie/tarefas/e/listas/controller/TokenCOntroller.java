package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.dto.LoginRequest;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.dto.LoginResponse;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.repository.UserRepository;
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

@RestController
public class TokenCOntroller {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public TokenCOntroller(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> Login(@RequestBody LoginRequest loginRequest) {

      var user =   userRepository.findByUsername(loginRequest.username());

              if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)){
                  throw new BadCredentialsException("user or password is invalid!");
              }

              var now = Instant.now();
              var experesIn = 300L;

              var claims = JwtClaimsSet.builder()
                      .issuer("myBackend")
                      .subject(user.get().getUserId().toString())
                      .issuedAt(now)
                      .expiresAt(now.plusSeconds(experesIn))
                      .build();

              var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
              return ResponseEntity.ok(new LoginResponse(jwtValue, experesIn));

    }
}
