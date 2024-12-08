package com.mfa.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


public class OtpAuthenticationProvider implements AuthenticationProvider {
    // otp repository

    private OtpRepository otpRepository;

    public OtpAuthenticationProvider(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // check the token that is associated with the user
        // if it's valid then set to true else throw an error
        OtpAuthentication otpAuthentication = (OtpAuthentication) authentication;
        String token = (String) otpAuthentication.getCredentials();
        String userId = (String) otpAuthentication.getPrincipal();
        if(otpRepository.isOtpValid(userId, token)){
            otpAuthentication.setAuthenticated(true);
            // remove the token
            otpRepository.removeOtpById(userId);
            return otpAuthentication;
        }
        throw new BadCredentialsException("Otp token is invalid");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
