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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PessoaRepositoryTest {

    @MockBean
    private PessoaRepository pessoaRepository;

    @Test
    @DisplayName("Should return a page of active persons")
    void findAllAndInativedFalse_ShouldReturnPageOfActivePersons() {
        List<Pessoa> pessoas = new ArrayList<>();
        pessoas.add(createPessoa());
        pessoas.add(createPessoa());
        Page<Pessoa> pessoaPage = new PageImpl<>(pessoas);

        when(pessoaRepository.findAllAndInativedFalse(any(Pageable.class))).thenReturn(pessoaPage);
        Page<Pessoa> result = pessoaRepository.findAllAndInativedFalse(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(pessoas.size(), result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(p -> !p.isInatived()));
    }

    @Test
    @DisplayName("Should return an active person by ID")
    void findByIdAndInativedFalse_ShouldReturnActivePersonById() {
        Pessoa pessoa = createPessoa();

        when(pessoaRepository.findByIdAndInativedFalse(1L)).thenReturn(Optional.of(pessoa));
        Optional<Pessoa> result = pessoaRepository.findByIdAndInativedFalse(1L);

        assertTrue(result.isPresent());
        assertFalse(result.get().isInatived());
    }

    @Test
    @DisplayName("Should return empty when person by ID is not found or is inactive")
    void findByIdAndInativedFalse_ShouldReturnEmptyWhenNotFoundOrInactive() {
        when(pessoaRepository.findByIdAndInativedFalse(1L)).thenReturn(Optional.empty());

        Optional<Pessoa> result = pessoaRepository.findByIdAndInativedFalse(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return false if the person registered by CPF does not exist")
    void findByExistsCpf_ShouldReturnFalseIfItDoesNotExists() {
        Pessoa pessoa = createPessoa();

        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(false);
        Boolean result = pessoaRepository.existsByCpf(pessoa.getCpf());

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true if there is a person registered by CPF")
    void findByExistsCpf_ShouldReturnTrueIfItExists() {
        Pessoa pessoa = createPessoa();
        when(pessoaRepository.save(pessoa)).thenReturn(pessoa);
        pessoaRepository.save(pessoa);
        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(true);
        Boolean result = pessoaRepository.existsByCpf(pessoa.getCpf());

        assertTrue(result);
    }

    private Pessoa createPessoa(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .id(1L)
                .nome("Alice")
                .cpf("12345678911")
                .endereco("Rua 1")
                .telefone("123456789")
                .conta(createConta())
                .build();
    }

    private Conta createConta(){
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(1L)
                .agencia(1234L)
                .numero(123456L)
                .saldo(new BigDecimal(1000.00))
                .titular(new Pessoa(1L, "Alice", "12345678911", "Rua 1", "123456789", null))
                .build();
    }
}