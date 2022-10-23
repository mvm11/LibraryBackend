package com.iud.library.gateway;

import com.iud.library.dto.CopyDTO;

import java.util.List;

public interface CopyGateway {

    CopyDTO saveCopy(Integer bookId, CopyDTO copyDTO);
    List<CopyDTO> findCopyByBook(Integer bookId);
    CopyDTO findCopyById(Integer bookId, Integer copyId);
    CopyDTO updateCopy(Integer bookId, Integer copyId, CopyDTO copyRequest);

    void deleteCopy(Integer bookId, Integer copyId);
}
