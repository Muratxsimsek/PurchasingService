package com.emlakjet.purchasing.security;

import com.emlakjet.purchasing.persistence.entity.UserEntity;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Service
public class TokenService {

    private final KeyPair keyPair;

    public TokenService() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        this.keyPair = keyGen.generateKeyPair();
    }

    public String generateToken(String firstName, String lastName, String email) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTimeMillis = currentTimeMillis + 1000 * 60 * 60 * 10; // 10 hours validity

        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"RS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"sub\":\"" + email + "\",\"iat\":" + currentTimeMillis + ",\"exp\":" + expirationTimeMillis + "}").getBytes(StandardCharsets.UTF_8));
        String unsignedToken = header + "." + payload;

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(unsignedToken.getBytes(StandardCharsets.UTF_8));
        String signatureBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(signature.sign());

        return unsignedToken + "." + signatureBase64;
    }

    public boolean validateToken(String token, String email) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        String header = parts[0];
        String payload = parts[1];
        String signature = parts[2];

        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(keyPair.getPublic());
        String unsignedToken = header + "." + payload;
        publicSignature.update(unsignedToken.getBytes(StandardCharsets.UTF_8));

        byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);
        if (!publicSignature.verify(signatureBytes)) {
            return false;
        }

        String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        long expirationTime = Long.parseLong(payloadJson.replaceAll(".*\"exp\":(\\d+).*", "$1"));
        if (System.currentTimeMillis() > expirationTime) {
            return false;
        }

        String tokenEmail = payloadJson.replaceAll(".*\"sub\":\"([^\"]+)\".*", "$1");
        return tokenEmail.equals(email);
    }

    public String extractEmail(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token");
        }

        String payload = parts[1];
        String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        return payloadJson.replaceAll(".*\"sub\":\"([^\"]+)\".*", "$1");
    }

    public String extractFirstName(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token");
        }

        String payload = parts[1];
        String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        return payloadJson.replaceAll(".*\"firstName\":\"([^\"]+)\".*", "$1");
    }

    public String extractLastName(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token");
        }

        String payload = parts[1];
        String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        return payloadJson.replaceAll(".*\"lastName\":\"([^\"]+)\".*", "$1");
    }

    public UserEntity getUserEntity(String token) {
        String email = extractEmail(token);
        String firstName = extractFirstName(token);
        String lastName = extractLastName(token);
        return UserEntity.builder().firstName(firstName).lastName(lastName).email(email).build();
    }
}
