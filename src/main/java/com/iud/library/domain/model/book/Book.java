package com.iud.library.domain.model.book;

import com.iud.library.domain.model.resource.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name= "Book")
@Table(name="book")
@Data


public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;
    private String ISBN;
    private Integer numberOfPages;
    private String publishing;
    private String format;
    private String category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book", fetch = FetchType.LAZY)
    private List<Resource> resourceList;


}