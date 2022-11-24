package com.iud.library.gateway;

import com.iud.library.dto.AuthorDTO;

import java.util.List;

public interface AuthorGateway {

    AuthorDTO saveAuthor(Integer bookId, AuthorDTO authorDTO);
    List<AuthorDTO> findAuthorByBook(Integer bookId);
    AuthorDTO findAuthorById(Integer bookId, Integer authorId);
    AuthorDTO updateAuthor(Integer bookId, Integer authorId, AuthorDTO authorDTO);
    void deleteAuthor(Integer bookId, Integer authorId);
}
