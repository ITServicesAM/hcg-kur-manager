package itservicesam.android.hcg_kur_manager;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import itservicesam.android.hcg_kur_manager.chart.ChartActivity;

/**
 * Created by armin on 26.07.2016.
 */

public class BaseNavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();

        ButterKnife.bind(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        setActiveNavDrawerItem();
    }

    private void setActiveNavDrawerItem() {
        /*Setze aktives Menü*/
        if (navigationView != null) {
            if (this instanceof MainActivity)
                navigationView.getMenu().getItem(0).setChecked(true);
            else if (this instanceof HistoryActivity)
                navigationView.getMenu().getItem(1).setChecked(true);
            else if (this instanceof ChartActivity)
                navigationView.getMenu().getItem(2).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_add_data:
                intent = new Intent(BaseNavDrawerActivity.this, MainActivity.class);
                break;
            case R.id.nav_show_data:
                intent = new Intent(BaseNavDrawerActivity.this, HistoryActivity.class);
                break;
            case R.id.nav_show_data_chart:
                intent = new Intent(BaseNavDrawerActivity.this, ChartActivity.class);
                break;
        }

        drawer.closeDrawers();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final Intent fIntent = intent;
        drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(fIntent);
            }
        }, 300);

        return false;
    }

}
