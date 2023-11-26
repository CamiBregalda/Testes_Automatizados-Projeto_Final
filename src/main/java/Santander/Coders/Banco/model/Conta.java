package Santander.Coders.Banco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contas")
@Data
@RequiredArgsConstructor
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "agencia", nullable = false)
    private Long agencia;

    @Column(name = "numero", nullable = false)
    private Long numero;

    @Column(name = "tipo", nullable = false)
    private TipoConta tipo;

    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "titular_id", unique = true)
    private Pessoa titular;

    @Column(nullable = false)
    private boolean canceled = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    public Conta(Long id, Long agencia, Long numero, Pessoa titular) {
        this.id = id;
        this.agencia = agencia;
        this.numero = numero;
        this.titular = titular;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void delete() {
        canceled = true;
        canceledAt = LocalDateTime.now();
    }

    public void update(Conta conta) {
        if (conta.getAgencia() != null) setAgencia(conta.getAgencia());
        if (conta.getNumero() != null) setNumero(conta.getNumero());
        if (conta.getTitular() != null) setTitular(conta.getTitular());
        updatedAt = LocalDateTime.now();
    }

    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero.");
        }

        saldo = saldo.add(valor);
    }

    public void sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser maior que zero.");
        }

        if (valor.compareTo(saldo) > 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o saque.");
        }

        saldo = saldo.subtract(valor);
    }

    public void transferir(Conta contaDestino, BigDecimal valor) {
        if (contaDestino == null) {
            throw new IllegalArgumentException("Conta de destino não pode ser nula.");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero.");
        }

        if (valor.compareTo(saldo) > 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência.");
        }

        saldo = saldo.subtract(valor);
        contaDestino.saldo = contaDestino.saldo.add(valor);
    }
}
