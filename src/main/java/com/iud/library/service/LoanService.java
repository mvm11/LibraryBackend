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
import com.iud.library.request.loan.SavingLoanRequest;
import com.iud.library.request.loan.UpdatingLoanRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    @Override
    public LoanDTO saveLoan(SavingLoanRequest savingLoanRequest) {

        // Validate if the user exists
        LibraryUser libraryUser = getLibraryUserByDni(savingLoanRequest.getLibraryUserDni());

        //Validate if  the user can borrow books
        validateUseBorrowBooks(libraryUser);

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
            loan.setLibraryUser(libraryUser);
            loanRepository.save(loan);
            //Load the cron expression from database
            taskScheduler.schedule(
                    () -> startJob(copy, libraryUser),
                    new Date(OffsetDateTime.now().plusSeconds(2).toInstant().toEpochMilli())
            );
            return convertLoanToDTO(loan);
        }
        return null;
    }

    @Override
    public List<LoanDTO> findLoansByUserId(Integer userId) {
        LibraryUser libraryUser = getUserById(userId);

        return libraryUser.getLoans()
                .stream()
                .map(this::convertLoanToDTO)
                .collect(Collectors.toList());
    }

    private LibraryUser getUserById(Integer userId) {
        return libraryUserRepository.findAll()
                .stream().filter(libraryUser1 -> libraryUser1.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User", "id", userId));
    }

    private void validateUseBorrowBooks(LibraryUser libraryUser) {
        if(Boolean.FALSE.equals(libraryUser.getCanBorrowBooks())){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "Actually the user with the dni: " + libraryUser.getDni() + " cannot borrow books");
        }
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
                    new Date(OffsetDateTime.now().plusSeconds(2).toInstant().toEpochMilli())
            );

            return convertLoanToDTO(loan);
        }
    }

    public void startJob(Copy copy, LibraryUser libraryUser) {
        if(copy.isLend()){
            libraryUser.setCanBorrowBooks(false);
            libraryUserRepository.save(libraryUser);
            log.info("the user " + libraryUser.getName() +" has not returned the copy " + copy.getEditionNumber() + ", so he will not be able to lend more books until he returns it.");
        }
    }
    private void validateAvailableCopies(List<Copy> copies) {
        if(copies.isEmpty()){
            throw new LibraryException(HttpStatus.INTERNAL_SERVER_ERROR, "There aren't available copies");
        }
    }

    private LibraryUser getLibraryUserByDni(String userDni) {
        return libraryUserRepository
                .findAll()
                .stream()
                .filter(libraryUser1 -> libraryUser1.getDni().equalsIgnoreCase(userDni))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User", "dni", userDni));
    }
    @Override
    public LoanDTO findLoanById(Integer userId, Integer loanId) {
        LibraryUser libraryUser = getUserById(userId);
        Loan loan = getLoanByUserAndLoanId(loanId, libraryUser);
        return convertLoanToDTO(loan);
    }

    private Loan getLoanByUserAndLoanId(Integer loanId, LibraryUser libraryUser) {
        return libraryUser.getLoans()
                .stream()
                .filter(loan -> loan.getId().equals(loanId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("loan", "id", loanId));
    }

    @Override
    public LoanDTO returnBook(UpdatingLoanRequest updatingLoanRequest) {
        // Validate if the user exists
        LibraryUser libraryUser = getLibraryUserByDni(updatingLoanRequest.getLibraryUserDni());
        Loan loan = getLoanByEditionNumber(updatingLoanRequest, libraryUser);
        Copy copy = loan.getCopy();
        validateIfCopyIsLend(copy);
        libraryUser.setCanBorrowBooks(true);
        copy.setLend(false);
        copyRepository.save(copy);
        libraryUserRepository.save(libraryUser);
        loanRepository.save(loan);
        return convertLoanToDTO(loan);
    }

    private Loan getLoanByEditionNumber(UpdatingLoanRequest updatingLoanRequest, LibraryUser libraryUser) {
        return libraryUser.getLoans().stream()
                .filter(loan1 -> loan1.getCopy().getEditionNumber().equalsIgnoreCase(updatingLoanRequest.getEditionNumber()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Copy", "edition number", updatingLoanRequest.getEditionNumber()));
    }

    private void validateIfCopyIsLend(Copy copy) {
        if(!copy.isLend()){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "Actually that copy book is not lend");
        }
    }

    private LoanDTO convertLoanToDTO(Loan loan){return modelMapper.map(loan, LoanDTO.class);}

    private Loan convertDTOToLoan(LoanDTO loanDTO){return modelMapper.map(loanDTO, Loan.class);}
}
