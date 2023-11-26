package Santander.Coders.Banco.model.response;

import Santander.Coders.Banco.model.Conta;
import jakarta.persistence.*;

import java.util.List;

public record PessoaResponseDto(
        Long id,
        String nome,
        String cpf,
        String endereco,
        String telefone,
        ContaResponseDto conta
) {
}
