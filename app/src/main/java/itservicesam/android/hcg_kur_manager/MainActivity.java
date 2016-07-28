package itservicesam.android.hcg_kur_manager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseNavDrawerActivity implements DatePickerDialog.OnDateSetListener, TextView.OnEditorActionListener, View.OnFocusChangeListener {

    private DecimalFormat df = new DecimalFormat("#.##");
    private DateFormat sdf = SimpleDateFormat.getDateInstance();
    private BodyDataDao bodyDataDao;

    @BindView(R.id.textViewDayTitle)
    TextView dateTextView;

    @BindView(R.id.textViewGewicht)
    EditText weightTextView;

    @BindView(R.id.textViewStomach)
    EditText stomachTextView;

    @BindView(R.id.textViewLeftThigh)
    EditText leftThighTextView;

    @BindView(R.id.textViewRightThigh)
    EditText rightThighTextView;

    @BindView(R.id.textViewChest)
    EditText chestTextView;

    @BindView(R.id.textViewButt)
    EditText buttTextView;

    @BindView(R.id.nest_scrollview)
    NestedScrollView nestedScrollView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.coordinatorLayoutMain)
    CoordinatorLayout coordinatorLayoutMain;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        KeyboardUtil keyboardUtil = new KeyboardUtil(this, findViewById(android.R.id.content));
        //enable it
        keyboardUtil.enable();

//        nestedScrollView.setSmoothScrollingEnabled(true);
//
        weightTextView.setOnFocusChangeListener(this);
        stomachTextView.setOnFocusChangeListener(this);
        leftThighTextView.setOnFocusChangeListener(this);
        rightThighTextView.setOnFocusChangeListener(this);
        chestTextView.setOnFocusChangeListener(this);
        buttTextView.setOnFocusChangeListener(this);

        bodyDataDao = ((App) getApplication()).getDaoSession().getBodyDataDao();

        Calendar cal = Calendar.getInstance();
        setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save the bodydata
                String snackBarString = "Daten wurden erfolgreich gespeichert";
                String dateString = dateTextView.getText().toString();
                if (TextUtils.isEmpty(dateString)) {
                    snackBarString = "Das Datum muss angegeben werden!";
                    showSnackBar(snackBarString);
                    return;
                }
                String gewichtString = weightTextView.getText().toString();
                if (TextUtils.isEmpty(gewichtString)) {
                    snackBarString = "Das Gewicht muss angegeben werden!";
                    showSnackBar(snackBarString);
                    return;
                }
                String bauchString = stomachTextView.getText().toString();
                String linkerOberschenkelString = leftThighTextView.getText().toString();
                String rechterOberschenkelString = rightThighTextView.getText().toString();
                String brustString = chestTextView.getText().toString();
                String poString = buttTextView.getText().toString();

                Date date = null;
                try {
                    date = sdf.parse(dateString);
                } catch (ParseException e) {
                    snackBarString = e.getLocalizedMessage();
                    showSnackBar(snackBarString);
                    return;
                }

                double gewichtDouble;
                try {
                    gewichtDouble = Double.parseDouble(gewichtString.replace(",", "."));
                } catch (NumberFormatException e) {
                    snackBarString = e.getLocalizedMessage();
                    showSnackBar(snackBarString);
                    return;
                }

                int bauchInt = -1;
                if (!TextUtils.isEmpty(bauchString))
                    bauchInt = Integer.parseInt(bauchString);

                int linkerOberschenkelInt = -1;
                if (!TextUtils.isEmpty(linkerOberschenkelString))
                    linkerOberschenkelInt = Integer.parseInt(linkerOberschenkelString);

                int rechterOberschenkelInt = -1;
                if (!TextUtils.isEmpty(rechterOberschenkelString))
                    rechterOberschenkelInt = Integer.parseInt(rechterOberschenkelString);

                int brustInt = -1;
                if (!TextUtils.isEmpty(brustString))
                    brustInt = Integer.parseInt(brustString);

                int poInt = -1;
                if (!TextUtils.isEmpty(poString))
                    poInt = Integer.parseInt(poString);

                BodyData bodyData = new BodyData(null, date, gewichtDouble, bauchInt,
                        linkerOberschenkelInt, rechterOberschenkelInt, brustInt, poInt);
                bodyDataDao.insert(bodyData);

                //alle Felder leeren
                dateTextView.setText(sdf.format(new Date()));
                weightTextView.setText("");
                stomachTextView.setText("");
                leftThighTextView.setText("");
                rightThighTextView.setText("");
                chestTextView.setText("");
                buttTextView.setText("");

                showSnackBar(snackBarString);
            }
        });
    }

    private void showSnackBar(String snackBarString) {
        Snackbar.make(coordinatorLayoutMain, snackBarString, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.textViewDayTitle)
    void enterDate() {
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog;
        if (!TextUtils.isEmpty(dateTextView.getText())) {
            String dtStart = dateTextView.getText().toString();
            try {
                Date date = sdf.parse(dtStart);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                datePickerDialog = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
                datePickerDialog = new DatePickerDialog(this, this, year, month, day);
            }
        } else {
            datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        }

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        setDate(year, month, day);
    }

    private void setDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        dateTextView.setText(sdf.format(cal.getTime()));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("App schließen?");
        builder.setPositiveButton("Schließen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuItemAddTestData) {
            //Testdaten hinzufügen
            List<BodyData> testDaten = new ArrayList<>(3);
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("15072016"), 92.7, 103, 66, 66, 100, 99));
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("16072016"), 92.2, -1, -1, -1, -1, -1));
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("17072016"), 91.8, -1, -1, -1, -1, -1));
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("18072016"), 91.1, -1, -1, -1, -1, -1));
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("19072016"), 91.9, -1, -1, -1, -1, -1));
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("20072016"), 90.5, -1, -1, -1, -1, -1));
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("21072016"), 88.9, 97, 63, 63, 96, 95));

            bodyDataDao.insertInTx(testDaten);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        Log.i("TEST", "onEditorAction: " + actionId);
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            nestedScrollView.scrollBy(0, 200);
        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
            if (behavior != null) {
                behavior.onNestedFling(coordinatorLayoutMain, appBarLayout, null, 0, view.getBottom(), true);
            }
        }
    }
}
