package Santander.Coders.Banco.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ContaTest {
    private Conta conta;

    @BeforeEach
    void setUp() {
        conta = new Conta(1L, 1234L, 123456L, null);
        conta.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should get and set id")
    void getId() {
        assertEquals(1L, conta.getId());
        conta.setId(2L);
        assertEquals(2L, conta.getId());
    }

    @Test
    @DisplayName("Should get and set agencia")
    void getAgencia() {
        assertEquals(1234L, conta.getAgencia());
        conta.setAgencia(5678L);
        assertEquals(5678L, conta.getAgencia());
    }

    @Test
    @DisplayName("Should get and set numero")
    void getNumero() {
        assertEquals(123456L, conta.getNumero());
        conta.setNumero(654321L);
        assertEquals(654321L, conta.getNumero());
    }

    @Test
    @DisplayName("Should get and set tipo")
    void getTipo() {
        assertNull(conta.getTipo());
        conta.setTipo(TipoConta.CORRENTE);
        assertEquals(TipoConta.CORRENTE, conta.getTipo());
    }

    @Test
    @DisplayName("Should get and set saldo")
    void getSaldo() {
        assertEquals(BigDecimal.ZERO, conta.getSaldo());
        conta.setSaldo(BigDecimal.valueOf(2000));
        assertEquals(BigDecimal.valueOf(2000), conta.getSaldo());
    }

    @Test
    @DisplayName("Should get and set titular")
    void getTitular() {
        assertNull(conta.getTitular());
        Pessoa titular = new Pessoa();
        conta.setTitular(titular);
        assertEquals(titular, conta.getTitular());
    }

    @Test
    @DisplayName("Should check if conta is canceled")
    void isCanceled() {
        assertFalse(conta.isCanceled());
        conta.setCanceled(true);
        assertTrue(conta.isCanceled());
    }

    @Test
    @DisplayName("Should get created_at timestamp")
    void getCreatedAt() {
        assertNotNull(conta.getCreatedAt());
    }

    @Test
    @DisplayName("Should get and set updated_at timestamp")
    void getUpdatedAt() {
        assertNull(conta.getUpdatedAt());
        LocalDateTime updatedAt = LocalDateTime.now();
        conta.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, conta.getUpdatedAt());
    }

    @Test
    @DisplayName("Should get and set canceled_at timestamp")
    void getCanceledAt() {
        assertNull(conta.getCanceledAt());
        LocalDateTime canceledAt = LocalDateTime.now();
        conta.setCanceledAt(canceledAt);
        assertEquals(canceledAt, conta.getCanceledAt());
    }

    @Test
    @DisplayName("Should set created_at timestamp")
    void setCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        conta.setCreatedAt(createdAt);
        assertEquals(createdAt, conta.getCreatedAt());
    }

    @Test
    @DisplayName("Should set updated_at timestamp")
    void setUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        conta.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, conta.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set canceled_at timestamp")
    void setCanceledAt() {
        LocalDateTime canceledAt = LocalDateTime.now();
        conta.setCanceledAt(canceledAt);
        assertEquals(canceledAt, conta.getCanceledAt());
    }

    @Test
    @DisplayName("Should delete conta")
    void delete() {
        assertFalse(conta.isCanceled());
        conta.delete();
        assertTrue(conta.isCanceled());
        assertNotNull(conta.getCanceledAt());
    }

    @Test
    @DisplayName("Should update conta")
    void update() {
        assertNull(conta.getTitular());
        Pessoa novoTitular = new Pessoa();
        Conta novaConta = new Conta();
        novaConta.setTitular(novoTitular);

        conta.update(novaConta);

        assertEquals(novoTitular, conta.getTitular());
        assertNotNull(conta.getUpdatedAt());
    }

    @Test
    void depositarValorPositivoDeveAumentarSaldo() {
        conta.depositar(new BigDecimal("500"));
        assertEquals(new BigDecimal("500"), conta.getSaldo());
    }

    @Test
    void depositarValorZeroDeveLancarExcecao() {
        BigDecimal valorDeposito = BigDecimal.ZERO;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conta.depositar(valorDeposito));
        assertEquals("O valor do depósito deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void sacarValorPositivoDeveDiminuirSaldo() {
        conta.setSaldo(new BigDecimal("1000"));
        conta.sacar(new BigDecimal("500"));
        assertEquals(new BigDecimal("500"), conta.getSaldo());
    }

    @Test
    void sacarValorMaiorQueSaldoDeveLancarExcecao() {
        BigDecimal valorSaque = new BigDecimal("1500");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conta.sacar(valorSaque));
        assertEquals("Saldo insuficiente para realizar o saque.", exception.getMessage());
    }

    @Test
    void sacarValorZeroDeveLancarExcecao() {
        BigDecimal valorSaque = BigDecimal.ZERO;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conta.sacar(valorSaque));
        assertEquals("O valor do saque deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void transferirValorPositivoDeveDiminuirSaldoEIncrementarSaldoDestino() {
        conta.setSaldo(new BigDecimal("1000"));
        Conta contaDestino = new Conta();
        contaDestino.setSaldo(BigDecimal.ZERO);

        conta.transferir(contaDestino, new BigDecimal("500"));

        assertEquals(new BigDecimal("500"), conta.getSaldo());
        assertEquals(new BigDecimal("500"), contaDestino.getSaldo());
    }

    @Test
    void transferirValorMaiorQueSaldoDeveLancarExcecao() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conta.transferir(new Conta(), new BigDecimal("1500")));
        assertEquals("Saldo insuficiente para realizar a transferência.", exception.getMessage());
    }

    @Test
    void transferirValorZeroDeveLancarExcecao() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conta.transferir(new Conta(), BigDecimal.ZERO));
        assertEquals("O valor da transferência deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void transferirParaContaDestinoNulaDeveLancarExcecao() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conta.transferir(null, new BigDecimal("500")));
        assertEquals("Conta de destino não pode ser nula.", exception.getMessage());
    }
}