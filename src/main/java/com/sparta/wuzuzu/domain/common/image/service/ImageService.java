package com.sparta.wuzuzu.domain.common.image.service;

import com.sparta.wuzuzu.domain.common.image.entity.Image;
import com.sparta.wuzuzu.domain.common.image.repository.ImageRepository;
import com.sparta.wuzuzu.domain.community_posts.entity.CommunityPost;
import com.sparta.wuzuzu.domain.sale_post.entity.SalePost;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${defaultImage.path}")
    private String defaultImagePath;

    @Value("${upload.path}")
    private String uploadPath;

    private final S3Service s3Service;

    private final ImageRepository imageRepository;

    @Transactional
    public String createImage(MultipartFile file, Object object) throws IOException {
        String imageName = getImageName(file);
        return imageRepository.save(new Image(imageName, object)).getImageUrl();
    }

    @Transactional
    public void deleteImage(String url, Object object){
        Optional<Image> imageUrlToDelete;

        // Object 에 속한 이미지 목록에서 해당 URL 을 가진 이미지 탐색
        if(object instanceof SalePost salePost){
            imageUrlToDelete = salePost.getImageUrl().stream()
                .filter(imageUrl -> imageUrl.getImageUrl().equals(uploadPath + url))
                .findFirst();
            if (imageUrlToDelete.isPresent()) {
                // 해당 URL 을 가진 이미지가 존재하면 SalePost 에서 삭제
                Image image = imageUrlToDelete.get();
                salePost.getImageUrl().remove(image);
                imageRepository.delete(image);
            }
        }

        else if(object instanceof CommunityPost communityPost){
            imageUrlToDelete = communityPost.getImageUrl().stream()
                .filter(imageUrl -> imageUrl.getImageUrl().equals(uploadPath + url))
                .findFirst();
            if (imageUrlToDelete.isPresent()) {
                // 해당 URL 을 가진 이미지가 존재하면 CommunityPost 에서 삭제
                Image image = imageUrlToDelete.get();
                communityPost.getImageUrl().remove(image);
                imageRepository.delete(image);
            }
        }

        else {
            throw new IllegalArgumentException("Object 가 잘못되었습니다.");
        }

        if (imageUrlToDelete.isEmpty()) {
            // 해당 URL 을 가진 이미지가 없는 경우 예외 발생
            throw new IllegalArgumentException("해당 URL 을 가진 이미지가 없습니다: " + uploadPath + url);
        }

        // S3 에 이미지 제거 요청
        s3Service.delete(url);
    }

    public Image getImageByCommunityPost(CommunityPost communityPost) {
        return imageRepository.findByCommunityPost(communityPost);
    }

    private String getImageName(MultipartFile file) throws IOException {
        if (file != null) {
            // UUID.randomUUID() : UUID 클래스를 이용해 시간과 공간을 기반으로 128비트의 고유한 식별자 생성
            // file.getOriginalFilename() : 클라이언트가 업로드한 파일의 원래 파일 이름 반환
            String originalFileName = UUID.randomUUID() + file.getOriginalFilename();
            return s3Service.upload(file, originalFileName);
        }
        return defaultImagePath;
    }
}