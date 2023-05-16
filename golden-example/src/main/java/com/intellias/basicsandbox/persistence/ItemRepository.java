package com.intellias.basicsandbox.persistence;

import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

// You may choose more technology-specific parent repository class like JpaRepository, MongoRepository.
// Those interfaces extend CrudRepository and expose the capabilities of the underlying persistence technology in addition to
// the rather generic persistence technology-agnostic interfaces such as CrudRepository.
public interface ItemRepository extends CrudRepository<ItemEntity, UUID> {

    @Modifying
    @Query("update ItemEntity i set i.name = :name where i.id = :itemId")
    void updateItem(@Param("name") final String name, @Param("itemId") final UUID itemId);

}
