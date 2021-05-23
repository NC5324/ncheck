package com.nc.ncheck.services;

import com.nc.ncheck.entities.Profile;
import com.nc.ncheck.payload.requests.AccountDto;
import com.nc.ncheck.repository.ProfileRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    final ProfileRepository profileRepository;

    public MyUserDetailsService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Profile profile = profileRepository.findByUsername(username);
        return new User(profile.getUsername(), profile.getPassword(), new ArrayList<>());
    }
}
