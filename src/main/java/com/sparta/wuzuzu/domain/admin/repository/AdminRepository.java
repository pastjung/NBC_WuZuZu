package com.sparta.wuzuzu.domain.admin.repository;

import com.sparta.wuzuzu.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByEmail(String email);
}
