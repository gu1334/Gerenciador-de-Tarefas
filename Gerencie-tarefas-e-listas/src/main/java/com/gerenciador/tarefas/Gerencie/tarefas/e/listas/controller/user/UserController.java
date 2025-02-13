package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.user;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.view.dto.CreateUserDTO;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.exceptions.UsuarioJaCadastradoException;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Role;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Users;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.repository.user.RoleRepository;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<String> newUser(@RequestBody CreateUserDTO dto) {

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userFromDb = userRepository.findByUsername(dto.username());

        if (userFromDb.isPresent()) {
            throw new UsuarioJaCadastradoException(dto.username());
        }

        var user = new Users();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));
        userRepository.save(user);

        return ResponseEntity.ok("Usuário: " + user.getUsername() + " criado com sucesso");
    }


    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('SCOPE_admin')")
    public ResponseEntity<List<Users>> listUsers(){
       try{
           var users = userRepository.findAll();
           return ResponseEntity.ok(users);
       }catch (Exception e){
           throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
       }

    }

}
