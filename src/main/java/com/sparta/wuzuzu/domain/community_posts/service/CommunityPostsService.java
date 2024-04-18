package com.sparta.wuzuzu.domain.community_posts.service;

import com.sparta.wuzuzu.domain.common.image.entity.Image;
import com.sparta.wuzuzu.domain.common.image.service.ImageService;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostDetailResponse;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostListRequest;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostListResponse;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostRequest;
import com.sparta.wuzuzu.domain.community_posts.dto.CommunityPostResponse;
import com.sparta.wuzuzu.domain.community_posts.entity.CommunityCategory;
import com.sparta.wuzuzu.domain.community_posts.entity.CommunityPost;
import com.sparta.wuzuzu.domain.community_posts.repository.CommunityCategoryRepository;
import com.sparta.wuzuzu.domain.community_posts.repository.CommunityPostRepository;
import com.sparta.wuzuzu.domain.community_posts.repository.CustomCommunityPostRepository;
import com.sparta.wuzuzu.domain.community_posts.repository.PostLikeRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.global.exception.ValidateUserException;
import com.sparta.wuzuzu.global.util.PagingUtil;
import java.io.IOException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CommunityPostsService {

    private final CommunityPostRepository communityPostRepository;
    private final CommunityCategoryRepository communityCategoryRepository;
    private final CustomCommunityPostRepository customCommunityPostRepository;
    private final ImageService imageService;
    private final PostLikeRepository postLikeRepository;

    public CommunityPostResponse saveCommunityPosts(CommunityPostRequest communityPostRequest,
        MultipartFile image, User user) {

        CommunityCategory communityCategory = communityCategoryRepository.findByNameEquals(
                communityPostRequest.getCategoryName())
            .orElseThrow();

        if (!communityCategory.getStatus()) {
            throw new NoSuchElementException();
        }

        CommunityPost communityPost = communityPostRepository.save(new CommunityPost(
                communityPostRequest.getTitle(),
                communityCategory,
                communityPostRequest.getContent(),
                user
            )
        );

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = imageService.createImage(image, communityPost);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new CommunityPostResponse(communityPost, user.getUserName(), imageUrl);
    }

    @Transactional
    public CommunityPostResponse updateCommunityPosts(CommunityPostRequest communityPostRequest,
        Long userId, Long communityPostsId) {
        CommunityPost post = communityPostRepository.findById(communityPostsId).orElseThrow();
        post.validateUser(userId);
        post.updateCommunityPosts(
            communityPostRequest.getTitle(),
            communityCategoryRepository.findByNameEquals(communityPostRequest.getCategoryName())
                .orElseThrow(),
            communityPostRequest.getContent());
        return CommunityPostResponse.builder().
            title(post.getTitle()).
            categoryName(post.getCategory().getName()).
            username(post.getUser().getUserName()).
            contents(post.getContent()).
            build();
    }


    public CommunityPostListResponse searchCommunityPosts(CommunityPostListRequest request) {
        if (request.getColumn() == null || request.getColumn().isEmpty()) {
            request.setColumn("createdAt");//조회 기준 컬럼 입력 없을 경우 날짜순을 기본으로 지정
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(),
            Sort.by(request.getSortDirection(), request.getColumn()));
        Page<CommunityPostResponse> postsPage = customCommunityPostRepository.findByConditions(
            request.getKeyword(), request.getCategoryName(), pageable);

        PagingUtil pagingUtil = new PagingUtil(postsPage.getTotalElements(),
            postsPage.getTotalPages(), request.getPage(), request.getPageSize());

        return CommunityPostListResponse.builder()
            .postList(postsPage.getContent())
            .pagingUtil(pagingUtil)
            .build();
    }

    @Transactional
    public CommunityPostDetailResponse getDetail(Long communityPostId, User user) {
        CommunityPost post = communityPostRepository.findById(communityPostId)
            .orElseThrow(() -> new NoSuchElementException("해당 글을 찾을 수 없습니다."));

        String imageUrl = null;
        Image image = imageService.getImageByCommunityPost(post);
        if (image != null) {
            imageUrl = image.getImageUrl();
        }

        post.increaseViews();

        Boolean isLiked = postLikeRepository.findByUserAndCommunityPost(user, post).isPresent();

        return CommunityPostDetailResponse.builder().
            communityPostId(post.getCommunityPostId()).
            title(post.getTitle()).
            username(post.getUser().getUserName()).
            contents(post.getContent()).
            views(post.getViews()).
            categoryName(post.getCategory().getName()).
            comments(post.getCommentList().size()).
            image(imageUrl).
            likeCount(post.getLikeCount()).
            isLiked(isLiked).
            build();
    }

    @Transactional
    public void deleteCommunityPost(Long communityPostsID, User user) {
        communityPostRepository.findById(communityPostsID)
            .filter(communityPosts -> user.getUserId().equals(communityPosts.getUser().getUserId()))
            .orElseThrow(ValidateUserException::new); // 사용자 ID가 게시글 작성자의 ID와 일치하지 않을 경우 예외 발생

        communityPostRepository.deleteById(communityPostsID); // 게시글 삭제
    }

    @Transactional
    public void uploadImage(User user, Long communityPostId, MultipartFile imageFile)
        throws IOException {
        CommunityPost communityPost = checkCommunityPost(user, communityPostId);

        if (imageFile != null && !imageFile.isEmpty()) {
            imageService.createImage(imageFile, communityPost);
        }
    }

    public void deleteImage(User user, Long communityPostId, String key) {
        CommunityPost communityPost = checkCommunityPost(user, communityPostId);
        imageService.deleteImage(key, communityPost);
    }

    private CommunityPost checkCommunityPost(User user, Long communityPostId) {
        CommunityPost communityPost = communityPostRepository.findById(communityPostId).orElseThrow(
            () -> new IllegalArgumentException("post is empty.")
        );

        if (!user.getUserId().equals(communityPost.getUser().getUserId())) {
            throw new IllegalArgumentException("post is not yours");
        }

        return communityPost;
    }
}
