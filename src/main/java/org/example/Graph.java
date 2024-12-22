package org.example;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Graph extends JFrame {
    public Graph(Map<String, Integer> map){
        init(map);
    }

    private void init(Map<String, Integer> map) {
        CategoryDataset dataset = createDataset(map);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartpanel = new ChartPanel(chart);
        var padding = 15;
        chartpanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        chartpanel.setBackground(Color.WHITE);
        add(chartpanel);
        pack();
        setTitle("Среднее количество студентов в 10 разных странах");
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "Среднее количество студентов в 10 разных странах",
                "",
                "Количество студентов",
                dataset);
    }

    private CategoryDataset createDataset(Map<String, Integer> map) {
        var dataset = new DefaultCategoryDataset();
        map.forEach((key, value) ->{
            dataset.setValue(value, "students", key);
        });
        return  dataset;
    }
}
