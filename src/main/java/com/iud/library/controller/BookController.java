package com.iud.library.controller;

import com.iud.library.dto.BookDTO;
import com.iud.library.entity.Book;
import com.iud.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    // Listar
    @GetMapping(value = "/getAllBooks")
    public List<BookDTO> FindAllBooks(){
        return bookService.findAllBooks();
    }
    // listar por ID
    @GetMapping(value = "/getBookById/{id}")
    public ResponseEntity<BookDTO> findBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.findBookById(id));
    }
    // Crear, Guardar
    @PostMapping("/saveBook")
    ResponseEntity<BookDTO> saveBook(@RequestBody BookDTO bookDTO){
        return new ResponseEntity<>(bookService.createBook(bookDTO), HttpStatus.CREATED);
    }
    // Actualizar
    @PutMapping("/updateBook/{id}")
    ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO bookDTO, @PathVariable Integer id){
        return new ResponseEntity<>(bookService.updateBook(bookDTO, id), HttpStatus.OK);
    }
    //Eliminar
    @DeleteMapping("/deleteBook/{id}")
    ResponseEntity deleteBook(@PathVariable Integer id){
        bookService.deleteBook(id);
        return new ResponseEntity<>("The book has been deleted successful",HttpStatus.NO_CONTENT);
    }
}
