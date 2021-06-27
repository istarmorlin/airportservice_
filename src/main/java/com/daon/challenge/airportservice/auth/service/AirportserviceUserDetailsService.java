package com.daon.challenge.airportservice.auth.service;

import com.daon.challenge.airportservice.auth.principal.UserPrincipal;
import com.daon.challenge.airportservice.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirportserviceUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));

        return new UserPrincipal(user);
    }
}
