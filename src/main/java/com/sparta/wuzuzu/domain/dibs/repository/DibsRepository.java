package com.sparta.wuzuzu.domain.dibs.repository;

import com.sparta.wuzuzu.domain.dibs.entity.Dibs;
import com.sparta.wuzuzu.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DibsRepository extends JpaRepository<Dibs,Long> {

    Dibs findByUserAndSalePostId(User user, Long salePostId);

    boolean existsBySalePostId(Long salePostId);
}
