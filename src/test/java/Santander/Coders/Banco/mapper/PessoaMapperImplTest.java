package Santander.Coders.Banco.mapper;

import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.builder.ContaBuilder;
import Santander.Coders.Banco.model.builder.PessoaBuilder;
import Santander.Coders.Banco.model.request.ContaPostDto;
import Santander.Coders.Banco.model.request.ContaPutDto;
import Santander.Coders.Banco.model.request.PessoaPostDto;
import Santander.Coders.Banco.model.request.PessoaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PessoaMapperImplTest {

    private final PessoaMapper pessoaMapper = new PessoaMapperImpl();

    @Test
    void toResponseDto() {
        Conta conta = createConta();
        Pessoa titular = createPessoa();

        titular.setConta(conta);

        PessoaResponseDto pessoaResponseDto = pessoaMapper.toResponseDto(titular);

        assertAll(
                () -> assertNotNull(pessoaResponseDto),
                () -> assertEquals(titular.getId(), pessoaResponseDto.id()),
                () -> assertEquals(titular.getNome(), pessoaResponseDto.nome()),
                () -> assertEquals(titular.getCpf(), pessoaResponseDto.cpf()),
                () -> assertEquals(titular.getEndereco(), pessoaResponseDto.endereco()),
                () -> assertEquals(titular.getTelefone(), pessoaResponseDto.telefone()),
                () -> assertNotNull(pessoaResponseDto.conta()),
                () -> assertEquals(titular.getId(), pessoaResponseDto.conta().id()),
                () -> assertEquals(conta.getNumero(), pessoaResponseDto.conta().numero()),
                () -> assertEquals(conta.getAgencia(), pessoaResponseDto.conta().agencia())
        );
    }

    @Test
    void toResponseDto_WithNullDto_ShouldReturnNull() {
        PessoaResponseDto result = pessoaMapper.toResponseDto(null);
        assertNull(result);
    }

    @Test
    void postDtoToEntity() {
        PessoaPostDto pessoaPostDto = createPessoaPostDto();
        Pessoa result = pessoaMapper.postDtoToEntity(createPessoaPostDto());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(pessoaPostDto.nome(), result.getNome()),
                () -> assertEquals(pessoaPostDto.cpf(), result.getCpf()),
                () -> assertEquals(pessoaPostDto.endereco(), result.getEndereco()),
                () -> assertEquals(pessoaPostDto.telefone(), result.getTelefone()),
                () -> assertEquals(pessoaPostDto.conta(), result.getConta())
        );
    }


    @Test
    void postDtoToEntity_WithNullDto_ShouldReturnNull() {
        Pessoa result = pessoaMapper.postDtoToEntity(null);
        assertNull(result);
    }

    @Test
    void putDtoToEntity() {
        PessoaPutDto pessoaPutDto = createPessoaPutDto();
        Pessoa result = pessoaMapper.putDtoToEntity(createPessoaPutDto());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(pessoaPutDto.nome(), result.getNome()),
                () -> assertEquals(pessoaPutDto.cpf(), result.getCpf()),
                () -> assertEquals(pessoaPutDto.endereco(), result.getEndereco()),
                () -> assertEquals(pessoaPutDto.telefone(), result.getTelefone()),
                () -> assertEquals(pessoaPutDto.conta(), result.getConta())
        );
    }

    @Test
    void putDtoToEntity_WithNullDto_ShouldReturnNull() {
        Pessoa result = pessoaMapper.putDtoToEntity(null);
        assertNull(result);
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

    private PessoaResponseDto createPessoaResponseDto(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .id(1L)
                .nome("Alice")
                .cpf("12345678911")
                .endereco("Rua 1")
                .telefone("123456789")
                .contaResponseDto(createContaResponseDto())
                .buildResponseDto();
    }

    private PessoaPutDto createPessoaPutDto(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .nome("Pedro")
                .cpf("98765432100")
                .endereco("Rua 2")
                .telefone("987654321")
                .conta(createConta())
                .buildPutDto();
    }

    private PessoaPostDto createPessoaPostDto(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .nome("Alice")
                .cpf("12345678911")
                .endereco("Rua 1")
                .telefone("123456789")
                .contaResponseDto(createContaResponseDto())
                .buildPostDto();
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

    private ContaResponseDto createContaResponseDto() {
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(1L)
                .agencia(1234L)
                .numero(123456L)
                .saldo(new BigDecimal(1000.00))
                .titularResponseDto(new PessoaResponseDto(1L, "Alice", "12345678911", "Rua 1", "123456789", null))
                .buildResponseDto();
    }
}