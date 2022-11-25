package com.iud.library.request.libraryuser;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatingLibraryUserRequest {

    private String name;
    private String lastName;
    private String address;
    private String dni;
    private String cellphone;
    private String username;
    private String email;
    private String password;
}
