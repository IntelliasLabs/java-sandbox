package com.intellias.basicsandbox.domain;

import com.intellias.basicsandbox.service.exception.ItemAlreadyExistsException;
import com.intellias.basicsandbox.persistence.ItemRepository;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.dto.ItemDTO;
import com.intellias.basicsandbox.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void whenItemToCreateAlreadyExistsThenThrows() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemToCreate = new ItemDTO(itemId, "Item name");

        when(itemRepository.existsById(itemId)).thenReturn(true);
        assertThatThrownBy(() -> itemService.save(itemToCreate))
                .isInstanceOf(ItemAlreadyExistsException.class)
                .hasMessage("An item with ID " + itemId + " already exists.");
    }

    @Test
    void whenItemToReadDoesNotExistThenSave() {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemName = "Item name";

        var itemToCreate = new ItemDTO(itemId, itemName);
        var itemEntity = new ItemEntity(itemId, itemName);

        when(itemRepository.existsById(itemId)).thenReturn(false);
        when(itemRepository.save(itemEntity)).thenReturn(itemEntity);

        ItemDTO savedItem = itemService.save(itemToCreate);

        assertEquals(savedItem.getId(), itemToCreate.getId());
        assertEquals(savedItem.getName(), itemToCreate.getName());
    }

}
