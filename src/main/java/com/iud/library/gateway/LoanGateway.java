package com.iud.library.gateway;

import com.iud.library.dto.LoanDTO;
import com.iud.library.request.SavingLoanRequest;

import java.util.List;

public interface LoanGateway {

    LoanDTO saveLoan(SavingLoanRequest savingLoanRequest);
    List<LoanDTO> findLoanByCopy(Integer copyId);
    LoanDTO findLoanById(Integer copyId, Integer loanId);
    LoanDTO updateLoan(Integer copyId, Integer loanId, LoanDTO loanDTO);
    void deleteLoan(Integer bookId, Integer copyId);

}
