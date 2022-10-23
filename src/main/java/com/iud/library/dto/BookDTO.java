package com.iud.library.dto;

import com.iud.library.entity.Copy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

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


    private String publisher;


    private String format;


    private String category;

    private List<Copy> copies;

}
