package com.backendhotelmanagment.Repository;

import com.backendhotelmanagment.Entity.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IAppUserRepository extends CrudRepository<AppUser, Long> {

    @Query(value = "SELECT u FROM AppUser u WHERE u.email = ?1")
    AppUser findAppUserByEmail(String email);

}
