package com.sparta.wuzuzu.domain.community_posts.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CommunityCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TINYINT(1) default 0")
    private Boolean status = true;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityPost> communityPosts;

    public CommunityCategory(String name) {
        this.name = name;
    }

    public void update(String name) {
        this.name = name;
    }

    public void delete() {
        this.status = false;
    }

    public void reCreate() {
        this.status = true;
    }


}
