package ru.khalov.testbankrest.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CardNumberEncryptor {

    private static final String ALGORITHM = "AES/GСM/NoPadding";
    private static final int GCM_TAG_LENGTH_BYTES = 128;
    private static final int FOUR_LENGTH_BYTES = 12;

    private final SecretKeySpec secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public CardNumberEncryptor(@Value("${CARD_ENCRYPTION_KEY}") String base64Key){
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String cardNumber){
        try{
            byte[] four = new byte[FOUR_LENGTH_BYTES];
            secureRandom.nextBytes(four);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH_BYTES, four));
            byte[] cipherText = cipher.doFinal(cardNumber.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(four) + ":" + Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e){
            throw new IllegalStateException("Couldn't encrypt the card number, error: " + e.getMessage());
        }
    }

    public String decrypt(String cardNumber){
        try{
            String[] parts = cardNumber.split(":", 2);
            byte[] four = Base64.getDecoder().decode(parts[0]);
            byte[] cipherText = Base64.getDecoder().decode(parts[1]);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH_BYTES, four));
            byte[] plain = cipher.doFinal(cipherText);

            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception e){
            throw new IllegalStateException("Couldn't decrypt the card number, error: " + e.getMessage());
        }
    }
}
