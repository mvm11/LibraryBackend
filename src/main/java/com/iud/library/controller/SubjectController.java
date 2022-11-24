package com.iud.library.controller;

import com.iud.library.dto.SubjectDTO;
import com.iud.library.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
@CrossOrigin(origins = "*")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping(value = "/findSubjects/bookId/{bookId}")
    public List<SubjectDTO>  findAllSubjects(@PathVariable(value = "bookId")Integer bookId){
        return subjectService.findSubjectByBook(bookId);
    }

    @GetMapping("/findSubject/bookId/{bookId}/subjectId/{subjectId}")
    public ResponseEntity<SubjectDTO> findSubjectById(
            @PathVariable(value = "bookId") Integer bookId,
            @PathVariable(value = "subjectId") Integer subjectId
    ){
        SubjectDTO subjectDTO = subjectService.findSubjectById(bookId, subjectId);
        return new ResponseEntity<>(subjectDTO, HttpStatus.OK);

    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/saveSubject/bookId/{bookId}")
    public ResponseEntity<SubjectDTO> saveSubject
            (@PathVariable(value = "bookId") Integer bookId, @RequestBody SubjectDTO subjectDTO){

        return new ResponseEntity<>(subjectService.saveSubject(bookId, subjectDTO), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateSubject/bookId/{bookId}/subjectId/{subjectId}")
    public ResponseEntity<SubjectDTO> updateSubject(
            @PathVariable(value = "bookId") Integer bookId,
            @PathVariable(value = "subjectId") Integer subjectId,
            @RequestBody SubjectDTO subjectDTO){
        SubjectDTO updatedSubject = subjectService.updateSubject(bookId, subjectId, subjectDTO);
        return new ResponseEntity<>(updatedSubject, HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteSubject/bookId/{bookId}/subjectId/{subjectId}")
    public ResponseEntity<String> deleteSubject(
            @PathVariable(value = "bookId") Integer bookId,
            @PathVariable(value = "subjectId") Integer subjectId
    ){
        subjectService.deleteCopy(bookId, subjectId);
        return new ResponseEntity<>("The subject has been deleted successful", HttpStatus.NO_CONTENT);
    }
}
