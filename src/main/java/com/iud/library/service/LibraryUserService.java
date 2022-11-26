package com.iud.library.service;

import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.LibraryUserDTO;
import com.iud.library.entity.LibraryUser;
import com.iud.library.gateway.LibraryUserGateway;
import com.iud.library.repository.LibraryUserRepository;
import com.iud.library.request.libraryuser.UpdatingLibraryUserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryUserService implements LibraryUserGateway {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    LibraryUserRepository libraryUserRepository;



    @Override
    public List<LibraryUserDTO> findAllLibraryUsers() {
        return libraryUserRepository.findAll()
                .stream()
                .map(this::convertLibraryUserToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LibraryUserDTO findLibraryUserById(Integer libraryUserId) {
        return convertLibraryUserToDTO(getLibraryUserById(libraryUserId));

    }

    @Override
    public LibraryUserDTO updateLibraryUser(Integer libraryUserId, UpdatingLibraryUserRequest updatingLibraryUserRequest) {
        LibraryUser foundLibraryUser = getLibraryUserById(libraryUserId);
        foundLibraryUser.setName(updatingLibraryUserRequest.getName());
        foundLibraryUser.setLastName(updatingLibraryUserRequest.getLastName());
        foundLibraryUser.setAddress(updatingLibraryUserRequest.getAddress());
        foundLibraryUser.setDni(updatingLibraryUserRequest.getDni());
        foundLibraryUser.setCellphone(updatingLibraryUserRequest.getCellphone());
        foundLibraryUser.setUsername(updatingLibraryUserRequest.getUsername());
        foundLibraryUser.setEmail(updatingLibraryUserRequest.getEmail());
        foundLibraryUser.setPassword(updatingLibraryUserRequest.getPassword());
        libraryUserRepository.save(foundLibraryUser);
        return convertLibraryUserToDTO(foundLibraryUser);
    }

    @Override
    public void deleteLibraryUser(Integer libraryUserId) {
        LibraryUser libraryUser = getLibraryUserById(libraryUserId);
        libraryUserRepository.delete(libraryUser);
    }

    private LibraryUser getLibraryUserById(Integer libraryUserId) {
        return libraryUserRepository
                .findAll()
                .stream()
                .filter(libraryUser1 -> libraryUser1.getId().equals(libraryUserId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User", "id", libraryUserId));
    }


    private LibraryUserDTO convertLibraryUserToDTO(LibraryUser libraryUser) {
        return modelMapper.map(libraryUser, LibraryUserDTO.class);
    }

    private LibraryUser convertDTOToLibraryUser(LibraryUserDTO libraryUserDTO) {
        return modelMapper.map(libraryUserDTO, LibraryUser.class);
    }


}
