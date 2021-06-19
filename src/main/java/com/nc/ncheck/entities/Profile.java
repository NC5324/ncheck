package com.nc.ncheck.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    String username;

    @JsonIgnore
    String password;

    @ManyToMany(mappedBy = "participants")
    Set<Room> rooms;

    @Column(name = "profile_pic")
    String profilePicPath;


    public Profile() {

    }

    public Profile(String username, String password) {
        this.username = username;
        this.password = password;
        this.rooms = new HashSet<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }
}
