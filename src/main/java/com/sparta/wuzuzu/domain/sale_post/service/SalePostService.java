package com.sparta.wuzuzu.domain.sale_post.service;

import com.sparta.wuzuzu.domain.category.entity.Category;
import com.sparta.wuzuzu.domain.category.repository.CategoryRepository;
import com.sparta.wuzuzu.domain.common.image.service.ImageService;
import com.sparta.wuzuzu.domain.sale_post.dto.SalePostRequest;
import com.sparta.wuzuzu.domain.sale_post.dto.SalePostResponse;
import com.sparta.wuzuzu.domain.sale_post.dto.SalePostVo;
import com.sparta.wuzuzu.domain.sale_post.entity.SalePost;
import com.sparta.wuzuzu.domain.sale_post.repository.SalePostRepository;
import com.sparta.wuzuzu.domain.sale_post.repository.query.SalePostQueryRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SalePostService {

    private final SalePostRepository salePostRepository;
    private final SalePostQueryRepository salePostQueryRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public SalePostResponse createSalePost(
        User user,
        SalePostRequest requestDto,
        MultipartFile image) {
        Category category = categoryRepository.findByName(requestDto.getCategory()).orElseThrow(
            () -> new IllegalArgumentException("카테고리가 존재하지 않습니다.")
        );

        SalePost salePost = createSalePostToRedis(user, requestDto, category);

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = imageService.createImage(image, salePost);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new SalePostResponse(salePost, imageUrl);
    }

    public SalePost createSalePostToRedis(User user, SalePostRequest requestDto,
        Category category) {
        SalePost salePost = salePostRepository.save(new SalePost(user, requestDto, category));

        // Redis 에 초기 세팅
        redisTemplate.opsForValue().set("salePost:" + salePost.getSalePostId() + ":stock",
            String.valueOf(salePost.getStock()));

        return salePost;
    }

    // 전체 게시글 : 제목, 조회수만 출력
    // 전체 게시글 목록 조회 : createAt 기준으로 pageable 사용하기(미적용)
    // Category 와 User 의 name 을 가져오면서 N+1 문제 생김 -> QueryDSL 로 변경하기
    @Transactional
    public List<SalePostResponse> getSalePosts() {
        List<SalePost> salePostList = salePostRepository.findAllByStatusTrue();

        return salePostList.stream()
            .map(SalePostResponse::new)
            .collect(Collectors.toList());
    }

    // 상세 게시글 : 제목, 조회수, 설명, 상품 내용 출력
    // 게시물 상세 조회 : QueryDSL 사용하기
    @Transactional
    public SalePostVo getSalePost(
        Long salePostId
    ) {
        SalePost salePost = salePostRepository.findById(salePostId).orElseThrow(
            () -> new IllegalArgumentException("post is empty.")
        );

        if (!salePost.getStatus()) {
            throw new IllegalArgumentException("post is deleted");
        }

        salePost.increaseViews();   // 동시성 제어 고려

        return salePostQueryRepository.findPostByPostId(salePostId);
    }

    @Transactional
    public void updateSalePost(
        User user,
        Long salePostId,
        SalePostRequest requestDto
    ) {
        SalePost salePost = checkSalePost(user, salePostId);

        Category category = categoryRepository.findByName(requestDto.getCategory()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 카테고리 입니다.")
        );

        salePost.update(requestDto, category);
    }

    @Transactional
    public void deleteSalePost(
        User user,
        Long salePostId
    ) {
        SalePost salePost = checkSalePost(user, salePostId);
        salePost.delete();
    }

    @Transactional
    public void uploadImage(User user, Long salePostId, List<MultipartFile> imageFiles)
        throws IOException {
        SalePost salePost = checkSalePost(user, salePostId);

        for (MultipartFile imageFile : imageFiles) {
            imageService.createImage(imageFile, salePost);
        }
    }

    public void deleteImage(User user, Long salePostId, String key) {
        SalePost salePost = checkSalePost(user, salePostId);
        imageService.deleteImage(key, salePost);
    }

    private SalePost checkSalePost(User user, Long salePostId) {
        SalePost salePost = salePostRepository.findById(salePostId).orElseThrow(
            () -> new IllegalArgumentException("post is empty.")
        );

        if (!user.getUserId().equals(salePost.getUser().getUserId())) {
            throw new IllegalArgumentException("post is not yours");
        }

        if (!salePost.getStatus()) {
            throw new IllegalArgumentException("post is already deleted.");
        }

        return salePost;
    }
}
