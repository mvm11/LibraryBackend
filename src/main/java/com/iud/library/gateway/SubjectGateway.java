package com.iud.library.gateway;


import com.iud.library.dto.SubjectDTO;

import java.util.List;

public interface SubjectGateway {

    SubjectDTO saveSubject(Integer bookId, SubjectDTO subjectDTO);
    List<SubjectDTO> findAllSubjects();
    SubjectDTO findSubjectById(Integer subjectId);
    List<SubjectDTO> findSubjectByBook(Integer bookId);
    SubjectDTO findSubjectBySubjectAndBookId(Integer bookId, Integer subjectId);
    SubjectDTO updateSubject(Integer bookId, Integer subjectId, SubjectDTO subjectDTO);
    void deleteCopy(Integer bookId, Integer copyId);
}
