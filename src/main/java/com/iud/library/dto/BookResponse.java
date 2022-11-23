package com.iud.library.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private List<BookDTO> bookContentList;
    private Integer pageNumber;
    private Integer pageQuantityOfBooks;
    private Long totalBooks;
    private Integer totalPages;
    private Boolean isTheLastOnePage;


}
