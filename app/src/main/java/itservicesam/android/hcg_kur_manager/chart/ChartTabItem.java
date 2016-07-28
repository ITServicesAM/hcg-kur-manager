package itservicesam.android.hcg_kur_manager.chart;

/**
 * Created by armin on 28.07.2016.
 */

public class ChartTabItem {

    private String title;
    private int chartType;

    public ChartTabItem(String title, int chartType) {
        this.title = title;
        this.chartType = chartType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }
}
