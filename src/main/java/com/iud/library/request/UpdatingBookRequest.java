package com.iud.library.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatingBookRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String isbn;

    private Integer numberOfPages;

    @NotEmpty
    private String format;
}
