package sample.cafekiosk.unit.beverage;

public class Americano implements Beverage {

    private boolean isHot;

    public Americano() {
        this.isHot = true; // 디폴트값을 true로 설정
    }

    public Americano(boolean isHot){
        this.isHot = isHot;
    }

    @Override
    public int getPrice() {
        return 4000;
    }

    @Override
    public String getName() {
        return "아메리카노";
    }

    @Override
    public boolean isHot() {
        return isHot;
    }


}
