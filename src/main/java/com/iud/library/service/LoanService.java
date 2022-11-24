package com.iud.library.service;

import com.iud.library.common.exception.LibraryException;
import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.LoanDTO;
import com.iud.library.entity.*;
import com.iud.library.gateway.LoanGateway;
import com.iud.library.repository.BookRepository;
import com.iud.library.repository.CopyRepository;
import com.iud.library.repository.LibraryUserRepository;
import com.iud.library.repository.LoanRepository;
import com.iud.library.request.SavingLoanRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class LoanService implements LoanGateway {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private LibraryUserRepository libraryUserRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LoanDTO saveLoan(SavingLoanRequest savingLoanRequest) {

        // Validate if the user exists
        LibraryUser libraryUser = getLibraryUserByDni(savingLoanRequest);

        // Validate if there are available copies
        List<Copy> copies = getAvailableCopies(savingLoanRequest);

        validateAvailableCopies(copies);

        if(libraryUser.getRole().getName().equalsIgnoreCase("ROLE_STUDENT")){
            return validateStudentLoans(libraryUser, copies);
        } else if (libraryUser.getRole().getName().equalsIgnoreCase("ROLE_TEACHER")){
            Random random = new SecureRandom();
            Loan loan = new Loan();
            loan.setStartDate(LocalDate.now());
            loan.setFinishDate(loan.getStartDate().plus(1, ChronoUnit.WEEKS));
            Copy copy = copies.get(random.nextInt(copies.size()));
            copy.setLend(true);
            copyRepository.save(copy);
            loan.setCopy(copy);
            loanRepository.save(loan);
            return convertLoanToDTO(loan);
        }
        return null;
    }

    private List<Copy> getAvailableCopies(SavingLoanRequest savingLoanRequest) {
        return  bookRepository.findAll()
                .stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(savingLoanRequest.getBookTitle()))
                .collect(Collectors.toList())
                .stream()
                .map(Book::getCopies)
                .flatMap(copies1 -> copies1.stream()
                        .filter(copy -> !copy.isLend()))
                .collect(Collectors.toList());
    }

    private LoanDTO validateStudentLoans(LibraryUser libraryUser, List<Copy> copies) {
        if(libraryUser.getLoans().size() > 5){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "A student only can lend five (5) books");
        }else {
            Random random = new SecureRandom();
            Loan loan = new Loan();
            loan.setStartDate(LocalDate.now());
            loan.setFinishDate(loan.getStartDate().plus(1, ChronoUnit.WEEKS));
            Copy copy = copies.get(random.nextInt(copies.size()));
            copy.setLend(true);
            copyRepository.save(copy);
            loan.setCopy(copy);
            loan.setLibraryUser(libraryUser);
            loanRepository.save(loan);

            //Load the cron expression from database
            taskScheduler.schedule(
                    () -> startJob(copy, libraryUser),
                    new Date(OffsetDateTime.now().plusSeconds(5).toInstant().toEpochMilli())
            );

            return convertLoanToDTO(loan);
        }
    }

    public void startJob(Copy copy, LibraryUser libraryUser) {
        if(copy.isLend()){
            libraryUser.setCanBorrowBooks(false);
            libraryUserRepository.save(libraryUser);
        }
    }
    private void validateAvailableCopies(List<Copy> copies) {
        if(copies.isEmpty()){
            throw new LibraryException(HttpStatus.INTERNAL_SERVER_ERROR, "There aren't available copies");
        }
    }

    private LibraryUser getLibraryUserByDni(SavingLoanRequest savingLoanRequest) {
        return libraryUserRepository
                .findAll()
                .stream()
                .filter(libraryUser1 -> libraryUser1.getDni().equalsIgnoreCase(savingLoanRequest.getLibraryUserDni()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User", "dni", savingLoanRequest.getLibraryUserDni()));
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
