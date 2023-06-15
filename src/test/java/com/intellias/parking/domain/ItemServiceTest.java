package com.intellias.parking.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.intellias.parking.service.ItemService;
import com.intellias.parking.service.impl.TransactionalItemService;
import com.intellias.parking.persistence.ItemRepository;
import com.intellias.parking.persistence.entity.ItemEntity;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemService = new TransactionalItemService(itemRepository);
    }

    @Test
    void whenItemExistsThenGetById() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemEntity = new ItemEntity(itemId, "Item name", null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));

        ItemEntity existItem = itemService.getById(itemId);

        assertEquals(existItem.getId(), itemId);
        assertEquals(existItem.getName(), itemEntity.getName());
    }

    @Test
    void whenItemDoesNotExistThenSave() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemName = "Item name";

        var itemToCreate = new ItemEntity(null, itemName, null);
        var itemEntity = new ItemEntity(itemId, itemName, null);

        when(itemRepository.save(any())).thenReturn(itemEntity);

        ItemEntity savedItem = itemService.save(itemToCreate);

        assertEquals(savedItem.getId(), itemId);
        assertEquals(savedItem.getName(), itemToCreate.getName());
    }

    @Test
    void updateItemById() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemEntity = new ItemEntity(itemId, "Item name", null);
        ItemEntity changedItem = new ItemEntity(itemId, "Item new name", "Credit card #1");
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));
        when(itemRepository.save(eq(changedItem))).thenReturn(changedItem);

        ItemEntity updatedItem = itemService.update(itemId, changedItem);

        assertEquals(updatedItem.getId(), itemId);
        assertEquals(updatedItem.getName(), changedItem.getName());
        assertEquals(updatedItem.getCreditCard(), changedItem.getCreditCard());
    }

}
