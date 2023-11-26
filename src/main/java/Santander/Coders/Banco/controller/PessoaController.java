package Santander.Coders.Banco.controller;

import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.request.ContaPostDto;
import Santander.Coders.Banco.model.request.ContaPutDto;
import Santander.Coders.Banco.model.request.PessoaPostDto;
import Santander.Coders.Banco.model.request.PessoaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;
import Santander.Coders.Banco.service.ContaService;
import Santander.Coders.Banco.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pessoas")
@RequiredArgsConstructor
public class PessoaController {
    private final PessoaService pessoaService;

    @GetMapping
    public Page<PessoaResponseDto> findAll(
            Pageable pageable
    ) {
        return pessoaService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public PessoaResponseDto findById(
            @PathVariable Long id
    ) {
        return pessoaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PessoaResponseDto save(
            @RequestBody @Valid PessoaPostDto pessoaPostDto
    ) {
        return pessoaService.save(pessoaPostDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid PessoaPutDto pessoaPutDto) {
        pessoaService.update(id, pessoaPutDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        pessoaService.delete(id);
    }
}
