package com.iud.library.request.book;


import com.iud.library.entity.Author;
import com.iud.library.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingBookRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String isbn;

    private Integer numberOfPages;

    @NotEmpty
    private String format;

    @NotEmpty
    private String publisherName;

    @NotEmpty
    private Set<Subject> subjects = new HashSet<>();

    @NotEmpty
    private Set<Author> authors = new HashSet<>();

}
