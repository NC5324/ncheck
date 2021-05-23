package com.nc.ncheck.payload.requests;

import java.util.List;
import java.util.Set;

public class RoomRequest {
    String name;
    Long owner;
    Set<Long> participants;
    Set<Long> items;

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

    public Set<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Long> participants) {
        this.participants = participants;
    }

    public Set<Long> getItems() {
        return items;
    }

    public void setItems(Set<Long> items) {
        this.items = items;
    }
}
