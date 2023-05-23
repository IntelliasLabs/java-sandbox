package com.intellias.basicsandbox.controller;

import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import com.intellias.basicsandbox.controller.dto.ItemDTO;
import com.intellias.basicsandbox.service.exception.ItemAlreadyExistsException;
import com.intellias.basicsandbox.service.exception.ItemNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static com.intellias.basicsandbox.utils.TestUtils.asJsonString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    void whenGetItemNotExistingThenShouldReturn404() throws Exception {
        var itemId = UUID.randomUUID();
        given(itemService.getById(itemId)).willThrow(ItemNotFoundException.class);

        mockMvc.perform(get(ItemController.API_VERSION + ItemController.PATH + "/" + itemId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetItemByIdFoundThenReturn200() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var item = new ItemEntity(itemId, "Item name", null);
        given(itemService.getById(itemId)).willReturn(item);

        mockMvc.perform(get(ItemController.API_VERSION + ItemController.PATH + "/" + itemId))
                .andExpect(status().isOk());
    }

    @Test
    void whenSaveItemThenReturn201() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemDTO = new ItemDTO(itemId, "Item name", null);
        var item = new ItemEntity(itemId, "Item name", null);
        given(itemService.save(item)).willReturn(item);

        mockMvc.perform(post(ItemController.API_VERSION + ItemController.PATH)
                        .content(asJsonString(itemDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(itemDTO.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(itemDTO.getName()));
    }

    @Test
    void whenSaveAlreadyExistsItemThenReturn422() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemDTO = new ItemDTO(itemId, "Item name", null);
        var item = new ItemEntity(itemId, "Item name", null);
        given(itemService.save(item)).willThrow(ItemAlreadyExistsException.class);

        mockMvc.perform(post(ItemController.API_VERSION + ItemController.PATH)
                        .content(asJsonString(itemDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenUpdateItemThenReturn200() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemDTO = new ItemDTO(itemId, "Item updated name", null);
        var item = new ItemEntity(itemId, "Item updated name", null);
        given(itemService.update(itemId, item)).willReturn(item);

        mockMvc.perform(put(ItemController.API_VERSION + ItemController.PATH + "/" + itemId)
                        .content(asJsonString(itemDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(itemDTO.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(itemDTO.getName()));
    }

    @Test
    void whenDeleteItemThenReturn204() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");

        mockMvc.perform(delete(ItemController.API_VERSION + ItemController.PATH + "/" + itemId))
                .andExpect(status().isNoContent());
    }
}
