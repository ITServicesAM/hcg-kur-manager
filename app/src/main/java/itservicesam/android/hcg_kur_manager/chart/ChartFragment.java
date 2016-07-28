package itservicesam.android.hcg_kur_manager.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import itservicesam.android.hcg_kur_manager.App;
import itservicesam.android.hcg_kur_manager.BodyData;
import itservicesam.android.hcg_kur_manager.BodyDataDao;
import itservicesam.android.hcg_kur_manager.R;
import itservicesam.android.hcg_kur_manager.Utils;

/**
 * Created by armin on 28.07.2016.
 */

public class ChartFragment extends Fragment {

    private static final String ARG_CHART_DATEN = "ARG_CHART_DATEN";
    static final int CHART_DATA_GEWICHT = 1;
    static final int CHART_DATA_BAUCH = 2;
    static final int CHART_DATA_LINKER_OBERSCHENKEL = 3;
    static final int CHART_DATA_RECHTER_OBERSCHENKEL = 4;
    static final int CHART_DATA_BRUST = 5;
    static final int CHART_DATA_PO = 6;

    private int mChartDaten;
    private BodyDataDao bodyDataDao;

    @BindView(R.id.lineChart)
    LineChart lineChart;

    static ChartFragment newInstance(int chartDaten) {
        Bundle args = new Bundle();
        args.putInt(ARG_CHART_DATEN, chartDaten);
        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChartDaten = getArguments().getInt(ARG_CHART_DATEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bodyDataDao = ((App) getActivity().getApplication()).getDaoSession().getBodyDataDao();

        setUpChart();
        addChartData();
    }

    private void setUpChart() {
        lineChart.setHardwareAccelerationEnabled(true);
        lineChart.setNoDataTextDescription("Keine Datem zum anzeigen vorhanden");
        lineChart.setNoDataText("NoDataText");
        lineChart.setNoDataTextColor(getResources().getColor(R.color.colorAccent));
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("");
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setExtraRightOffset(32);
        lineChart.setExtraLeftOffset(16);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
    }

    private void addChartData() {
        // TODO: 21.07.2016 maybe async is better here!
        final List<BodyData> allData = bodyDataDao.loadAll();
        ArrayList<Entry> chartValues = new ArrayList<>(allData.size());
        for (int i = 0; i < allData.size(); i++) {
            BodyData bodyData = allData.get(i);
            switch (mChartDaten) {
                case CHART_DATA_GEWICHT:
                    chartValues.add(new Entry((float) i, (float) bodyData.getGewicht()));
                    break;
                case CHART_DATA_BAUCH:
                    chartValues.add(new Entry((float) i,
                            bodyData.getBauchUmfang() == -1 ?
                                    (float) Utils.lastValue(Utils.VALUE_TYPE_BAUCH, allData, bodyData.getDate()) :
                                    (float) bodyData.getBauchUmfang()));
                    break;
                case CHART_DATA_LINKER_OBERSCHENKEL:
                    chartValues.add(new Entry((float) i,
                            bodyData.getOberschenkelUmfangLinks() == -1 ?
                                    (float) Utils.lastValue(Utils.VALUE_TYPE_LI_OBERSCH, allData, bodyData.getDate()) :
                                    (float) bodyData.getOberschenkelUmfangLinks()));
                    break;
                case CHART_DATA_RECHTER_OBERSCHENKEL:
                    chartValues.add(new Entry((float) i,
                            bodyData.getOberschenkelUmfangRechts() == -1 ?
                                    (float) Utils.lastValue(Utils.VALUE_TYPE_RE_OBERSCH, allData, bodyData.getDate()) :
                                    (float) bodyData.getOberschenkelUmfangRechts()));
                    break;
                case CHART_DATA_BRUST:
                    chartValues.add(new Entry((float) i,
                            bodyData.getBrustUmfang() == -1 ?
                                    (float) Utils.lastValue(Utils.VALUE_TYPE_BRUST, allData, bodyData.getDate()) :
                                    (float) bodyData.getBrustUmfang()));
                    break;
                case CHART_DATA_PO:
                    chartValues.add(new Entry((float) i,
                            bodyData.getPoUmfang() == -1 ?
                                    (float) Utils.lastValue(Utils.VALUE_TYPE_PO, allData, bodyData.getDate()) :
                                    (float) bodyData.getPoUmfang()));
                    break;
            }
        }

        LineDataSet lineDataSet = null;
        int einheitsTyp = -1;
        switch (mChartDaten) {
            case CHART_DATA_GEWICHT:
                lineDataSet = new LineDataSet(chartValues, "Gewicht");
                einheitsTyp = MyMarkerView.TYP_KG;
                break;
            case CHART_DATA_BAUCH:
                lineDataSet = new LineDataSet(chartValues, "Bauch");
                einheitsTyp = MyMarkerView.TYP_CM;
                break;
            case CHART_DATA_LINKER_OBERSCHENKEL:
                lineDataSet = new LineDataSet(chartValues, "Linker Oberschenkel");
                einheitsTyp = MyMarkerView.TYP_CM;
                break;
            case CHART_DATA_RECHTER_OBERSCHENKEL:
                lineDataSet = new LineDataSet(chartValues, "Rechter Oberschenkel");
                einheitsTyp = MyMarkerView.TYP_CM;
                break;
            case CHART_DATA_BRUST:
                lineDataSet = new LineDataSet(chartValues, "Brust");
                einheitsTyp = MyMarkerView.TYP_CM;
                break;
            case CHART_DATA_PO:
                lineDataSet = new LineDataSet(chartValues, "Po");
                einheitsTyp = MyMarkerView.TYP_CM;
                break;
        }

        lineChart.setMarkerView(new MyMarkerView(getActivity(), R.layout.my_marker_view, einheitsTyp));
        lineDataSet.setColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setFillColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setFillAlpha(255);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(Color.DKGRAY);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        LineData lineData = new LineData(lineDataSet);
        lineData.calcMinMax();

        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setYOffset(16);
        xAxis.setGranularity(2f);
        xAxis.setValueFormatter(new AxisValueFormatter() {

            private FormattedStringCache.Generic<Long, Date> mFormattedStringCache = new FormattedStringCache.Generic<>(SimpleDateFormat.getDateInstance());

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Long v = (long) value;
                return mFormattedStringCache.getFormattedValue(allData.get((int) value).getDate(), v);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        lineChart.setVisibleXRangeMinimum(3);
        lineChart.animateXY(2000, 2000, Easing.EasingOption.EaseInOutCubic, Easing.EasingOption.EaseInOutCubic);

        //Move to last xVal
        lineChart.moveViewToAnimated(lineData.getXMax(), (float) allData.get(allData.size() - 1).getGewicht(), YAxis.AxisDependency.LEFT, 2000);
    }
}
