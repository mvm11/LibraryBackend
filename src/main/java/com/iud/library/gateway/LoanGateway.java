package com.iud.library.gateway;

import com.iud.library.dto.LoanDTO;
import com.iud.library.request.loan.SavingLoanRequest;
import com.iud.library.request.loan.UpdatingLoanRequest;

import java.util.List;

public interface LoanGateway {

    LoanDTO saveLoan(SavingLoanRequest savingLoanRequest);
    List<LoanDTO> findLoansByUserId(Integer userId);
    LoanDTO findLoanById(Integer userId, Integer loanId);
    LoanDTO returnBook(UpdatingLoanRequest updatingLoanRequest);

}
