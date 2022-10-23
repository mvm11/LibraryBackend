package com.iud.library.service;

import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.BookDTO;
import com.iud.library.dto.BookResponse;
import com.iud.library.entity.Book;
import com.iud.library.gateway.BookGateway;
import com.iud.library.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService implements BookGateway {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BookResponse findAllBooks(int pageNumber, int pageQuantityOfBooks, String sortBookBy) {

        // Create Pageable
        Pageable pageable = PageRequest.of(pageNumber, pageQuantityOfBooks, Sort.by(sortBookBy));

        // Add pageable to the method findAll
        Page<Book> bookPages = bookRepository.findAll(pageable);
        // Get all books from the repository
        List<Book> bookList = bookPages.getContent();

        // Convert each book of the bookList to a DTO
        List<BookDTO> bookContentList = bookList.stream()
                .map(this::convertBookToDTO)
                .collect(Collectors.toList());

        return BookResponse.builder()
                .bookContentList(bookContentList)
                .pageNumber(bookPages.getNumber())
                .pageQuantityOfBooks(bookPages.getSize())
                .totalBooks(bookPages.getTotalElements())
                .totalPages(bookPages.getTotalPages())
                .isTheLastOnePage(bookPages.isLast())
                .build();
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

    private BookDTO convertBookToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book convertDTOToBook(BookDTO bookDTO) {return modelMapper.map(bookDTO, Book.class);
    }

}
