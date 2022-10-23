package com.backendhotelmanagment.Service;

import com.backendhotelmanagment.Entity.Cancellation;
import com.backendhotelmanagment.Repository.ICancellationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CancellationService {

    private final ICancellationRepository cancellationRepository;

    @Autowired
    public CancellationService(ICancellationRepository cancellationRepository){
        this.cancellationRepository = cancellationRepository;
    }

    @Transactional(readOnly = true)
    public List<Cancellation> findAll(){
        return (List<Cancellation>)cancellationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cancellation findById(Long id){
        return cancellationRepository.findById(id).orElse(null);
    }

    @Transactional
    public Cancellation save(Cancellation cancellation){
        return cancellationRepository.save(cancellation);
    }

    @Transactional
    public void delete(Long id){
        cancellationRepository.deleteById(id);
    }
}
