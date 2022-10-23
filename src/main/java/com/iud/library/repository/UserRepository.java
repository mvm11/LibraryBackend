package com.iud.library.repository;

import com.iud.library.entity.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<LibraryUser, Integer> {

    Optional<LibraryUser> findByEmail(String email);
    Optional<LibraryUser> findByUsernameOrEmail(String username, String email);
    Optional<LibraryUser> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String username);
}