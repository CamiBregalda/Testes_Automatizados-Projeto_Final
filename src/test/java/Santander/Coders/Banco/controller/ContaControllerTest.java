package Santander.Coders.Banco.controller;

import Santander.Coders.Banco.exception.EntityNotFoundException;
import Santander.Coders.Banco.mapper.ContaMapper;
import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.TipoConta;
import Santander.Coders.Banco.model.builder.ContaBuilder;
import Santander.Coders.Banco.model.builder.PessoaBuilder;
import Santander.Coders.Banco.model.request.ContaPostDto;
import Santander.Coders.Banco.model.request.ContaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;
import Santander.Coders.Banco.repository.ContaRepository;
import Santander.Coders.Banco.repository.TransacaoRepository;
import Santander.Coders.Banco.service.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContaControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ContaService service;

    @MockBean
    private ContaRepository contaRepository;

    @Test
    @DisplayName("Returns a list of accounts")
    void findAll_ShouldReturnListOfAccounts() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ContaResponseDto> contas = new PageImpl<>(List.of(createContaResponseDto(), createContaResponseDto()));

        when(service.findAll(pageable)).thenReturn(contas);

        mvc.perform(get("/api/v1/contas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Returns a empty list of accounts")
    void findAll_ShouldReturnAEmptyListOfAccounts() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(service.findAll(pageable)).thenReturn(Page.empty());

        mvc.perform(get("/api/v1/contas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Returns a account by id")
    void findById_ShouldReturnAccountById() throws Exception {
        Long id = 1L;

        mvc.perform(get("/api/v1/contas/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return HTTP 201 Created when creating a new account")
    void save_ShouldReturnCreateStatus() throws Exception {
        ContaPostDto dto = createContaPostDto();

        mvc.perform(post("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Update account with complete data - Should return No Content status")
    void updateCompleteAccount_ShouldReturnNoContentStatus() throws Exception {
        Long id = 1L;
        ContaPutDto dto = createContaPutDto();

        mvc.perform(put("/api/v1/contas/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Update account with complete data - Should return No Content status")
    void updateIncompleteAccount_ShouldReturnNoContentStatus() throws Exception {
        Long id = 1L;
        ContaPutDto dto = createIncompleteContaPutDto();

        mvc.perform(put("/api/v1/contas/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("Returns 204 after successful deletion")
    void delete_ShouldReturnNoContentStatus() throws Exception {
        Long id = 1L;

        mvc.perform(delete("/api/v1/contas/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 404 if ID is missing in the URL")
    void delete_ShouldReturnBadRequestStatus() throws Exception {
        mvc.perform(delete("/api/v1/contas/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Depositar should return 204 No Content")
    void depositar_ShouldReturnNoContent() throws Exception {
        mvc.perform(put("/api/v1/contas/{id}/depositar", 1L)
                        .param("valor", "100.0"))
                .andExpect(status().isNoContent());

        verify(service).depositar(eq(1L), eq(100.0));
    }

    @Test
    @DisplayName("Sacar should return 204 No Content")
    void sacar_ShouldReturnNoContent() throws Exception {
        mvc.perform(put("/api/v1/contas/{id}/sacar", 1L)
                        .param("valor", "50.0"))
                .andExpect(status().isNoContent());

        verify(service).sacar(eq(1L), eq(50.0));
    }

    @Test
    @DisplayName("Transferir should return 204 No Content")
    void transferir_ShouldReturnNoContent() throws Exception {
        mvc.perform(put("/api/v1/contas/{id}/transferir", 1L)
                        .param("valor", "50.0")
                        .param("idDestino", "2"))
                .andExpect(status().isNoContent());

        verify(service).transferir(eq(1L), eq(2L), eq(50.0));
    }

    private Conta createConta(){
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(1L)
                .agencia(1234L)
                .numero(123456L)
                .tipo(TipoConta.CORRENTE)
                .titular(createPessoa())
                .build();
    }

    private ContaResponseDto createContaResponseDto() {
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(1L)
                .agencia(1234L)
                .numero(123456L)
                .tipo(TipoConta.CORRENTE)
                .saldo(new BigDecimal(1000.00))
                .titularResponseDto(createPessoaResponseDto())
                .buildResponseDto();
    }

    private ContaPostDto createContaPostDto() {
        ContaBuilder builder = new ContaBuilder();

        return builder
                .agencia(1234L)
                .numero(123456L)
                .tipo(TipoConta.CORRENTE)
                .titularResponseDto(createPessoaResponseDto())
                .buildPostDto();
    }

    private ContaPutDto createContaPutDto() {
        ContaBuilder builder = new ContaBuilder();

        return builder
                .agencia(1234L)
                .numero(123456L)
                .tipo(TipoConta.CORRENTE)
                .titularResponseDto(createPessoaResponseDto())
                .buildPutDto();
    }

    private ContaPutDto createIncompleteContaPutDto() {
        ContaBuilder builder = new ContaBuilder();

        return builder
                .agencia(1234L)
                .numero(null)
                .tipo(TipoConta.CORRENTE)
                .titularResponseDto(null)
                .buildPutDto();
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

    private PessoaResponseDto createPessoaResponseDto(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .id(1L)
                .nome("Alice")
                .cpf("12345678911")
                .endereco("Rua 1")
                .telefone("123456789")
                .contaResponseDto(new ContaResponseDto(1L, 1234L, 123456L, TipoConta.CORRENTE, new BigDecimal(1000.00), new PessoaResponseDto(1L, "Alice", "12345678911", "Rua 1", "123456789", null)))
                .buildResponseDto();
    }
}