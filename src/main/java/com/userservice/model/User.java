package com.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String email;
    private int status;
    @Column(name="token", length = 1000)
    private String token;
}
