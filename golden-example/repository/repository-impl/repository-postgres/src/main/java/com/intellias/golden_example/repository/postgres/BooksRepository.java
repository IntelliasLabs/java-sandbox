package com.intellias.golden_example.repository.postgres;

import com.intellias.golden_example.dto.BookDTO;
import com.intellias.golden_example.repository.IBooksRepository;
import com.intellias.golden_example.repository.postgres.entities.Tables;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public class BooksRepository implements IBooksRepository {

    private final DSLContext context;

    @Override
    public Collection<BookDTO> getAll() {
        return context
                .selectFrom(Tables.BOOKS)
                .fetchInto(BookDTO.class);
    }

    @Override
    public Optional<BookDTO> getById(long id) {
        return context.fetchOptional(Tables.BOOKS, Tables.BOOKS.ID.equal(id))
                .map(record -> new BookDTO(
                        record.getId(),
                        record.getTitle(),
                        record.getAuthor(),
                        record.getIsbn()
                ));
    }

    @Override
    public BookDTO save(BookDTO bookDTO) {
        Long id = context.insertInto(Tables.BOOKS)
                .columns(Tables.BOOKS.TITLE, Tables.BOOKS.AUTHOR, Tables.BOOKS.ISBN)
                .values(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getIsbn())
                .returningResult(Tables.BOOKS.ID)
                .fetchOne(Tables.BOOKS.ID);
        return new BookDTO(id, bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getIsbn());
    }

    @Override
    public BookDTO update(BookDTO bookDTO) {
        context.update(Tables.BOOKS)
                .set(Tables.BOOKS.TITLE, bookDTO.getTitle())
                .set(Tables.BOOKS.AUTHOR, bookDTO.getAuthor())
                .where(Tables.BOOKS.ISBN.eq(bookDTO.getIsbn()))
                .execute();
        return bookDTO;
    }

    @Override
    public void deleteById(long id) {
        context.delete(Tables.BOOKS)
                .where(Tables.BOOKS.ID.eq(id))
                .execute();
    }

}
