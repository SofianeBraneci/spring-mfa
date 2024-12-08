package com.mfa.controllers;

import com.mfa.models.LoginRequest;
import com.mfa.models.OtpValidationRequest;
import com.mfa.otp.OtpRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private OtpRepository otpRepository;

    Logger log = LoggerFactory.getLogger(getClass().getName());

    @PostMapping("/login")
    public void login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
          try {
              log.info("Login started");
              Authentication token = UsernamePasswordAuthenticationToken.unauthenticated(
                      loginRequest.username(), loginRequest.password());
              token = authenticationManager.authenticate(token);
              if(token.isAuthenticated()){
                  // we generate a token
                  String value = UUID.randomUUID().toString();
                  log.info("GENERATED OTP IS {}", value);
                  otpRepository.saveOtp(token.getName(), value);
                  // we redirect the user to the otp validation page
                  String redirect = request.getContextPath() + "/auth/otp?id=" + URLEncoder.encode(token.getName(), StandardCharsets.UTF_8);
                  response.sendRedirect(redirect);
              }

          }catch (AuthenticationException e){
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          } catch (IOException e) {
              throw new RuntimeException(e);
          }

    }

    @GetMapping("/login")
    public String getLogin( Model model){

        return "login";
    }

    @GetMapping("/otp")
    public String otp(@RequestParam("id") String id,  Model model){
        model.addAttribute("otpId", id);
        return "otp-login";
    }





}
