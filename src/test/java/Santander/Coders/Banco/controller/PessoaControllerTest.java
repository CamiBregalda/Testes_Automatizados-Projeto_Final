package Santander.Coders.Banco.controller;

import Santander.Coders.Banco.exception.EntityNotFoundException;
import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.builder.ContaBuilder;
import Santander.Coders.Banco.model.builder.PessoaBuilder;
import Santander.Coders.Banco.model.request.PessoaPostDto;
import Santander.Coders.Banco.model.request.PessoaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;
import Santander.Coders.Banco.service.ContaService;
import Santander.Coders.Banco.service.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class PessoaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PessoaService service;

    @Test
    @DisplayName("Returns a list of people")
    void findAll_ShouldReturnListOfPeople() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<PessoaResponseDto> pessoas = new PageImpl<>(List.of(createPessoaResponseDto(), createPessoaResponseDto()));

        when(service.findAll(pageable)).thenReturn(pessoas);

        mvc.perform(get("/api/v1/pessoas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Returns a empty list of accounts")
    void findAll_ShouldReturnAEmptyListOfAccounts() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(service.findAll(pageable)).thenReturn(Page.empty());

        mvc.perform(get("/api/v1/pessoas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Returns a people by id")
    void findById_ShouldReturnAccountById() throws Exception {
        Long id = 1L;

        mvc.perform(get("/api/v1/pessoas/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return HTTP 201 Created when creating a new order")
    void save_ShouldReturnCreateStatus() throws Exception {
        PessoaPostDto dto = createPessoaPostDto();

        mvc.perform(post("/api/v1/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Update order with complete data - Should return No Content status")
    void updateCompleteOrder_ShouldReturnNoContentStatus() throws Exception {
        Long id = 1L;
        PessoaPutDto dto = createPessoaPutDto();

        mvc.perform(put("/api/v1/pessoas/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Update people with incomplete data - Should return No Content status")
    void updateIncompleteOrder_ShouldReturnNoContentStatus() throws Exception {
        Long id = 1L;
        PessoaPutDto dto = createIncompletePessoaPostDto();

        mvc.perform(put("/api/v1/pessoas/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 404 if ID is missing in the URL")
    void update_ShouldReturnBadRequestStatus() throws Exception {
        mvc.perform(put("/api/v1/pessoas/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Returns 204 after successful deletion")
    void delete_ShouldReturnNoContentStatus() throws Exception {
        Long id = 1L;

        mvc.perform(delete("/api/v1/pessoas/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 404 if ID is missing in the URL")
    void delete_ShouldReturnBadRequestStatus() throws Exception {
        mvc.perform(delete("/api/v1/pessoas/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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

    private PessoaPutDto createIncompletePessoaPostDto(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .nome("Pedro")
                .cpf(null)
                .endereco("Rua 2")
                .telefone(null)
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