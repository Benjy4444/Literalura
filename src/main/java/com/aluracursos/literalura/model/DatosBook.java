package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosBook(
        @JsonAlias("count") String title,
        @JsonAlias("next") String author,
        @JsonAlias("results:author:birthyear") Integer birthYear,
        @JsonAlias("results.author.deathyear") Integer deathYear,
        @JsonAlias("previous") String language ) {
}
