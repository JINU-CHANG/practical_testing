package sample.cafekiosk.unit.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Order {
    //주문일시
    //final -> 필수로 받기
    private final LocalDateTime orderDateTime;
    //음료리스트
    private final List<Beverage> beverages;
}
