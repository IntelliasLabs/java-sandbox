package com.intellias.basicsandbox.persistence;

import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import jakarta.transaction.Transactional;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

// You may choose more technology-specific parent repository class like JpaRepository, MongoRepository.
// Those interfaces extend CrudRepository and expose the capabilities of the underlying persistence technology in addition to
// the rather generic persistence technology-agnostic interfaces such as CrudRepository.
public interface ItemRepository extends CrudRepository<ItemEntity, UUID> {

    // TODO tests
    @Modifying
    @Query("update ItemEntity i set i.name = :name where i.id = :postId")
    int updatePost(@Param("name") final String name, @Param("postId") final UUID postId);

}
