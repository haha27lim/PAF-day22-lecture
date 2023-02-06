package sg.edu.nus.iss.day22_lecture.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.day22_lecture.model.Room;
import sg.edu.nus.iss.day22_lecture.repository.RoomRepo;

@Service
public class RoomService implements RoomRepo {
    
    //Autowiring the RoomRepo interface
    @Autowired
    RoomRepo roomRepo;

    // method returns the count of rooms in the room table
    public int count() {    
        return roomRepo.count();
    }

    // This method saves a new room in the room table. Boolean value indicating if the save operation was successful or not
    public Boolean save(Room room) {  
        return roomRepo.save(room);
    }

    // This method returns list of all rooms in the room table.
    public List<Room> findAll() {       
        return roomRepo.findAll();
    }

    // This method returns the room with the specified id from the room table.
    public Room findById(Integer id) {        
        return roomRepo.findById(id);
    }

    // This method updates an existing room in the room table.
    public int update(Room room) {        
        return roomRepo.update(room);
    }

    // This method deletes the room with the specified id from the room table.
    public int deleteById(Integer id) {        
        return roomRepo.deleteById(id);
    }
}
