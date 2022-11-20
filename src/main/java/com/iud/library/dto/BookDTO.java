package com.iud.library.dto;

import com.iud.library.entity.Author;
import com.iud.library.entity.Category;
import com.iud.library.entity.Copy;
import com.iud.library.entity.Publisher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@Builder
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


    private Publisher publisher;


    private String format;

    private List<Copy> copies;

    @NotEmpty
    private Set<Author> authors;

    @NotEmpty
    private Set<Category> categories;

}
