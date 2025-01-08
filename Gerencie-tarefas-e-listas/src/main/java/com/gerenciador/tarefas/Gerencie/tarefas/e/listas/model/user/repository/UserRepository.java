package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.repository;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
}
