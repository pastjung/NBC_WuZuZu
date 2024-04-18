package com.sparta.wuzuzu.domain.spot.dto.response;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class SpotDetailResponse {

    private String spotName;
    private String address;
    private String category;
    private String telephone;
    private String link;

    public SpotDetailResponse(JSONObject item) {
        this.spotName = item.getString("place_name");
        this.address = item.getString("address_name");
        this.category = item.getString("category_name");
        this.telephone = item.getString("phone");
        this.link = item.getString("place_url");
    }
}
