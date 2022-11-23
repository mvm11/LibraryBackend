package com.iud.library.controller;

import com.iud.library.dto.BookDTO;
import com.iud.library.request.SavingBookRequest;
import com.iud.library.request.UpdatingBookRequest;
import com.iud.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;


    @GetMapping(value = "/findBooks/categoryId/{categoryId}")
    public List<BookDTO> findBooksByCategoryId(@PathVariable(value = "categoryId")Integer categoryId){
        return bookService.findBookByCategory(categoryId);
    }

    @GetMapping(value = "/findBook/categoryId/{categoryId}/bookId/{bookId}")
    public ResponseEntity<BookDTO> findBookById(
            @PathVariable(value = "categoryId") Integer categoryId,
            @PathVariable(value = "bookId") Integer bookId
    ) {
        return ResponseEntity.ok(bookService.findBookById(categoryId, bookId));
    }

    // Find by publisher
    @GetMapping(value = "/getBookByPublisher/publisher/{publisher}")
    public ResponseEntity<List<BookDTO>> findBookByPublisher(@PathVariable String publisher) {
        return ResponseEntity.ok(bookService.findBookByPublisher(publisher));
    }

    // Find by category
    @GetMapping(value = "/getBookByCategory/categoryName/{categoryName}")
    public ResponseEntity<List<BookDTO>> findBookByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(bookService.findBookByCategory(categoryName));
    }

    // Find by format
    @GetMapping(value = "/getBookByFormat/format/{format}")
    public ResponseEntity<List<BookDTO>> findBookByFormat(@PathVariable String format) {
        return ResponseEntity.ok(bookService.findBookByFormat(format));
    }

    // Find by author
    @GetMapping(value = "/getBookByAuthor/author/{author}")
    public ResponseEntity<List<BookDTO>> findBookByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.findBookByAuthor(author));
    }

    // Find by subject
    @GetMapping(value = "/getBookBySubject/subject/{subject}")
    public ResponseEntity<List<BookDTO>> findBookBySubject(@PathVariable String subject) {
        return ResponseEntity.ok(bookService.findBookBySubject(subject));
    }
    // Save
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/saveBook/categoryId/{categoryId}")
    ResponseEntity<BookDTO> saveBook(@Valid @PathVariable Integer categoryId, @RequestBody SavingBookRequest savingBookRequest){
        return new ResponseEntity<>(bookService.createBook(categoryId, savingBookRequest), HttpStatus.CREATED);
    }
    // Update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateBook/categoryId/{categoryId}/bookId/{bookId}")
    ResponseEntity<BookDTO> updateBook(
            @PathVariable(value = "categoryId") Integer categoryId,
            @PathVariable(value = "bookId") Integer bookId,
            @RequestBody UpdatingBookRequest updatingBookRequest){
        return new ResponseEntity<>(bookService.updateBook(categoryId, bookId, updatingBookRequest), HttpStatus.OK);
    }
    //Delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteBook/categoryId/{categoryId}/bookId/{bookId}")
    ResponseEntity<String> deleteBook(
            @PathVariable(value = "categoryId") Integer categoryId,
            @PathVariable(value = "bookId") Integer bookId
    ){
        bookService.deleteBook(categoryId, bookId);
        return new ResponseEntity<>("The book has been deleted successful",HttpStatus.NO_CONTENT);
    }
}
