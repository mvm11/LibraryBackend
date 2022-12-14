package com.iud.library.security;

import com.iud.library.entity.LibraryUser;
import com.iud.library.entity.Role;
import com.iud.library.repository.LibraryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private LibraryUserRepository libraryUserRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        LibraryUser libraryUser = libraryUserRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with that username or email: " + usernameOrEmail));

        Set<Role> roles = Set.of(libraryUser.getRole());

        return new org.springframework.security.core.userdetails.User(libraryUser.getEmail(), libraryUser.getPassword(), mapRoles(roles));
    }

    private Collection<? extends GrantedAuthority> mapRoles(Set<Role> roles){
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getName())).collect(Collectors.toList());
    }
}
