package com.backendhotelmanagment.Service;

import com.backendhotelmanagment.Entity.Ticket;
import com.backendhotelmanagment.Repository.ITicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    private final ITicketRepository ticketRepository;

    @Autowired
    public TicketService(ITicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket save(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Ticket findById(Long id){
        return ticketRepository.findById(id).orElse(null);
    }
}
