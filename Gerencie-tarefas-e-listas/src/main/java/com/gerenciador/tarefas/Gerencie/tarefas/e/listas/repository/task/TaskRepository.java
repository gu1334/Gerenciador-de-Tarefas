package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.repository.task;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.task.Task;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Users;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.task.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(Users user);

    List<Task> findByStatus(Status status);

    List<Task> findByStatusAndUser(Status status, Users user);
}
