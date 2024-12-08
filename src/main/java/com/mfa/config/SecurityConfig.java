package com.mfa.config;

import com.mfa.otp.OtpAuthenticationProvider;
import com.mfa.otp.OtpFilter;
import com.mfa.otp.OtpRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Stream;

@Configuration
public class SecurityConfig {

    @Autowired
    private OtpRepository otpRepository;
    Logger log = LoggerFactory.getLogger(getClass().getName());



    // example only, use a strong encoder for real scenarios
    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) throws Exception {

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(r ->
                        r.requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/welcome").authenticated()
                ).formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling( e -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/auth/login")).accessDeniedHandler(new AccessDeniedHandlerImpl()))
                .addFilterAfter(new OtpFilter(authenticationManager, securityContextRepository), UsernamePasswordAuthenticationFilter.class)
                .build();
    }



}
