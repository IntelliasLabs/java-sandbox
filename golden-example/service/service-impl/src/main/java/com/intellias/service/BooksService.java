package com.intellias.service;

import com.intellias.golden_example.dto.BookDTO;
import com.intellias.golden_example.repository.IBooksRepository;
import com.intellias.golden_example.model.Book;
import com.intellias.golden_example.service.IBooksService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooksService implements IBooksService {

    private final IBooksRepository booksRepository;
    private final ModelMapper modelMapper;

    @Override
    public Collection<Book> getAll() {
        return booksRepository.getAll()
                .stream()
                .map(this::mapDTO2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> getById(long id) {
        return booksRepository.getById(id)
                .map(this::mapDTO2Model);
    }

    @Override
    public Book save(Book book) {
        BookDTO inputBookDTO = mapModel2DTO(book);
        BookDTO storedBookDTO = booksRepository.save(inputBookDTO);
        return mapDTO2Model(storedBookDTO);
    }

    @Override
    public Book update(Book book) {
        BookDTO inputBookDTO = mapModel2DTO(book);
        BookDTO storedBookDTO = booksRepository.update(inputBookDTO);
        return mapDTO2Model(storedBookDTO);
    }

    @Override
    public void deleteById(long id) {
        booksRepository.deleteById(id);
    }

    private Book mapDTO2Model(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    private BookDTO mapModel2DTO(Object simpleEntity) {
        return modelMapper.map(simpleEntity, BookDTO.class);
    }

}
