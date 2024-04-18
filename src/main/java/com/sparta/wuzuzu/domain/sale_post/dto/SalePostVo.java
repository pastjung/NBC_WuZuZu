package com.sparta.wuzuzu.domain.sale_post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SalePostVo {
    private Long salePostId;
    private String title;
    private String description;
    private Long views;
    private String author;
    private String category;
    private Boolean status;
    private String goods;
    private Long price;
    private Long stock;
    private List<String> imageUrls;
}
