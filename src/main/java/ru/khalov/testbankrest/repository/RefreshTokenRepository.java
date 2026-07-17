package ru.khalov.testbankrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khalov.testbankrest.entity.RefreshToken;
import ru.khalov.testbankrest.entity.User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findById(Long id);
    
    void deleteById(Long id);

    void deleteByUser(User user);

    Optional<RefreshToken> findByToken(String token);
}
