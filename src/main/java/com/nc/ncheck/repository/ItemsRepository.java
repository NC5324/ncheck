package com.nc.ncheck.repository;

import com.nc.ncheck.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Item, Long> {

    @Query("select item from Item item where item.room.id = :roomId")
    List<Item> findByRoomId(Long roomId);
}
