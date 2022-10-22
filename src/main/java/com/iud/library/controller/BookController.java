package com.iud.library.controller;

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
    public List<Book> FindAllBooks(){
        return bookService.findAllBooks();
    }
    // listar por ID
    @GetMapping(value = "/getBookById/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findBookById(id));
    }
    // Crear, Guardar
    @PostMapping("/saveBook")
    ResponseEntity<Book> saveBook(@RequestBody Book book){
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }
    // Actualizar
    @PutMapping("/updateBook/{id}")
    ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable Integer id){
        return new ResponseEntity<>(bookService.updateBook(book), HttpStatus.OK);
    }
    //Eliminar
    @DeleteMapping("/deleteBook/{id}")
    ResponseEntity deleteBook(@PathVariable Integer id){
        bookService.deleteBook(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
