package com.matheus.liter;

import com.matheus.liter.model.Author;
import com.matheus.liter.model.Book;
import com.matheus.liter.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiterApplication implements CommandLineRunner {

	private final BookService service;

	public LiterApplication(BookService service) {
		this.service = service;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiterApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		int option = -1;

		while (option != 0) {
			System.out.println("\n==== MENU GUTENBERG ===="); // Corrigi o "GUTENDEX"
			System.out.println("1 - Buscar livro por título");
			System.out.println("2 - Listar todos os livros");
			System.out.println("3 - Listar livros por idioma");
			System.out.println("4 - Listar autores vivos em um ano");
			System.out.println("0 - Sair");
			System.out.print("Escolha uma opção: ");

			try {
				option = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Opção inválida!");
				continue;
			}

			switch (option) {
				case 1 -> {
					System.out.print("Digite o título do livro: ");
					String title = scanner.nextLine();
					try {
						Book book = service.searchAndSaveBook(title);
						System.out.println("📚 Livro encontrado e salvo: " + book);
					} catch (Exception e) {
						System.out.println("Erro ao buscar livro: " + e.getMessage());
					}
				}
				case 2 -> {
					List<Book> books = service.listAllBooks();
					if (books.isEmpty()) {
						System.out.println("Nenhum livro encontrado!");
					} else {
						books.forEach(System.out::println);
					}
				}
				case 3 -> {
					System.out.print("Digite o idioma (ex: 'en', 'fr', 'pt'): ");
					String lang = scanner.nextLine();
					List<Book> booksByLang = service.listBooksByLanguage(lang);
					if (booksByLang.isEmpty()) {
						System.out.println("Nenhum livro encontrado nesse idioma!");
					} else {
						booksByLang.forEach(System.out::println);
					}
				}
				case 4 -> {
					System.out.print("Digite o ano: ");
					try {
						int year = Integer.parseInt(scanner.nextLine());
						List<Author> authors = service.listAuthorsAliveInYear(year);
						if (authors.isEmpty()) {
							System.out.println("Nenhum autor vivo nesse ano!");
						} else {
							authors.forEach(System.out::println);
						}
					} catch (NumberFormatException e) {
						System.out.println("Ano inválido!");
					}
				}
				case 0 -> System.out.println("Saindo...");
				default -> System.out.println("Opção inválida!");
			}
		}
	}
}