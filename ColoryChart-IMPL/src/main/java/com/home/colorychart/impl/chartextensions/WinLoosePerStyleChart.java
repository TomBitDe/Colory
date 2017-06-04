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
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.util.Rotation;
import org.jfree.util.TableOrder;
import util.ChartUtil;

/**
 * Create a Colory Win/Loose per Style chart
 */
public class WinLoosePerStyleChart implements ColoryChart {
    private static final Logger log = Logger.getLogger(WinLoosePerStyleChart.class.getName());
    private static final String DEFAULT_CHART_TITLE = "Win/Loose per Style";

    private final CategoryDataset dataset;
    private final JFreeChart chart;
    private final ChartPanel chartPanel;

    /**
     * Create a new Win/Loose per style chart panel with default title
     */
    public WinLoosePerStyleChart() {
        dataset = createDataset();
        chart = createChart(dataset, DEFAULT_CHART_TITLE);
        chartPanel = new ChartPanel(chart);
    }

    /**
     * Create a new Win/Loose per style chart panel with a given title
     *
     * @param chartTitle the charts title
     */
    public WinLoosePerStyleChart(String chartTitle) {
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
        Map<String, DataWinLose> map = new HashMap<String, DataWinLose>();
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
                    DataWinLose val = map.get(style);
                    if (requested.equals(pushed)) {
                        // Win
                        val.setWin(val.getWin() + 1);
                    }
                    else {
                        // Loose
                        val.setLose(val.getLose() + 1);
                    }
                    map.put(style, val);
                }
                else {
                    if (requested.equals(pushed)) {
                        // Win
                        map.put(style, new DataWinLose(1, 0));
                    }
                    else {
                        // Loose
                        map.put(style, new DataWinLose(0, 1));
                    }
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

        // Title per Pie Chart
        String[] rowKeys = new String[map.keySet().size()];
        rowKeys = map.keySet().toArray(rowKeys);

        // Legend per Pie Chart
        String[] columnKeys = {"Win", "Loose"};

        // Data for all Pie Charts
        double[][] data = new double[map.keySet().size()][columnKeys.length];
        int idx = 0;
        for (Entry<String, DataWinLose> entry : map.entrySet()) {
            data[idx][0] = entry.getValue().getWin();
            data[idx][1] = entry.getValue().getLose();
            ++idx;
        }

        CategoryDataset result = DatasetUtilities.createCategoryDataset(
                rowKeys,
                columnKeys,
                data
        );

        return result;
    }

    static class DataWinLose {
        Integer win;
        Integer lose;

        public DataWinLose(Integer win, Integer lose) {
            this.win = win;
            this.lose = lose;
        }

        public Integer getWin() {
            return win;
        }

        public void setWin(Integer win) {
            this.win = win;
        }

        public Integer getLose() {
            return lose;
        }

        public void setLose(Integer loose) {
            this.lose = loose;
        }

        @Override
        public String toString() {
            return "DataWinLose{" + "win=" + win + ", lose=" + lose + '}';
        }
    }

    private JFreeChart createChart(CategoryDataset dataset, String title) {
        JFreeChart locChart = ChartFactory.createMultiplePieChart(
                title, // chart title
                dataset, // data
                TableOrder.BY_ROW,
                false, // include legend
                true,
                false);

        MultiplePiePlot plot = (MultiplePiePlot) locChart.getPlot();
        plot.setForegroundAlpha(0.8f);
        final JFreeChart subchart = plot.getPieChart();
        final PiePlot subPlot = (PiePlot) subchart.getPlot();
        subPlot.setStartAngle(290);
        subPlot.setDirection(Rotation.CLOCKWISE);
        subPlot.setInteriorGap(0.05);

        return locChart;
    }
}
