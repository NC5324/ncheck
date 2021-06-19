package com.nc.ncheck.controllers;

import com.nc.ncheck.JwtUtils;
import com.nc.ncheck.helpers.FileUploadUtil;
import com.nc.ncheck.payload.requests.FormDataRequest;
import com.nc.ncheck.payload.response.AuthenticationResponse;
import com.nc.ncheck.repository.ItemsRepository;
import com.nc.ncheck.repository.ProfileRepository;
import com.nc.ncheck.repository.RoomsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SettingsController {

    final ProfileRepository profileRepository;
    final AuthenticationManager authenticationManager;
    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;
    final RoomsRepository roomsRepository;
    final ItemsRepository itemsRepository;

    public SettingsController(ProfileRepository profileRepository,
                              AuthenticationManager authenticationManager,
                              PasswordEncoder passwordEncoder,
                              JwtUtils jwtUtils,
                              RoomsRepository roomsRepository, ItemsRepository itemsRepository) {
        this.profileRepository = profileRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.roomsRepository = roomsRepository;
        this.itemsRepository = itemsRepository;
    }

    /**
     * Request should consist of: { id, username, newUsername, password, file }
     * This method attempts to authenticate with password and old username
     * and if successful updates the user's details.
     *
     * If the user details are incorrect it returns unauthorized error status.
     *
     * @return Updated state of the user
     */
    //TODO: Replace map request object with a dedicated POJO
    @PostMapping("/user")
    public ResponseEntity<?> editUser(@ModelAttribute FormDataRequest request) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var user = profileRepository.findByUsername(request.getUsername());

        try {
            // Construct filename for profile picture
            String fileName = "AVT-" + request.getUsername().toUpperCase() +
                    // Append the extension to the filename
                    "." + request.getFile().getOriginalFilename().substring(request.getFile().getOriginalFilename().lastIndexOf(".") + 1);

            // Save the file to storage and change path in user's details
            FileUploadUtil.saveFile("src/main/resources", fileName, request.getFile());
            user.setProfilePicPath(fileName);
        } catch (Exception e) {
            System.out.println("Profile picture not changed");
        }

        user.setUsername(request.getUsername());

        System.out.println(user.getUsername() + " " + user.getProfilePicPath());
        profileRepository.save(user);

        final String jwt = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, user.getUsername(), user.getId(), user.getProfilePicPath()));
    }

    /**
     * Request should consist of: { id, username, password, newPassword }
     * This method attempts to authenticate with password and username
     * and if successful changes the password to the new password.
     *
     * If the user details are incorrect it returns unauthorized error status.
     *
     * @return Updated state of the user
     */
    @PostMapping("/password")
    public ResponseEntity<?> editPassword(@RequestBody Map<String, Object> request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password")));
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        var user = profileRepository.findByUsername((String) request.get("username"));
        user.setPassword(passwordEncoder.encode((String) request.get("newPassword")));
        profileRepository.save(user);

        final String jwt = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, user.getUsername(), user.getId()));
    }

    /**
     * Request should consist of: { id, username, password, RoomRequest object }
     * This method attempts to authenticate with password and username
     * and if successful replaces specified room's details with new details
     *
     * If the user details are incorrect it returns unauthorized error status.
     *
     * @return Updated state of the user
     */
    @PostMapping("/room")
    public ResponseEntity<?> editRoom(@RequestBody Map<String, Object> request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password")));
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        var roomReq = (Map<String, Object>) request.get("room");
        var roomEntry = roomsRepository.findById((Long) Long.parseLong(roomReq.get("id")+""));
        if(roomEntry.isEmpty())
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        var room = roomEntry.get();
        room.setName((String) roomReq.get("name"));
        roomsRepository.save(room);

        return ResponseEntity.ok(room);
    }
}
