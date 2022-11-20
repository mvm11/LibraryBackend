package com.iud.library.controller;

import com.iud.library.dto.BookDTO;
import com.iud.library.request.BookRequest;
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

    // Find All
    @GetMapping(value = "/getAllBooks")
    public List<BookDTO>  findAllBooks(){
        return bookService.findAllBooks();
    }
    // Find by id
    @GetMapping(value = "/getBookById/{id}")
    public ResponseEntity<BookDTO> findBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.findBookById(id));
    }

    // Find by publisher
    @GetMapping(value = "/getBookByPublisher/{publisher}")
    public ResponseEntity<List<BookDTO>> findBookByPublisher(@PathVariable String publisher) {
        return ResponseEntity.ok(bookService.findBookByPublisher(publisher));
    }

    // Find by category
    @GetMapping(value = "/getBookByCategory/{category}")
    public ResponseEntity<List<BookDTO>> findBookByCategory(@PathVariable String category) {
        return ResponseEntity.ok(bookService.findBookByCategory(category));
    }

    // Find by format
    @GetMapping(value = "/getBookByFormat/{format}")
    public ResponseEntity<List<BookDTO>> findBookByFormat(@PathVariable String format) {
        return ResponseEntity.ok(bookService.findBookByFormat(format));
    }

    // Find by author
    @GetMapping(value = "/getBookByAuthor/{author}")
    public ResponseEntity<List<BookDTO>> findBookByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.findBookByAuthor(author));
    }
    // Save
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/saveBook")
    ResponseEntity<BookDTO> saveBook(@Valid @RequestBody BookRequest bookRequest){
        return new ResponseEntity<>(bookService.createBook(bookRequest), HttpStatus.CREATED);
    }
    // Update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateBook/{id}")
    ResponseEntity<BookDTO> updateBook(@Valid @RequestBody BookRequest bookRequest, @PathVariable Integer id){
        return new ResponseEntity<>(bookService.updateBook(id, bookRequest), HttpStatus.OK);
    }
    //Delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteBook/{id}")
    ResponseEntity<String> deleteBook(@PathVariable Integer id){
        bookService.deleteBook(id);
        return new ResponseEntity<>("The book has been deleted successful",HttpStatus.NO_CONTENT);
    }
}
