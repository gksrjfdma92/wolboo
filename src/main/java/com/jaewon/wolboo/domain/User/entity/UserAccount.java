package com.jaewon.wolboo.domain.User.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long Id;

    private String userName;

    private String emailAddress;

    private String phoneNumber;

    private String password;

    private String userRole;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean isDeleted = false;

    public UserAccount(String userName, String emailAddress, String phoneNumber, String password, String userRole) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userRole = userRole;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
