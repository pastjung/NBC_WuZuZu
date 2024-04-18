package com.sparta.wuzuzu.domain.spot.dto.response;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Getter
@NoArgsConstructor
public class CategorySpotResponse implements Serializable {

    private String spotName;
    private String address;

    public CategorySpotResponse(JSONObject item) {
        this.spotName = item.getString("place_name");
        this.address = item.getString("address_name");
    }
}
