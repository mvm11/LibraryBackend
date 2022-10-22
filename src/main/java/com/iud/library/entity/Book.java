package com.iud.library.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "books", uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "isbn"})})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="isbn", nullable = false)
    private String isbn;

    @Column(name="numberOfPages", nullable = false)
    private Integer numberOfPages;

    @Column(name="publisher", nullable = false)
    private String publisher;

    @Column(name="format", nullable = false)
    private String format;

    @Column(name="category", nullable = false)
    private String category;


}
