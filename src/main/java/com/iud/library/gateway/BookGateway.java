package com.iud.library.gateway;

import com.iud.library.entity.Book;

import java.util.List;

public interface BookGateway {

    public List<Book> findAllBooks();
    public Book findBookById(Integer bookId);
    public Book createBook(Book book);
    public void deleteBook(Integer bookId);
    public Book updateBook(Book book);
}