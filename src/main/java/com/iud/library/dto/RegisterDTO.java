package com.iud.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

    private String name;
    private String lastName;
    private String address;
    private String dni;
    private String cellphone;
    private Boolean canBorrowBooks;
    private String username;
    private String email;
    private String password;



}
