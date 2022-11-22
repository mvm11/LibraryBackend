package com.iud.library.gateway;

import com.iud.library.dto.BookDTO;
import com.iud.library.request.BookRequest;

import java.util.List;

public interface BookGateway {

    BookDTO createBook(Integer categoryId, BookRequest bookRequest);
    List<BookDTO> findBookByCategory(Integer categoryId);
    BookDTO findBookById(Integer categoryId, Integer bookId);
    List<BookDTO> findBookByPublisher(String publisher);
    List<BookDTO> findBookByCategory(String categoryName);
    List<BookDTO> findBookByFormat(String format);
    List<BookDTO> findBookByAuthor(String author);
    BookDTO updateBook(Integer categoryId, Integer bookId, BookRequest bookRequest);
    void deleteBook(Integer categoryId, Integer bookId);



}