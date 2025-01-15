package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.Priority;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.Status;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.view.Views;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_tarefasuser")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tarefa_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @JsonView(Views.Public.class)
    private String title;

    @JsonView(Views.Public.class)
    private String description;

    @Enumerated(EnumType.STRING)  //
    @Column(name = "status", nullable = false)
    @JsonView(Views.Public.class)
    private Status status;

    @Enumerated(EnumType.STRING)  //
    @Column(name = "priority", nullable = false)
    @JsonView(Views.Public.class)
    private Priority priority;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonView(Views.PublicWithCreateAt.class)
    private Instant createDateTimeStamp;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonView(Views.PublicWithUpdateAt.class)
    private Instant updateAtDateTimeStamp;

    public Instant getUpdateAtDateTimeStamp() {
        return updateAtDateTimeStamp;
    }

    public void setUpdateAtDateTimeStamp(Instant updateAtDateTimeStamp) {
        this.updateAtDateTimeStamp = updateAtDateTimeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long tarefasId) {
        this.id = tarefasId;
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
