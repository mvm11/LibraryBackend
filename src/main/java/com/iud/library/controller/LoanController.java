package com.iud.library.controller;

import com.iud.library.dto.CopyDTO;
import com.iud.library.dto.LoanDTO;
import com.iud.library.request.SavingLoanRequest;
import com.iud.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/loan")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/saveLoan")
    public ResponseEntity<LoanDTO> saveLoan(@Valid  @RequestBody SavingLoanRequest savingLoanRequest){

        return new ResponseEntity<>(loanService.saveLoan(savingLoanRequest), HttpStatus.CREATED);
    }
}
