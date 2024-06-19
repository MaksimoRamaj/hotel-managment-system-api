package com.example.hotelManagmentSystem.dataproviders.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "client_log")
public class ClientLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private User client;

    @Column(name = "total_reservations")
    private Integer totalReservations;

    @Column(name = "score")
    private Double score;

}