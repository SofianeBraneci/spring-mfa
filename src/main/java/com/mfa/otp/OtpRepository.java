package com.mfa.otp;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpRepository {

    private final Map<String, String> STORE = new ConcurrentHashMap<>();


    public void saveOtp(String id, String otp){

        STORE.put(id, otp);
    }

    public String getOtpById(String id){
        return STORE.get(id);
    }

    public boolean isOtpValid(String id, String value){
        String v = getOtpById(id);
        return Objects.nonNull(v) && v.equals(value);
    }

    public String removeOtpById(String id){
        return STORE.remove(id);
    }
}
