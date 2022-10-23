package com.backendhotelmanagment.Repository;

import com.backendhotelmanagment.Entity.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface ITicketRepository extends CrudRepository<Ticket, Long> {
}
