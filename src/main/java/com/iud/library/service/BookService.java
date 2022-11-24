package com.iud.library.service;

import com.iud.library.common.exception.LibraryException;
import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.BookDTO;
import com.iud.library.dto.CategoryDTO;
import com.iud.library.entity.*;
import com.iud.library.gateway.BookGateway;
import com.iud.library.repository.*;
import com.iud.library.request.book.SavingBookRequest;
import com.iud.library.request.book.UpdatingBookRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService implements BookGateway {

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
    public BookDTO createBook(Integer categoryId, SavingBookRequest savingBookRequest) {

        Book book = new Book();

        Category category = getCategory(categoryId);

        Publisher publisher = getPublisherByBookRequest(savingBookRequest);

        validatePublisher(savingBookRequest, publisher);

        validateBookRequest(savingBookRequest);

        book.setTitle(savingBookRequest.getTitle());
        book.setIsbn(savingBookRequest.getIsbn());
        book.setNumberOfPages(savingBookRequest.getNumberOfPages());
        book.setFormat(savingBookRequest.getFormat());
        book.setPublisher(publisher);
        book.setCategory(category);

        //Save publisher
        publisherRepository.save(publisher);

        // Save Book into the repository
        bookRepository.save(book);

        // save subjects
        savingBookRequest.getSubjects().forEach(subject -> subject.setBook(book));
        subjectRepository.saveAll(savingBookRequest.getSubjects());

        // save authors
        savingBookRequest.getAuthors().forEach(author -> author.setBook(book));
        authorRepository.saveAll(savingBookRequest.getAuthors());



        //setting values
        book.setSubjects(savingBookRequest.getSubjects());
        book.setAuthors(savingBookRequest.getAuthors());

        // Parse from Entity to DTO
        return convertBookToDTO(book);
    }

    private Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category", "id", categoryId));
    }

    private Publisher getPublisherByBookRequest(SavingBookRequest savingBookRequest) {
        return publisherRepository.findAll()
                .stream()
                .filter(publisher1 -> publisher1.getPublisherName().equalsIgnoreCase(savingBookRequest.getPublisherName()))
                .findFirst()
                .orElse(new Publisher());
    }

    private void validatePublisher(SavingBookRequest savingBookRequest, Publisher publisher) {
        if(publisher.getPublisherName() == null){
            publisher.setPublisherName(savingBookRequest.getPublisherName());
        }
    }

    private void validateBookRequest(SavingBookRequest savingBookRequest) {
        if(validateBookRequestFields(savingBookRequest)){
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



    private boolean validateBookRequestFields(SavingBookRequest savingBookRequest) {
        return savingBookRequest.getAuthors() == null ||
                savingBookRequest.getAuthors().isEmpty() ||
                savingBookRequest.getSubjects() == null ||
                savingBookRequest.getSubjects().isEmpty();
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
    public BookDTO updateBook(Integer categoryId, Integer bookId, UpdatingBookRequest updatingBookRequest) {
        Category category = getCategory(categoryId);
        Book book = getBook(bookId);
        validateCategoryAndBookId(category, book);
        // Set the DTO values into the found book
        book.setTitle(updatingBookRequest.getTitle());
        book.setIsbn(updatingBookRequest.getIsbn());
        book.setNumberOfPages(updatingBookRequest.getNumberOfPages());
        book.setFormat(updatingBookRequest.getFormat());
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
