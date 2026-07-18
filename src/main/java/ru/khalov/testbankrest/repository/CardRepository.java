package ru.khalov.testbankrest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khalov.testbankrest.entity.Card;
import ru.khalov.testbankrest.entity.User;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Page<Card> findByOwner(User owner, Pageable pageable);

}
