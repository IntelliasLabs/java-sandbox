package com.intellias.basicsandbox.domain;

import com.intellias.basicsandbox.persistence.ItemRepository;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void whenItemExistsThenGetById() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemEntity = new ItemEntity(itemId, "Item name");
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));

        ItemEntity existItem = itemService.getById(itemId);

        assertEquals(existItem.getId(), itemId);
        assertEquals(existItem.getName(), itemEntity.getName());
    }

    @Test
    void whenItemDoesNotExistThenSave() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemName = "Item name";

        var itemToCreate = new ItemEntity(null, itemName);
        var itemEntity = new ItemEntity(itemId, itemName);

        when(itemRepository.save(any())).thenReturn(itemEntity);

        ItemEntity savedItem = itemService.save(itemToCreate);

        assertEquals(savedItem.getId(), itemId);
        assertEquals(savedItem.getName(), itemToCreate.getName());
    }

}
