package Santander.Coders.Banco.service;

import Santander.Coders.Banco.exception.EntityNotFoundException;
import Santander.Coders.Banco.mapper.ContaMapper;
import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.TipoTransacao;
import Santander.Coders.Banco.model.Transacao;
import Santander.Coders.Banco.model.request.ContaPostDto;
import Santander.Coders.Banco.model.request.ContaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.repository.ContaRepository;
import Santander.Coders.Banco.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ContaService {
    private final ContaRepository contaRepository;
    private final ContaMapper contaMapper;
    private final TransacaoRepository transacaoRepository;

    public Page<ContaResponseDto> findAll(Pageable pageable) {
        Page<Conta> contas = contaRepository.findAllAndCanceledFalse(pageable);

        return contas.isEmpty() ? Page.empty() : contas.map(contaMapper::toResponseDto);
    }


    public ContaResponseDto findById(Long id) {
            Conta conta = contaRepository.findByIdAndCanceledFalse(id)
                    .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada."));

            return contaMapper.toResponseDto(conta);
    }

    public ContaResponseDto save(ContaPostDto contaPostDto) {
        Conta conta = contaMapper.postDtoToEntity(contaPostDto);

        if (contaRepository.existsByNumero(conta.getNumero())) {
            throw new EntityNotFoundException("Conta já existente!");
        }

        return contaMapper.toResponseDto(contaRepository.save(conta));
    }

    public void update(Long id, ContaPutDto contaPutDto) {
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Conta não encontrada."));

        conta.update(contaMapper.putDtoToEntity(contaPutDto));
        contaRepository.save(conta);
    }

    public void delete(Long id) {
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Conta não encontrada."));

        conta.delete();
        contaRepository.save(conta);
    }

    public void depositar(Long id, Double valor) {
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Conta não encontrada."));

        conta.depositar(new BigDecimal(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao(conta.getId(), TipoTransacao.TRANSFERENCIA_RECEBIMENTO, new BigDecimal(valor), LocalDateTime.now());
        transacaoRepository.save(transacao);
    }

    public void sacar(Long id, Double valor) {
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Conta não encontrada."));

        conta.sacar(new BigDecimal(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao(conta.getId(), TipoTransacao.TRANSFERENCIA_ENVIO, new BigDecimal(valor), LocalDateTime.now());
        transacaoRepository.save(transacao);
    }

    public void transferir(Long id, Long idDestino, Double valor) {
        Conta contaOrigem = contaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Conta de origem não encontrada."));
        Conta contaDestino = contaRepository.findById(idDestino).orElseThrow(() -> new EntityNotFoundException("Conta de destino não encontrada."));

        contaOrigem.transferir(contaDestino, new BigDecimal(valor));
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);


        Transacao transacao = new Transacao(contaOrigem.getId(), TipoTransacao.TRANSFERENCIA_ENVIO, new BigDecimal(valor), LocalDateTime.now());
        transacaoRepository.save(transacao);

        transacao = new Transacao(contaDestino.getId(), TipoTransacao.TRANSFERENCIA_ENVIO, new BigDecimal(valor), LocalDateTime.now());
        transacaoRepository.save(transacao);
    }
}
