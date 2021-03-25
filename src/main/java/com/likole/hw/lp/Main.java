package com.likole.hw.lp;

import com.likole.hw.lp.pojo.Table;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author likole
 * @date 2021/3/25
 */
@Slf4j

public class Main {


    public static void main(String[] args) throws IOException {
        // 从文件中读取数据
        BufferedReader bufferedReader = new BufferedReader(new FileReader("input.txt"));
        String line = bufferedReader.readLine();
        List<Double> c = Arrays.stream(line.split(" ")).map(Double::valueOf).collect(Collectors.toList());
        List<Double> b = new ArrayList<>();
        List<List<Double>> xs = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            List<Double> list = Arrays.stream(line.split(" ")).map(Double::valueOf).collect(Collectors.toList());
            b.add(list.get(list.size() - 1));
            list.remove(list.size() - 1);
            xs.add(list);
        }
        int m = xs.size();
        int n = c.size();
        List<Integer> xb = new ArrayList<>();
        for (int i = n - m; i < n; i++) {
            xb.add(i);
        }

        // 初始化单纯形表
        Table table = new Table(m, n, c, xs, b, xb);
        table.printTable();
        while (true) {
            // 判断是否是最优解
            if (table.isOptimal()) {
                // 输出最优解
                table.printResult();
                return;
            }
            // 获取入基变量
            Integer inVariable = table.getInVariable();
            // 获取离基变量
            Integer outVariable = table.getOutVariable(inVariable);
            if (outVariable == null) {
                System.out.println("无有限最优解");
                return;
            }
            // 变换
            table.updateTable(outVariable, inVariable);
            table.printTable();
        }
    }
}
