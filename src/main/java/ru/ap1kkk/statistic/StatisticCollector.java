package ru.ap1kkk.statistic;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.NumericShaper;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class StatisticCollector {
    private static final HashMap<Integer, List<Integer>> statisticLoadData = new HashMap<>();
    private static final HashMap<Integer, List<Integer>> statisticProcessedData = new HashMap<>();
    private static final HashMap<Integer, Integer> maxLoadRates = new HashMap<>();

    public static void setMaxLoadRate(int receiverId, int maxLoadRate) {
        if(!maxLoadRates.containsKey(receiverId))
            maxLoadRates.put(receiverId, maxLoadRate);
    }
    public static void addLoadStats(int receiverId, int currentLoad) {
        addStats(receiverId, currentLoad, statisticLoadData);
    }
    public static void addProcessedStats(int receiverId, int currentLoad) {
        addStats(receiverId, currentLoad, statisticProcessedData);
    }

    public static void printStats(StatsMode statsMode) {
        String directoryName = "charts/" + UUID.randomUUID();

        for (Integer key : statisticLoadData.keySet()) {
            System.out.printf("Stats for element(%s):%n", key);

            // Создаем коллекцию данных для XYSeries
            XYSeriesCollection dataset = new XYSeriesCollection();

            // Создаем XYSeries для графика нагрузки
            XYSeries loadSeries = new XYSeries("Load");
            List<Integer> loadValues = statisticLoadData.get(key);
            for (int i = 0; i < loadValues.size(); i++) {
                loadSeries.add(i + 1, loadValues.get(i));
            }
            dataset.addSeries(loadSeries);

            // Создаем XYSeries для графика обработанных данных
            XYSeries processedSeries = new XYSeries("Unprocessed");
            List<Integer> processedValues = statisticProcessedData.get(key);
            for (int i = 0; i < processedValues.size(); i++) {
                processedSeries.add(i + 1, processedValues.get(i));
            }
            dataset.addSeries(processedSeries);

            // Создаем линию
            XYSeries horizontalLine = new XYSeries("Max load level");
            int lineYValue = maxLoadRates.get(key); // Уровень, на котором будет находиться линия
            for (int i = 0; i < loadValues.size(); i++) {
                horizontalLine.add(loadSeries.getX(i).doubleValue(), lineYValue);
            }
            dataset.addSeries(horizontalLine);

            // Создаем график
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Load Statistics", // Заголовок графика
                    "Time", // Подпись оси X
                    "Load", // Подпись оси Y
                    dataset, // Данные для графика
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            // Получаем объект XYPlot (плот графика) и настраиваем толщину линий
            XYPlot plot = chart.getXYPlot();
            plot.getRenderer().setSeriesStroke(0, new BasicStroke(2)); // Настраиваем толщину линии для первой серии данных
            plot.getRenderer().setSeriesPaint(0, Color.CYAN);
            plot.getRenderer().setSeriesStroke(1, new BasicStroke(2)); // Настраиваем толщину линии для второй серии данных
            plot.getRenderer().setSeriesPaint(1, Color.ORANGE);
            plot.getRenderer().setSeriesStroke(2, new BasicStroke(3)); // Настраиваем толщину линии для горизонтальной линии
            plot.getRenderer().setSeriesPaint(2, Color.RED);

            if(statsMode != null && statsMode.equals(StatsMode.FILE)) {
                File directory = new File(directoryName);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Сохраняем график в файл
                try {
                    File chartFile = new File(directory, "chart_for_element_" + key + ".png");
                    int width = 800;
                    int height = 600;
                    BufferedImage chartImage = chart.createBufferedImage(width, height);
                    ImageIO.write(chartImage, "png", chartFile);
                    System.out.println("Chart saved to: " + chartFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Отображаем график в окне
                JFrame frame = new JFrame("Load Statistics for Element " + key);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                ChartPanel chartPanel = new ChartPanel(chart);
                frame.add(chartPanel, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            }
        }
    }

    private static void addStats(int receiverId, int currentLoad, HashMap<Integer, List<Integer>> collection) {
        if(collection.containsKey(receiverId)) {
            List<Integer> list = collection.get(receiverId);
            list.add(currentLoad);
        } else {
            List<Integer> list = new ArrayList<>();
            list.add(currentLoad);
            collection.put(receiverId, list);
        }
    }
}
