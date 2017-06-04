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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import util.ChartUtil;

/**
 * Create a Colory Frequency of requested Clicks per Style chart
 */
public class FrequencyOfRequestClicksPerStyleChart implements ColoryChart {
    private static final Logger log = Logger.getLogger(FrequencyOfRequestClicksPerStyleChart.class.getName());
    private static final String DEFAULT_CHART_TITLE = "Frequency of requested Clicks per Style";

    private final CategoryDataset dataset;
    private final JFreeChart chart;
    private final ChartPanel chartPanel;

    /**
     * Create a new Frequency of requested Clicks per Style chart panel with default title
     */
    public FrequencyOfRequestClicksPerStyleChart() {
        dataset = createDataset();
        chart = createChart(dataset, DEFAULT_CHART_TITLE);
        chartPanel = new ChartPanel(chart);
    }

    /**
     * Create a new Frequency of requested Clicks per Style chart panel with a given title
     *
     * @param chartTitle the charts title
     */
    public FrequencyOfRequestClicksPerStyleChart(String chartTitle) {
        dataset = createDataset();
        chart = createChart(dataset, chartTitle);
        chartPanel = new ChartPanel(chart);
    }

    /**
     * Supply the chart panel
     *
     * @return the charts panel
     */
    @Override
    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    /**
     * Supply the chart
     *
     * @return the chart
     */
    @Override
    public JFreeChart getChart() {
        return chart;
    }

    private CategoryDataset createDataset() {
        Map<String, Map<Integer, Integer>> map = new HashMap<String, Map<Integer, Integer>>();
        BufferedReader in = null;
        String line;
        String[] column;
        String style;
        Integer requested;

        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(ChartUtil.getStatisticPath()), StandardCharsets.UTF_8));

            while ((line = in.readLine()) != null) {
                column = line.split(";");
                style = column[0];
                requested = Integer.valueOf(column[2]);

                if (map.containsKey(style)) {
                    HashMap val = (HashMap) map.get(style);
                    if (val.containsKey(requested)) {
                        // Incr
                        Integer sum = (Integer) val.get(requested);
                        val.put(requested, ++sum);
                    }
                    else {
                        // Init
                        val.put(requested, 1);
                    }
                }
                else {
                    // Init
                    Map<Integer, Integer> val = new HashMap<Integer, Integer>();
                    val.put(requested, 1);
                    map.put(style, val);
                }
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

        DefaultCategoryDataset result = new DefaultCategoryDataset();

        // Fill the dataset
        for (Entry<String, Map<Integer, Integer>> series : map.entrySet()) {
            for (Entry<Integer, Integer> category : series.getValue().entrySet()) {
                result.addValue(category.getValue(), series.getKey(), category.getKey());
            }
        }

        return result;
    }

    private JFreeChart createChart(CategoryDataset dataset, String title) {
        JFreeChart locChart = ChartFactory.createBarChart(
                title, // chart title
                "Requested Clicks", // domain axis label
                "Frequency", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false);

        CategoryPlot plot = (CategoryPlot) locChart.getPlot();
        plot.setForegroundAlpha(0.8f);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return locChart;
    }
}
