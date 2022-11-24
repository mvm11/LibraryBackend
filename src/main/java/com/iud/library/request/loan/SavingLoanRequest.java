package com.iud.library.request.loan;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingLoanRequest {

    @NotEmpty
    private String libraryUserDni;

    @NotEmpty
    private String bookTitle;

}
