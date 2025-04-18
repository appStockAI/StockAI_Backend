package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "balance")
public class Balance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @OneToOne
    @JoinColumn(name="user_id", unique = true)
    private User user;

    private Long amount;

    public Balance(User user, Long amount) {
        this.user = user;
        this.amount = amount;
    }
}
