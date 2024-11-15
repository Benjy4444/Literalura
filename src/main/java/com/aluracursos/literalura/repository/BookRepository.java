package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title); //Metodo para buscar por título

    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT DISTINCT b.author FROM Book b")
    List<String> findDistinctAuthors();

    @Query("SELECT b.author FROM Book b WHERE b.birthYear <= :year AND (b.deathYear IS NULL OR b.deathYear > :year)")
    List<String> findAuthorsAliveInYear(int year);

    List<Book> findByLanguage(String language);
}
