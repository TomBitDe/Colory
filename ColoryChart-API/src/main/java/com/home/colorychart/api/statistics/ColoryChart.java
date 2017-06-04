package com.home.colorychart.api.statistics;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * Colory charts to display in dialog
 */
public interface ColoryChart {
    /**
     * Supply the chart panel
     *
     * @return the charts panel
     */
    public ChartPanel getChartPanel();

    /**
     * Supply the chart
     *
     * @return the chart
     */
    public JFreeChart getChart();
}
