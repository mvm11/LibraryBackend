package com.iud.library.service;

import com.iud.library.common.exception.LibraryException;
import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.CopyDTO;
import com.iud.library.entity.Book;
import com.iud.library.entity.Copy;
import com.iud.library.gateway.CopyGateway;
import com.iud.library.repository.BookRepository;
import com.iud.library.repository.CopyRepository;
import com.iud.library.request.UpdatingCopyRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CopyService implements CopyGateway {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CopyDTO saveCopy(Integer bookId, CopyDTO copyDTO) {
        Copy copy = new Copy();
        copy.setEditionNumber(copyDTO.getEditionNumber());
        copy.setState(copyDTO.getState());
        copy.setLend(false);
        Book book = getBook(bookId);
        copy.setBook(book);
        copyRepository.save(copy);
        return convertCopyToDTO(copy);
    }

    private Book getBook(Integer bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book", "id", bookId));
    }

    @Override
    public List<CopyDTO> findCopyByBook(Integer bookId) {
        List<Copy> copyList = copyRepository.findByBookId(bookId);
        return copyList.stream()
                .map(this::convertCopyToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CopyDTO findCopyById(Integer bookId, Integer copyId) {
        Book book = getBook(bookId);
        Copy copy  = getCopy(copyId);

        validateBookAndCopyId(book, copy);

        return convertCopyToDTO(copy);
    }

    private void validateBookAndCopyId(Book book, Copy copy) {
        if(!copy.getBook().getId().equals(book.getId())){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "the copy does not belong to the book");
        }
    }

    private Copy getCopy(Integer copyId) {
        return copyRepository.findById(copyId)
                .orElseThrow(() -> new NotFoundException("copy", "id", copyId));
    }

    @Override
    public CopyDTO updateCopy(Integer bookId, Integer copyId, UpdatingCopyRequest updatingCopyRequest) {
        Book book = getBook(bookId);
        Copy copy  = getCopy(copyId);
        validateBookAndCopyId(book, copy);
        copy.setEditionNumber(updatingCopyRequest.getEditionNumber());
        copy.setState(updatingCopyRequest.getState());

        Copy updatedCopy = copyRepository.save(copy);
        return convertCopyToDTO(updatedCopy);
    }

    @Override
    public void deleteCopy(Integer bookId, Integer copyId) {

        Book book = getBook(bookId);
        Copy copy  = getCopy(copyId);
        validateBookAndCopyId(book, copy);

        copyRepository.delete(copy);
    }

    private CopyDTO convertCopyToDTO(Copy copy){return modelMapper.map(copy, CopyDTO.class);}

    private Copy convertDTOToCopy(CopyDTO copyDTO){return modelMapper.map(copyDTO, Copy.class);}
}
