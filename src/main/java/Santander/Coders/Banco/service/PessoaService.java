package Santander.Coders.Banco.service;

import Santander.Coders.Banco.exception.EntityNotFoundException;
import Santander.Coders.Banco.mapper.PessoaMapper;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.request.PessoaPostDto;
import Santander.Coders.Banco.model.request.PessoaPutDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;
import Santander.Coders.Banco.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PessoaService {
    private PessoaRepository pessoaRepository;
    private PessoaMapper pessoaMapper;

    public Page<PessoaResponseDto> findAll(Pageable pageable) {
        Page<Pessoa> pessoas = pessoaRepository.findAllAndInativedFalse(pageable);

        return pessoas.isEmpty() ? Page.empty() : pessoas.map(pessoaMapper::toResponseDto);
    }


    public PessoaResponseDto findById(Long id) {
        Pessoa pessoa = pessoaRepository.findByIdAndInativedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        return pessoaMapper.toResponseDto(pessoa);
    }

    public PessoaResponseDto save(PessoaPostDto pessoaPostDto) {
        Pessoa pessoa = pessoaMapper.postDtoToEntity(pessoaPostDto);

        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new EntityNotFoundException("Pessoa já cadastrada!");
        }

        return pessoaMapper.toResponseDto(pessoaRepository.save(pessoa));
    }

    public void update(Long id, PessoaPutDto pessoaPutDto) {
        Pessoa pessoa = pessoaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        pessoa.update(pessoaMapper.putDtoToEntity(pessoaPutDto));
        pessoaRepository.save(pessoa);
    }

    public void delete(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        pessoa.delete();
        pessoaRepository.save(pessoa);
    }
}
