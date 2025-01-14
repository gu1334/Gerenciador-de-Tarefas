package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.Priority;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_tarefasuser")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tarefa_id")
    private Long taskId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)  //
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)  //
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @CreationTimestamp
    private Instant createDateTimeStamp;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long tarefasId) {
        this.taskId = tarefasId;
    }

    public Users getUser() { // Alterado para o nome correto
        return user;
    }

    public void setUser(Users user) { // Alterado para o nome correto
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Instant getCreateDateTimeStamp() {
        return createDateTimeStamp;
    }

    public void setCreateDateTimeStamp(Instant createDateTimeStamp) {
        this.createDateTimeStamp = createDateTimeStamp;
    }
}
