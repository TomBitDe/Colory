package util;

/** Chart utilities for Colory
 */
public class ChartUtil {
    /** Filename postfix for statistic data */
    static final String STATISTIC_FILE = "ColoryStatistic.csv";

    /** No instance should be possible; suppress default constructor
     */
    private ChartUtil() {
        throw new AssertionError();
    }

    /** Get the complete path and filename where statistic data is saved
     * @return the complete path and filename
     */
    public static String getStatisticPath() {
        StringBuilder path;
        path = new StringBuilder(System.getProperty("user.dir"))
            .append(System.getProperty("file.separator"))
            .append(System.getProperty("user.name"))
            .append(STATISTIC_FILE);

        return path.toString();
    }
}
