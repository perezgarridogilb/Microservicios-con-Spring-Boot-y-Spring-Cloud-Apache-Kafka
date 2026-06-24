package com.example.crudrapido;

import java.util.Base64;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class KeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Jwts.SIG.HS256.key().build();
        String base64Key = Base64.getEncoder()
        .encodeToString(key.getEncoded());

        System.out.println(base64Key);
    }
}
