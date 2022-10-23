package com.backendhotelmanagment.Repository;

import com.backendhotelmanagment.Entity.Room;
import com.backendhotelmanagment.Entity.BedType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRoomRepository extends CrudRepository<Room, Long> {

    @Query(value = "SELECT r FROM Room r GROUP BY r.roomType")
    List<Room> findAllRoomTypes();
}
