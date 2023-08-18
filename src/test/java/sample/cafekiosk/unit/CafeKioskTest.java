package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

    @Test
    void add_manual_test(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
    }

    @DisplayName("음료를 1개 추가할 수 있다.")
    @Test
    void add(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
    }

    @Test
    void addSeveralBeverages(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano,2);

        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
    }

    @Test
    void addZeroBeverages(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(()->cafeKiosk.add(americano,0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void remove(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();

    }

    @Test
    void createOrder() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano,1);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023,7,22,10,0));
        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    @Test
    void calculateTotalPrice(){
        // Given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano,1);
        cafeKiosk.add(latte,1);

        // When
        int totalPrice = cafeKiosk.calculateTotalPrice();

        // Then
        assertThat(totalPrice).isEqualTo(8500);
    }

    @DisplayName("음료 선택시 ice/hot 여부를 선택할 수 있다.")
    @Test
    void addIceAndHotOption() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano(false); //ice
        Americano americano2 = new Americano(true); //hot

        // when
        cafeKiosk.add(americano,1);
        cafeKiosk.add(americano2,3);

        // then
        assertThat(cafeKiosk.getBeverages().get(0).isHot()).isEqualTo(false);
        assertThat(cafeKiosk.getBeverages().get(1).isHot()).isEqualTo(true);
    }

}