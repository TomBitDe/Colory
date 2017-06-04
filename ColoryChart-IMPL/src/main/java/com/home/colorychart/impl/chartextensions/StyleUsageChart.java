package com.home.colorychart.impl.chartextensions;

import com.home.colorychart.api.statistics.ColoryChart;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;
import util.ChartUtil;

/**
 * Create a Colory style usage chart
 */
public class StyleUsageChart implements ColoryChart {
    private static final Logger log = Logger.getLogger(StyleUsageChart.class.getName());
    private static final String DEFAULT_CHART_TITLE = "Style Usage";

    private PieDataset dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    public StyleUsageChart() {
        dataset = createDataset();
        chart = createChart(dataset, DEFAULT_CHART_TITLE);
        chartPanel = new ChartPanel(chart);
    }

    public StyleUsageChart(String chartTitle) {
        dataset = createDataset();
        chart = createChart(dataset, chartTitle);
        chartPanel = new ChartPanel(chart);
    }

    @Override
    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    @Override
    public JFreeChart getChart() {
        return chart;
    }

    private PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        Map<String, Integer> map = new HashMap<String, Integer>();
        BufferedReader in = null;
        String line;
        String style;

        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(ChartUtil.getStatisticPath()), StandardCharsets.UTF_8));

            while ((line = in.readLine()) != null) {
                style = line.substring(0, line.indexOf(';'));

                if (map.containsKey(style)) {
                    Integer val = map.get(style);
                    ++val;
                    map.put(style, val);
                }
                else {
                    map.put(style, 1);
                }
            }

            for (Entry<String, Integer> entry : map.entrySet()) {
                result.setValue(entry.getKey(), entry.getValue());
            }
        }
        catch (FileNotFoundException fnfex) {
            log.severe(new StringBuffer("Statistic file not found: ").append(ChartUtil.getStatisticPath()).toString());
        }
        catch (IOException ioex) {
            log.severe(new StringBuffer("IO error while accessing statistic file: ").append(ChartUtil.getStatisticPath()).toString());
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ioex) {
                    log.severe("File close FAILED");
                }
            }
        }

        return result;
    }

    private JFreeChart createChart(PieDataset dataset, String title) {
        JFreeChart locChart = ChartFactory.createPieChart3D(
                title, // chart title
                dataset, // data
                true, // include legend
                true,
                false);

        PiePlot3D plot = (PiePlot3D) locChart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.8f);

        return locChart;
    }
}
