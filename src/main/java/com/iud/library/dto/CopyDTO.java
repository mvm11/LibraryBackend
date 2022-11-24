package com.iud.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CopyDTO {

    private Integer id;
    private String editionNumber;
    private String state;
    private boolean isLend;
}
