package com.example.open_mission.user.repository;

import com.example.open_mission.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User,Long> {
    boolean existsByUserName(String userName);
}
