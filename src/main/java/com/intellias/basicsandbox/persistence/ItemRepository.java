package com.intellias.basicsandbox.persistence;

import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

// You may choose more technology-specific parent repository class like JpaRepository, MongoRepository.
// Those interfaces extend CrudRepository and expose the capabilities of the underlying persistence technology in addition to
// the rather generic persistence technology-agnostic interfaces such as CrudRepository.
public interface ItemRepository extends CrudRepository<ItemEntity, UUID> {

    /**
     * This is example of direct modifying query. But for simplicity and usage of JPA features and configuration like involvement
     * of encoding converters (applied in the Entity fields) it is preferred to fetch and update entities with find and save
     * operations.
     *
     * Warning: as such methods bypassing the entity manager it's entities may become outdated. It doesn't make clear() by
     * default to update them.
     * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.modifying-queries">Spring Data Docs</a>
     */
    @Modifying
    @Query("update ItemEntity i set i.name = :name where i.id = :id")
    int updateItemName(@Param("id") final UUID itemId, @Param("name") final String name);

}
