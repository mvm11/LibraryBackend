package com.iud.library.controller;

import com.iud.library.dto.CopyDTO;
import com.iud.library.dto.LoanDTO;
import com.iud.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books/copies")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{copyId}/loans/saveLoan")
    public ResponseEntity<LoanDTO> saveLoan
            (@PathVariable(value = "copyId") Integer copyId, @RequestBody LoanDTO loanDTO){

        return new ResponseEntity<>(loanService.saveLoan(copyId, loanDTO), HttpStatus.CREATED);

    }
}
