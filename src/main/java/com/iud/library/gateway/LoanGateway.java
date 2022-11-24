package com.iud.library.gateway;

import com.iud.library.dto.LoanDTO;
import com.iud.library.request.loan.SavingLoanRequest;
import com.iud.library.request.loan.UpdatingLoanRequest;

import java.util.List;

public interface LoanGateway {

    LoanDTO saveLoan(SavingLoanRequest savingLoanRequest);
    List<LoanDTO> findLoanByCopy(Integer copyId);
    LoanDTO findLoanById(Integer copyId, Integer loanId);
    LoanDTO returnBook(UpdatingLoanRequest updatingLoanRequest);
    void deleteLoan(Integer bookId, Integer copyId);

}
