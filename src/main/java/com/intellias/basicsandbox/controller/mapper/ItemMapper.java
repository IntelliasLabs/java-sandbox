package com.intellias.basicsandbox.controller.mapper;

import com.intellias.basicsandbox.controller.dto.item.ItemRequestDTO;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.controller.dto.item.ItemSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemEntity toEntityFromRequestDTO(final ItemRequestDTO item);

    ItemSummaryDTO toSummaryDTO(final ItemEntity itemEntity);
}
