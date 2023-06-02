package com.intellias.basicsandbox.service.impl;

import com.intellias.basicsandbox.persistence.ItemRepository;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import com.intellias.basicsandbox.service.exception.ItemNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// The transactional functionality rely on enabled Transaction Management in PersistenceConfig
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class TransactionalItemService implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public ItemEntity save(ItemEntity item) {
        return itemRepository.save(item);
    }

    @Override
    public ItemEntity update(UUID id, ItemEntity item) {
        return itemRepository.findById(id)
                .map(attachedEntity -> {
                    attachedEntity.setName(item.getName());
                    attachedEntity.setCreditCard(item.getCreditCard());
                    attachedEntity.setCurrencyCode(item.getCurrencyCode());
                    itemRepository.save(attachedEntity);
                    return attachedEntity;
                })
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemEntity getById(UUID id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemEntity> findAll() {
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false) // Note: if you use more advanced repository
                // interface like JpaRepository it usually has better type [List, Stream] for method findAll()
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        itemRepository.deleteById(id);
    }
}
