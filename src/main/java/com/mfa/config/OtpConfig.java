package com.mfa.config;

import com.mfa.otp.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtpConfig {

    @Bean
    public OtpRepository otpRepository(){
        return new OtpRepository();
    }



}
