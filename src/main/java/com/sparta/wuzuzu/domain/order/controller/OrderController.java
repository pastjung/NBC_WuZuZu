package com.sparta.wuzuzu.domain.order.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.sparta.wuzuzu.domain.common.dto.CommonResponse;
import com.sparta.wuzuzu.domain.order.dto.OrderRequest;
import com.sparta.wuzuzu.domain.order.dto.OrdersVo;
import com.sparta.wuzuzu.domain.order.service.OrderService;
import com.sparta.wuzuzu.global.security.UserDetailsImpl;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Value("${iamport.key}")
    private String restApiKey;
    @Value("${iamport.secret}")
    private String restApiSecret;

    @PostConstruct
    public void init() {
        IamportClient iamportClient = new IamportClient(restApiKey, restApiSecret);
        orderService.initIamPortClient(iamportClient);
    }

    @PostMapping("/{imp_uid}")
    public ResponseEntity<IamportResponse<Payment>> createOrder(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody OrderRequest requestDto,
        @PathVariable("imp_uid") String imp_uid
    ) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = orderService.createOrder(userDetails.getUser(), requestDto, imp_uid);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<OrdersVo>>> getOrders(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        List<OrdersVo> orderResponseList = orderService.getOrders(userDetails.getUser());
        return CommonResponse.ofDataWithHttpStatus(orderResponseList, HttpStatus.CREATED);
    }
}

