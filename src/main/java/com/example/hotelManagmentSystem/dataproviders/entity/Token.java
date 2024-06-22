package com.example.hotelManagmentSystem.dataproviders.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "token", length = Integer.MAX_VALUE)
    private String token;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "revoked")
    private Boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}