package Santander.Coders.Banco.controller;

import Santander.Coders.Banco.model.request.ContaPostDto;
import Santander.Coders.Banco.model.request.ContaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contas")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService contaService;

    @GetMapping
    public Page<ContaResponseDto> findAll(
            Pageable pageable
    ) {
        return contaService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ContaResponseDto findById(
            @PathVariable Long id
    ) {
        return contaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaResponseDto save(
            @RequestBody @Valid ContaPostDto contaPostDto
    ) {
        return contaService.save(contaPostDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid ContaPutDto contaPutDto) {
        contaService.update(id, contaPutDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        contaService.delete(id);
    }

    @PutMapping("/{id}/depositar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void depositar(
            @PathVariable Long id,
            @RequestParam Double valor
    ) {
        contaService.depositar(id, valor);
    }

    @PutMapping("/{id}/sacar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sacar(
            @PathVariable Long id,
            @RequestParam Double valor
    ) {
        contaService.sacar(id, valor);
    }

    @PutMapping("/{id}/transferir")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferir(
            @PathVariable Long id,
            @RequestParam Double valor,
            @RequestParam Long idDestino
    ) {
        contaService.transferir(id, idDestino, valor);
    }
}
