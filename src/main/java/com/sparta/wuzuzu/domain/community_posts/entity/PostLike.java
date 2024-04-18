package com.sparta.wuzuzu.domain.community_posts.entity;

import com.sparta.wuzuzu.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "likePosts")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_postsid", nullable = false)
    private CommunityPost communityPost;

    public PostLike(User user, CommunityPost communityPost) {
        this.user = user;
        this.communityPost = communityPost;
    }

    public void addLike(CommunityPost communityPost) {
        this.communityPost = communityPost;
        communityPost.getPostLikeList().add(this);
    }

    public void removeLike(CommunityPost communityPost) {
        this.communityPost = communityPost;
        communityPost.getPostLikeList().remove(this);
    }
}

