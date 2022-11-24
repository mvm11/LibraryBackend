package com.iud.library.controller;

import com.iud.library.dto.CopyDTO;
import com.iud.library.dto.LoanDTO;
import com.iud.library.dto.PublisherDTO;
import com.iud.library.request.loan.SavingLoanRequest;
import com.iud.library.request.loan.UpdatingLoanRequest;
import com.iud.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/loan")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @PostMapping("/saveLoan")
    public ResponseEntity<LoanDTO> saveLoan(@Valid  @RequestBody SavingLoanRequest savingLoanRequest){

        return new ResponseEntity<>(loanService.saveLoan(savingLoanRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @GetMapping("/findLoans/userId/{userId}")
    public List<LoanDTO> findLoansByUserId(@PathVariable(value = "userId") Integer userId){
        return loanService.findLoansByUserId(userId);
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @GetMapping("/findLoan/userId/{userId}/loanId/{loanId}")
    public ResponseEntity<LoanDTO> findLoanById(
            @PathVariable(value = "userId") Integer userId,
            @PathVariable(value = "loanId") Integer loanId){

        return new ResponseEntity<>(loanService.findLoanById(userId, loanId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @PutMapping("/returnBook")
    public ResponseEntity<LoanDTO> returnBook(@RequestBody UpdatingLoanRequest updatingLoanRequest){

        return new ResponseEntity<>(loanService.returnBook(updatingLoanRequest), HttpStatus.CREATED);
    }

}
