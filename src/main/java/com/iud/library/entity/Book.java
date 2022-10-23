package com.iud.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }),
        @UniqueConstraint(columnNames = { "isbn" }) })
@Data
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

    @JsonBackReference
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Copy> copies = new ArrayList<>();


}
