package com.nc.ncheck.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    String name;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    Profile createdBy;

    @ManyToOne
    @JoinColumn(name="room_id", nullable=false)
    Room room;


    public Item() {

    }

    public Item(String name, Profile creator, Room room) {
        this.name = name;
        this.createdBy = creator;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Profile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Profile createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }
}
