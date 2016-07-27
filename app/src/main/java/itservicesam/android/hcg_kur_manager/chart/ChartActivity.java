package itservicesam.android.hcg_kur_manager.chart;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FormattedStringCache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import itservicesam.android.hcg_kur_manager.App;
import itservicesam.android.hcg_kur_manager.BaseNavDrawerActivity;
import itservicesam.android.hcg_kur_manager.BodyData;
import itservicesam.android.hcg_kur_manager.BodyDataDao;
import itservicesam.android.hcg_kur_manager.R;

public class ChartActivity extends BaseNavDrawerActivity {

    private BodyDataDao bodyDataDao;

    @BindView(R.id.lineChart)
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        bodyDataDao = ((App) getApplication()).getDaoSession().getBodyDataDao();

        setUpChart();
    }

    private void setUpChart() {
        // TODO: 21.07.2016 maybe async is better here!
        final List<BodyData> allData = bodyDataDao.loadAll();
        ArrayList<Entry> valsGewichtData = new ArrayList<>(allData.size());
        for (int i = 0; i < allData.size(); i++) {
            BodyData bodyData = allData.get(i);
            valsGewichtData.add(new Entry((float) i, (float) bodyData.getGewicht()));
        }

        LineDataSet lineDataSet = new LineDataSet(valsGewichtData, "Gewicht");
        lineDataSet.setColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setFillColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setFillAlpha(255);
        lineDataSet.setFillFormatter(new MyFillFormatter());
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(Color.DKGRAY);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        LineData lineData = new LineData(lineDataSet);
        lineData.calcMinMax();

        lineChart.setData(lineData);
        lineChart.setMarkerView(new MyMarkerView(this, R.layout.my_marker_view));
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("");
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
//        xAxis.setGranularity(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // one day in millis
        xAxis.setValueFormatter(new AxisValueFormatter() {

            private FormattedStringCache.Generic<Long, Date> mFormattedStringCache = new FormattedStringCache.Generic<>(new SimpleDateFormat("dd.MM.yy"));

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Long v = (long) value;
//                return mFormattedStringCache.getFormattedValue(new Date(v), v);
                return mFormattedStringCache.getFormattedValue(allData.get((int) value).getDate(), v);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        lineChart.getAxisRight().setEnabled(false);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        lineChart.invalidate();
    }
}
