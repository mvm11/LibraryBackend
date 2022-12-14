package com.iud.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDTO {

    private Integer id;

    @NotEmpty
    @Size(min = 2, message = "The publisher's name should have at least two characters")
    private String publisherName;

}
