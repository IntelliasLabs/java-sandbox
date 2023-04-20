package com.intellias.golden_example.repository.mysql;

import com.intellias.golden_example.repository.mysql.entities.BookEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;

@ConditionalOnProperty(value = "repository.mysql.enabled", havingValue = "true", matchIfMissing = true)
public interface BooksJPARepository extends CrudRepository<BookEntity, Long>{
}
