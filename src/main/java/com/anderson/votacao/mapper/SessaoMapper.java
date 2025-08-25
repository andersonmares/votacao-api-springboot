package com.anderson.votacao.mapper;

import com.anderson.votacao.dto.SessaoResponseDTO;
import com.anderson.votacao.entity.Sessao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SessaoMapper {

    @Mapping(target = "pautaId", source = "pauta.id")
    @Mapping(
            target = "duracaoMinutos",
            expression = "java((int) java.time.Duration.between(entity.getDataHoraInicio(), entity.getDataHoraFim()).toMinutes())"
    )
    SessaoResponseDTO toResponse(Sessao entity);

    List<SessaoResponseDTO> toResponseList(List<Sessao> sessoes);
}