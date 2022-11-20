package com.iud.library.service;

import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.BookDTO;
import com.iud.library.dto.CopyDTO;
import com.iud.library.dto.PublisherDTO;
import com.iud.library.entity.Book;
import com.iud.library.entity.Copy;
import com.iud.library.entity.Publisher;
import com.iud.library.gateway.PublisherGateway;
import com.iud.library.repository.PublisherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class PublisherService implements PublisherGateway {

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private ModelMapper modelMapper;



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

    @Override
    public List<CopyDTO> findCopyByBook(Integer bookId) {
        return null;
    }

    private Publisher getPublisher(Integer publisherId) {
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> new NotFoundException("publisher", "id", publisherId));
    }

    @Override
    public PublisherDTO savePublisher(PublisherDTO publisherDTO) {
        // Parse from DTO to Entity
        Publisher publisher = convertDTOToPublisher(publisherDTO);

        //Save in the repository
        Publisher newPublisher = publisherRepository.save(publisher);

        // Parse from Entity to DTO
        return convertPublisherToDTO(newPublisher);
    }

    @Override
    public PublisherDTO updatePublisher(Integer publisherId, PublisherDTO publisherDTO) {
        return null;
    }

    @Override
    public void deletePublisher(Integer publisherId) {

    }

    private PublisherDTO convertPublisherToDTO(Publisher publisher) {
        return modelMapper.map(publisher, PublisherDTO.class);
    }

    private Publisher convertDTOToPublisher(PublisherDTO publisherDTO) {return modelMapper.map(publisherDTO, Publisher.class);
    }
}
