package com.sparta.wuzuzu.domain.order.repository.query;

import com.sparta.wuzuzu.domain.order.dto.OrdersVo;
import java.util.List;

public interface OrderQueryRepository {

    List<OrdersVo> findAllOrders(Long userId);
}
