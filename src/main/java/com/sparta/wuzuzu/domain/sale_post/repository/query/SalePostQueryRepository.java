package com.sparta.wuzuzu.domain.sale_post.repository.query;

import com.sparta.wuzuzu.domain.sale_post.dto.SalePostVo;

public interface SalePostQueryRepository {

    public SalePostVo findPostByPostId(Long postId);
}
