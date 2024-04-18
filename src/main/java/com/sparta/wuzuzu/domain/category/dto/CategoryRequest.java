package com.sparta.wuzuzu.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryRequest {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
}