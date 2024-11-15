package com.aluracursos.literalura.consolecontroller;

import com.aluracursos.literalura.model.Book;
import com.aluracursos.literalura.repository.BookRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleController implements CommandLineRunner {

    private static String URL_BASE = "https://gutendex.com/books/?search=";

    @Autowired
    private BookRepository bookRepository;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) throws JsonProcessingException {
        while (true) {
            System.out.println("*************************************************");
            System.out.println("LiterAlura - Opciones:");
            System.out.println("1 - Registrar un libro por título (Gutendex -> Base de Datos");
            System.out.println("2 - Listar libros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos en determinado año");
            System.out.println("5 - Listar libros por idioma");
            System.out.println("0 - Salir");
            System.out.println("*************************************************");
            System.out.println("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.println("*************************************************");
                    elegirLibroPorTitulo();
                    break;
                case "2":
                    System.out.println("*************************************************");
                    System.out.println(":::Lista de libros registrados:::");
                    listarLibrosRegistrados();
                    break;
                case "3":
                    System.out.println("*************************************************");
                    System.out.println(":::Lista de autores registrados:::");
                    listarAutoresRegistrados();
                    break;
                case "4":
                    System.out.println("*************************************************");
                    System.out.println(":::Lista de autores vivos en un año determinado:::");
                    listarAutoresVivos();
                    break;
                case "5":
                    System.out.println("*************************************************");
                    System.out.println(":::Lista de libros por idioma:::");
                    listarLibrosPorIdioma();
                    break;
                case "0":
                    System.out.println("*************************************************");
                    System.out.println("Saliendo del sistema");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void elegirLibroPorTitulo() throws JsonProcessingException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escribe el nombre del libro que deseas buscar: ");
        var nombreLibro = scanner.nextLine();
        ConsumoAPI consumoAPI = new ConsumoAPI();
        Book booknuevo = new Book();

        var jsonDevuelto = consumoAPI.obtenerDatos(URL_BASE + nombreLibro);
        //System.out.println("----" + jsonDevuelto);
        booknuevo = jsonStringALibro(jsonDevuelto);
        System.out.println(booknuevo.toString());
        bookRepository.save(booknuevo);
    }

    private void listarLibrosRegistrados() {
        List<Book> libros = bookRepository.findAll();
        libros.forEach(libro -> System.out.println(libro.getTitle() + " - " + libro.getAuthor()));
    }

    private void listarAutoresRegistrados() {
        List<String> autores = bookRepository.findDistinctAuthors();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.println("Introduce el año:");
        int año = Integer.parseInt(scanner.nextLine());
        List<String> autoresVivos = bookRepository.findAuthorsAliveInYear(año);
        autoresVivos.forEach(System.out::println);
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Introduce el idioma (ej. 'en' para inglés):");
        String idioma = scanner.nextLine();
        List<Book> libros = bookRepository.findByLanguage(idioma);
        libros.forEach(libro -> System.out.println(libro.getTitle() + " - " + libro.getAuthor()));
    }

    private Book jsonStringALibro(String jsonDevuelto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonDevuelto);

        JsonNode firstResult = rootNode.path("results").get(0);

        Book book = new Book();
        book.setTitle(firstResult.path("title").asText());
        book.setAuthor(firstResult.path("authors").get(0).path("name").asText());
        book.setBirthYear(firstResult.path("authors").get(0).path("birth_year").asInt());
        book.setDeathYear(firstResult.path("authors").get(0).path("death_year").asInt());
        book.setLanguage(firstResult.path("languages").get(0).asText());

        return book;
    }

}





