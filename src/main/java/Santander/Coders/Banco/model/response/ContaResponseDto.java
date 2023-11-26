package Santander.Coders.Banco.model.response;

import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.TipoConta;

import java.math.BigDecimal;
import java.util.List;

public record ContaResponseDto(
        Long id,
        Long agencia,
        Long numero,
        TipoConta tipo,
        BigDecimal saldo,
        PessoaResponseDto titular
) {
}
