package com.sparta.wuzuzu.domain.category.repository;

import com.sparta.wuzuzu.domain.category.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
