package com.iud.library.gateway;

import com.iud.library.dto.BookDTO;
import com.iud.library.dto.BookResponse;

import java.util.List;

public interface BookGateway {

    BookResponse findAllBooks(int pageNumber, int pageQuantity, String sortBookBy);
    BookDTO findBookById(Integer bookId);
    List<BookDTO> findBookByPublisher(String publisher);
    BookDTO createBook(BookDTO bookDTO);
    void deleteBook(Integer bookId);
    BookDTO updateBook(BookDTO bookDTO, Integer bookId);
}