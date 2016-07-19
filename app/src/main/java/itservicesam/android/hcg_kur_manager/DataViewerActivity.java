package itservicesam.android.hcg_kur_manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataViewerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RecyclerViewAdapter.OnItemClickListener, RecyclerViewAdapter.OnContextItemDeleteClickListener {


    private BodyDataDao bodyDataDao;
    private List<BodyData> mList;
    private RecyclerViewAdapter recyclerViewAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_viewer);

        ButterKnife.bind(this);

        bodyDataDao = ((App) getApplication()).getDaoSession().getBodyDataDao();
        mList = bodyDataDao.queryBuilder().orderDesc(BodyDataDao.Properties.Date).list();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(mList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration());
        recyclerViewAdapter.setOnItemClickListener(this);
        recyclerViewAdapter.setOnContextItemDeleteClickListener(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_view_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemDeleteAll:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alle Daten löschen?");
                builder.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAllEntries();
                    }
                });
                builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllEntries() {
        bodyDataDao.deleteAll();
        mList.clear();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_add_data:
                drawer.closeDrawers();
                drawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(DataViewerActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 300);
        }
        return false;
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
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("ITEM_ID", mList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onContextItemDeleteClick(int position) {
        bodyDataDao.delete(mList.get(position));
        mList.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 400);
    }
}
