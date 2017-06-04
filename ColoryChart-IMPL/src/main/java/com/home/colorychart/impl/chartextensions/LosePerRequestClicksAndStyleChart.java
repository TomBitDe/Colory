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
 * Create a Colory Lose per requested Clicks and Style chart
 */
public class LosePerRequestClicksAndStyleChart implements ColoryChart {
    private static final Logger log = Logger.getLogger(LosePerRequestClicksAndStyleChart.class.getName());
    private static final String DEFAULT_CHART_TITLE = "Win per Request and Style";

    private CategoryDataset dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    /**
     * Create a new Lose per requested Clicks and style chart panel with default title
     */
    public LosePerRequestClicksAndStyleChart() {
        dataset = createDataset();
        chart = createChart(dataset, DEFAULT_CHART_TITLE);
        chartPanel = new ChartPanel(chart);
    }

    /**
     * Create a new Lose per request Clicks and style chart panel with a given title
     *
     * @param chartTitle the charts title
     */
    public LosePerRequestClicksAndStyleChart(String chartTitle) {
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

    private static class Attrib {
        private Integer attribCnt;
        private Integer allCnt;

        public Attrib(Integer attribCnt, Integer allCnt) {
            this.attribCnt = attribCnt;
            this.allCnt = allCnt;
        }

        public void incrAttrib() {
            ++attribCnt;
            ++allCnt;
        }

        public void incrAll() {
            ++allCnt;
        }

        public Integer getAttribCnt() {
            return attribCnt;
        }

        public Integer getAllCnt() {
            return allCnt;
        }

        public Integer getAttribPercent() {
            return (attribCnt * 100 / allCnt);
        }

        @Override
        public String toString() {
            return "Attrib{" + "attribCnt=" + attribCnt + ", allCnt=" + allCnt + '}';
        }
    }

    private CategoryDataset createDataset() {
        Map<String, Map<Integer, Attrib>> map = new HashMap<String, Map<Integer, Attrib>>();
        BufferedReader in = null;
        String line;
        String[] column;
        String style;
        Integer requested;
        Integer pushed;

        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(ChartUtil.getStatisticPath()), StandardCharsets.UTF_8));

            while ((line = in.readLine()) != null) {
                column = line.split(";");
                style = column[0];
                requested = Integer.valueOf(column[2]);
                pushed = Integer.valueOf(column[3]);

                if (map.containsKey(style)) {
                    HashMap val = (HashMap) map.get(style);
                    if (requested > pushed) {
                        // Lose
                        if (val.containsKey(requested)) {
                            // Incr lose
                            Attrib attrib = (Attrib) val.get(requested);
                            attrib.incrAttrib();
                            val.put(requested, attrib);
                        }
                        else {
                            // Init
                            val.put(requested, new Attrib(1, 1));
                        }
                    }
                    else {
                        if (val.containsKey(requested)) {
                            // Incr all but not attrib
                            Attrib attrib = (Attrib) val.get(requested);
                            attrib.incrAll();
                            val.put(requested, attrib);
                        }
                        else {
                            // Init
                            val.put(requested, new Attrib(0, 1));
                        }
                    }
                }
                else {
                    Map<Integer, Attrib> val = new HashMap<Integer, Attrib>();

                    if (requested > pushed) {
                        // Lose -> Init
                        val.put(requested, new Attrib(1, 1));
                    }
                    else {
                        val.put(requested, new Attrib(0, 1));
                    }
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
        for (Entry<String, Map<Integer, Attrib>> series : map.entrySet()) {
            for (Entry<Integer, Attrib> category : series.getValue().entrySet()) {
                Attrib attrib = category.getValue();
                log.fine(new StringBuffer(series.getKey())
                        .append(" ")
                        .append(category)
                        .append(" ")
                        .append(attrib.toString())
                        .append(" ").append(attrib.getAttribPercent()).toString());

                result.addValue(attrib.getAttribPercent(), series.getKey(), category.getKey());
            }
        }

        return result;
    }

    private JFreeChart createChart(CategoryDataset dataset, String title) {
        JFreeChart locChart = ChartFactory.createBarChart(
                title, // chart title
                "Requested Clicks", // domain axis label
                "Lose [%]", // range axis label
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

        // set upper bound to 100 %
        rangeAxis.setUpperBound(100.f);

        return locChart;
    }
}
