package com.iud.library.service;

import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.BookDTO;
import com.iud.library.dto.BookResponse;
import com.iud.library.entity.Author;
import com.iud.library.entity.Book;
import com.iud.library.entity.Category;
import com.iud.library.entity.Publisher;
import com.iud.library.gateway.BookGateway;
import com.iud.library.repository.BookRepository;
import com.iud.library.repository.PublisherRepository;
import com.iud.library.request.BookRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService implements BookGateway {


    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

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
    public List<BookDTO> findBookByPublisher(String publisher) {
        List<Book> bookList = bookRepository.findAll();

        List<Book> bookListFilteredByPublisher = filterBookByPublisher(publisher, bookList);

        return validateFilteredBook(bookListFilteredByPublisher, "publisher", publisher);
    }

    @Override
    public List<BookDTO> findBookByCategory(String category) {

        List<Book> bookList = bookRepository.findAll();

        List<Book> bookListFilteredByCategory = filterBookByCategory(category, bookList);

        return validateFilteredBook(bookListFilteredByCategory, "category", category);
    }

    @Override
    public List<BookDTO> findBookByFormat(String format) {

        List<Book> bookList = bookRepository.findAll();

        List<Book> bookListFilteredByFormat = filterBookByFormat(format, bookList);

        return validateFilteredBook(bookListFilteredByFormat, "format", format);
    }

    @Override
    public List<BookDTO> findBookByAuthor(String author) {

        List<Book> bookList = bookRepository.findAll();

        List<Book> bookListFilteredByAuthor = filterBookByAuthor(author, bookList);

        return validateFilteredBook(bookListFilteredByAuthor, "author", author);
    }

    private List<Book> filterBookByAuthor(String author, List<Book> bookList) {

        Set<Author> bookAuthors = getBookAuthor(author, bookList);

        return validateBookAuthor(author, bookList, bookAuthors);
    }

    private List<Book> validateBookAuthor(String author, List<Book> bookList, Set<Author> bookAuthors) {

        if(bookAuthors.isEmpty()){
            throw new NotFoundException("Book", "author", author);
        }else{
            return bookList.stream()
                    .filter(book -> book.getAuthors().containsAll(bookAuthors))
                    .collect(Collectors.toList());
        }
    }

    private Set<Author> getBookAuthor(String author, List<Book> bookList) {
        return bookList.stream()
                .map(Book::getAuthors)
                .flatMap(Collection::stream)
                .filter(author1 -> author1.getName().equalsIgnoreCase(author))
                .collect(Collectors.toSet());
    }

    private List<Book> filterBookByFormat(String format, List<Book> bookList) {
        return bookList.stream()
                .filter(book -> book.getFormat().equalsIgnoreCase(format))
                .collect(Collectors.toList());
    }

    private List<BookDTO> validateFilteredBook(List<Book> bookListFilteredByQuery, String queryName, String query) {
        if(bookListFilteredByQuery.isEmpty()){
            throw new NotFoundException("Book", queryName, query);
        }else{
            return bookListFilteredByQuery.stream()
                    .map(this::convertBookToDTO)
                    .collect(Collectors.toList());
        }
    }

    private List<Book> filterBookByPublisher(String publisher, List<Book> bookList) {
        return bookList.stream()
                .filter(book -> book.getPublisher().getPublisherName().equalsIgnoreCase(publisher))
                .collect(Collectors.toList());
    }

    private List<Book> filterBookByCategory(String category, List<Book> bookList) {


        Set<Category> bookCategories = getBookCategories(category, bookList);

        return validateBookCategories(category, bookList, bookCategories);
    }

    private List<Book> validateBookCategories(String category, List<Book> bookList, Set<Category> bookCategories) {
        if(bookCategories.isEmpty()){
            throw new NotFoundException("Book", "category", category);
        }else{
            return bookList.stream()
                    .filter(book -> book.getCategories().containsAll(bookCategories))
                    .collect(Collectors.toList());
        }
    }

    private Set<Category> getBookCategories(String category, List<Book> bookList) {
        return bookList.stream()
                .map(Book::getCategories)
                .flatMap(Collection::stream)
                .filter(category1 -> category1.getName().equalsIgnoreCase(category))
                .collect(Collectors.toSet());
    }

    @Override
    public BookDTO createBook(BookRequest bookRequest) {

        Publisher publisher = Publisher.builder()
                .publisherName(bookRequest.getPublisherName())
                .build();


        Book book = Book.builder()
                .title(bookRequest.getTitle())
                .isbn(bookRequest.getIsbn())
                .numberOfPages(bookRequest.getNumberOfPages())
                .format(bookRequest.getFormat())
                .publisher(publisher)
                .categories(bookRequest.getCategories())
                .copies(bookRequest.getCopies())
                .authors(bookRequest.getAuthors())
                .build();

        // Save publisher into the repository
        publisherRepository.save(publisher);

        // Save Book into the repository
        Book newBook = bookRepository.save(book);

        //Load the cron expression from database
        taskScheduler.schedule(
                this::startJob,
                new Date(OffsetDateTime.now().plusSeconds(10).toInstant().toEpochMilli())
        );

        // Parse from Entity to DTO
        return convertBookToDTO(newBook);
    }

    public void startJob() {
        System.out.println("me imprimí "+ new Date());
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
        //book.setPublisher(bookDTO.getPublisher());
        book.setFormat(bookDTO.getFormat());
        book.setCategories(bookDTO.getCategories());
        book.setAuthors(bookDTO.getAuthors());

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
