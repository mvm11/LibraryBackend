package com.iud.library.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class LibraryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String lastName;
    private String address;
    @Column(name = "dni", length = 30, nullable = false, unique = true)
    private String dni;
    private String cellphone;
    private Boolean canBorrowBooks;
    private String username;
    private String email;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "libraryUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Loan> loans = new HashSet<>();

}
