package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderRefundResponse;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderProduct;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sample.cafekiosk.spring.domain.order.OrderStatus.CANCELED;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /**
     * 재고 감소 -> 동시성 고민
     * optimistic lock / pessimistic lock / ...
     */
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();

        List<Product> products = findProductsBy(productNumbers);

        updateStockQuantities(products, false);

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    // 스터디-코드 리팩토링
    public OrderRefundResponse refundOrder(Long orderId, LocalDateTime cancellationDateTime){
        Order order = orderRepository.findById(orderId).orElseThrow();

        List<Product> products = findProductsByOrder(order.getOrderProducts());

        updateStockQuantities(products, true);

        order.refund(cancellationDateTime);
        orderRepository.save(order);

        return OrderRefundResponse.of(order);
    }

    // 하나의 메소드에 증가 감소 로직 둘다 있어도 괜찮은가 ?
    private void updateStockQuantities(List<Product> products, boolean increase) {
        List<String> productNumbers = extractProductNumbersWithStock(products);

        Map<String, Long> productCountingMap = createCountingMapBy(productNumbers);

        for(String productNumber : new HashSet<>(productNumbers)) {
            Stock stock = stockRepository.findByProductNumber(productNumber);
            int quantity = productCountingMap.get(productNumber).intValue();

            if (increase) {
                stock.increaseQuantity(quantity);
            } else {
                if (stock.isQuantityLessThan(quantity)) {
                    throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
                }
                stock.deductQuantity(quantity);
            }
        }
    }

//    private void increaseStockQuantities(List<Product> products) {
//        List<String> stockProductNumbers = extractStockProductNumbers(products);
//
//        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
//        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);
//
//        for(String productNumber : stockProductNumbers) {
//            Stock stock = stockRepository.findByProductNumber(productNumber);
//            int quantity = productCountingMap.get(productNumber).intValue();
//
//            stock.increaseQuantity(quantity);
//        }
//    }
//
//    private void  deductStockQuantities(List<Product> products) {
//        List<String> stockProductNumbers = extractStockProductNumbers(products);
//
//        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
//        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);
//
//        for(String stockProductNumber : new HashSet<>(stockProductNumbers)) {
//            Stock stock = stockMap.get(stockProductNumber);
//            int quantity = productCountingMap.get(stockProductNumber).intValue();
//
//            if(stock.isQuantityLessThan(quantity)) {
//                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
//            }
//
//            stock.deductQuantity(quantity);
//        }
//    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p->p, Collectors.counting()));
    }

//    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
//        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
//        return stocks.stream()
//                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
//    }

    // 메서드명 변경(기존 : extractStockProductNumbers)
    private static List<String> extractProductNumbersWithStock(List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(product -> product.getProductNumber())
                .collect(Collectors.toList());
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(product -> product.getProductNumber(), p ->p));

        List<Product> duplicateProducts = productNumbers.stream()
                .map(productNumber -> productMap.get(productNumber))
                .collect(Collectors.toList());
        return duplicateProducts;
    }

    private List<Product> findProductsByOrder(List<OrderProduct> orderProducts) {
            return orderProducts.stream()
                .map(OrderProduct::getProduct)
                .collect(Collectors.toList());
    }
}
