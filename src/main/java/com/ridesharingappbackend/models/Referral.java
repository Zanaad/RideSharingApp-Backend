package com.ridesharingappbackend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "referrals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "referrer_id")
    private User referrer;

    @ManyToOne
    @JoinColumn(name = "referred_id")
    private User referred;

    private String referralCode;
}
