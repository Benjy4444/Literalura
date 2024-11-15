package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name="libros")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private Integer birthYear;
    private Integer deathYear;
    private String language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "Título='" + title + '\'' +
                ", Autor='" + author + '\'' +
                ", Año de nacimiento=" + birthYear +
                ", Año de fallecimiento=" + deathYear +
                ", Lenguaje de la obra='" + language + '\'' +
                '}';
    }
}
