package com.iud.library.controller;

import com.iud.library.dto.CategoryDTO;
import com.iud.library.dto.CopyDTO;
import com.iud.library.dto.LibraryUserDTO;
import com.iud.library.entity.LibraryUser;
import com.iud.library.request.libraryuser.UpdatingLibraryUserRequest;
import com.iud.library.service.LibraryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libraryUser")
@CrossOrigin(origins = "*")
public class LibraryUserController {

    @Autowired
    private LibraryUserService libraryUserService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findLibraryUsers")
    public List<LibraryUserDTO> findLibraryUsers(){

        return libraryUserService.findAllLibraryUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findLibraryUser/libraryUserId/{libraryUserId}")
    public ResponseEntity<LibraryUserDTO> libraryUserById(@PathVariable(value = "libraryUserId") Integer libraryUserId){

        return new ResponseEntity<>(libraryUserService.findLibraryUserById(libraryUserId), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    @PutMapping("/updateLibraryUser/libraryUserId/{libraryUserId}")
    public ResponseEntity<LibraryUserDTO> updateLibraryUser(
            @PathVariable Integer libraryUserId,
            @RequestBody UpdatingLibraryUserRequest updatingLibraryUserRequest){

        return new ResponseEntity<>(libraryUserService.updateLibraryUser(libraryUserId, updatingLibraryUserRequest), HttpStatus.OK);
    }

    //Delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteLibraryUser/libraryUserId/{libraryUserId}")
    ResponseEntity<String> deleteLibraryUser(@PathVariable Integer libraryUserId){
        libraryUserService.deleteLibraryUser(libraryUserId);
        return new ResponseEntity<>("The user has been deleted successful",HttpStatus.NO_CONTENT);
    }

}
