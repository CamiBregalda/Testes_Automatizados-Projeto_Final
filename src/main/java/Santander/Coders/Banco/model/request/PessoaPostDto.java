package Santander.Coders.Banco.model.request;

import Santander.Coders.Banco.model.Conta;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PessoaPostDto(
        Long id,
        @NotBlank
        String nome,
        @NotBlank
        String cpf,
        @NotBlank
        String endereco,
        @NotBlank
        String telefone,
        Conta conta
) {
}
