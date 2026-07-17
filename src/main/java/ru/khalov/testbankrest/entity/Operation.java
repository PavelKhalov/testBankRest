package ru.khalov.testbankrest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "operation")
@Getter
@Setter
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_card", referencedColumnName = "id")
    private Card fromCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_card", referencedColumnName = "id")
    private Card toCard;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "when", nullable = false, updatable = false)
    private Instant when;

    public Operation(Card fromCard, Card toCard, BigDecimal amount){
        this.fromCard = fromCard;
        this.toCard = toCard;
        this.amount = amount;
    }

    @PrePersist
    public void prePersist(){
        this.when = Instant.now();
    }

}
