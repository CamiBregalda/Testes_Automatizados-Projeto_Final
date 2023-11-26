package Santander.Coders.Banco.service;

import Santander.Coders.Banco.exception.EntityNotFoundException;
import Santander.Coders.Banco.mapper.ContaMapper;
import Santander.Coders.Banco.mapper.PessoaMapper;
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
import Santander.Coders.Banco.repository.ContaRepository;
import Santander.Coders.Banco.repository.PessoaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;
    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    private PessoaMapper pessoaMapper;

    @Test
    @DisplayName("Returns a list of accounts")
    void findAll_ShouldReturnListOfAccounts() {
        Pageable pageable = PageRequest.of(0, 10);
        Pessoa pessoa = createPessoa();

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        pessoaRepository.save(pessoa);
        when(pessoaRepository.findAllAndInativedFalse(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(pessoa), pageable, 1));
        Page<PessoaResponseDto> response = pessoaService.findAll(pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(pessoaRepository, times(1)).findAllAndInativedFalse(pageable);
    }

    @Test
    @DisplayName("Returns an empty accounts list")
    void findAll_ShouldReturnAnEmptyListOfAccounts() {
        Pageable pageable = PageRequest.of(0, 10);

        when(pessoaRepository.findAllAndInativedFalse(pageable)).thenReturn(new PageImpl<>(Collections.EMPTY_LIST, pageable, 0));
        Page<PessoaResponseDto> response = pessoaService.findAll(pageable);

        assertEquals(0, response.getContent().size());
        verify(pessoaRepository, times(1)).findAllAndInativedFalse(pageable);
    }

    @Test
    @DisplayName("Return ContaResponseDto if account is found")
    void findById_ShouldReturnAccountResponseDto() {
        Pessoa pessoa = createPessoa();

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        pessoa = pessoaRepository.save(pessoa);
        when(pessoaRepository.findByIdAndInativedFalse(pessoa.getId())).thenReturn(Optional.of(pessoa));
        when(pessoaMapper.toResponseDto(pessoa)).thenReturn(createPessoaResponseDto());
        PessoaResponseDto response = pessoaService.findById(pessoa.getId());

        assertNotNull(response);
        assertEquals(pessoa.getId(), response.id());
    }

    @Test
    @DisplayName("Return EntityNotFoundException if account is not found")
    void findById_WithInvalidOrderId_ShouldThrowEntityNotFoundException() {
        Long orderId = 999L;

        when(pessoaRepository.findByIdAndInativedFalse(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pessoaService.findById(orderId));
    }

    @Test
    @DisplayName("Should return the account after successfully saving it")
    void save_ShouldReturnAccountResponseDto() {
        Pessoa pessoa = createPessoa();
        PessoaPostDto pessoaPostDto = createPessoaPostDto();

        when(pessoaMapper.postDtoToEntity(pessoaPostDto)).thenReturn(pessoa);
        when(pessoaRepository.save(pessoa)).thenReturn(pessoa);
        when(pessoaMapper.toResponseDto(pessoa)).thenReturn(createPessoaResponseDto());
        PessoaResponseDto response = pessoaService.save(pessoaPostDto);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(pessoaPostDto.nome(), response.nome()),
                () -> assertEquals(pessoaPostDto.cpf(), response.cpf()),
                () -> assertEquals(pessoaPostDto.endereco(), response.endereco()),
                () -> assertEquals(pessoaPostDto.telefone(), response.telefone()),
                () -> verify(pessoaRepository, times(1)).save(any(Pessoa.class))
        );
    }

    @Test
    @DisplayName("Should not have interactions with the repository when PessoaPostDto is null")
    void save_ShouldNotHaveInteractionsWithTheRepository() {
        PessoaPostDto pessoaPostDto = null;

        assertThrows(NullPointerException.class, () -> pessoaService.save(pessoaPostDto));

        verifyNoInteractions(pessoaRepository);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when trying to save a Pessoa with existing CPF")
    void save_ShouldThrowEntityNotFoundExceptionForExistingCPF() {
        Pessoa pessoa = createPessoa();
        PessoaPostDto pessoaPostDto = createPessoaPostDto();

        when(pessoaMapper.postDtoToEntity(pessoaPostDto)).thenReturn(pessoa);
        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(true);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> pessoaService.save(pessoaPostDto));

        assertEquals("Pessoa já cadastrada!", exception.getMessage());

        verify(pessoaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update the people successfully")
    void update_ShouldUpdatePeople() {
        Pessoa pessoaExistente = createPessoa();
        PessoaPutDto pessoaPutDto = createPessoaPutDto();
        Pessoa pessoaAtualizada = createPessoaAtualizada();

        when(pessoaRepository.save(pessoaExistente)).thenReturn(pessoaExistente);
        pessoaExistente = pessoaRepository.save(pessoaExistente);

        when(pessoaRepository.findById(pessoaExistente.getId())).thenReturn(Optional.of(pessoaExistente));
        when(pessoaMapper.putDtoToEntity(pessoaPutDto)).thenReturn(pessoaAtualizada);
        pessoaExistente = pessoaRepository.findById(pessoaExistente.getId()).get();
        pessoaService.update(pessoaExistente.getId(), pessoaPutDto);
        when(pessoaRepository.findById(pessoaExistente.getId())).thenReturn(Optional.of(pessoaAtualizada));
        Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaExistente.getId());

        if (pessoa.isPresent()){
            assertNotNull(pessoa);

            if (pessoaPutDto.nome() != null){
                assertEquals(pessoaPutDto.nome(), pessoaAtualizada.getNome());
            }
            if (pessoaPutDto.cpf() != null){
                assertEquals(pessoaPutDto.cpf(), pessoaAtualizada.getCpf());
            }
            if (pessoaPutDto.endereco() != null){
                assertEquals(pessoaPutDto.endereco(), pessoaAtualizada.getEndereco());
            }
            if (pessoaPutDto.telefone() != null){
                assertEquals(pessoaPutDto.telefone(), pessoaAtualizada.getTelefone());
            }
            if (pessoaPutDto.conta() != null){
                assertEquals(pessoaPutDto.conta(), pessoaAtualizada.getConta());
            }
            verify(pessoaRepository, times(2)).save(any(Pessoa.class));
        }
    }

    @Test
    @DisplayName("Should update the people successfully")
    void update_ShouldUpdateIncompletePutPeople() {
        Pessoa pessoaExistente = createPessoa();
        PessoaPutDto pessoaPutDto = createPessoaPutDto();
        Pessoa pessoaAtualizada = createPessoaAtualizada();

        when(pessoaRepository.save(pessoaExistente)).thenReturn(pessoaExistente);
        pessoaExistente = pessoaRepository.save(pessoaExistente);

        when(pessoaRepository.findById(pessoaExistente.getId())).thenReturn(Optional.of(pessoaExistente));
        when(pessoaMapper.putDtoToEntity(pessoaPutDto)).thenReturn(pessoaAtualizada);
        pessoaExistente = pessoaRepository.findById(pessoaExistente.getId()).get();
        pessoaService.update(pessoaExistente.getId(), pessoaPutDto);
        when(pessoaRepository.findById(pessoaExistente.getId())).thenReturn(Optional.of(pessoaAtualizada));
        Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaExistente.getId());

        if (pessoa.isPresent()){
            assertNotNull(pessoa);

            if (pessoaPutDto.nome() != null){
                assertEquals(pessoaPutDto.nome(), pessoaAtualizada.getNome());
            }
            if (pessoaPutDto.cpf() != null){
                assertEquals(pessoaPutDto.cpf(), pessoaAtualizada.getCpf());
            }
            if (pessoaPutDto.endereco() != null){
                assertEquals(pessoaPutDto.endereco(), pessoaAtualizada.getEndereco());
            }
            if (pessoaPutDto.telefone() != null){
                assertEquals(pessoaPutDto.telefone(), pessoaAtualizada.getTelefone());
            }
            if (pessoaPutDto.conta() != null){
                assertEquals(pessoaPutDto.conta(), pessoaAtualizada.getConta());
            }
            verify(pessoaRepository, times(2)).save(any(Pessoa.class));
        }
    }

    @Test
    @DisplayName("Should delete order and order items")
    void delete_ShouldDeleteOrderAndOrderItems() {
        Long id = 1L;
        Pessoa pessoa = createPessoa();

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));
        pessoaService.delete(id);

        verify(pessoaRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Return EntityNotFoundException if account is not found")
    void delete_ShouldThrowEntityNotFoundException() {
        Long id = 999L;

        when(pessoaRepository.findById(id)).thenThrow(new EntityNotFoundException("Pessoa não encontrada."));

        assertThrows(EntityNotFoundException.class, () -> pessoaService.delete(id));
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

    private Pessoa createPessoaAtualizada(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .id(1L)
                .nome("Pedro")
                .cpf("98765432100")
                .endereco("Rua 2")
                .telefone("987654321")
                .conta(createConta())
                .build();
    }

    private Pessoa createPessoaAtualizadaIncompletePut(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .id(1L)
                .nome("Pedro")
                .cpf("12345678911")
                .endereco("Rua 2")
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