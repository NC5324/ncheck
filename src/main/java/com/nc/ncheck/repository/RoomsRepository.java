package com.nc.ncheck.repository;

import com.nc.ncheck.entities.Profile;
import com.nc.ncheck.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomsRepository extends JpaRepository<Room, Long> {

    @Query("select r from Room r where :user member of r.participants")
    List<Room> findAllByUser(Profile user);


    @Modifying
    @Transactional
    @Query(value = "delete from profile_room where room_id = :roomId", nativeQuery = true)
    void deleteConstraints(@Param("roomId") Long roomId);
}
