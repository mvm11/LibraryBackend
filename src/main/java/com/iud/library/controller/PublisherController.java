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
    @PostMapping("/savePublisher")
    public ResponseEntity<PublisherDTO> savePublisher(@RequestBody PublisherDTO publisherDTO){

        return new ResponseEntity<>(publisherService.savePublisher(publisherDTO), HttpStatus.CREATED);

    }
}
