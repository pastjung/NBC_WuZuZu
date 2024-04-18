package com.sparta.wuzuzu.domain.order.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.sparta.wuzuzu.domain.order.dto.CreateOrderMessage;
import com.sparta.wuzuzu.domain.order.dto.OrderRequest;
import com.sparta.wuzuzu.domain.order.dto.OrdersVo;
import com.sparta.wuzuzu.domain.order.entity.Order;
import com.sparta.wuzuzu.domain.order.repository.OrderRepository;
import com.sparta.wuzuzu.domain.order.repository.query.OrderQueryRepository;
import com.sparta.wuzuzu.domain.sale_post.entity.SalePost;
import com.sparta.wuzuzu.domain.sale_post.repository.SalePostRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final SalePostRepository salePostRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SqsTemplate sqsTemplate;

    private IamportClient iamportClient;

    public void initIamPortClient(IamportClient iamportClient) {
        this.iamportClient = iamportClient;
    }

    @Value("${cloud.aws.sqs.queue.url}")
    private String queueUrl;

    @Value("${cloud.aws.sqs.queue.name}")
    private String queueName;

    // 결제 완료 시 주문 생성 요청 및 SQS 로 주문 정보 메세지 송신
    public IamportResponse<Payment> createOrder(User user, OrderRequest request, String imp_uid)
        throws IamportResponseException, IOException {
        sqsTemplate.send(to -> to
            .queue(queueName)
            .messageGroupId(String.valueOf(user.getUserId()))
            .payload(new CreateOrderMessage(user, request, imp_uid))
        );

        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);

        if (response == null || !response.getResponse().getAmount()
            .equals(BigDecimal.valueOf(request.getAmount()))) {
            cancelPayment(imp_uid, "결제 정보가 일치하지 않음.");
        }

        // SSE 구독

        return iamportClient.paymentByImpUid(imp_uid);
    }

    // SQS 에서 메세지를 하나씩 수신해 주문 처리 
    @SqsListener("${cloud.aws.sqs.queue.name}")
    @Transactional
    public void receiveOrderMessage(@Payload CreateOrderMessage message) {
        OrderRequest orderRequest = message.getRequest();

        SalePost salePost = salePostRepository.findById(orderRequest.getSalePostId()).orElseThrow(
            () -> {
                // 결제 취소 로직
                cancelPayment(message.getImp_uid(), "상품이 존재하지 않음.");

                return new IllegalArgumentException("post is empty.");
            });

        if (salePost.getStock() < orderRequest.getCount()) {
            // 결제 취소 로직
            cancelPayment(message.getImp_uid(), "재고보다 주문 수량이 많습니다.");

            throw new IllegalArgumentException("재고보다 주문 수량이 많습니다.");
        }

        Order order = orderRepository.save(new Order(orderRequest, message.getUser()));
        salePost.updateStock(salePost.getStock() - order.getCount());
        // SSE 알림

        log.info(salePost.getStock().toString());
    }

    // 결제 취소 메소드
    private void cancelPayment(String imp_uid, String reason) {
        CancelData cancelData = new CancelData(imp_uid, true);
        cancelData.setReason(reason);
        try {
            iamportClient.cancelPaymentByImpUid(cancelData);
        } catch (IamportResponseException | IOException e) {
            // SSE 알림

            throw new RuntimeException(e);
        }
    }

    public List<OrdersVo> getOrders(
        User user
    ) {
        List<OrdersVo> orderList = orderQueryRepository.findAllOrders(user.getUserId());

        if (orderList.isEmpty()) {
            throw new IllegalArgumentException("orderList is empty.");
        }

        return orderList;
    }
}
