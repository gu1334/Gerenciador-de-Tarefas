package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.repository;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Role;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.TarefasUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefasUserRepository extends JpaRepository<TarefasUser, Long> {
}
