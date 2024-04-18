package com.sparta.wuzuzu.domain.sale_post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class SalePostRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String description;

    @NotBlank(message = "상품명은 필수입니다.")
    private String goods;

    @Positive(message = "가격은 양수이어야 합니다.")
    private Long price;

    @Positive(message = "판매 수량은 양수이어야 합니다.")
    private Long stock;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;
}
