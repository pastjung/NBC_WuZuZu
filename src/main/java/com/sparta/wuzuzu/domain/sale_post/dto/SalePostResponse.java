package com.sparta.wuzuzu.domain.sale_post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sparta.wuzuzu.domain.sale_post.entity.SalePost;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SalePostResponse {

    private Long salePostId;
    private String title;
    private Long views;
    private String author;
    private String category;
    private String image;

    public SalePostResponse(SalePost post) {
        this.salePostId = post.getSalePostId();
        this.title = post.getTitle();
        this.views = post.getViews();
        this.author = post.getUser().getUserName();
        this.category = post.getCategory().getName();
    }

    public SalePostResponse(SalePost post, String image) {
        this.salePostId = post.getSalePostId();
        this.title = post.getTitle();
        this.views = post.getViews();
        this.author = post.getUser().getUserName();
        this.category = post.getCategory().getName();
        this.image = image;
    }
}