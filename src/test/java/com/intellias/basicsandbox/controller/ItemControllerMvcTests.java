package com.intellias.basicsandbox.controller;

import static com.intellias.basicsandbox.utils.TestUtils.asJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.intellias.basicsandbox.controller.dto.ItemDTO;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import com.intellias.basicsandbox.service.exception.ItemNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
        var item = new ItemEntity(itemId, "Item name", null, null);
        given(itemService.getById(itemId)).willReturn(item);

        mockMvc.perform(get(ItemController.API_VERSION + ItemController.PATH + "/" + itemId))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetItemByIdLocalizedFoundThenReturnLocalizedCurrency() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var item = new ItemEntity(itemId, "Item name", "credit card", "UAH");
        given(itemService.getById(itemId)).willReturn(item);

        String locale = "uk_UA";
        mockMvc.perform(get(ItemController.API_VERSION + ItemController.PATH + "/" + itemId + "/" + locale))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Гривня ₴")));
    }

    @Test
    void whenGetItemByIdLocalizedFoundButLocalizationIsNot_ThenReturnParentLocalizationCurrency() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var item = new ItemEntity(itemId, "Item name", "credit card", "UAH");
        given(itemService.getById(itemId)).willReturn(item);

        String locale = "uk_US";
        mockMvc.perform(get(ItemController.API_VERSION + ItemController.PATH + "/" + itemId + "/" + locale))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Гривня")));
    }

    @Test
    void whenNameIsEmpty_ThenReturnLocalizedMessage_onUpdate() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemDTO = new ItemDTO(itemId, "", "4916338506082835 Q", "UAH");

        String contentAsString = mockMvc.perform(put(ItemController.API_VERSION + ItemController.PATH + "/" + itemId)
                        .content(asJsonString(itemDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .locale(Locale.forLanguageTag("uk-UA")))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertTrue(contentAsString.contains("[name] Вкажіть імʼя до 250 символів. "));
        Assertions.assertTrue(contentAsString.contains("[creditCard] має відповідати шаблону \\\"^(?:4[0-9]{12}("));
    }

    @Test
    void whenNameIsEmptyAndTranslationIsMissed_ThenReturnLocalizedMessageFromParentLocale_onUpdate() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemDTO = new ItemDTO(itemId, "", "4916338506082835 Q", "UAH");

        String contentAsString = mockMvc.perform(put(ItemController.API_VERSION + ItemController.PATH + "/" + itemId)
                        .content(asJsonString(itemDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .locale(Locale.forLanguageTag("uk")))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertTrue(contentAsString.contains("[name] Add a name up to 250 characters."));
        Assertions.assertTrue(contentAsString.contains("[creditCard] має відповідати шаблону \\\"^(?:4[0-9]{12}("));
    }

    @Test
    void whenSaveItemThenReturn201() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemDTO = new ItemDTO(itemId, "Item name", null, null);
        var item = new ItemEntity(itemId, "Item name", null, null);
        given(itemService.save(item)).willReturn(item);

        mockMvc.perform(post(ItemController.API_VERSION + ItemController.PATH)
                        .content(asJsonString(itemDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(itemDTO.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(itemDTO.getName()));
    }

    @Test
    void whenUpdateItemThenReturn200() throws Exception {
        var itemId = UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04");
        var itemDTO = new ItemDTO(itemId, "Item updated name", null, null);
        var item = new ItemEntity(itemId, "Item updated name", null, null);
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
