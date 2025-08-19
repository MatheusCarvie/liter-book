package com.matheus.liter.controller;

import com.matheus.liter.model.Author;
import com.matheus.liter.model.Book;
import com.matheus.liter.service.BookService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public Book searchBook(@RequestParam String title) throws Exception {
        return service.searchAndSaveBook(title);
    }

    @GetMapping
    public List<Book> listBooks() {
        return service.listAllBooks();
    }

    @GetMapping("/language/{lang}")
    public List<Book> booksByLanguage(@PathVariable String lang) {
        return service.listBooksByLanguage(lang);
    }

    @GetMapping("/authors/alive/{year}")
    public List<Author> authorsAlive(@PathVariable Integer year) {
        return service.listAuthorsAliveInYear(year);
    }
}