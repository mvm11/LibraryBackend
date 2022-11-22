package com.iud.library.controller;


import com.iud.library.dto.AuthorDTO;
import com.iud.library.dto.SubjectDTO;
import com.iud.library.service.AuthorService;
import com.iud.library.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@CrossOrigin(origins = "*")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping(value = "/findAuthors/bookId/{bookId}")
    public List<AuthorDTO> findAllAuthors(@PathVariable(value = "bookId")Integer bookId){
        return authorService.findAuthorByBook(bookId);
    }

    @GetMapping("/findAuthor/bookId/{bookId}/authorId/{authorId}")
    public ResponseEntity<AuthorDTO> findAuthorById(
            @PathVariable(value = "bookId") Integer bookId,
            @PathVariable(value = "authorId") Integer authorId
    ){
        return new ResponseEntity<>(authorService.findAuthorById(bookId, authorId), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/saveAuthor/bookId/{bookId}")
    public ResponseEntity<AuthorDTO> saveAuthor
            (@PathVariable(value = "bookId") Integer bookId, @RequestBody AuthorDTO authorDTO){

        return new ResponseEntity<>(authorService.saveAuthor(bookId, authorDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateAuthor/bookId/{bookId}/authorId/{authorId}")
    public ResponseEntity<AuthorDTO> updateAuthor(
            @PathVariable(value = "bookId") Integer bookId,
            @PathVariable(value = "authorId") Integer authorId,
            @RequestBody AuthorDTO authorDTO){

        return new ResponseEntity<>(authorService.updateAuthor(bookId, authorId, authorDTO), HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteAuthor/bookId/{bookId}/authorId/{authorId}")
    public ResponseEntity<String> deleteAuthor(
            @PathVariable(value = "bookId") Integer bookId,
            @PathVariable(value = "authorId") Integer authorId
    ){
        authorService.deleteAuthor(bookId, authorId);
        return new ResponseEntity<>("The author has been deleted successful", HttpStatus.NO_CONTENT);
    }

}
