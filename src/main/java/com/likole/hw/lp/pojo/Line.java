package com.likole.hw.lp.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author likole
 * @date 2021/3/25
 */
@Data
public class Line {
    private double b;
    private List<Double> x;

    Line(double b, List<Double> x) {
        this.b = b;
        this.x = x;
    }

    public void minusToZero(Line other, int zeroIndex) {
        final double factor = x.get(zeroIndex) / other.x.get(zeroIndex);
        b -= other.b * factor;
        for (int i = 0; i < x.size(); i++) {
            x.set(i, x.get(i) - factor * other.getX().get(i));
        }
    }

    public void divideToOne(int oneIndex) {
        double factor = x.get(oneIndex);
        for (int i = 0; i < x.size(); i++) {
            x.set(i, x.get(i) / factor);
        }
        b /= factor;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%.2f", b)).append("\t");
        for (Double a : x) {
            stringBuilder.append(String.format("%.2f", a)).append("\t");
        }
        return stringBuilder.toString();
    }
}
