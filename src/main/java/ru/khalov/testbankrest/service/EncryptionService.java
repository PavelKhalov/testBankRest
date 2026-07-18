package ru.khalov.testbankrest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;



@Service
public class EncryptionService {

    private final TextEncryptor textEncryptor;

    @Autowired
    public EncryptionService(@Value("${app.encrypting.password}") String password,
                             @Value("${app.encrypting.salt}") String salt){
        this.textEncryptor = Encryptors.text(password, salt);
    }

    public String encrypt(String number){
        return textEncryptor.encrypt(number);
    }

    public String decrypt(String number){
        return textEncryptor.decrypt(number);
    }
}
