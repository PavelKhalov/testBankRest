package ru.khalov.testbankrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khalov.testbankrest.entity.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
