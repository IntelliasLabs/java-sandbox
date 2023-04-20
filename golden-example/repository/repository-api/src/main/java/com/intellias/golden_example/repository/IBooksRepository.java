package com.intellias.golden_example.repository;

import com.intellias.golden_example.dto.BookDTO;

import java.util.Collection;
import java.util.Optional;

public interface IBooksRepository {

    Collection<BookDTO> getAll();

    Optional<BookDTO> getById(long id);

    BookDTO save(BookDTO bookDTO);

    BookDTO update(BookDTO inputBookDTO);

    void deleteById(long id);
}
