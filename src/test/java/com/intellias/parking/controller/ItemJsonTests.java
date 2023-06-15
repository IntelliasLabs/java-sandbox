package com.intellias.parking.controller;

import com.intellias.parking.controller.dto.ItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemJsonTests {

    @Autowired
    private JacksonTester<ItemDTO> json;

    @Test
    void testSerialize() throws Exception {
        var itemDTO = new ItemDTO(UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04"), "Item name", null);
        var jsonContent = json.write(itemDTO);

        assertThat(jsonContent).extractingJsonPathStringValue("@.id")
                .isEqualTo(itemDTO.getId().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.name")
                .isEqualTo(itemDTO.getName());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                    "id": "55fd4dd7-3da4-40c8-a940-10c9c3c75e04",
                    "name": "Item name"
                }
                """;

        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new ItemDTO(UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04"), "Item name", null));
    }

}
