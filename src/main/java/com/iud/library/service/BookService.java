package com.iud.library.service;

import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.BookDTO;
import com.iud.library.entity.Book;
import com.iud.library.gateway.BookGateway;
import com.iud.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService implements BookGateway {


    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BookDTO> findAllBooks() {

        // Get all books from the repository
        List<Book> bookList = bookRepository.findAll();

        // Convert all the repository books to DTO
        return bookList.stream()
                .map(BookService::convertBookToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO findBookById(Integer bookId) {
        // Search the book by id into the repository
        Book book = getBook(bookId);

        return convertBookToDTO(book);
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {

        // Parse from DTO to Entity
        Book book = convertDTOToBook(bookDTO);

        // Save into the repository
        Book newBook = bookRepository.save(book);

        // Parse from Entity to DTO
        return convertBookToDTO(newBook);
    }

    private static BookDTO convertBookToDTO(Book newBook) {
        return BookDTO.builder()
                .id(newBook.getId())
                .title(newBook.getTitle())
                .isbn(newBook.getIsbn())
                .numberOfPages(newBook.getNumberOfPages())
                .publisher(newBook.getPublisher())
                .format(newBook.getFormat())
                .category(newBook.getCategory())
                .build();
    }

    private static Book convertDTOToBook(BookDTO bookDTO) {
        return Book.builder()
                .title(bookDTO.getTitle())
                .isbn(bookDTO.getIsbn())
                .numberOfPages(bookDTO.getNumberOfPages())
                .publisher(bookDTO.getPublisher())
                .format(bookDTO.getFormat())
                .category(bookDTO.getCategory())
                .build();
    }

    @Override
    public void deleteBook(Integer bookId) {
        // Get book from the repository
        Book book = getBook(bookId);
        // Delete book
        bookRepository.delete(book);
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO, Integer bookId) {
        // Get book from the repository
        Book book = getBook(bookId);

        // Set the DTO values into the found book
        book.setTitle(bookDTO.getTitle());
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setNumberOfPages(bookDTO.getNumberOfPages());
        book.setPublisher(bookDTO.getPublisher());
        book.setFormat(bookDTO.getFormat());
        book.setCategory(bookDTO.getCategory());

        Book bookUpdated = bookRepository.save(book);

        return convertBookToDTO(bookUpdated);
    }

    private Book getBook(Integer bookId) {
        return bookRepository.findById(bookId)
                // if the book doesn't exist throw NotFoundException
                .orElseThrow(() -> new NotFoundException("Book", "id", bookId));
    }

}
