package com.nc.ncheck.controllers;

import com.nc.ncheck.entities.Item;
import com.nc.ncheck.entities.Profile;
import com.nc.ncheck.entities.Room;
import com.nc.ncheck.payload.requests.ItemDeleteRequest;
import com.nc.ncheck.payload.requests.RoomRequest;
import com.nc.ncheck.repository.ItemsRepository;
import com.nc.ncheck.repository.ProfileRepository;
import com.nc.ncheck.repository.RoomsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/rooms")
public class RoomsController {

    final RoomsRepository roomsRepository;
    final ProfileRepository profileRepository;
    final ItemsRepository itemsRepository;

    public RoomsController(RoomsRepository roomsRepository,
                           ProfileRepository profileRepository,
                           ItemsRepository itemsRepository) {
        this.profileRepository = profileRepository;
        this.roomsRepository = roomsRepository;
        this.itemsRepository = itemsRepository;
    }


    @GetMapping("/user")
    public ResponseEntity<?> getAllRoomsByUser(@RequestParam Long userId) {
        var user = profileRepository.findById(userId);
        if(user.isEmpty())
            return ResponseEntity.ok("No entry found matching the given data.");

        var response = roomsRepository.findAllByUser(user.get());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/new")
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest roomRequest) {
        var newRoom = new Room(roomRequest.getName(), roomRequest.getOwner());

        Set<Profile> participants = new HashSet<>();
        var creator = profileRepository.findById(roomRequest.getOwner());
        creator.ifPresent(participants::add);

        var participantIds = roomRequest.getParticipants();
        if(participantIds != null) {
            for(var participantId : participantIds) {
                var participant = profileRepository.findById(participantId);
                participant.ifPresent(participants::add);
            }
        }
        newRoom.setParticipants(participants);

        Set<Item> items = new HashSet<>();

        var itemIds = roomRequest.getItems();
        if(itemIds != null) {
            for(var itemId : itemIds) {
                var item = itemsRepository.findById(itemId);
                item.ifPresent(items::add);
            }
        }
        newRoom.setItems(items);

        roomsRepository.save(newRoom);

        return ResponseEntity.ok(newRoom);
    }

    @PostMapping("/{roomId}/users")
    public ResponseEntity<?> joinRoom(@PathVariable Long roomId,
                                      @RequestBody Map<String, Object> request) {
        var room = roomsRepository.findById(roomId);
        var user = profileRepository.findById(Long.parseLong(request.get("userId")+""));

        if(room.isEmpty() || user.isEmpty()) {
            return ResponseEntity.ok("No entry found matching the given data.");
        }

        var participants = room.get().getParticipants();
        participants.add(user.get());

        room.get().setParticipants(participants);
        roomsRepository.save(room.get());

        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{roomId}/users")
    public ResponseEntity<?> leaveRoom(@PathVariable Long roomId,
                                       @RequestBody Map<String, Object> request) {
        var room = roomsRepository.findById(roomId);
        var user = profileRepository.findById(Long.parseLong(request.get("userId")+""));

        if(room.isEmpty() || user.isEmpty()) {
            return ResponseEntity.ok("No entry found matching the given data.");
        }

        var participants = room.get().getParticipants();
        participants.remove(user.get());

        room.get().setParticipants(participants);
        roomsRepository.save(room.get());

        return ResponseEntity.ok(room);
    }

    @PostMapping("/{roomId}/items")
    public ResponseEntity<?> addItem(@PathVariable Long roomId,
                                     @RequestBody Map<String, Object> request) {
        var targetRoom = roomsRepository.findById(roomId);
        if(targetRoom.isEmpty())
            return ResponseEntity.ok("No room found with the given ID.");

        var roomItems = targetRoom.get().getItems();
        var item = new Item((String)request.get("name"), targetRoom.get());

        itemsRepository.save(item);
        roomItems.add(item);
        targetRoom.get().setItems(roomItems);
        roomsRepository.save(targetRoom.get());

        Map<String, Object> response = new HashMap<>();
        response.put("data", item);
        response.put("message", "Successfully added new list item.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{roomId}/items/delete")
    public ResponseEntity<?> deleteItems(@PathVariable Long roomId, @RequestBody ItemDeleteRequest request) {
        var targetRoom = roomsRepository.findById(roomId);
        if(targetRoom.isEmpty())
            return ResponseEntity.ok("No room found with the given ID.");

        var creator = profileRepository.findById(Long.parseLong(request.getUserId()+""));
        if(creator.isEmpty())
            return ResponseEntity.ok("No profile found with the given ID.");

        var items = targetRoom.get().getItems();
        var itemIds = request.getDeletedItems();
        if(itemIds != null) {
            for(var itemId : itemIds) {
                var item = itemsRepository.findById(itemId);
                item.ifPresent(items::remove);
            }
        }
        targetRoom.get().setItems(items);
        roomsRepository.save(targetRoom.get());

        Map<String, Object> response = new HashMap<>();
        response.put("data", items);
        response.put("message", "Successfully added new list item.");
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRoomById(@RequestParam Long id) {
        var deletedRoom = roomsRepository.findById(id);
        if(deletedRoom.isEmpty())
            return ResponseEntity.ok("No entry found matching the given data.");

        roomsRepository.deleteConstraints(deletedRoom.get().getId());
        roomsRepository.delete(deletedRoom.get());

        Map<String, Object> response = new HashMap<>();
        response.put("data", deletedRoom);
        response.put("message", String.format("Deleted room with ID: %d", id));
        return ResponseEntity.ok(response);
    }
}
