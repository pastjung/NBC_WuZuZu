package com.sparta.wuzuzu.domain.category.service;

import com.sparta.wuzuzu.domain.category.dto.CategoryRequest;
import com.sparta.wuzuzu.domain.category.dto.CategoryResponse;
import com.sparta.wuzuzu.domain.category.entity.Category;
import com.sparta.wuzuzu.domain.category.repository.CategoryRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    public final CategoryRepository categoryRepository;

    @Transactional
    public void createCategory(User user, CategoryRequest requestDto) {
        validationAdmin(user);

        Optional<Category> category = categoryRepository.findByName(requestDto.getName());

        if (category.isPresent()) {
            if (category.get().getStatus()) {
                throw new IllegalArgumentException("이미 존재하는 category");
            } else {
                category.get().reCreate();
            }
        } else {
            categoryRepository.save(new Category(requestDto.getName()));
        }
    }

    public List<CategoryResponse> getCategory() {
        List<Category> categoryList = categoryRepository.findAll();

        if (categoryList.isEmpty()) {
            throw new IllegalArgumentException("postList is empty.");
        }

        return categoryList.stream()
            .map(CategoryResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updatePost(User user, Long categoryId, CategoryRequest requestDto) {
        validationAdmin(user);

        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new IllegalArgumentException("카테고리가 존재하지 않습니다.")
        );

        category.update(requestDto.getName());
    }

    @Transactional
    public void deletePost(User user, Long categoryId) {
        validationAdmin(user);

        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new IllegalArgumentException("카테고리가 존재하지 않습니다.")
        );

        if (!category.getStatus()) {
            throw new IllegalArgumentException("category is already deleted");
        }

        category.delete();
    }

    private void validationAdmin(User user) {
//        if(!user.getRole().equals(UserRole.ADMIN)){
//            throw new IllegalArgumentException("권한이 없습니다.");
//        }
    }
}
