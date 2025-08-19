package com.matheus.liter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.matheus.liter.client.BookClient;
import com.matheus.liter.model.Author;
import com.matheus.liter.model.Book;
import com.matheus.liter.repository.AuthorRepository;
import com.matheus.liter.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookClient client;
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    public BookService(BookClient client, BookRepository bookRepo, AuthorRepository authorRepo) {
        this.client = client;
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    public Book searchAndSaveBook(String title) throws Exception {
        JsonNode json = client.searchBookByTitle(title);
        JsonNode firstResult = json.get("results").get(0);

        Author author = new Author();
        author.setName(firstResult.get("authors").get(0).get("name").asText());
        author.setBirthYear(firstResult.get("authors").get(0).get("birth_year").asInt());
        author.setDeathYear(firstResult.get("authors").get(0).get("death_year").isNull() ? null :
                firstResult.get("authors").get(0).get("death_year").asInt());

        authorRepo.save(author);

        Book book = new Book();
        book.setTitle(firstResult.get("title").asText());
        book.setLanguage(firstResult.get("languages").get(0).asText());
        book.setDownloadCount(firstResult.get("download_count").asInt());
        book.setAuthor(author);

        return bookRepo.save(book);
    }

    public List<Book> listAllBooks() {
        return bookRepo.findAll();
    }

    public List<Book> listBooksByLanguage(String lang) {
        return bookRepo.findByLanguage(lang);
    }

    public List<Author> listAuthorsAliveInYear(Integer year) {
        return authorRepo.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
    }
}