package sg.edu.nus.iss.day22_lecture.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day22_lecture.model.Room;


@Repository
public class RoomRepoImpl implements RoomRepo {

    // Automatically inject an instance of JdbcTemplate, this is a JDBC-based data access object for executing SQL statements
    @Autowired
    JdbcTemplate jdbcTemplate;

    // these are the SQL statements for various database operations
    String countSQL = "select count(*) from room";
    String selectSQL = "select * from room";
    String selectByIdSQL = "select * from room where id = ?";
    String insertSQL = "insert into room (room_type, price) values (?, ?)";
    String updateSQL = "update room set room_type = ? , price = ? where id = ?";
    String deleteSQL = "delete from room where id = ?";


    // this is the implementation of the count method from the RoomRepo interface
    @Override
    public int count() {
        Integer result = 0;
        result = jdbcTemplate.queryForObject(countSQL, Integer.class);
        if (result == null) {
            return 0;
        } else
        return result;
    }

    // this is the implementation of the save method from the RoomRepo interface
    @Override
    public Boolean save(Room room) {
        Boolean saved = false;

        // executing the insertSQL statement using the jdbcTemplate and passing the values for room type and price
        saved = jdbcTemplate.execute(insertSQL, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, room.getRoomType());
                ps.setInt(2, room.getPrice());
                Boolean rslt = ps.execute();
                return rslt;
            }
        });

        return saved;
    }

    // this is the implementation of the findAll method from the RoomRepo interface
    @Override
    public List<Room> findAll() {
        List<Room> rsltList = new ArrayList<Room>();
        // executing the selectSQL statement to get all records from the room table
        rsltList = jdbcTemplate.query(selectSQL, BeanPropertyRowMapper.newInstance(Room.class));

        return rsltList;
    }

    // this is the implementation of the findById method from the RoomRepo interface
    @Override
    public Room findById(Integer id) {
        // "select * from room where id = ?"
        // executing the selectByIdSQL statement to get the record with the given id from the room table
        return jdbcTemplate.queryForObject(selectByIdSQL, BeanPropertyRowMapper.newInstance(Room.class), id);
    }

    // this is the implementation of the update method from the RoomRepo interface
    @Override
    public int update(Room room) {
        int updated = 0;

        // "update room set room_type = ? , price = ? where id = ?"
        // executing the updateSQL statement to update the record with the given id in the room table
        updated = jdbcTemplate.update(updateSQL, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // setting the values to be updated in the PreparedStatement
                ps.setString (1, room.getRoomType());
                ps.setInt(2, room.getPrice());
                ps.setInt(3, room.getId());
            }
        });
        // returning the number of rows updated
        return updated;
    }

    @Override
    public int deleteById(Integer id) {
        int deleted = 0;

        // creating a PreparedStatementSetter to set the values in the deleteSQL statement
        PreparedStatementSetter pss = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // setting the id value to be deleted in the PreparedStatement
                ps.setInt(1, id);               
            }            
        };

        // executing the deleteSQL statement to delete the record with the given id in the room table
        deleted = jdbcTemplate.update(deleteSQL, pss);

        // returning the number of rows deleted
        return deleted;
    }
 
}
