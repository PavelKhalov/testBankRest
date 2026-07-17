package ru.khalov.testbankrest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.khalov.testbankrest.entity.RefreshToken;
import ru.khalov.testbankrest.entity.User;
import ru.khalov.testbankrest.exception.RefreshTokenException;
import ru.khalov.testbankrest.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public RefreshToken create(User user){
        refreshTokenRepository.deleteByUser(user);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(Instant.now().plusMillis(refreshExpiration));
        return refreshTokenRepository.save(token);
    }

    public RefreshToken verify(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new RefreshTokenException("Refresh token not found"));

        if(refreshToken.isExpired() || refreshToken.isRevoked()){
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Refresh token expired. Please login again.");
        }

        return refreshToken;
    }

    public void revoke(String token){
        refreshTokenRepository.findByToken(token).ifPresent(t ->{
            t.setRevoked(true);
            refreshTokenRepository.save(t);
        });
    }

}
