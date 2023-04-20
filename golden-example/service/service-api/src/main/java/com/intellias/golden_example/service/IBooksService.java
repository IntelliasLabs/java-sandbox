package com.intellias.golden_example.service;

import com.intellias.golden_example.model.Book;

import java.util.Collection;
import java.util.Optional;

public interface IBooksService {

    Collection<Book> getAll();

    Optional<Book> getById(long id);

    Book save(Book book);

    Book update(Book book);

    void deleteById(long id);


}
