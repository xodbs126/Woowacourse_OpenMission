package com.example.open_mission.user.domain;

import com.example.open_mission.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Builder
    public User(String userName) {
        this.userName = userName;
    }

    protected User() {
    }


    public static User createUser(String userName) {
        return User.builder()
                .userName(userName)
                .build();
    }

    public User updateName(String nickname) {
        return User.builder()
                .userName(nickname)
                .build();
    }
}
