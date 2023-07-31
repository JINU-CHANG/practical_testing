package sample.cafekiosk.spring.api.service.order.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.stock.Stock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderRefundResponse {

    private Long id;

    private int totalPrice;

    private OrderStatus orderStatus;

    private LocalDateTime registeredDateTime;

    private LocalDateTime cancellationTime;

    private List<ProductResponse> refundedProducts;

    @Builder
    public OrderRefundResponse(Long id, int totalPrice, OrderStatus orderStatus, LocalDateTime registeredDateTime, LocalDateTime cancellationTime, List<ProductResponse> refundedProducts) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.registeredDateTime = registeredDateTime;
        this.cancellationTime = cancellationTime;
        this.refundedProducts = refundedProducts;
    }

    public static OrderRefundResponse of(Order order) {
        return OrderRefundResponse.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .cancellationTime(order.getCancellationDateTime())
                .refundedProducts(order.getOrderProducts().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
