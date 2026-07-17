package ru.khalov.testbankrest.repository;

import io.swagger.v3.oas.annotations.Parameter;
import org.hibernate.annotations.ColumnTransformers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.khalov.testbankrest.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "delete from users u where u.username= :username", nativeQuery = true)
    boolean deleteByUsername(@Param("username") String username);
}
