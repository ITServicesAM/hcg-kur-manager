package itservicesam.android.hcg_kur_manager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class MainActivity extends BaseNavDrawerActivity implements DatePickerDialog.OnDateSetListener, TextView.OnEditorActionListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        KeyboardUtil keyboardUtil = new KeyboardUtil(this, findViewById(android.R.id.content));
        //enable it
        keyboardUtil.enable();

        nestedScrollView.setSmoothScrollingEnabled(true);

        stomachTextView.setOnEditorActionListener(this);
        chestTextView.setOnEditorActionListener(this);

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

//    @OnClick(R.id.textViewGewicht)
//    void enterDimensionsWheight() {
//        showDialogForInput(weightTextView, 500, 1100, 650, 10.0, "Gewicht in kg auswählen");
//    }
//
//    @OnClick(R.id.textViewStomach)
//    void enterDimensionsStomake() {
//        showDialogForInput(stomachTextView, 50, 120, 70, "Bauchumfang in cm auswählen");
//    }
//
//    @OnClick(R.id.textViewLeftThigh)
//    void enterDimensionsThigh() {
//        showDialogForInput(leftThighTextView, 40, 120, 70, "Oberschenkelumfang in cm auswählen");
//    }
//
//    @OnClick(R.id.textViewRightThigh)
//    void enterDimensionsRightThigh() {
//        showDialogForInput(rightThighTextView, 40, 120, 70, "Oberschenkelumfang in cm auswählen");
//    }
//
//
//    @OnClick(R.id.textViewChest)
//    void enterDimensionsChest() {
//        showDialogForInput(chestTextView, 70, 120, 80, "Brustumfang in cm auswählen");
//    }
//
//    @OnClick(R.id.textViewButt)
//    void enterDimensionsButt() {
//        showDialogForInput(buttTextView, 60, 150, 90, "Poumfang in cm auswählen");
//    }
//
//    private void showDialogForInput(final TextView targetForFinalValue, int min, int max,
//                                    int currentValue, String title) {
//        showDialogForInput(targetForFinalValue, min, max, currentValue, 1.0, title);
//    }
//
//    private void showDialogForInput(final TextView targetForFinalValue, int min, int max,
//                                    int currentValue, final double multiplicator, String title) {
//        CharSequence currentValueString = targetForFinalValue.getText();
//        if (!TextUtils.isEmpty(targetForFinalValue.getText())) {
//            try {
//                currentValue = multiplicator == 1.0 ? Integer.valueOf(currentValueString.toString()) :
//                        (int) multiplicator * Integer.valueOf(currentValueString.toString());
//            } catch (NumberFormatException e) {
//                currentValue = multiplicator == 1.0 ? Integer.valueOf(currentValueString.toString()) :
//                        (int) (multiplicator * Double.parseDouble(currentValueString.toString().replace(",", ".")));
//            }
//        }
//        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
//        final DiscreteSeekBar seek = new DiscreteSeekBar(this);
//        seek.setMin(min);
//        seek.setMax(max);
//        seek.setProgress(currentValue);
//        seek.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
//            @Override
//            public int transform(int value) {
//                return value / (int) multiplicator;
//            }
//
//            @Override
//            public String transformToString(int value) {
//                return df.format(value / multiplicator);
//            }
//
//            @Override
//            public boolean useStringTransform() {
//                return true;
//            }
//        });
//
//        final TextView textView = new TextView(this);
//        textView.setText(multiplicator == 1.0 ? String.valueOf(currentValue) : String.valueOf(currentValue / multiplicator));
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        //get resources
//        Resources r = getResources();
//        float pxMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
//        lp.setMargins(0, Math.round(pxMargin), 0, Math.round(pxMargin));
//        lp.gravity = Gravity.CENTER_HORIZONTAL;
//        textView.setLayoutParams(lp);
//        textView.setGravity(Gravity.CENTER_HORIZONTAL);
//        linearLayout.addView(textView);
//        linearLayout.addView(seek);
//
//        seek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
//            @Override
//            public void onProgressChanged(DiscreteSeekBar seekBar, int progress, boolean fromUser) {
//                textView.setText(df.format(progress / multiplicator));
////                progress = (progress / stepSize) * 10;
////                seek.setProgress(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
//
//            }
//        });
//
//        popDialog.setTitle(title);
//        popDialog.setView(linearLayout);
//
//        popDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                targetForFinalValue.setText(textView.getText());
//                dialogInterface.cancel();
//            }
//        });
//        popDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                targetForFinalValue.setText("");
//            }
//        });
//
//        popDialog.show();
//    }

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
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("16072016"), 92.2, 103, 66, 66, 100, 99));
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("17072016"), 91.5, 103, 66, 66, 100, 99));
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("18072016"), 90.1, 103, 66, 66, 100, 99));
            testDaten.add(new BodyData(null, Utils.getDateFromDbDateString("19072016"), 88.8, 97, 63, 63, 98, 95));

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
}
