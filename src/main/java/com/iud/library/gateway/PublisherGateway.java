package com.iud.library.gateway;

import com.iud.library.dto.PublisherDTO;

import java.util.List;

public interface PublisherGateway {

    PublisherDTO savePublisher(Integer bookId, PublisherDTO publisherDTO);
    List<PublisherDTO> findAllPublishers();
    PublisherDTO findPublisherById(Integer publisherId);
    PublisherDTO updatePublisher(Integer publisherId, PublisherDTO publisherDTO);
    void deletePublisher(Integer bookId, Integer publisherId);
}
