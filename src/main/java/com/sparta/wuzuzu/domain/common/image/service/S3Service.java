package com.sparta.wuzuzu.domain.common.image.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.net.URLDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    /* 파일 업로드 */
    public String upload(MultipartFile multipartFile, String s3FileName) throws IOException {
        // 업로드할 파일의 메타데이터 생성
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());   // 파일의 크기를 가져와 업로드할 파일의 크기 설정
        objMeta.setContentType(MediaType.IMAGE_JPEG_VALUE);                     // 업로드할 파일의 유형을 설정 : JPEG 이미지의 MIME 유형 설정

        // putObject(버킷명, 파일명, 파일데이터, 메타데이터)로 S3에 객체 등록
        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        // 등록된 객체의 url 반환
        // getUrl : 업로드된 객체의 URL(AWS S3에 업로드된 파일에 대한 고유한 위치) 가져오기
        // decode: url 안의 한글 or 특수문자 깨짐 방지
        return URLDecoder.decode(amazonS3.getUrl(bucket, s3FileName).toString(), "utf-8");
    }

    /* 파일 삭제 */
    public void delete(String key){
        try {
            // deleteObject(버킷명, 키값)으로 객체 삭제
            amazonS3.deleteObject(bucket, key);
        } catch (AmazonServiceException e) {
            log.error(e.toString());
        }
    }
}