package com.iud.library.service;

import com.iud.library.common.exception.LibraryException;
import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.PublisherDTO;
import com.iud.library.entity.Book;
import com.iud.library.entity.Publisher;
import com.iud.library.gateway.PublisherGateway;
import com.iud.library.repository.BookRepository;
import com.iud.library.repository.PublisherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class PublisherService implements PublisherGateway {

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public PublisherDTO savePublisher(Integer bookId, PublisherDTO publisherDTO) {

        Book book = getBook(bookId);
        if(book.getPublisher() == null){
            return setPublisher(publisherDTO, book);
        }
        throw new LibraryException(HttpStatus.BAD_REQUEST, "the book with id: " + book.getId() + " already has a publisher");
    }

    private PublisherDTO setPublisher(PublisherDTO publisherDTO, Book book) {
        book.setPublisher(convertDTOToPublisher(publisherDTO));
        publisherRepository.save(book.getPublisher());
        bookRepository.save(book);
        return convertPublisherToDTO(book.getPublisher());
    }

    private Book getBook(Integer bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book", "id", bookId));
    }

    @Override
    public List<PublisherDTO> findAllPublishers() {
        return publisherRepository.findAll()
                .stream()
                .map(this::convertPublisherToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PublisherDTO findPublisherById(Integer publisherId) {
        Publisher publisher = getPublisher(publisherId);
        return convertPublisherToDTO(publisher);
    }
    private Publisher getPublisher(Integer publisherId) {
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> new NotFoundException("publisher", "id", publisherId));
    }
    @Override
    public PublisherDTO updatePublisher(Integer publisherId, PublisherDTO publisherDTO) {

        Publisher foundPublisher = getPublisher(publisherId);
        foundPublisher.setPublisherName(publisherDTO.getPublisherName());

        publisherRepository.save(foundPublisher);

        return convertPublisherToDTO(foundPublisher);
    }

    @Override
    public void deletePublisher(Integer bookId, Integer publisherId) {

        Book book = getBook(bookId);
        Publisher foundPublisher = getPublisher(publisherId);
        book.setPublisher(null);
        bookRepository.save(book);
        publisherRepository.delete(foundPublisher);
    }

    private PublisherDTO convertPublisherToDTO(Publisher publisher) {
        return modelMapper.map(publisher, PublisherDTO.class);
    }

    private Publisher convertDTOToPublisher(PublisherDTO publisherDTO) {return modelMapper.map(publisherDTO, Publisher.class);
    }
}
