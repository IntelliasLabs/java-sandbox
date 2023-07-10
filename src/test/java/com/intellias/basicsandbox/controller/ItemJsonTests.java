package com.intellias.basicsandbox.controller;

import com.intellias.basicsandbox.controller.dto.item.ItemRequestDTO;
import com.intellias.basicsandbox.controller.dto.item.ItemSummaryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemJsonTests {

    @Autowired
    private JacksonTester<ItemSummaryDTO> jsonSummary;

    @Autowired
    private JacksonTester<ItemRequestDTO> jsonRequest;

    @Test
    void testSerializeToSummaryDTO() throws Exception {
        var itemSummaryDTO = new ItemSummaryDTO(UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04"), "Item name", null);
        var jsonContent = jsonSummary.write(itemSummaryDTO);

        assertThat(jsonContent).extractingJsonPathStringValue("@.id")
                .isEqualTo(itemSummaryDTO.getId().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.name")
                .isEqualTo(itemSummaryDTO.getName());
    }

    @Test
    void testDeserializeFromSummaryDTO() throws Exception {
        var content = """
                {
                    "id": "55fd4dd7-3da4-40c8-a940-10c9c3c75e04",
                    "name": "Item name"
                }
                """;

        assertThat(jsonSummary.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new ItemSummaryDTO(UUID.fromString("55fd4dd7-3da4-40c8-a940-10c9c3c75e04"), "Item name", null));
    }

    @Test
    void testSerializeToRequestDTO() throws Exception {
        var itemRequestDTO = new ItemRequestDTO("Item name", null);
        var jsonContent = jsonRequest.write(itemRequestDTO);

        assertThat(jsonContent).extractingJsonPathStringValue("@.name")
                .isEqualTo(itemRequestDTO.getName());
        assertThat(jsonContent).extractingJsonPathStringValue("@.creditCard")
                .isEqualTo(null);
    }

    @Test
    void testDeserializeFromRequestDTO() throws Exception {
        var content = """
                {
                    "name": "Item name"
                }
                """;

        assertThat(jsonRequest.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new ItemRequestDTO("Item name", null));
    }

}
