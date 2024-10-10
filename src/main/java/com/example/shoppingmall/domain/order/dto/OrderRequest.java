package com.example.shoppingmall.domain.order.dto;

import com.example.shoppingmall.domain.order.type.OrderCancelOption;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class OrderRequest {

    private OrderCancelOption orderCancelOption;  // 값을 보내지 않으면 서비스 내부에서 CANCEL_OUT_OF_STOCK_ONLY 로직 타게끔 하였음

    private DeliveryInfo deliveryInfo;
    private List<OrderItemRequest> orderItemRequests;
}
