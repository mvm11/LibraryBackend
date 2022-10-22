package com.iud.library.gateway;

import com.iud.library.dto.BookDTO;

import java.util.List;

public interface BookGateway {

    public List<BookDTO> findAllBooks();
    public BookDTO findBookById(Integer bookId);
    public BookDTO createBook(BookDTO bookDTO);
    public void deleteBook(Integer bookId);
    public BookDTO updateBook(BookDTO bookDTO, Integer bookId);
}