package com.example.shoppingmall.domain.order.dto;

import com.example.shoppingmall.domain.order.domain.Orders;
import com.example.shoppingmall.domain.order.type.OrderCancelOption;
import com.example.shoppingmall.domain.order.type.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.shoppingmall.domain.order.type.OrderResult.SUCCESS;

@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
public class OrderResponse {

    private Long orderId;

    private OrderStatus orderStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime orderedAt;

    private DeliveryInfo deliveryInfo;

    private List<OrderItemResponse> orderItemResponses;

    private int totalPayment;


    public static OrderResponse of(Orders order,
                                   DeliveryInfo deliveryInfo,
                                   List<OrderItemResponse> orderItemResponses) {

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .orderedAt(order.getCreatedAt())
                .deliveryInfo(deliveryInfo)
                .orderItemResponses(orderItemResponses)
                .totalPayment(orderItemResponses.stream()
                        .filter(orderItem -> SUCCESS.equals(orderItem.getOrderResult()))  // SUCCESS 인 항목만 필터링
                        .mapToInt(OrderItemResponse::getPrice)  // 각 항목의 가격을 가져옴
                        .sum())
                .build();
    }
}
