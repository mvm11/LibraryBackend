package com.iud.library.dto;

import com.iud.library.entity.Book;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Integer id;

    @NotEmpty
    @Size(min = 2, message = "The category's name should have at least two characters")
    private String categoryName;

    private List<Book> books = new ArrayList<>();
}
