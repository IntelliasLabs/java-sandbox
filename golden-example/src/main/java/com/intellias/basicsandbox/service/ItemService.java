package com.intellias.basicsandbox.service;

import com.intellias.basicsandbox.service.dto.ItemDTO;

import java.util.List;
import java.util.UUID;

public interface ItemService {
    ItemDTO save(ItemDTO item);

    void update(ItemDTO item);

    ItemDTO getById(UUID id);

    List<ItemDTO> findAll();

    void delete(UUID id);
}
