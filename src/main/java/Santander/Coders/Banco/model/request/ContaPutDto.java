package Santander.Coders.Banco.model.request;

import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.TipoConta;

import java.math.BigDecimal;
import java.util.List;

public record ContaPutDto(
        Long agencia,
        Long numero,
        TipoConta tipo,
        Pessoa titular
) {
}
