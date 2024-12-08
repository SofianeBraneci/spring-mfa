package com.mfa.otp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;




public class OtpFilter extends OncePerRequestFilter {


    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;

    Logger log = LoggerFactory.getLogger(getClass().getName());

    public OtpFilter(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // check the url
        // if the url matches then, authenticate,
        // and create the session if success else redirect to log in
        if("/auth/otp-validation".equals(request.getRequestURI()) && "POST".equals(request.getMethod())){
            String username = request.getParameter("otpId");
            String otp = request.getParameter("token");
            Authentication otpAuthentication = new OtpAuthentication(username, otp);
            try{
                otpAuthentication = authenticationManager.authenticate(otpAuthentication);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(otpAuthentication);
                SecurityContextHolder.setContext(context);
                securityContextRepository.saveContext(context, request, response);
                response.sendRedirect(request.getContextPath() + "/welcome");

            }catch (AuthenticationException e){
                log.error("Error OTP", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid OTP");
                return;
            }

        }
        filterChain.doFilter(request, response);


    }
}
