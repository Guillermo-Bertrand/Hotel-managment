package com.backendhotelmanagment.Auth;

import com.backendhotelmanagment.Entity.AppUser;
import com.backendhotelmanagment.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AdditionalTokenInformation implements TokenEnhancer {

    private final AppUserService appUserService;

    @Autowired
    public AdditionalTokenInformation(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

        AppUser appUser = appUserService.findAppUserByEmail(oAuth2Authentication.getName());
        Map<String, Object> information = new HashMap<>();

        information.put("name", appUser.getName());
        information.put("last_name", appUser.getLastName());
        information.put("email", appUser.getEmail());

        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setAdditionalInformation(information);

        return oAuth2AccessToken;
    }
}
