package com.iud.library.controller;

import com.iud.library.common.constants.AppConstants;
import com.iud.library.dto.LoginDTO;
import com.iud.library.dto.RegisterDTO;
import com.iud.library.entity.LibraryUser;
import com.iud.library.entity.Role;
import com.iud.library.repository.RoleRepository;
import com.iud.library.repository.LibraryUserRepository;
import com.iud.library.security.JWTAuthResponseDTO;
import com.iud.library.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LibraryUserRepository libraryUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //get token from jwtTokenProvider
        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponseDTO(token));
    }

    // Register Admin

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterDTO registerDTO){

        if(libraryUserRepository.existsByUsername(registerDTO.getUsername())) {
            return new ResponseEntity<>("That username already exists ",HttpStatus.BAD_REQUEST);
        }

        if(libraryUserRepository.existsByEmail(registerDTO.getEmail())) {
            return new ResponseEntity<>("That email already exists ",HttpStatus.BAD_REQUEST);
        }

        LibraryUser libraryUser = LibraryUser.builder()
                .name(registerDTO.getName())
                .lastName(registerDTO.getLastName())
                .address(registerDTO.getAddress())
                .dni(registerDTO.getDni())
                .cellphone(registerDTO.getCellphone())
                .canBorrowBooks(registerDTO.getCanBorrowBooks())
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();

        Role roles = roleRepository.findByName(AppConstants.ROLE_ADMIN).get();
        libraryUser.setRoles(Collections.singleton(roles));

        libraryUserRepository.save(libraryUser);
        return new ResponseEntity<>("The admin has been register successful",HttpStatus.OK);
    }

    // Register Teacher

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerTeacher")
    public ResponseEntity<?> registerTeacher(@RequestBody RegisterDTO registerDTO){

        if(libraryUserRepository.existsByUsername(registerDTO.getUsername())) {
            return new ResponseEntity<>("That username already exists ",HttpStatus.BAD_REQUEST);
        }

        if(libraryUserRepository.existsByEmail(registerDTO.getEmail())) {
            return new ResponseEntity<>("That email already exists ",HttpStatus.BAD_REQUEST);
        }

        LibraryUser libraryUser = LibraryUser.builder()
                .name(registerDTO.getName())
                .lastName(registerDTO.getLastName())
                .address(registerDTO.getAddress())
                .dni(registerDTO.getDni())
                .cellphone(registerDTO.getCellphone())
                .canBorrowBooks(registerDTO.getCanBorrowBooks())
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();

        Role roles = roleRepository.findByName(AppConstants.ROLE_TEACHER).get();
        libraryUser.setRoles(Collections.singleton(roles));

        libraryUserRepository.save(libraryUser);
        return new ResponseEntity<>("The teacher has been register successful",HttpStatus.OK);
    }

    // Register Student

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerStudent")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterDTO registerDTO){
        if(Boolean.TRUE.equals(libraryUserRepository.existsByUsername(registerDTO.getUsername()))) {
            return new ResponseEntity<>("That username already exists ",HttpStatus.BAD_REQUEST);
        }

        if(Boolean.TRUE.equals(libraryUserRepository.existsByEmail(registerDTO.getEmail()))) {
            return new ResponseEntity<>("That email already exists ",HttpStatus.BAD_REQUEST);
        }

        LibraryUser libraryUser = LibraryUser.builder()
                .name(registerDTO.getName())
                .lastName(registerDTO.getLastName())
                .address(registerDTO.getAddress())
                .dni(registerDTO.getDni())
                .cellphone(registerDTO.getCellphone())
                .canBorrowBooks(registerDTO.getCanBorrowBooks())
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();

        Role roles = roleRepository.findByName(AppConstants.ROLE_STUDENT).get();
        libraryUser.setRoles(Collections.singleton(roles));

        libraryUserRepository.save(libraryUser);
        return new ResponseEntity<>("The student has been register successful",HttpStatus.OK);
    }


}
