package com.backendhotelmanagment.Service;

import com.backendhotelmanagment.Entity.Guest;
import com.backendhotelmanagment.Repository.IGuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuestService {

    private final IGuestRepository guestRepository;

    @Autowired
    public GuestService(IGuestRepository guestRepository){
        this.guestRepository = guestRepository;
    }

    @Transactional(readOnly = true)
    public List<Guest> findAll(){
        return (List<Guest>)guestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Guest findById(Long id){
        return guestRepository.findById(id).orElse(null);
    }

    @Transactional
    public Guest save(Guest guest){
        return guestRepository.save(guest);
    }

    @Transactional
    public void delete(Long id){
        guestRepository.deleteById(id);
    }
}
