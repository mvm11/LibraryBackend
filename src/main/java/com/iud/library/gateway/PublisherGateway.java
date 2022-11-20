package com.iud.library.gateway;

import com.iud.library.dto.CopyDTO;
import com.iud.library.dto.PublisherDTO;

import java.util.List;

public interface PublisherGateway {


    List<PublisherDTO> findAllPublishers();
    PublisherDTO findPublisherById(Integer publisherId);

    List<CopyDTO> findCopyByBook(Integer bookId);
    PublisherDTO savePublisher(PublisherDTO publisherDTO);
    PublisherDTO updatePublisher(Integer publisherId, PublisherDTO publisherDTO);
    void deletePublisher(Integer publisherId);
}
