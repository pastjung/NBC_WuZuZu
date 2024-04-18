package com.sparta.wuzuzu.domain.common.image.entity;

import com.sparta.wuzuzu.domain.common.entity.Timestamped;
import com.sparta.wuzuzu.domain.community_posts.entity.CommunityPost;
import com.sparta.wuzuzu.domain.sale_post.entity.SalePost;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "images")
public class Image extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salePost_id")
    private SalePost salePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communityPost_id")
    private CommunityPost communityPost;

    public Image(String imageName, Object object) {
        this.imageUrl = imageName;
        if(object instanceof SalePost){
            this.salePost = (SalePost) object;
        } else if (object instanceof CommunityPost) {
            this.communityPost = (CommunityPost) object;
        }
    }
}
