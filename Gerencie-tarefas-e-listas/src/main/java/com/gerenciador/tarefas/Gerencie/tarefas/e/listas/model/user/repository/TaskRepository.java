package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.repository;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.Status;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Task;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(Users user);

    List<Task> findByStatus(Status status);

    List<Task> findByStatusAndUser(Status status, Users user);
}
