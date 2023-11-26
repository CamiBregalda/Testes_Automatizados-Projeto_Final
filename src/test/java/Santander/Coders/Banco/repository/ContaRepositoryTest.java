package Santander.Coders.Banco.repository;

import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.builder.ContaBuilder;
import Santander.Coders.Banco.model.builder.PessoaBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ContaRepositoryTest {

    @MockBean
    private ContaRepository contaRepository;

    @Test
    @DisplayName("Should return a list of active accounts")
    void findAllAndCanceledFalse_ShouldReturnListOfActiveAccounts() {
        Pageable pageable = Pageable.unpaged();

        when(contaRepository.findAllAndCanceledFalse(pageable)).thenReturn(new PageImpl<>(List.of(createConta(), createConta())));
        Page<Conta> result = contaRepository.findAllAndCanceledFalse(pageable);

        assertEquals(2, result.getContent().size());
        assertFalse(result.getContent().get(0).isCanceled());
        assertFalse(result.getContent().get(1).isCanceled());
    }

    @Test
    @DisplayName("Should return an active account by ID")
    void findByIdAndCanceledFalse_ShouldReturnActiveAccountById() {
        Conta conta = createConta();

        when(contaRepository.findByIdAndCanceledFalse(1L)).thenReturn(Optional.of(conta));
        Optional<Conta> result = contaRepository.findByIdAndCanceledFalse(1L);

        assertTrue(result.isPresent());
        assertFalse(result.get().isCanceled());
    }

    @Test
    @DisplayName("Should return empty when account by ID is not found or is inactive")
    void findByIdAndCanceledFalse_ShouldReturnEmptyWhenNotFoundOrInactive() {
        when(contaRepository.findByIdAndCanceledFalse(1L)).thenReturn(Optional.empty());

        Optional<Conta> result = contaRepository.findByIdAndCanceledFalse(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return an active account by numero")
    void findByNumero() {
        Conta conta = createConta();

        when(contaRepository.findByNumero(conta.getNumero())).thenReturn(conta);
        Conta result = contaRepository.findByNumero(conta.getNumero());

        assertNotNull(result);
        assertEquals(conta.getNumero(), result.getNumero());
    }

    private Conta createConta(){
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(1L)
                .agencia(1234L)
                .numero(123456L)
                .titular(createPessoa())
                .build();
    }

    private Pessoa createPessoa(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .id(1L)
                .nome("Alice")
                .cpf("12345678911")
                .endereco("Rua 1")
                .telefone("123456789")
                .conta(new Conta(1L, 1234L, 123456L, null))
                .build();
    }
}