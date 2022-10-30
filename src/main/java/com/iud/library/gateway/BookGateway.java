package com.iud.library.gateway;

import com.iud.library.dto.BookDTO;
import com.iud.library.dto.BookResponse;
import com.iud.library.entity.Book;

import java.util.List;

public interface BookGateway {

    BookResponse findAllBooks(int pageNumber, int pageQuantity, String sortBookBy);
    BookDTO findBookById(Integer bookId);
    List<BookDTO> findBookByPublisher(String publisher);
    List<BookDTO> findBookByCategory(String category);
    BookDTO createBook(BookDTO bookDTO);
    void deleteBook(Integer bookId);
    BookDTO updateBook(BookDTO bookDTO, Integer bookId);
}