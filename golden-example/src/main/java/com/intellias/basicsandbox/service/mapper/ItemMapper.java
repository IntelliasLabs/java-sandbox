package com.intellias.basicsandbox.service.mapper;

import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.dto.ItemDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ItemMapper {
    ItemEntity toEntity(final ItemDTO item);
    ItemDTO toDTO(final ItemEntity itemEntity);
}
