package com.iud.library.service;

import com.iud.library.entity.Book;
import com.iud.library.gateway.BookGateway;
import com.iud.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService implements BookGateway {


    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book findBookById(Integer bookId) {

        Optional<Book> persona = bookRepository.findById(bookId);
        if(persona.isEmpty()){
            throw new RuntimeException("La persona no existe");
        }
        return persona.get();
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Integer bookId) {
        bookRepository.delete(findBookById(bookId));
    }

    @Override
    public Book updateBook(Book book) {
        Book foundBook = findBookById(book.getId());
        foundBook.setTitle(book.getTitle());
        return bookRepository.save(foundBook);
    }
}
