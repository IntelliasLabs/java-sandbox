package com.intellias.basicsandbox;

import com.intellias.basicsandbox.controller.ItemController;
import com.intellias.basicsandbox.controller.exception.dto.ErrorDTO;
import com.intellias.basicsandbox.service.dto.ItemDTO;
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
        var itemToCreate = new ItemDTO(null, "Item name");

        ItemDTO expectedItem = webTestClient
                .post()
                .uri("/" + ItemController.PATH)
                .bodyValue(itemToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ItemDTO.class).value(item -> assertThat(item).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/" + ItemController.PATH + "/" + expectedItem.getId())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ItemDTO.class).value(actualItem -> {
                    assertThat(actualItem).isNotNull();
                    assertThat(actualItem.getId()).isEqualTo(expectedItem.getId());
                });
    }

    @Test
    void whenPostRequestThenItemCreated() {
        var expectedItem = new ItemDTO(null, "Item name");

        webTestClient
                .post()
                .uri("/" + ItemController.PATH)
                .bodyValue(expectedItem)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ItemDTO.class).value(actualItem -> {
                    assertThat(actualItem).isNotNull();
                    assertThat(actualItem.getId()).isNotNull();
                    assertThat(actualItem.getName()).isEqualTo(expectedItem.getName());
                });
    }

    @Test
    void whenPutRequestThenItemUpdated() {
        var itemToCreate = new ItemDTO(null, "Item name");

        ItemDTO createdItem = webTestClient
                .post()
                .uri("/" + ItemController.PATH)
                .bodyValue(itemToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ItemDTO.class).value(item -> assertThat(item).isNotNull())
                .returnResult().getResponseBody();

        createdItem.setName("Updated name");

        webTestClient
                .put()
                .uri("/" + ItemController.PATH + "/" + createdItem.getId())
                .bodyValue(createdItem)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemDTO.class).value(actualItem -> {
                    assertThat(actualItem).isNotNull();
                    assertThat(actualItem.getName()).isEqualTo(createdItem.getName());
                });
    }

    @Test
    void whenDeleteRequestThenItemDeleted() {
        var itemToCreate = new ItemDTO(null, "Item name");

        ItemDTO createdItem = webTestClient
                .post()
                .uri("/" + ItemController.PATH)
                .bodyValue(itemToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ItemDTO.class).value(item -> assertThat(item).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .delete()
                .uri("/" + ItemController.PATH + "/" + createdItem.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/" + ItemController.PATH + "/" + createdItem.getId())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorDTO.class).value(errorMessage ->
                        assertThat(errorMessage.getMessage()).isEqualTo("An item with ID " + createdItem.getId() + " not found.")
                );
    }

}
