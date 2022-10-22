package com.iud.library.gateway;

import com.iud.library.dto.BookDTO;
import com.iud.library.dto.BookResponse;

public interface BookGateway {

    public BookResponse findAllBooks(int pageNumber, int pageQuantity, String sortBookBy);
    public BookDTO findBookById(Integer bookId);
    public BookDTO createBook(BookDTO bookDTO);
    public void deleteBook(Integer bookId);
    public BookDTO updateBook(BookDTO bookDTO, Integer bookId);
}