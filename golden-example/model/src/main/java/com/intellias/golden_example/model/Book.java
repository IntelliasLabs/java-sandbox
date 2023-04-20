package com.intellias.golden_example.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Book {

    private Long id;

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;

}
