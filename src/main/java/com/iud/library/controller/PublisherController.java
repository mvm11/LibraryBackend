package com.iud.library.controller;


import com.iud.library.dto.PublisherDTO;
import com.iud.library.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publisher")
@CrossOrigin(origins = "*")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;
    @GetMapping("/findAllPublishers")
    public List<PublisherDTO> findAllPublishers(){

        return publisherService.findAllPublishers();

    }

    @GetMapping("/findPublisherById/{publisherId}")
    public ResponseEntity<PublisherDTO> findPublisherById(
            @PathVariable(value = "publisherId") Integer publisherId
    ){
        PublisherDTO publisherDTO = publisherService.findPublisherById(publisherId);
        return new ResponseEntity<>(publisherDTO, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{bookId}/publishers/savePublisher")
    public ResponseEntity<PublisherDTO> saveCopy
            (@PathVariable(value = "bookId") Integer bookId, @RequestBody PublisherDTO publisherDTO){

        return new ResponseEntity<>(publisherService.savePublisher(bookId, publisherDTO), HttpStatus.CREATED);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updatePublisher/{publisherId}")
    public ResponseEntity<PublisherDTO> updatePublisher(@PathVariable Integer publisherId, @RequestBody PublisherDTO publisherDTO){

        return new ResponseEntity<>(publisherService.updatePublisher(publisherId, publisherDTO), HttpStatus.CREATED);
    }

    //Delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookId}/publishers/{publisherId}/deletePublisher")
    ResponseEntity<String> deletePublisher(@PathVariable Integer bookId, @PathVariable Integer publisherId){
        publisherService.deletePublisher(bookId, publisherId);
        return new ResponseEntity<>("The publisher has been deleted successful",HttpStatus.NO_CONTENT);
    }
}
