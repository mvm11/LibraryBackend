package com.iud.library.gateway;

import com.iud.library.dto.BookDTO;
import com.iud.library.request.SavingBookRequest;
import com.iud.library.request.UpdatingBookRequest;

import java.util.List;

public interface BookGateway {

    BookDTO createBook(Integer categoryId, SavingBookRequest savingBookRequest);
    List<BookDTO> findBookByCategory(Integer categoryId);
    BookDTO findBookById(Integer categoryId, Integer bookId);
    List<BookDTO> findBookByPublisher(String publisher);
    List<BookDTO> findBookByCategory(String categoryName);
    List<BookDTO> findBookByFormat(String format);
    List<BookDTO> findBookByAuthor(String subject);
    List<BookDTO> findBookBySubject(String author);
    BookDTO updateBook(Integer categoryId, Integer bookId, UpdatingBookRequest updatingBookRequest);
    void deleteBook(Integer categoryId, Integer bookId);



}