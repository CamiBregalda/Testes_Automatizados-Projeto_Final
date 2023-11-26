package Santander.Coders.Banco.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pessoas")
@RequiredArgsConstructor
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "endereco", nullable = false)
    private String endereco;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @OneToOne(mappedBy = "titular", cascade = CascadeType.ALL)
    private Conta conta;

    @Column(nullable = false)
    private boolean inatived = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inatived_at")
    private LocalDateTime inativedAt;

    public Pessoa(Long id, String nome, String cpf, String endereco, String telefone, Conta conta) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.telefone = telefone;
        this.conta = conta;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void delete() {
        inatived = true;
        inativedAt = LocalDateTime.now();
    }

    public void update(Pessoa pessoa) {
        if (pessoa.getNome() != null) setNome(pessoa.getNome());
        if (pessoa.getCpf() != null) setCpf(pessoa.getCpf());
        if (pessoa.getEndereco() != null) setEndereco(pessoa.getEndereco());
        if (pessoa.getTelefone() != null) setTelefone(pessoa.getTelefone());
        if (pessoa.getConta() != null) setConta(pessoa.getConta());
        updatedAt = LocalDateTime.now();
    }
}
