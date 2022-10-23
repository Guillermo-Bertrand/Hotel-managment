package com.backendhotelmanagment.Service;

import com.backendhotelmanagment.Entity.Guest;
import com.backendhotelmanagment.Entity.Reservation;
import com.backendhotelmanagment.Repository.IReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final IReservationRepository reservationRepository;

    @Autowired
    public ReservationService(IReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll(){
        return (List<Reservation>)reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Guest> findGuest(Long idReservation){
        return reservationRepository.findGuests(idReservation);
    }

    @Transactional(readOnly = true)
    public Reservation findById(Long id){
        return reservationRepository.findById(id).orElse(null);
    }

    @Transactional
    public Reservation save(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void delete(Long id){
        reservationRepository.deleteById(id);
    }
}
