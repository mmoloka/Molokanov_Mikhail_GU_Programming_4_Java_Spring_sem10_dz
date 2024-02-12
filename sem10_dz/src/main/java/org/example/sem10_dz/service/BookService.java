package org.example.sem10_dz.service;

import jakarta.annotation.PostConstruct;
import org.example.sem10_dz.model.Book;
import org.example.sem10_dz.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void generateData() {
        bookRepository.save(new Book("война и мир"));
        bookRepository.save(new Book("мертвые души"));
        bookRepository.save(new Book("чистый код"));
        bookRepository.save(new Book("мастер и маргарита"));
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        bookRepository.delete(book);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

}
