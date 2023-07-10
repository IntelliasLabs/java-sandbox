package com.intellias.basicsandbox;

import com.intellias.basicsandbox.controller.ItemController;
import com.intellias.basicsandbox.controller.dto.ErrorDTO;
import com.intellias.basicsandbox.controller.dto.item.ItemSummaryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class SandboxApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenGetRequestWithIdThenItemReturned() {
        var itemToCreate = new ItemSummaryDTO(null, "Item name", null);

        ItemSummaryDTO expectedItem = webTestClient
                .post()
                .uri(ItemController.API_VERSION + ItemController.PATH)
                .bodyValue(itemToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ItemSummaryDTO.class).value(item -> assertThat(item).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri(ItemController.API_VERSION + ItemController.PATH + "/" + expectedItem.getId())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ItemSummaryDTO.class).value(actualItem -> {
                    assertThat(actualItem).isNotNull();
                    assertThat(actualItem.getId()).isEqualTo(expectedItem.getId());
                });
    }

    @Test
    void whenPostRequestThenItemCreated() {
        var expectedItem = new ItemSummaryDTO(null, "Item name", null);

        webTestClient
                .post()
                .uri(ItemController.API_VERSION + ItemController.PATH)
                .bodyValue(expectedItem)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ItemSummaryDTO.class).value(actualItem -> {
                    assertThat(actualItem).isNotNull();
                    assertThat(actualItem.getId()).isNotNull();
                    assertThat(actualItem.getName()).isEqualTo(expectedItem.getName());
                });
    }

    @Test
    void whenPutRequestThenItemUpdated() {
        var itemToCreate = new ItemSummaryDTO(null, "Item name", null);

        ItemSummaryDTO createdItem = webTestClient
                .post()
                .uri(ItemController.API_VERSION + ItemController.PATH)
                .bodyValue(itemToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ItemSummaryDTO.class).value(item -> assertThat(item).isNotNull())
                .returnResult().getResponseBody();

        createdItem.setName("Updated name");

        webTestClient
                .put()
                .uri(ItemController.API_VERSION + ItemController.PATH + "/" + createdItem.getId())
                .bodyValue(createdItem)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemSummaryDTO.class).value(actualItem -> {
                    assertThat(actualItem).isNotNull();
                    assertThat(actualItem.getName()).isEqualTo(createdItem.getName());
                });
    }

    @Test
    void whenDeleteRequestThenItemDeleted() {
        var itemToCreate = new ItemSummaryDTO(null, "Item name", null);

        ItemSummaryDTO createdItem = webTestClient
                .post()
                .uri(ItemController.API_VERSION + ItemController.PATH)
                .bodyValue(itemToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ItemSummaryDTO.class).value(item -> assertThat(item).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .delete()
                .uri(ItemController.API_VERSION + ItemController.PATH + "/" + createdItem.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri(ItemController.API_VERSION + ItemController.PATH + "/" + createdItem.getId())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorDTO.class).value(errorMessage ->
                        assertThat(errorMessage.getMessage()).isEqualTo("An item with ID " + createdItem.getId() + " not found.")
                );
    }

}
