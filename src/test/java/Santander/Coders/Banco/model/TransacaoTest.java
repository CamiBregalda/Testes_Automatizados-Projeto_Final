package Santander.Coders.Banco.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoTest {
    private Transacao transacao;
    private LocalDateTime agora;

    @BeforeEach
    void setUp() {
        agora = LocalDateTime.now();
        transacao = new Transacao(1L, TipoTransacao.TRANSFERENCIA_ENVIO, BigDecimal.valueOf(1000), agora);
    }

    @Test
    @DisplayName("Should get and set id")
    void getId() {
        transacao.setId(2L);
        assertEquals(2L, transacao.getId());
    }

    @Test
    @DisplayName("Should get and set account id")
    void getContaId() {
        assertEquals(1L, transacao.getContaId());
        transacao.setContaId(2L);
        assertEquals(2L, transacao.getContaId());
    }

    @Test
    @DisplayName("Should get and set type")
    void getTipo() {
        assertEquals(TipoTransacao.TRANSFERENCIA_ENVIO, transacao.getTipo());
        transacao.setTipo(TipoTransacao.TRANSFERENCIA_RECEBIMENTO);
        assertEquals(TipoTransacao.TRANSFERENCIA_RECEBIMENTO, transacao.getTipo());
    }

    @Test
    @DisplayName("Should get and set value")
    void getValor() {
        assertEquals(new BigDecimal("1000"), transacao.getValor());
        transacao.setValor(new BigDecimal("2000"));
        assertEquals(new BigDecimal("2000"), transacao.getValor());
    }

    @Test
    @DisplayName("Should get and set date")
    void getData() {
        assertEquals(agora, transacao.getData());
        transacao.setData(agora.plusHours(2));
        assertEquals(agora.plusHours(2), transacao.getData());
    }
}