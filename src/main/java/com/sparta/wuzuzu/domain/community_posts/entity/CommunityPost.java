package com.sparta.wuzuzu.domain.community_posts.entity;

import com.sparta.wuzuzu.domain.comment.entity.Comment;
import com.sparta.wuzuzu.domain.common.entity.Timestamped;
import com.sparta.wuzuzu.domain.common.image.entity.Image;
import com.sparta.wuzuzu.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "community_posts", indexes = {
    @Index(name = "idx_like_count", columnList = "likeCount"),
    @Index(name = "idx_views", columnList = "views")
})
public class CommunityPost extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "communitypost_id")
    private Long communityPostId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long views = 0L;

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(nullable = false)
    private Long comments = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @OneToMany(mappedBy = "communityPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikeList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CommunityCategory category;

    @OneToMany(mappedBy = "communityPost")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "communityPost")
    private List<Image> imageUrl;

    public CommunityPost(String title, CommunityCategory category, String content, User user) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.user = user;

    }

    public void addLike() {
        this.likeCount++;
    }

    public void removeLike() {
        this.likeCount = Math.max(0, this.likeCount - 1); // 0 미만으로 내려가지 않도록 보장
    }
    public void increaseViews() {
        views++;
    }

    public void addComment() {this.comments++;}

    public void removeComments() {
        this.comments = Math.max(0, this.comments - 1); // 0 미만으로 내려가지 않도록 보장
    }

    public boolean validateUser(Long loginUserId) {
        return this.user.getUserId().equals(loginUserId);
    }

    public void updateCommunityPosts(String title, CommunityCategory category, String content) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
