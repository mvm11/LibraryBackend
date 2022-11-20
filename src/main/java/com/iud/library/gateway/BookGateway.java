package com.iud.library.gateway;

import com.iud.library.dto.BookDTO;
import com.iud.library.request.BookRequest;

import java.util.List;

public interface BookGateway {

    List<BookDTO>  findAllBooks();
    BookDTO findBookById(Integer bookId);
    List<BookDTO> findBookByPublisher(String publisher);
    List<BookDTO> findBookByCategory(String category);
    List<BookDTO> findBookByFormat(String format);
    List<BookDTO> findBookByAuthor(String author);
    BookDTO createBook(BookRequest bookRequest);
    void deleteBook(Integer bookId);
    BookDTO updateBook(BookDTO bookDTO, Integer bookId);
}