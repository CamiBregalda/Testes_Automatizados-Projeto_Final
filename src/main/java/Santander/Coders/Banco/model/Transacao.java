package Santander.Coders.Banco.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transacoes")
@RequiredArgsConstructor
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "conta_id", nullable = false)
    private Long contaId;

    @Column(name = "tipo_transacao", nullable = false)
    private TipoTransacao tipo;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    public Transacao(Long contaId, TipoTransacao tipo, BigDecimal valor, LocalDateTime data) {
        this.contaId = contaId;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
    }
}
