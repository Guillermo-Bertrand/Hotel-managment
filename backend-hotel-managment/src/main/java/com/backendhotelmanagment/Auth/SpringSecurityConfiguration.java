package com.backendhotelmanagment.Auth;

import com.backendhotelmanagment.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;

    @Autowired
    public SpringSecurityConfiguration(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @Bean("createPasswordEncoder")//Use bean to register the returned object from this method into spring container, this way we can approach it in advance.
    public static BCryptPasswordEncoder createPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean("authenticationManager")//For the same porpoise as the method above.
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.appUserService).passwordEncoder(createPasswordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //antMatchers works to routes for all users, even if they haven't log-in yet. In this application,
        //everything will work in a private way, so you need to be authenticated to see something.
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
