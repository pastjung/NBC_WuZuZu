package com.sparta.wuzuzu.domain.favorite.repository;

import com.sparta.wuzuzu.domain.favorite.entity.Favorite;
import com.sparta.wuzuzu.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUser(User user);
}
