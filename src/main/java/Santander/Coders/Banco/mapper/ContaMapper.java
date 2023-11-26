package Santander.Coders.Banco.mapper;

import Santander.Coders.Banco.model.Conta;
import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.request.ContaPostDto;
import Santander.Coders.Banco.model.request.ContaPutDto;
import Santander.Coders.Banco.model.request.PessoaPostDto;
import Santander.Coders.Banco.model.request.PessoaPutDto;
import Santander.Coders.Banco.model.response.ContaResponseDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContaMapper {
    ContaResponseDto toResponseDto(Conta conta);

    @Mapping(target = "id", ignore = true)
    Conta postDtoToEntity(ContaPostDto contaPostDto);

    @Mapping(target = "id", ignore = true)
    Conta putDtoToEntity(ContaPutDto contaPutDto);
}
