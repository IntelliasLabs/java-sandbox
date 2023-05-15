package com.intellias.basicsandbox.service.impl;

import com.intellias.basicsandbox.service.exception.ItemAlreadyExistsException;
import com.intellias.basicsandbox.service.exception.ItemNotFoundException;
import com.intellias.basicsandbox.persistence.ItemRepository;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import com.intellias.basicsandbox.service.dto.ItemDTO;
import com.intellias.basicsandbox.service.mapper.ItemMapper;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    private final ItemRepository itemRepository;

    @Override
    public ItemDTO save(ItemDTO item) {
        ItemEntity itemEntity = itemMapper.toEntity(item);
        final ItemEntity savedEntity = itemRepository.save(itemEntity);
        return itemMapper.toDTO(savedEntity);
    }

    @Override
    public ItemDTO update(UUID id, ItemDTO item) {
        if (!itemRepository.existsById(id)){
            throw new ItemNotFoundException(id);
        }

        itemRepository.updateItem(item.getName(), id);

        return itemRepository.findById(id)
                .map(itemMapper::toDTO)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDTO getById(UUID id) {
        return itemRepository.findById(id)
                .map(itemMapper::toDTO)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> findAll() {
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false) // Note: if you use more advanced repository
                // interface like JpaRepository it usually has better type [List, Stream] for method findAll()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        itemRepository.deleteById(id);
    }
}
