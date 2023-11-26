package Santander.Coders.Banco.mapper;

import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.builder.ContaBuilder;
import Santander.Coders.Banco.model.builder.PessoaBuilder;
import Santander.Coders.Banco.model.request.ContaPostDto;
import Santander.Coders.Banco.model.request.ContaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ContaMapperImplTest {

    private final ContaMapper contaMapper = new ContaMapperImpl();

    @Test
    void toResponseDto() {
        Conta conta = createConta();
        Pessoa titular = createPessoa();

        conta.setTitular(titular);
        conta.setSaldo(new BigDecimal("1000"));

        ContaResponseDto contaResponseDto = contaMapper.toResponseDto(conta);

        assertAll(
                () -> assertNotNull(contaResponseDto),
                () -> assertEquals(conta.getId(), contaResponseDto.id()),
                () -> assertEquals(conta.getAgencia(), contaResponseDto.agencia()),
                () -> assertEquals(conta.getNumero(), contaResponseDto.numero()),
                () -> assertEquals(conta.getSaldo(), contaResponseDto.saldo()),
                () -> assertNotNull(contaResponseDto.titular()),
                () -> assertEquals(titular.getId(), contaResponseDto.titular().id()),
                () -> assertEquals(titular.getNome(), contaResponseDto.titular().nome()),
                () -> assertEquals(titular.getCpf(), contaResponseDto.titular().cpf()),
                () -> assertEquals(titular.getEndereco(), contaResponseDto.titular().endereco()),
                () -> assertEquals(titular.getTelefone(), contaResponseDto.titular().telefone())
        );
    }

    @Test
    void toResponseDto_WithNullDto_ShouldReturnNull() {
        ContaResponseDto result = contaMapper.toResponseDto(null);
        assertNull(result);
    }

    @Test
    void postDtoToEntity() {
        ContaPostDto contaPostDto = createContaPostDto();
        Conta result = contaMapper.postDtoToEntity(contaPostDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(contaPostDto.agencia(), result.getAgencia()),
                () -> assertEquals(contaPostDto.numero(), result.getNumero()),
                () -> assertEquals(contaPostDto.titular(), result.getTitular())
        );
    }


    @Test
    void postDtoToEntity_WithNullDto_ShouldReturnNull() {
        Conta result = contaMapper.postDtoToEntity(null);
        assertNull(result);
    }

    @Test
    void putDtoToEntity() {
        ContaPutDto contaPutDto = createContaPutDto();
        Conta result = contaMapper.putDtoToEntity(createContaPutDto());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(contaPutDto.agencia(), result.getAgencia()),
                () -> assertEquals(contaPutDto.numero(), result.getNumero()),
                () -> assertEquals(contaPutDto.titular(), result.getTitular())
        );
    }

    @Test
    void putDtoToEntity_WithNullDto_ShouldReturnNull() {
        Conta result = contaMapper.putDtoToEntity(null);
        assertNull(result);
    }

    private Conta createConta(){
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(1L)
                .agencia(123L)
                .numero(456L)
                .titular(null)
                .build();
    }

    private ContaResponseDto createContaResponseDto() {
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(1L)
                .agencia(1234L)
                .numero(123456L)
                .saldo(new BigDecimal(1000.00))
                .titularResponseDto(null)
                .buildResponseDto();
    }

    private ContaPostDto createContaPostDto() {
        ContaBuilder builder = new ContaBuilder();

        return builder
                .agencia(1234L)
                .numero(123456L)
                .titularResponseDto(createPessoaResponseDto())
                .buildPostDto();
    }

    private ContaPutDto createContaPutDto() {
        ContaBuilder builder = new ContaBuilder();

        return builder
                .agencia(1234L)
                .numero(123456L)
                .titularResponseDto(createPessoaResponseDto())
                .buildPutDto();
    }

    private Pessoa createPessoa(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .id(1L)
                .nome("John Doe")
                .cpf("12345678901")
                .endereco("Rua 123")
                .telefone("987654321")
                .conta(null)
                .build();
    }

    private PessoaResponseDto createPessoaResponseDto(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .id(1L)
                .nome("John Doe")
                .cpf("12345678901")
                .endereco("Rua 123")
                .telefone("987654321")
                .conta(null)
                .buildResponseDto();
    }
}