package Santander.Coders.Banco.mapper;

import Santander.Coders.Banco.model.Pessoa;
import Santander.Coders.Banco.model.request.PessoaPostDto;
import Santander.Coders.Banco.model.request.PessoaPutDto;
import Santander.Coders.Banco.model.response.PessoaResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PessoaMapper {
    PessoaResponseDto toResponseDto(Pessoa pessoa);

    @Mapping(target = "id", ignore = true)
    Pessoa postDtoToEntity(PessoaPostDto pessoaPostDto);

    @Mapping(target = "id", ignore = true)
    Pessoa putDtoToEntity(PessoaPutDto pessoaPutDto);
}
