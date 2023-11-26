package Santander.Coders.Banco.model.builder;

import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.request.PessoaPostDto;
import Santander.Coders.Banco.model.request.PessoaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;

import java.math.BigDecimal;
import java.util.List;

public class PessoaBuilder {
    private Long id;
    private String nome;
    private String cpf;
    private String endereco;
    private String telefone;
    private Conta conta;
    private ContaResponseDto contaResponseDto;

    public PessoaBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PessoaBuilder nome(String nome){
        this.nome = nome;
        return this;
    }

    public PessoaBuilder cpf(String cpf){
        this.cpf = cpf;
        return this;
    }

    public PessoaBuilder endereco(String endereco){
        this.endereco = endereco;
        return this;
    }

    public PessoaBuilder telefone(String telefone){
        this.telefone = telefone;
        return this;
    }

    public PessoaBuilder conta(Conta conta){
        this.conta = conta;
        return this;
    }

    public PessoaBuilder contaResponseDto(ContaResponseDto contaResponseDto){
        this.contaResponseDto = contaResponseDto;
        return this;
    }

    public Pessoa build(){
        return new Pessoa(id, nome, cpf, endereco, telefone, conta);
    }

    public PessoaResponseDto buildResponseDto(){
        return new PessoaResponseDto(id, nome, cpf, endereco, telefone, contaResponseDto);
    }

    public PessoaPostDto buildPostDto(){
        return new PessoaPostDto(id, nome, cpf, endereco, telefone, conta);
    }

    public PessoaPutDto buildPutDto(){
        return new PessoaPutDto(nome, cpf, endereco, telefone, conta);
    }
}
