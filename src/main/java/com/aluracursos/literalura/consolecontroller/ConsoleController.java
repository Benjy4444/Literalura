package com.aluracursos.literalura.consolecontroller;

import com.aluracursos.literalura.model.Book;
import com.aluracursos.literalura.model.DatosBook;
import com.aluracursos.literalura.repository.BookRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
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
            System.out.println("1 - Elegir un libro por título");
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
        System.out.println("----" + jsonDevuelto);
        booknuevo = jsonStringALibro(jsonDevuelto);
        System.out.println(booknuevo.toString());


        //ConvierteDatos conversor = new ConvierteDatos();
        //DatosBook datosLibro = conversor.obtenerDatos(jsonDevuelto, DatosBook.class);
        //System.out.println(datosLibro);


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

    private Book jsonStringALibro (String jsonDevuelto){
        Book book = new Book();
        //Obtiene los datos del Json en formato string....
        var sTitle = jsonDevuelto.substring(jsonDevuelto.indexOf("title"));
        var sFinalTitle = sTitle.substring(8,38);
        var corte = sFinalTitle.indexOf(",");
        sFinalTitle = sFinalTitle.substring(0,(corte-1));
        book.setTitle(sFinalTitle);

        var sAuthors = jsonDevuelto.substring(jsonDevuelto.indexOf("name"));
        var sFinalAuthors = sAuthors.substring(7,47);
        var corteb = sFinalAuthors.indexOf(",");
        var sFinalAuthorsb = sFinalAuthors.substring(0,(corte+10));
        var cortec = sFinalAuthorsb.indexOf(",");
        sFinalAuthors = sFinalAuthors.substring(0,(corteb+cortec));
        book.setAuthor(sFinalAuthors);

        var sBirthYear = jsonDevuelto.substring(jsonDevuelto.indexOf("birth_year"));
        var sFinalBirthYear = sBirthYear.substring(12,16);
        book.setBirthYear(Integer.valueOf(sFinalBirthYear));

        var sDeathYear = jsonDevuelto.substring(jsonDevuelto.indexOf("death_year"));
        var sFinalDeathYear = sDeathYear.substring(12,16);
        book.setDeathYear(Integer.valueOf(sFinalDeathYear));

        var sLanguage = jsonDevuelto.substring(jsonDevuelto.indexOf("language"));
        var sFinalLanguage = sLanguage.substring(13,15);
        book.setLanguage(sFinalLanguage);

        Book libro = book;
        return libro;
    }

}





