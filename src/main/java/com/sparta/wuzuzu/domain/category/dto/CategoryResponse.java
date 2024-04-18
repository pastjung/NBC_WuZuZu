package com.sparta.wuzuzu.domain.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sparta.wuzuzu.domain.category.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CategoryResponse {
    private Long id;
    private String name;
    private boolean status;

    public CategoryResponse(Category category){
        this.id = category.getCategoryId();
        this.name = category.getName();
        this.status = category.getStatus();
    }
}
