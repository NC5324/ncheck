package com.nc.ncheck.controllers;

import com.nc.ncheck.repository.ItemsRepository;
import com.nc.ncheck.repository.RoomsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000/rooms/*")
@RequestMapping("/api/items")
public class ItemController {

    final ItemsRepository itemsRepository;
    final RoomsRepository roomsRepository;

    public ItemController(ItemsRepository itemsRepository, RoomsRepository roomsRepository) {
        this.itemsRepository = itemsRepository;
        this.roomsRepository = roomsRepository;
    }

    @GetMapping("/room")
    public ResponseEntity<?> getItemsByRoomId(@RequestParam Long roomId) {
        var room = roomsRepository.findById(roomId);

        if(room.isEmpty()) {
            return ResponseEntity.ok("No entries found.");
        }

        var response = itemsRepository.findByRoomId(Long.parseLong(roomId+""));
        return ResponseEntity.ok(response);
    }
}
