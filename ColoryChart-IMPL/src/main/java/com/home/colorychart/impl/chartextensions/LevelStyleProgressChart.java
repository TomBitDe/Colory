package com.home.colorychart.impl.chartextensions;

import com.home.colorychart.api.statistics.ColoryChart;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import util.ChartUtil;

/**
 * Create a Colory level style progress chart
 */
public class LevelStyleProgressChart implements ColoryChart {
    private static final Logger log = Logger.getLogger(LevelStyleProgressChart.class.getName());
    private static final String DEFAULT_CHART_TITLE = "Level per Style Progress";

    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    /**
     * Create a new level style progress chart panel with default title
     */
    public LevelStyleProgressChart() {
        dataset = createDataset();
        chart = createChart(dataset, DEFAULT_CHART_TITLE);
        chartPanel = new ChartPanel(chart);
    }

    /**
     * Create a new level style progress chart panel with a given title
     *
     * @param chartTitle the charts title
     */
    public LevelStyleProgressChart(String chartTitle) {
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

    private XYSeriesCollection createDataset() {
        XYSeriesCollection result = new XYSeriesCollection();
        XYSeries series;
        BufferedReader in = null;
        String line;
        String style;
        String level;
        int lineNo;

        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(ChartUtil.getStatisticPath()), StandardCharsets.UTF_8));

            while ((line = in.readLine()) != null) {
                style = line.substring(0, line.indexOf(';'));
                level = line.substring(line.indexOf(';') + 1);
                level = level.substring(0, level.indexOf(';'));

                try {
                    series = result.getSeries(style);
                    lineNo = series.getX(series.getItemCount() - 1).intValue();
                    series.add(++lineNo, Integer.valueOf(level));
                }
                catch (UnknownKeyException ukex) {
                    series = new XYSeries(style);
                    series.add(1, Integer.valueOf(level));
                    result.addSeries(series);
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

        return result;
    }

    private JFreeChart createChart(XYSeriesCollection dataset, String title) {
        JFreeChart locChart = ChartFactory.createXYLineChart(
                title, // chart title
                "Game", // X axis title
                "Level", // Y axis tile
                dataset, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true,
                false);

        XYPlot plot = (XYPlot) locChart.getPlot();
        plot.setForegroundAlpha(1.0f);
        plot.setRangeGridlinesVisible(true);
        for (int idx = 0; idx < plot.getSeriesCount(); ++idx) {
            plot.getRenderer().setSeriesStroke(idx, new java.awt.BasicStroke(2.0f));
        }
        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return locChart;
    }
}
