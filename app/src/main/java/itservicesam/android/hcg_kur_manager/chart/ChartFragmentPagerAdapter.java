package itservicesam.android.hcg_kur_manager.chart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by armin on 28.07.2016.
 */

public class ChartFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<ChartTabItem> tabs;

    public ChartFragmentPagerAdapter(FragmentManager fm, List<ChartTabItem> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return ChartFragment.newInstance(tabs.get(position).getChartType());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
