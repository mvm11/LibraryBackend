package com.iud.library.service;

import com.iud.library.common.exception.LibraryException;
import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.BookDTO;
import com.iud.library.dto.CategoryDTO;
import com.iud.library.entity.*;
import com.iud.library.gateway.BookGateway;
import com.iud.library.repository.*;
import com.iud.library.request.BookRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BookDTO createBook(Integer categoryId, BookRequest bookRequest) {

        Book book = new Book();

        Category category = getCategory(categoryId);

        Publisher publisher = getPublisherByBookRequest(bookRequest);

        validatePublisher(bookRequest, publisher);

        validateBookRequest(bookRequest);

        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());
        book.setNumberOfPages(bookRequest.getNumberOfPages());
        book.setFormat(bookRequest.getFormat());
        book.setPublisher(publisher);
        book.setCategory(category);

        //Save publisher
        publisherRepository.save(publisher);

        // Save Book into the repository
        bookRepository.save(book);

        // save subjects
        bookRequest.getSubjects().forEach(subject -> subject.setBook(book));
        subjectRepository.saveAll(bookRequest.getSubjects());

        // save authors
        bookRequest.getAuthors().forEach(author -> author.setBook(book));
        authorRepository.saveAll(bookRequest.getAuthors());

        //Load the cron expression from database
        taskScheduler.schedule(
                this::startJob,
                new Date(OffsetDateTime.now().plusSeconds(10).toInstant().toEpochMilli())
        );

        //setting values
        book.setSubjects(bookRequest.getSubjects());
        book.setAuthors(bookRequest.getAuthors());

        // Parse from Entity to DTO
        return convertBookToDTO(book);
    }

    private Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category", "id", categoryId));
    }

    private Publisher getPublisherByBookRequest(BookRequest bookRequest) {
        return publisherRepository.findAll()
                .stream()
                .filter(publisher1 -> publisher1.getPublisherName().equalsIgnoreCase(bookRequest.getPublisherName()))
                .findFirst()
                .orElse(new Publisher());
    }

    private void validatePublisher(BookRequest bookRequest, Publisher publisher) {
        if(publisher.getPublisherName() == null){
            publisher.setPublisherName(bookRequest.getPublisherName());
        }
    }

    private void validateBookRequest(BookRequest bookRequest) {
        if(validateBookRequestFields(bookRequest)){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "subjects and authors cannot be null");
        }
    }

    @Override
    public List<BookDTO> findBookByCategory(Integer categoryId) {
        List<Book> books = bookRepository.findByCategoryId(categoryId);
        return books.stream()
                .map(this::convertBookToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO findBookById(Integer categoryId, Integer bookId) {
        Category category = getCategory(categoryId);
        Book book = getBook(bookId);
        validateCategoryAndBookId(category, book);
        return convertBookToDTO(book);
    }

    @Override
    public List<BookDTO> findBookByPublisher(String publisher) {
        List<Book> bookList = bookRepository.findAll();
        List<Book> bookListFilteredByPublisher = filterBookByPublisher(publisher, bookList);
        return validateFilteredBook(bookListFilteredByPublisher, "publisher", publisher);
    }

    @Override
    public List<BookDTO> findBookByCategory(String categoryName) {
        Category category = getCategoryByCategoryName(categoryName);
        return category.getBooks().stream()
                .map(this::convertBookToDTO)
                .collect(Collectors.toList());
    }

    private Category getCategoryByCategoryName(String categoryName) {
        return categoryRepository.findAll()
                .stream()
                .filter(category1 -> category1.getCategoryName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("category", "name", categoryName));
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

    @Override
    public List<BookDTO> findBookBySubject(String subject) {
        List<Book> bookList = bookRepository.findAll();
        List<Book> bookListFilteredBySubject = filterBookBySubject(subject, bookList);
        return validateFilteredBook(bookListFilteredBySubject, "author", subject);
    }

    private List<Book> filterBookBySubject(String subject, List<Book> bookList) {
        Set<Subject> bookSubjects = getBookSubject(subject, bookList);
        return validateBookSubject(subject, bookList, bookSubjects);
    }

    private List<Book> validateBookSubject(String subject, List<Book> bookList, Set<Subject> bookSubjects) {
        if(bookSubjects.isEmpty()){
            throw new NotFoundException("Book", "subject", subject);
        }else{
            return bookList.stream()
                    .filter(book -> book.getSubjects().containsAll(bookSubjects))
                    .collect(Collectors.toList());
        }
    }

    private Set<Subject> getBookSubject(String subject, List<Book> bookList) {
        return bookList.stream()
                .map(Book::getSubjects)
                .flatMap(Collection::stream)
                .filter(subject1 -> subject1.getSubjectName().equalsIgnoreCase(subject))
                .collect(Collectors.toSet());
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
                .filter(author1 -> author1.getAuthorName().equalsIgnoreCase(author))
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



    private boolean validateBookRequestFields(BookRequest bookRequest) {
        return bookRequest.getAuthors() == null ||
                bookRequest.getAuthors().isEmpty() ||
                bookRequest.getSubjects() == null ||
                bookRequest.getSubjects().isEmpty();
    }





    public void startJob() {
        System.out.println("me imprimí "+ new Date());
    }

    @Override
    public void deleteBook(Integer categoryId, Integer bookId) {

        Category category  = getCategory(categoryId);
        Book book = getBook(bookId);

        validateCategoryAndBookId(category, book);

        bookRepository.delete(book);

        bookRepository.delete(book);
    }

    @Override
    public BookDTO updateBook(Integer categoryId, Integer bookId, BookRequest bookRequest) {
        Category category = getCategory(categoryId);
        Book book = getBook(bookId);
        validateCategoryAndBookId(category, book);
        // Set the DTO values into the found book
        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());
        book.setNumberOfPages(bookRequest.getNumberOfPages());
        book.setFormat(bookRequest.getFormat());
        bookRepository.save(book);
        return convertBookToDTO(book);
    }

    private void validateCategoryAndBookId(Category category, Book book) {
        if(!book.getCategory().getId().equals(category.getId())){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "the book does not belong to the category");
        }
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

    private CategoryDTO convertCategoryToDTO(Category category){return modelMapper.map(category, CategoryDTO.class);}



}
