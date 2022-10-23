package com.backendhotelmanagment.Repository;

import com.backendhotelmanagment.Entity.Guest;
import com.backendhotelmanagment.Entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IReservationRepository extends CrudRepository<Reservation, Long> {

    @Query("SELECT g FROM Guest g WHERE g.reservation.idReservation = ?1")
    List<Guest> findGuests(Long id);
}
