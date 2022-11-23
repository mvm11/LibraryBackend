package com.iud.library.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private Integer id;
    private LocalDate startDate;
    private LocalDate finishDate;
}
