package com.iud.library.request;


import com.iud.library.entity.Author;
import com.iud.library.entity.Category;
import com.iud.library.entity.Copy;
import com.iud.library.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

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
