package com.backendhotelmanagment.Repository;

import com.backendhotelmanagment.Entity.Guest;
import org.springframework.data.repository.CrudRepository;

public interface IGuestRepository extends CrudRepository<Guest, Long> {
}
