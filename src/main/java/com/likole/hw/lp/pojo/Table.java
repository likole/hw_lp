package com.likole.hw.lp.pojo;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author likole
 * @date 2021/3/25
 */
@Data
public class Table {
    private int m, n;

    private List<Integer> xb;
    private List<Double> theta;
    private List<Line> bases;
    private Line z;

    /**
     * initial table
     *
     * @param m  约束个数
     * @param n  变量个数
     * @param c  目标函数的系数
     * @param xs 约束的系数
     * @param b  b
     * @param xb 目前的基变量（index from 0）
     */
    public Table(int m, int n, List<Double> c, List<List<Double>> xs, List<Double> b, List<Integer> xb) {
        this.m = m;
        this.n = n;
        this.xb = xb;
        theta = new ArrayList<>(m);
        this.z = new Line(0, c);
        this.bases = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            bases.add(new Line(b.get(i), xs.get(i)));
        }
    }

    /**
     * print table
     */
    public void printTable() {
        for (int i = 0; i < m; i++) {
            System.out.println("x" + (xb.get(i) + 1) + "\t" + bases.get(i));
        }
        System.out.println("-z\t" + z);
        System.out.println("-------------------------------------------------");
    }

    public void printResult() {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(0.);
        }
        for (int i = 0; i < m; i++) {
            result.set(xb.get(i), bases.get(i).getB());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("最优解为 (");
        for (int i = 0; i < n - 1; i++) {
            sb.append(String.format("%.2f", result.get(i))).append(",");
        }
        sb.append(String.format("%.2f", result.get(n - 1))).append(") ");
        sb.append(" 对应的目标函数值为").append(String.format("%.2f", -z.getB()));
        System.out.println(sb.toString());
    }

    /**
     * 进行行变换
     *
     * @param line 离基变量编号
     * @param col  入基变量编号
     */
    public void updateTable(int line, int col) {
        // update X_B
        xb.set(line, col);
        // update matrix
        bases.get(line).divideToOne(col);
        for (int i = 0; i < m; i++) {
            if (i == line) {
                continue;
            }
            bases.get(i).minusToZero(bases.get(line), col);
        }
        z.minusToZero(bases.get(line), col);
    }

    /**
     * 当前是否达到最优解
     *
     * @return is optimal
     */
    public boolean isOptimal() {
        for (int i = 0; i < n; i++) {
            if (!xb.contains(i) && z.getX().get(i) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取入基变量
     *
     * @return 入基变量的编号
     */
    public Integer getInVariable() {
        int index = -1;
        double maxValue = 0;
        for (int i = 0; i < n; i++) {
            if (!xb.contains(i) && z.getX().get(i) > maxValue) {
                index = i;
                maxValue = z.getX().get(i);
            }
        }
        return index > -1 ? index : null;
    }

    /**
     * 获取离基变量
     *
     * @param inVariable 入基变量
     * @return 离基变量编号 or null(无有限最优解)
     */
    public Integer getOutVariable(int inVariable) {
        // 初始值不会用到，只是为了防止编译器警告
        double minTheta = 0;
        int index = -1;
        for (int i = 0; i < m; i++) {
            if (bases.get(i).getX().get(inVariable) > 0) {
                double theta = bases.get(i).getB() / bases.get(i).getX().get(inVariable);
                if (index == -1 || theta < minTheta) {
                    minTheta = theta;
                    index = i;
                }
            }
        }
        if (index == -1) {
            return null;
        }
        System.out.println("入基x" + (inVariable + 1) + " 离基x" + (xb.get(index) + 1));
        return index;
    }

}
