package com.intellias.parking.service;

import com.intellias.parking.persistence.entity.ItemEntity;

import java.util.List;
import java.util.UUID;

public interface ItemService {
    ItemEntity save(ItemEntity item);

    ItemEntity update(UUID id, ItemEntity item);

    ItemEntity getById(UUID id);

    List<ItemEntity> findAll();

    void delete(UUID id);
}
