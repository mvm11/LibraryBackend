package com.iud.library.dto;

import com.iud.library.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Integer id;

    @NotEmpty
    @Size(min = 2, message = "The book's title should have at least two characters")
    private String title;

    @NotEmpty
    @Size(min = 2, message = "The book's isbn should have at least two characters")
    private String isbn;

    private Integer numberOfPages;

    private String format;

    private Publisher publisher;

    private Category category;

    @NotEmpty
    private Set<Subject> subjects;

    @NotEmpty
    private Set<Author> authors;

    private List<Copy> copies;

}
