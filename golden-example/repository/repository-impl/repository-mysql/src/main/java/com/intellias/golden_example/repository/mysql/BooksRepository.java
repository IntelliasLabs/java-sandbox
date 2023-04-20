package com.intellias.golden_example.repository.mysql;

import com.intellias.golden_example.repository.IBooksRepository;
import com.intellias.golden_example.repository.mysql.entities.BookEntity;
import com.intellias.golden_example.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BooksRepository implements IBooksRepository {

    private final BooksJPARepository booksJPARepository;
    private final ModelMapper modelMapper;

    @Override
    public Collection<BookDTO> getAll() {
        return ((Collection<BookEntity>) booksJPARepository.findAll())
                .stream()
                .map(this::mapEntity2DTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDTO> getById(long id) {
        return booksJPARepository.findById(id)
                .map(this::mapEntity2DTO);
    }

    @Override
    public BookDTO save(BookDTO bookDTO) {
        BookEntity bookEntity = mapDTO2Entity(bookDTO);
        BookEntity savedEntity = booksJPARepository.save(bookEntity);
        return mapEntity2DTO(savedEntity);
    }

    @Override
    public BookDTO update(BookDTO inputBookDTO) {
        return save(inputBookDTO);
    }

    @Override
    public void deleteById(long id) {
        booksJPARepository.deleteById(id);
    }

    private BookDTO mapEntity2DTO(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDTO.class);
    }

    private BookEntity mapDTO2Entity(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, BookEntity.class);
    }
}
