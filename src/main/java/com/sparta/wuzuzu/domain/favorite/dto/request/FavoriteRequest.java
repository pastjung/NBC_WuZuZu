package com.sparta.wuzuzu.domain.favorite.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteRequest {

    private String spotName;
    private String address;
    private String category;

    public FavoriteRequest(String spotName, String address, String category) {
        this.spotName = spotName;
        this.address = address;
        this.category = category;

    }

}
