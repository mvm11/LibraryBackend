package com.iud.library.controller;

import com.iud.library.dto.CopyDTO;
import com.iud.library.request.UpdatingCopyRequest;
import com.iud.library.service.CopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/copy")
@CrossOrigin(origins = "*")
public class CopyController {

    @Autowired
    private CopyService copyService;

    @GetMapping("/findCopies/bookId/{bookId}")
    public List<CopyDTO> findCopiesByBookId(@PathVariable(value = "bookId") Integer bookId){

        return copyService.findCopyByBook(bookId);

    }

    @GetMapping("/findCopy/bookId/{bookId}/copyId/{copyId}")
    public ResponseEntity<CopyDTO> findCopyById(
            @PathVariable(value = "bookId") Integer bookId,
            @PathVariable(value = "copyId") Integer copyId
            ){
        CopyDTO copyDTO = copyService.findCopyById(bookId, copyId);
        return new ResponseEntity<>(copyDTO, HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{bookId}/copies/saveCopy")
    public ResponseEntity<CopyDTO> saveCopy
            (@PathVariable(value = "bookId") Integer bookId, @RequestBody CopyDTO copyDTO){

        return new ResponseEntity<>(copyService.saveCopy(bookId, copyDTO), HttpStatus.CREATED);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bookId}/copies/{copyId}/updateCopy")
    public ResponseEntity<CopyDTO> updateCopy(
            @PathVariable(value = "bookId") Integer bookId,
            @PathVariable(value = "copyId") Integer copyId,
            @RequestBody UpdatingCopyRequest updatingCopyRequest){
        CopyDTO copyDTOUpdated = copyService.updateCopy(bookId, copyId, updatingCopyRequest);
        return new ResponseEntity<>(copyDTOUpdated, HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookId}/copies/{copyId}/deleteCopy")
    public ResponseEntity<String> deleteCopy(
            @PathVariable(value = "bookId") Integer bookId,
            @PathVariable(value = "copyId") Integer copyId
    ){
        copyService.deleteCopy(bookId, copyId);
        return new ResponseEntity<>("The copy has been deleted successful", HttpStatus.NO_CONTENT);
    }

}
