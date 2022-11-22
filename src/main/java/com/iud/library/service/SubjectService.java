package com.iud.library.service;

import com.iud.library.common.exception.LibraryException;
import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.SubjectDTO;
import com.iud.library.entity.Book;
import com.iud.library.entity.Copy;
import com.iud.library.entity.Subject;
import com.iud.library.gateway.SubjectGateway;
import com.iud.library.repository.BookRepository;
import com.iud.library.repository.SubjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SubjectService implements SubjectGateway {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public SubjectDTO saveSubject(Integer bookId, SubjectDTO subjectDTO) {
        Book book = getBook(bookId);
        Subject subject = convertDTOToSubject(subjectDTO);
        subject.setBook(book);
        subjectRepository.save(subject);
        return convertSubjectToDTO(subject);
    }

    private Book getBook(Integer bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book", "id", bookId));
    }

    @Override
    public List<SubjectDTO> findSubjectByBook(Integer bookId) {
        List<Subject> subjects = subjectRepository.findByBookId(bookId);
        return subjects.stream()
                .map(this::convertSubjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectDTO findSubjectById(Integer bookId, Integer subjectId) {
        Book book = getBook(bookId);
        Subject subject  = getSubject(subjectId);
        validateBookAndSubjectId(book, subject);
        return convertSubjectToDTO(subject);
    }

    private void validateBookAndSubjectId(Book book, Subject subject) {
        if(!subject.getBook().getId().equals(book.getId())){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "the subject does not belong to the book");
        }
    }

    private Subject getSubject(Integer subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new NotFoundException("Subject", "id", subjectId));
    }




    @Override
    public SubjectDTO updateSubject(Integer bookId, Integer subjectId, SubjectDTO subjectDTO) {

        Book book = getBook(bookId);
        Subject subject  = getSubject(subjectId);

        subject.setSubjectName(subjectDTO.getSubjectName());
        subjectRepository.save(subject);
        return convertSubjectToDTO(subject);
    }

    @Override
    public void deleteCopy(Integer bookId, Integer copyId) {
        Book book = getBook(bookId);
        Subject subject = getSubject(copyId);

        subjectRepository.delete(subject);
    }

    private SubjectDTO convertSubjectToDTO(Subject subject){return modelMapper.map(subject, SubjectDTO.class);}

    private Subject convertDTOToSubject(SubjectDTO subjectDTO){return modelMapper.map(subjectDTO, Subject.class);}
}
