package com.sparta.wuzuzu.domain.favorite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {

    private Long favoriteId;
    private String spotName;
    private String address;
    private String category;
}
