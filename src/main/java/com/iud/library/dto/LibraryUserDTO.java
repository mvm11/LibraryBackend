package com.iud.library.dto;

import com.iud.library.entity.Loan;
import com.iud.library.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryUserDTO {

    private String id;
    private String name;
    private String lastName;
    private String address;
    private String dni;
    private String cellphone;
    private boolean canBorrowBooks;
    private String username;
    private String email;
    private String password;
    private Role role;
    private Set<Loan> loans = new HashSet<>();
}
