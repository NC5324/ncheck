package com.nc.ncheck.controllers;

import com.nc.ncheck.JwtUtils;
import com.nc.ncheck.entities.Profile;
import com.nc.ncheck.payload.requests.AccountDto;
import com.nc.ncheck.payload.response.AuthenticationResponse;
import com.nc.ncheck.repository.ProfileRepository;
import com.nc.ncheck.services.MyUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityController(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, JwtUtils jwtUtils, ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello world!");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AccountDto authenticationRequest) throws Exception  {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch(BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            //throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, authenticationRequest.getUsername()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody AccountDto accountDto) throws Exception {
        if(profileRepository.existsByUsername(accountDto.getUsername())) {
            throw new Exception("There is an account with this username: " + accountDto.getUsername());
        }

        Profile user = new Profile(accountDto.getUsername(), passwordEncoder.encode(accountDto.getPassword()));
        profileRepository.save(user);

        return ResponseEntity.ok("Registered successfully.");
    }
}
