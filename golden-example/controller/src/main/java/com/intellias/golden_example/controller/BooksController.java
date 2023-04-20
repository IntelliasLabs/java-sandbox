package com.intellias.golden_example.controller;

import com.intellias.golden_example.model.Book;
import com.intellias.golden_example.service.IBooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BooksController {

    private final IBooksService booksService;

    @GetMapping
    public Collection<Book> getAll() {
        return booksService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Book> getById(@PathVariable long id) {
        return booksService.getById(id);
    }

    @PostMapping
    public Book save(@Validated @RequestBody Book book) {
        return booksService.save(book);
    }

    @PutMapping
    public Book update(@Validated @RequestBody Book book) {
        return booksService.update(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        booksService.deleteById(id);
    }


}
