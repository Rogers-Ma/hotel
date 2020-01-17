package com.csr.hotelserver.util.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.csr.hotelserver.entity.Admin;
import com.csr.hotelserver.entity.Customer;

public class JwtUtil {
    public static String getToken(Customer customer) {
        String token = "";
        token = JWT.create().withAudience(customer.getName())
                .sign(Algorithm.HMAC256(customer.getPassword()));
        return token;
    }

    public static String getToken(Admin admin) {
        String token = "";
        token = JWT.create().withAudience(admin.getName())
                .sign(Algorithm.HMAC256(admin.getPassword()));
        return token;
    }
}
