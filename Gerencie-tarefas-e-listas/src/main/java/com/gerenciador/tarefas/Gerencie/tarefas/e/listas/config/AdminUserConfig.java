package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Role;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Users;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.repository.user.RoleRepository;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.repository.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                users -> {
                    System.out.println("admin ja existe!");
                },
                () -> {
                    var user = new Users();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );

    }
}
