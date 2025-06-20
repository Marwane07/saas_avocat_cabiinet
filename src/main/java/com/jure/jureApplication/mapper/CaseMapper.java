package com.jure.jureApplication.mapper;


import com.jure.jureApplication.persistent.dto.CaseDTO;
import com.jure.jureApplication.persistent.model.Case;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CaseMapper {

    CaseMapper INSTANCE = Mappers.getMapper(CaseMapper.class);

    @Mapping(source = "assignedUser.id", target = "assignedUserId")
    @Mapping(source = "assignedUser.nomComplet", target = "assignedUserName")
    @Mapping(source = "cabinet.id", target = "cabinetId")
    @Mapping(source = "cabinet.nomCabinet", target = "cabinetName")
    CaseDTO caseToDto(Case caseEntity);

    List<CaseDTO> casesToDtos(List<Case> cases);

    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "cabinet", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    Case dtoToCase(CaseDTO caseDTO);
}
