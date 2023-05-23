package com.intellias.basicsandbox.domain;

import com.intellias.basicsandbox.persistence.ItemRepository;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
class ItemRepositoryJpaTests {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllItems() {
        var item1 = new ItemEntity(null, "Item name", null);
        var item2 = new ItemEntity(null, "Item name 2", null);

        entityManager.persist(item1);
        entityManager.persist(item2);

        Iterable<ItemEntity> actualItems = itemRepository.findAll();

        assertThat(StreamSupport.stream(actualItems.spliterator(), true)
                .filter(item -> item.getId().equals(item1.getId()) || item.getId().equals(item2.getId()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findItemByIdWhenExisting() {
        var item = new ItemEntity(null, "Item name", null);
        ItemEntity savedItem = entityManager.persist(item);

        Optional<ItemEntity> actualItem = itemRepository.findById(savedItem.getId());

        assertThat(actualItem).isPresent();
        assertThat(actualItem.get().getId()).isEqualTo(savedItem.getId());
    }

    @Test
    void findItemByIdWhenNotExisting() {
        Optional<ItemEntity> actualItem = itemRepository.findById(UUID.randomUUID());
        assertThat(actualItem).isEmpty();
    }

    @Test
    void existsByIdWhenExisting() {
        var item = new ItemEntity(null, "Item name",  null);
        ItemEntity savedItem = entityManager.persist(item);

        boolean existing = itemRepository.existsById(savedItem.getId());

        assertThat(existing).isTrue();
    }

    @Test
    void existsByIdWhenNotExisting() {
        boolean existing = itemRepository.existsById(UUID.randomUUID());
        assertThat(existing).isFalse();
    }

    @Test
    void deleteByIsbn() {
        var item = new ItemEntity(null, "Item name",  null);
        ItemEntity savedItem = entityManager.persist(item);

        itemRepository.deleteById(savedItem.getId());

        assertThat(entityManager.find(ItemEntity.class, savedItem.getId())).isNull();
    }

}
