package com.intellias.basicsandbox.controller.mapper;

import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.controller.dto.ItemDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ItemMapper {
    ItemEntity toEntity(final ItemDTO item);
    ItemDTO toDTO(final ItemEntity itemEntity);
}
