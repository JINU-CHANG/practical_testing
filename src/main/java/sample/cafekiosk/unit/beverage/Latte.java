package sample.cafekiosk.unit.beverage;

import sample.cafekiosk.unit.beverage.Beverage;

public class Latte implements Beverage {

    private boolean isHot;
    @Override
    public int getPrice() {
        return 4500;
    }

    @Override
    public String getName() {
        return "라떼";
    }

    @Override
    public boolean isHot() {
        return isHot;
    }
}
