package com.nc.ncheck.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    String name;
    Long owner;

    @ManyToMany
    @JoinTable(
            name = "profile_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    Set<Profile> participants;

    @OneToMany(mappedBy = "room")
    Set<Item> items;

    public Room() {

    }

    public Room(String name, Long ownerId)
    {
        this.name = name;
        this.owner = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Set<Profile> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Profile> participants) {
        this.participants = participants;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
