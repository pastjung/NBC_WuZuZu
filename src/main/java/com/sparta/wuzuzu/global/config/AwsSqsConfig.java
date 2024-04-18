package com.sparta.wuzuzu.global.config;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class AwsSqsConfig {

    @Value("${cloud.aws.s3.credentials.accessKey}")
    private String awsAccessKey;

    @Value("${cloud.aws.s3.credentials.secretKey}")
    private String awsSecretKey;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    // 클라이언트 설정: Region 과 자격증명
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
            .credentialsProvider(() -> new AwsCredentials() {
                @Override
                public String accessKeyId() {
                    return awsAccessKey;
                }

                @Override
                public String secretAccessKey() {
                    return awsSecretKey;
                }
            })
            .region(Region.of(awsRegion))
            .build();
    }

    // Listener Factory 설정 (Listener 쪽)
    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory.builder()
            .sqsAsyncClient(sqsAsyncClient())
            .configure(
                option -> option
                    // 한 번에 몇 개의 메세지를 수신할 건지
                    .maxMessagesPerPoll(1)
                    // 메세지 처리 모드 설정(ALWAYS : 항상 처리 후 메세지 큐에서 삭제)
                    .acknowledgementMode(AcknowledgementMode.ALWAYS)
            )
            .build();
    }

    // 메시지 발송을 위한 SQS 템플릿 설정 (Sender 쪽)
    @Bean
    public SqsTemplate sqsTemplate() {
        return SqsTemplate.newTemplate(sqsAsyncClient());
    }
}