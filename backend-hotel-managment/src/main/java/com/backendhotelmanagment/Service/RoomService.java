package com.backendhotelmanagment.Service;

import com.backendhotelmanagment.Entity.Room;
import com.backendhotelmanagment.Entity.BedType;
import com.backendhotelmanagment.Repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomService {

    private final IRoomRepository roomRepository;

    @Autowired
    public RoomService(IRoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<Room> findAll(){
        return (List<Room>)roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Room> findAllRoomTypes(){
        return roomRepository.findAllRoomTypes();
    }

    @Transactional(readOnly = true)
    public Room findById(Long id){
        return roomRepository.findById(id).orElse(null);
    }

    @Transactional
    public Room save(Room room){
        return roomRepository.save(room);
    }

    @Transactional
    public void delete(Long id){
        roomRepository.deleteById(id);
    }
}
