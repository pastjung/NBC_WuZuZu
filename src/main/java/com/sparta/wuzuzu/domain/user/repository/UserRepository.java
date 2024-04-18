package com.sparta.wuzuzu.domain.user.repository;

import com.sparta.wuzuzu.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
