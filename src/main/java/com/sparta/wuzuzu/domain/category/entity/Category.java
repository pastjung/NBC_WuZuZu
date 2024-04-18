package com.sparta.wuzuzu.domain.category.entity;

import com.sparta.wuzuzu.domain.sale_post.entity.SalePost;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TINYINT(1) default 0")
    private Boolean status = true;

    @OneToMany(mappedBy = "category")
    private List<SalePost> posts;

    public Category(String name) {
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
