package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_tarefasuser")
public class TarefasUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tarefa_id")
    private Long tarefasId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user; // Corrigido para usar sua entidade "Users"

    private String content;

    @CreationTimestamp
    private Instant createDateTimeStamp;

    public Long getTarefasId() {
        return tarefasId;
    }

    public void setTarefasId(Long tarefasId) {
        this.tarefasId = tarefasId;
    }

    public Users getUser() { // Alterado para o nome correto
        return user;
    }

    public void setUser(Users user) { // Alterado para o nome correto
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreateDateTimeStamp() {
        return createDateTimeStamp;
    }

    public void setCreateDateTimeStamp(Instant createDateTimeStamp) {
        this.createDateTimeStamp = createDateTimeStamp;
    }
}
