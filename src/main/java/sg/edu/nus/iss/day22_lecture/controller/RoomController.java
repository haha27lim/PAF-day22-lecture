package sg.edu.nus.iss.day22_lecture.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.day22_lecture.model.Room;
import sg.edu.nus.iss.day22_lecture.service.RoomService;


@RequestMapping("/api/rooms")
@RestController
public class RoomController {
    
    // Autowire RoomService to have access to its methods
    @Autowired
    RoomService roomService;

    // Map requests to "/api/rooms/count" to getRoomCount method
    @GetMapping("/count")
    // Return the count of rooms
    public Integer getRoomCount() {

        // Get the count of rooms by calling roomService.count()
        Integer roomCount = roomService.count();
        return roomCount;
    }

    // Map requests to "/api/rooms" to retreiveAllRooms method
    @GetMapping("/")
    // Return a ResponseEntity containing all rooms or NO_CONTENT status code
    public ResponseEntity<List<Room>> retreiveAllRooms() {

        // Initialize rooms list
        List<Room> rooms = new ArrayList<Room>();
        // Get all rooms by calling roomService.findAll()
        rooms = roomService.findAll();

        // If there are no rooms, return NO_CONTENT status code
        if (rooms.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } // Return the list of rooms and OK status code
        return new ResponseEntity<>(rooms, HttpStatus.OK);        
    }

    // Map requests to "/api/rooms/{id}" to retreieveRoomById method
    @GetMapping("/{id}")
    // Return a ResponseEntity containing room with the specified id or NOT_FOUND status code
    public ResponseEntity<Room> retreieveRoomById (@PathVariable("id") int id) {
        // Get room by calling roomService.findById()
        Room rooms = roomService.findById(id);

        // If room exists, return it and OK status code
        if (rooms != null) {
            return new ResponseEntity<>(rooms, HttpStatus.OK);
          } else { 
            // If room does not exist, return NOT_FOUND status code
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
    }

    // Map requests to "/api/rooms" to createRoom method
    @PostMapping("/")
    // Create room and return a ResponseEntity containing a Boolean indicating success or failure
    // and CREATED or INTERNAL_SERVER_ERROR status code
    public ResponseEntity<Boolean> createRoom(@RequestBody Room room) {
        // Store the room in a local variable
        Room rm = room;
        // Call roomService.save() to create the room
        Boolean result = roomService.save(rm);

        // If the room creation was successful, return a ResponseEntity containing true and CREATED status code
        if (result) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
        // If the room creation was successful, return a ResponseEntity containing true and CREATED status code
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/")
    public ResponseEntity<Integer> updateRoom (@RequestBody Room room) {
        // Create a Room object and assign the values from the request body room
        Room rm = room;
        // Call the update method from the RoomService and assign the result to the updated variable
        int updated = roomService.update(rm);

        // Check if the update method returned 1 which update successfully, return a ResponseEntity with a 1 and HTTP status OK.
        if (updated == 1) {
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            // Return a ResponseEntity with null and HTTP status INTERNAL_SERVER_ERROR if the update was not successful
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }         
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteRoom (@PathVariable("id") Integer id) {
        // Initialize the deleteResult variable
        int deleteResult = 0;

        // Call the deleteById method from the RoomService and assign the result to deleteResult
        deleteResult = roomService.deleteById(id);

        // Check if the deleteResult is equal to 0 which delete was not successful, return a ResponseEntity with 0 and HTTP status NOT_FOUND.
        if (deleteResult == 0) {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        } else {
            // Return a ResponseEntity with 1 and HTTP status OK if the delete was successful
            return new ResponseEntity<>(1, HttpStatus.OK);
        }
    }
}
