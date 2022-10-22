package com.iud.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Integer id;
    private String title;
    private String isbn;
    private Integer numberOfPages;
    private String publisher;
    private String format;
    private String category;

}
