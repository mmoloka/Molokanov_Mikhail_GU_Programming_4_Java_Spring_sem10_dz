package org.example.sem10_dz.api;

import lombok.extern.slf4j.Slf4j;
import org.example.sem10_dz.model.Book;
import org.example.sem10_dz.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        final Book book;
        try {
            book = bookService.getBookById(id);
        } catch (NoSuchElementException e) {
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBookById(id);
        } catch (NoSuchElementException e) {
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        log.info("Добавлена книга:  name = {}", book.getName());
        return new ResponseEntity<>(bookService.addBook(book), HttpStatus.CREATED);
    }
}
