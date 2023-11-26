package Santander.Coders.Banco.model.builder;

import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.TipoConta;
import Santander.Coders.Banco.model.request.ContaPostDto;
import Santander.Coders.Banco.model.request.ContaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ContaBuilder {
    private Long id;
    private Long agencia;
    private Long numero;
    private TipoConta tipo;
    private BigDecimal saldo;
    private Pessoa titular;
    private PessoaResponseDto titularResponseDto;

    public ContaBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ContaBuilder agencia(Long agencia){
        this.agencia = agencia;
        return this;
    }

    public ContaBuilder numero(Long numero){
        this.numero = numero;
        return this;
    }

    public ContaBuilder saldo(BigDecimal saldo){
        this.saldo = saldo;
        return this;
    }

    public ContaBuilder tipo(TipoConta tipo){
        this.tipo = tipo;
        return this;
    }

    public ContaBuilder titular(Pessoa titular){
        this.titular = titular;
        return this;
    }

    public ContaBuilder titularResponseDto(PessoaResponseDto titularResponseDto){
        this.titularResponseDto = titularResponseDto;
        return this;
    }

    public Conta build(){
        return new Conta(id, agencia, numero, titular);
    }

    public ContaResponseDto buildResponseDto(){
        return new ContaResponseDto(id, agencia, numero, tipo, saldo, titularResponseDto);
    }

    public ContaPostDto buildPostDto(){
        return new ContaPostDto(id, agencia, numero, tipo, titular);
    }

    public ContaPutDto buildPutDto(){
        return new ContaPutDto(agencia, numero, tipo, titular);
    }
}
