package com.iud.library.service;

import com.iud.library.common.exception.LibraryException;
import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.AuthorDTO;
import com.iud.library.entity.Author;
import com.iud.library.entity.Book;
import com.iud.library.gateway.AuthorGateway;
import com.iud.library.repository.AuthorRepository;
import com.iud.library.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService implements AuthorGateway {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AuthorDTO saveAuthor(Integer bookId, AuthorDTO authorDTO) {
        Book book = getBook(bookId);
        Author author = convertDTOToAuthor(authorDTO);
        author.setBook(book);
        authorRepository.save(author);
        return convertAuthorToDTO(author);
    }

    private Book getBook(Integer bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book", "id", bookId));
    }

    @Override
    public List<AuthorDTO> findAuthorByBook(Integer bookId) {
        List<Author> authors = authorRepository.findByBookId(bookId);
        return authors.stream()
                .map(this::convertAuthorToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDTO findAuthorById(Integer bookId, Integer authorId) {
        Book book = getBook(bookId);
        Author author = getAuthor(authorId);

        validateBookAndAuthorId(book, author);

        return convertAuthorToDTO(author);
    }

    private void validateBookAndAuthorId(Book book, Author author) {
        if(!author.getBook().getId().equals(book.getId())){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "the author does not belong to the book");
        }
    }

    private Author getAuthor(Integer authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("author", "id", authorId));
    }


    @Override
    public AuthorDTO updateAuthor(Integer bookId, Integer authorId, AuthorDTO authorDTO) {
        Book book = getBook(bookId);
        Author author  = getAuthor(authorId);
        validateBookAndAuthorId(book, author);
        author.setAuthorName(authorDTO.getAuthorName());
        authorRepository.save(author);
        return convertAuthorToDTO(author);
    }

    @Override
    public void deleteAuthor(Integer bookId, Integer authorId) {
        Book book = getBook(bookId);
        Author author = getAuthor(authorId);
        validateBookAndAuthorId(book, author);
        authorRepository.delete(author);
    }

    private AuthorDTO convertAuthorToDTO(Author author){return modelMapper.map(author, AuthorDTO.class);}

    private Author convertDTOToAuthor(AuthorDTO authorDTO){return modelMapper.map(authorDTO, Author.class);}
}
