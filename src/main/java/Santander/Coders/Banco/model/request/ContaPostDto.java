package Santander.Coders.Banco.model.request;

import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.TipoConta;
import jakarta.validation.constraints.NotNull;

public record ContaPostDto(
        Long id,
        @NotNull
        Long agencia,
        @NotNull
        Long numero,
        @NotNull
        TipoConta tipo,
        Pessoa titular
) {
}
