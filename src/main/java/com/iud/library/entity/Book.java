package com.iud.library.entity;

import com.fasterxml.jackson.annotation.*;
import com.iud.library.request.BookRequest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name="numberOfPages", nullable = false)
    private Integer numberOfPages;

    @Column(name="format", nullable = false)
    private String format;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Copy> copies = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subject> subjects = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Author> authors = new HashSet<>();

    public Book(BookRequest bookRequest) {
        this.title = bookRequest.getTitle();
        this.isbn = bookRequest.getIsbn();
        this.numberOfPages = bookRequest.getNumberOfPages();
        this.format = bookRequest.getFormat();
    }
}
