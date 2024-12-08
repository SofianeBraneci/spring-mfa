package com.mfa.config;

import com.mfa.otp.OtpAuthenticationProvider;
import com.mfa.otp.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.stream.Stream;

@Configuration
public class AuthManagerConfig {
    @Autowired
    private OtpRepository otpRepository;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        UserDetails u = User.withUsername("sofiane")
                .password("azerty")
                .authorities(Stream.of("WRITE", "READ", "ADMIN").map(SimpleGrantedAuthority::new).toList())
                .build();

        UserDetailsService userDetailsService = new InMemoryUserDetailsManager(u);

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(NoOpPasswordEncoder.getInstance());
        provider.setUserDetailsService(userDetailsService);
        authenticationManagerBuilder.authenticationProvider(new OtpAuthenticationProvider(otpRepository));
        authenticationManagerBuilder.authenticationProvider(provider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository(){
        return new HttpSessionSecurityContextRepository();
    }
}
