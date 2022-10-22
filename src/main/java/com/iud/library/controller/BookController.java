package com.iud.library.controller;

import com.iud.library.common.constants.AppConstants;
import com.iud.library.dto.BookDTO;
import com.iud.library.dto.BookResponse;
import com.iud.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    // Find All
    @GetMapping(value = "/getAllBooks")
    public BookResponse FindAllBooks(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_NUMBER_PAGE, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_QUANTITY_BOOKS, required = false) int pageQuantityOfBooks,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BOOK_BY, required = false) String sortBookBy){
        return bookService.findAllBooks(pageNumber, pageQuantityOfBooks, sortBookBy);
    }
    // Find by id
    @GetMapping(value = "/getBookById/{id}")
    public ResponseEntity<BookDTO> findBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.findBookById(id));
    }
    // Save
    @PostMapping("/saveBook")
    ResponseEntity<BookDTO> saveBook(@RequestBody BookDTO bookDTO){
        return new ResponseEntity<>(bookService.createBook(bookDTO), HttpStatus.CREATED);
    }
    // Update
    @PutMapping("/updateBook/{id}")
    ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO bookDTO, @PathVariable Integer id){
        return new ResponseEntity<>(bookService.updateBook(bookDTO, id), HttpStatus.OK);
    }
    //Delete
    @DeleteMapping("/deleteBook/{id}")
    ResponseEntity deleteBook(@PathVariable Integer id){
        bookService.deleteBook(id);
        return new ResponseEntity<>("The book has been deleted successful",HttpStatus.NO_CONTENT);
    }
}
