package Santander.Coders.Banco.model.request;

import Santander.Coders.Banco.model.Conta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PessoaPutDto(
        String nome,
        String cpf,
        String endereco,
        String telefone,
        Conta conta
) {
}
