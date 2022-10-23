package com.backendhotelmanagment.Service;

import com.backendhotelmanagment.Entity.AppUser;
import com.backendhotelmanagment.Repository.IAppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(AppUserService.class);

    private IAppUserRepository appUserRepository;

    @Autowired
    public AppUserService(IAppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = appUserRepository.findAppUserByEmail(username);

        if(logger == null){
            logger.error("Error in login, user: " + username + " doesn't exist in database!");
            throw new UsernameNotFoundException("Error in login, user: " + username + " doesn't exist in database!");
        }

        //Take roles list with a map stream to transform every role - string into a role simpleGrantedAuthority.
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                //Peek gets every simpleGrantedAuthority created and use the logger to show role in console.
                .peek(authority -> logger.info("Role: " + authority.getAuthority()))
                .collect(Collectors.toList());

        return new User(user.getEmail(), user.getPassword(), user.getEnabled(), true, true, true, authorities);
    }

    @Transactional(readOnly = true)
    public List<AppUser> findAll(){
        return (List<AppUser>)appUserRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AppUser findById(Long id){
        return appUserRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public AppUser findAppUserByEmail(String email){
        return appUserRepository.findAppUserByEmail(email);
    }

    @Transactional
    public AppUser save(AppUser appUser){
        return appUserRepository.save(appUser);
    }

    @Transactional
    public void delete(Long id){
        appUserRepository.deleteById(id);
    }
}
