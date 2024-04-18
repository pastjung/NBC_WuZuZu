package com.sparta.wuzuzu.domain.favorite.entity;

import com.sparta.wuzuzu.domain.favorite.dto.request.FavoriteRequest;
import com.sparta.wuzuzu.domain.favorite.dto.response.FavoriteResponse;
import com.sparta.wuzuzu.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @Column(nullable = false)
    private String spotName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    public Favorite(FavoriteRequest request, User user) {
        this.spotName = request.getSpotName();
        this.address = request.getAddress();
        this.category = request.getCategory();
        this.user = user;
    }

    public FavoriteResponse createResponseFavorite() {
        return new FavoriteResponse(this.favoriteId, this.spotName, this.address, this.category);
    }
}
