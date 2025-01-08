package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.repository;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
