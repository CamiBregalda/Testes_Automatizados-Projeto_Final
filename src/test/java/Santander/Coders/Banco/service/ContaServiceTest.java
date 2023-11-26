package Santander.Coders.Banco.service;

import Santander.Coders.Banco.exception.EntityNotFoundException;
import Santander.Coders.Banco.mapper.ContaMapper;
import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.TipoConta;
import Santander.Coders.Banco.model.Transacao;
import Santander.Coders.Banco.model.builder.ContaBuilder;
import Santander.Coders.Banco.model.builder.PessoaBuilder;
import Santander.Coders.Banco.model.request.ContaPostDto;
import Santander.Coders.Banco.model.request.ContaPutDto;
import Santander.Coders.Banco.model.request.PessoaPostDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;
import Santander.Coders.Banco.repository.ContaRepository;
import Santander.Coders.Banco.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
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
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;
    @Mock
    private ContaRepository contaRepository;
    @Mock
    private TransacaoRepository transacaoRepository;
    @Mock
    private ContaMapper contaMapper;

    @Test
    @DisplayName("Returns a list of accounts")
    void findAll_ShouldReturnListOfAccounts() {
        Pageable pageable = PageRequest.of(0, 10);
        Conta conta = createConta();

        when(contaRepository.save(any(Conta.class))).thenReturn(conta);
        contaRepository.save(conta);
        when(contaRepository.findAllAndCanceledFalse(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(conta), pageable, 1));
        Page<ContaResponseDto> response = contaService.findAll(pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(contaRepository, times(1)).findAllAndCanceledFalse(pageable);
    }

    @Test
    @DisplayName("Returns an empty accounts list")
    void findAll_ShouldReturnAnEmptyListOfAccounts() {
        Pageable pageable = PageRequest.of(0, 10);

        when(contaRepository.findAllAndCanceledFalse(pageable)).thenReturn(new PageImpl<>(Collections.EMPTY_LIST, pageable, 0));
        Page<ContaResponseDto> response = contaService.findAll(pageable);

        assertEquals(0, response.getContent().size());
        verify(contaRepository, times(1)).findAllAndCanceledFalse(pageable);
    }

    @Test
    @DisplayName("Return ContaResponseDto if account is found")
    void findById_ShouldReturnAccountResponseDto() {
        Conta conta = createConta();

        when(contaRepository.save(any(Conta.class))).thenReturn(conta);
        conta = contaRepository.save(conta);
        when(contaRepository.findByIdAndCanceledFalse(conta.getId())).thenReturn(Optional.of(conta));
        when(contaMapper.toResponseDto(conta)).thenReturn(createContaResponseDto());
        ContaResponseDto response = contaService.findById(conta.getId());

        assertNotNull(response);
        assertEquals(conta.getId(), response.id());
    }

    @Test
    @DisplayName("Return EntityNotFoundException if account is not found")
    void findById_WithInvalidOrderId_ShouldThrowEntityNotFoundException() {
        Long orderId = 999L;

        when(contaRepository.findByIdAndCanceledFalse(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contaService.findById(orderId));
    }

    @Test
    @DisplayName("Should return the account after successfully saving it")
    void save_ShouldReturnAccountResponseDto() {
        Conta conta = createConta();
        ContaPostDto contaPostDto = createContaPostDto();

        when(contaMapper.postDtoToEntity(contaPostDto)).thenReturn(conta);
        when(contaRepository.save(conta)).thenReturn(conta);
        when(contaMapper.toResponseDto(conta)).thenReturn(createContaResponseDto());

        ContaResponseDto response = contaService.save(contaPostDto);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(contaPostDto.agencia(), response.agencia()),
                () -> assertEquals(contaPostDto.numero(), response.numero()),
                () -> verify(contaRepository, times(1)).save(any(Conta.class))
        );
    }

    @Test
    @DisplayName("Should not have interactions with the repository when OrderPostDto is null")
    void save_ShouldNotHaveInteractionsWithTheRepository() {
        ContaPostDto contaPostDto = null;

        assertThrows(NullPointerException.class, () -> contaService.save(contaPostDto));

        verifyNoInteractions(contaRepository);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when trying to save a Conta with existing numero")
    void save_ShouldThrowEntityNotFoundExceptionForExistingCPF() {
        Conta conta = createConta();
        ContaPostDto contaPostDto = createContaPostDto();

        when(contaMapper.postDtoToEntity(contaPostDto)).thenReturn(conta);
        when(contaRepository.existsByNumero(conta.getNumero())).thenReturn(true);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> contaService.save(contaPostDto));

        assertEquals("Conta já existente!", exception.getMessage());

        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update the account successfully")
    void update_ShouldUpdateAccounts() {
        Conta contaExistente = createConta();
        ContaPutDto contaPutDto = createContaPutDto();
        Conta contaAtualizada = createContaAtualizada();

        when(contaRepository.save(contaExistente)).thenReturn(contaExistente);
        contaExistente = contaRepository.save(contaExistente);

        when(contaRepository.findById(contaExistente.getId())).thenReturn(Optional.of(contaExistente));
        when(contaMapper.putDtoToEntity(contaPutDto)).thenReturn(contaAtualizada);
        contaExistente = contaRepository.findById(contaExistente.getId()).get();
        contaService.update(contaExistente.getId(), contaPutDto);
        when(contaRepository.findById(contaExistente.getId())).thenReturn(Optional.of(contaAtualizada));
        Optional<Conta> conta = contaRepository.findById(contaExistente.getId());

        if (conta.isPresent()){
            assertNotNull(conta);

            if (contaPutDto.agencia() != null){
                assertEquals(contaPutDto.agencia(), contaAtualizada.getAgencia());
            }
            if (contaPutDto.numero() != null){
                assertEquals(contaPutDto.numero(), contaAtualizada.getNumero());
            }
            if (contaPutDto.numero() != null){
                assertEquals(contaExistente.getTitular(), contaAtualizada.getTitular());
            }
            verify(contaRepository, times(2)).save(any(Conta.class));
        }
    }

    @Test
    @DisplayName("Should update the account incomplete successfully")
    void update_ShouldUpdateIncompletePutAccount() {
        Conta contaExistente = createConta();
        ContaPutDto contaPutDto = createIncompleteContaPutDto();
        Conta contaAtualizada = createContaAtualizadaIncompletePut();

        when(contaRepository.save(contaExistente)).thenReturn(contaExistente);
        contaExistente = contaRepository.save(contaExistente);

        when(contaRepository.findById(contaExistente.getId())).thenReturn(Optional.of(contaExistente));
        when(contaMapper.putDtoToEntity(contaPutDto)).thenReturn(contaAtualizada);
        contaExistente = contaRepository.findById(contaExistente.getId()).get();
        contaService.update(contaExistente.getId(), contaPutDto);
        when(contaRepository.findById(contaExistente.getId())).thenReturn(Optional.of(contaAtualizada));
        Optional<Conta> conta = contaRepository.findById(contaExistente.getId());

        if (conta.isPresent()){
            assertNotNull(conta);

            if (contaPutDto.agencia() != null){
                assertEquals(contaPutDto.agencia(), contaAtualizada.getAgencia());
            }
            if (contaPutDto.numero() != null){
                assertEquals(contaPutDto.numero(), contaAtualizada.getNumero());
            }
            if (contaPutDto.numero() != null){
                assertEquals(contaExistente.getTitular(), contaAtualizada.getTitular());
            }
            verify(contaRepository, times(2)).save(any(Conta.class));
        }
    }

    @Test
    @DisplayName("Should logically delete the account")
    void delete_ShouldDeleteAccount() {
        Long id = 1L;
        Conta conta = createConta();

        when(contaRepository.findById(id)).thenReturn(Optional.of(conta));
        contaService.delete(id);

        verify(contaRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Return EntityNotFoundException if account is not found")
    void delete_ShouldThrowEntityNotFoundException() {
        Long id = 999L;

        when(contaRepository.findById(id)).thenThrow(new EntityNotFoundException("Conta não encontrada."));

        assertThrows(EntityNotFoundException.class, () -> contaService.delete(id));
    }

    @Test
    void depositar_ShouldDepositAndSaveTransaction() {
        Double valor = 100.0;
        Conta conta = createConta();
        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));

        contaService.depositar(conta.getId(), valor);

        verify(contaRepository, times(1)).findById(conta.getId());
        verify(contaRepository, times(1)).save(conta);
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void depositar_ShouldWithdrawAndSaveTransaction() {
        Double valor = 0.0;
        Conta conta = createConta();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> conta.depositar(new BigDecimal(valor)));
        assertEquals("O valor do depósito deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void sacar_ShouldWithdrawAndSaveTransaction() {
        Double valor = 50.0;
        Conta conta = createConta();
        conta.setSaldo(new BigDecimal(100.0));
        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));

        contaService.sacar(conta.getId(), valor);

        verify(contaRepository, times(1)).findById(conta.getId());
        verify(contaRepository, times(1)).save(conta);
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void sacar_ShouldThrowExceptionWhenInsufficientBalance() {
        Double valor = 50.0;
        Conta conta = createConta();
        when(contaRepository.findById(conta.getId())).thenReturn(java.util.Optional.of(conta));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contaService.sacar(conta.getId(), valor));
        assertEquals("Saldo insuficiente para realizar o saque.", exception.getMessage());
    }

    @Test
    void transferir_ShouldTransferAndSaveTransactions() {
        Double valor = 75.0;
        Conta contaOrigem = createConta();
        contaOrigem.setSaldo(new BigDecimal(1000.00));
        Conta contaDestino = createContaDestino();
        when(contaRepository.findById(contaOrigem.getId())).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));

        contaService.transferir(contaOrigem.getId(), contaDestino.getId(), valor);

        verify(contaRepository, times(2)).findById(anyLong());
        verify(contaRepository, times(2)).save(any(Conta.class));
        verify(transacaoRepository, times(2)).save(any(Transacao.class));
    }

    @Test
    void transferir_ShouldThrowExceptionWhenInsufficientBalance() {
        Double valor = 75.0;
        Conta contaOrigem = createConta();
        Conta contaDestino = createContaDestino();
        when(contaRepository.findById(contaOrigem.getId())).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contaService.transferir(contaOrigem.getId(), contaDestino.getId(), valor));
        assertEquals("Saldo insuficiente para realizar a transferência.", exception.getMessage());
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

    private Conta createContaAtualizada(){
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(1L)
                .agencia(9875L)
                .numero(987654L)
                .tipo(TipoConta.CORRENTE)
                .titular(createPessoa())
                .build();
    }

    private Conta createContaAtualizadaIncompletePut(){
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(1L)
                .agencia(9875L)
                .numero(123456L)
                .tipo(TipoConta.CORRENTE)
                .titular(createPessoa())
                .build();
    }

    private Conta createContaDestino(){
        ContaBuilder builder = new ContaBuilder();

        return builder
                .id(2L)
                .agencia(987L)
                .numero(987654L)
                .tipo(TipoConta.CORRENTE)
                .titular(createPessoaDestino())
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
                .agencia(9875L)
                .numero(987654L)
                .tipo(TipoConta.CORRENTE)
                .titularResponseDto(createPessoaResponseDto())
                .buildPutDto();
    }

    private ContaPutDto createIncompleteContaPutDto() {
        ContaBuilder builder = new ContaBuilder();

        return builder
                .agencia(9875L)
                .numero(null)
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
                .conta(null)
                .build();
    }

    private Pessoa createPessoaDestino(){
        PessoaBuilder builder = new PessoaBuilder();

        return builder
                .id(2L)
                .nome("Pedro")
                .cpf("9876543521")
                .endereco("Rua 2")
                .telefone("987654321")
                .conta(new Conta(2L, 987L, 987654L, null))
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
                .contaResponseDto(null)
                .buildResponseDto();
    }
}