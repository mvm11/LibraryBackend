package com.iud.library.gateway;

import com.iud.library.dto.LibraryUserDTO;
import com.iud.library.request.libraryuser.UpdatingLibraryUserRequest;

import java.util.List;

public interface LibraryUserGateway {


    List<LibraryUserDTO> findAllLibraryUsers();
    LibraryUserDTO findLibraryUserById(Integer libraryUserId);
    LibraryUserDTO updateLibraryUser(Integer libraryUserId, UpdatingLibraryUserRequest updatingLibraryUserRequest);
    void deleteLibraryUser(Integer libraryUserId);

}
