package com.backendhotelmanagment.Repository;

import com.backendhotelmanagment.Entity.Cancellation;
import com.backendhotelmanagment.Entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ICancellationRepository extends CrudRepository<Cancellation, Long> { }
