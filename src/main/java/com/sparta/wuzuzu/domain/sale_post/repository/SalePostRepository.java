package com.sparta.wuzuzu.domain.sale_post.repository;

import com.sparta.wuzuzu.domain.sale_post.entity.SalePost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalePostRepository extends JpaRepository<SalePost, Long> {

    List<SalePost> findAllByStatusTrue();
}
