package sg.edu.nus.iss.day22_lecture.repository;

import java.util.List;

import sg.edu.nus.iss.day22_lecture.model.Room;

public interface RoomRepo {   
    int count();

    // Create
    Boolean save (Room room);

    // Read all
    List<Room> findAll();

    // Read one record
    Room findById(Integer id);

    // Update
    int update (Room room);

    // Delete
    int deleteById(Integer id);
    
}
