package com.iud.library.service;

import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.LoanDTO;
import com.iud.library.entity.Copy;
import com.iud.library.entity.Loan;
import com.iud.library.gateway.LoanGateway;
import com.iud.library.repository.CopyRepository;
import com.iud.library.repository.LoanRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class LoanService implements LoanGateway {


    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LoanDTO saveLoan(Integer copyId, LoanDTO loanDTO) {
        Loan loan = convertDTOToLoan(loanDTO);
        loan.setStartDate(new Date());
        Copy copy = getCopy(copyId);
        copy.setLend(true);
        copy.setLoan(loan);
        Loan newLoan = loanRepository.save(loan);
        return convertLoanToDTO(newLoan);
    }

    private Copy getCopy(Integer copyId) {
        return copyRepository.findById(copyId)
                .orElseThrow(() -> new NotFoundException("Copy", "id", copyId));
    }

    @Override
    public List<LoanDTO> findLoanByCopy(Integer copyId) {
        return null;
    }

    @Override
    public LoanDTO findLoanById(Integer copyId, Integer loanId) {
        return null;
    }

    @Override
    public LoanDTO updateLoan(Integer copyId, Integer loanId, LoanDTO loanDTO) {
        return null;
    }

    @Override
    public void deleteLoan(Integer bookId, Integer copyId) {

    }

    private LoanDTO convertLoanToDTO(Loan loan){return modelMapper.map(loan, LoanDTO.class);}

    private Loan convertDTOToLoan(LoanDTO loanDTO){return modelMapper.map(loanDTO, Loan.class);}
}
