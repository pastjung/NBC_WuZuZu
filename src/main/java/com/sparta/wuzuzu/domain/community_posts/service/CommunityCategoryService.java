package com.sparta.wuzuzu.domain.community_posts.service;

import com.sparta.wuzuzu.domain.community_posts.dto.CommunityCategoryRequest;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityCategoryResponse;
import com.sparta.wuzuzu.domain.community_posts.entity.CommunityCategory;
import com.sparta.wuzuzu.domain.community_posts.repository.CommunityCategoryRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.domain.user.entity.UserRole;
import com.sparta.wuzuzu.global.exception.ValidateAdminException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunityCategoryService {

    private final CommunityCategoryRepository community_CategoryRepository;

    public CommunityCategoryResponse createCategory(User user,
        CommunityCategoryRequest communityCategoryRequest) {

//        UserRole userRole = user.getRole();
//        if (userRole != UserRole.ADMIN) {
//            throw new ValidateAdminException();
//        }

        CommunityCategory category = community_CategoryRepository
            .findByNameEquals(communityCategoryRequest.getName())
            .orElse(null); // Optional 객체를 사용하여 값의 존재 유무를 먼저 확인

        if (category != null && category.getStatus()) {
            throw new IllegalArgumentException("중복된 카테고리 이름입니다.");
        }

        if (category == null) {
            // 카테고리가 존재하지 않으면 새로 생성
            category = community_CategoryRepository.save(
                new CommunityCategory(communityCategoryRequest.getName()));
        } else {
            // 이미 존재하는 카테고리를 재활용
            category.reCreate();
            community_CategoryRepository.save(category); // 변경사항 저장
        }

        return new CommunityCategoryResponse(category);

    }

    public List<CommunityCategoryResponse> getCategory() {
        List<CommunityCategory> community_Categories = community_CategoryRepository.findAll();
        if (community_Categories.size() == 0) {
            throw new IllegalArgumentException("저장된 카테고리가 없습니다.");
        }
        return community_Categories.stream().
            map(CommunityCategoryResponse::new).
            collect(Collectors.toList());

    }

    @Transactional
    public void updateCategory(User user, Long categoryId, CommunityCategoryRequest request) {
        if (user.getRole() == UserRole.ADMIN) {
            CommunityCategory category = community_CategoryRepository.findById(categoryId)
                .orElseThrow(
                    NoSuchElementException::new);
            category.update(request.getName());

        }
    }

    @Transactional
    public void deleteCategory(User user, Long categoryId) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new ValidateAdminException();
        }
        CommunityCategory community_Category = community_CategoryRepository.findById(
            categoryId).orElseThrow(NoSuchElementException::new);
        if (!community_Category.getStatus()) {
            throw new IllegalArgumentException("이미 삭제 된 카테고리입니다.");
        } else {
            community_Category.delete();
        }
    }

}

