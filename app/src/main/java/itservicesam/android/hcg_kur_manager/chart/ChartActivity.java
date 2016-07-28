package itservicesam.android.hcg_kur_manager.chart;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itservicesam.android.hcg_kur_manager.App;
import itservicesam.android.hcg_kur_manager.BaseNavDrawerActivity;
import itservicesam.android.hcg_kur_manager.BodyData;
import itservicesam.android.hcg_kur_manager.BodyDataDao;
import itservicesam.android.hcg_kur_manager.R;

public class ChartActivity extends BaseNavDrawerActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        BodyDataDao bodyDataDao = ((App) getApplication()).getDaoSession().getBodyDataDao();
        List<ChartTabItem> tabs = new ArrayList<>();

        int anzahlBauchMaße = 0;
        int anzahlLiOberScMaße = 0;
        int anzahlReOberScMaße = 0;
        int anzahlBrustMaße = 0;
        int anzahlPoMaße = 0;
        for (BodyData bodyData : bodyDataDao.loadAll()) {
            if (bodyData.getBauchUmfang() > -1)
                anzahlBauchMaße++;
            if (bodyData.getOberschenkelUmfangLinks() > -1)
                anzahlLiOberScMaße++;
            if (bodyData.getOberschenkelUmfangRechts() > -1)
                anzahlReOberScMaße++;
            if (bodyData.getBrustUmfang() > -1)
                anzahlBrustMaße++;
            if (bodyData.getPoUmfang() > -1)
                anzahlPoMaße++;
        }

        tabs.add(new ChartTabItem("Gewicht", ChartFragment.CHART_DATA_GEWICHT));
        if (anzahlBauchMaße > 1)
            tabs.add(new ChartTabItem("Bauch", ChartFragment.CHART_DATA_BAUCH));
        if (anzahlLiOberScMaße > 1 )
            tabs.add(new ChartTabItem("Linker Oberschenkel", ChartFragment.CHART_DATA_LINKER_OBERSCHENKEL));
        if (anzahlReOberScMaße > 1 )
            tabs.add(new ChartTabItem("Rechter Oberschenkel", ChartFragment.CHART_DATA_RECHTER_OBERSCHENKEL));
        if (anzahlBrustMaße > 1)
            tabs.add(new ChartTabItem("Brust", ChartFragment.CHART_DATA_BRUST));
        if (anzahlPoMaße > 1)
            tabs.add(new ChartTabItem("Po", ChartFragment.CHART_DATA_PO));

        viewPager.setAdapter(new ChartFragmentPagerAdapter(getSupportFragmentManager(), tabs));
        tabLayout.setupWithViewPager(viewPager, true);
    }
}
