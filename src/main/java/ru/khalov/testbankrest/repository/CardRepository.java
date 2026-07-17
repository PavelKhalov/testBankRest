package ru.khalov.testbankrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khalov.testbankrest.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
