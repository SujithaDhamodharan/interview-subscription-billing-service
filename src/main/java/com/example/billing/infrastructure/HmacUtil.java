package com.example.billing.infrastructure;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class HmacUtil {
    public static String calculateHMAC(String data, String secret) {
        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256.init(secretKey);
            byte[] hash = sha256.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating HMAC SHA256 signature", e);
        }
    }

    public static boolean verifySignature(String payload, String secret, String expectedSignature) {
        if (payload == null || secret == null || expectedSignature == null) return false;
        String calculated = calculateHMAC(payload, secret);
        return calculated.equalsIgnoreCase(expectedSignature);
    }
}