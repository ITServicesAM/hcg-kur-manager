package itservicesam.android.hcg_kur_manager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private BodyDataDao bodyDataDao;
    private BodyData bodyData;
    //    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private DateFormat sdf = SimpleDateFormat.getDateInstance();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.coordinatorLayoutDetails)
    CoordinatorLayout coordinatorLayoutDetails;

    @BindView(R.id.textViewDatum)
    TextView dateTextView;

    @BindView(R.id.textViewGewicht)
    EditText weightTextView;

    @BindView(R.id.textViewStomach)
    EditText stomachTextView;

    @BindView(R.id.textViewStandBauch)
    TextView textViewStandBauch;

    @BindView(R.id.textViewLeftThigh)
    EditText leftThighTextView;

    @BindView(R.id.textViewStandLiOberSc)
    TextView textViewStandLiOberSc;

    @BindView(R.id.textViewRightThigh)
    EditText rightThighTextView;

    @BindView(R.id.textViewStandReOberSc)
    TextView textViewStandReOberSc;

    @BindView(R.id.textViewChest)
    EditText chestTextView;

    @BindView(R.id.textViewStandBrust)
    TextView textViewStandBrust;

    @BindView(R.id.textViewButt)
    EditText buttTextView;

    @BindView(R.id.textViewStandPo)
    TextView textViewStandPo;

    @BindView(R.id.buttonSave)
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("ITEM_ID")) {
            finish();
        } else {
            bodyDataDao = ((App) getApplication()).getDaoSession().getBodyDataDao();
            bodyData = bodyDataDao.load(intent.getLongExtra("ITEM_ID", -1L));

            int bauchUmfang = bodyData.getBauchUmfang();
            stomachTextView.setText(bauchUmfang == -1 ? "" : String.valueOf(bauchUmfang));
            int liOberScUmfang = bodyData.getOberschenkelUmfangLinks();
            leftThighTextView.setText(liOberScUmfang == -1 ? "" : String.valueOf(liOberScUmfang));
            int reOberScUmfang = bodyData.getOberschenkelUmfangRechts();
            rightThighTextView.setText(reOberScUmfang == -1 ? "" : String.valueOf(reOberScUmfang));
            int brustUmfang = bodyData.getBrustUmfang();
            chestTextView.setText(brustUmfang == -1 ? "" : String.valueOf(brustUmfang));
            int poUmfang = bodyData.getPoUmfang();
            buttTextView.setText(poUmfang == -1 ? "" : String.valueOf(poUmfang));

            List<BodyData> data = bodyDataDao.loadAll();
            for (int i = (data.size() - 1); i >= 0; i--) {
                BodyData tmp = data.get(i);
                if (tmp.getBauchUmfang() > -1
                        && !tmp.getDate().equals(bodyData.getDate())
                        && TextUtils.isEmpty(textViewStandBauch.getText())) {
                    bauchUmfang = tmp.getBauchUmfang();
                    textViewStandBauch.setText(
                            String.format("Stand %s: %scm", sdf.format(tmp.getDate()), bauchUmfang));
                }
                if (tmp.getOberschenkelUmfangLinks() > -1
                        && !tmp.getDate().equals(bodyData.getDate())
                        && TextUtils.isEmpty(textViewStandLiOberSc.getText())) {
                    liOberScUmfang = tmp.getOberschenkelUmfangLinks();
                    textViewStandLiOberSc.setText(
                            String.format("Stand %s: %scm", sdf.format(tmp.getDate()), liOberScUmfang));
                }
                if (tmp.getOberschenkelUmfangRechts() > -1
                        && !tmp.getDate().equals(bodyData.getDate())
                        && TextUtils.isEmpty(textViewStandReOberSc.getText())) {
                    reOberScUmfang = tmp.getOberschenkelUmfangRechts();
                    textViewStandReOberSc.setText(
                            String.format("Stand %s: %scm", sdf.format(tmp.getDate()), reOberScUmfang));
                }
                if (tmp.getBrustUmfang() > -1
                        && !tmp.getDate().equals(bodyData.getDate())
                        && TextUtils.isEmpty(textViewStandBrust.getText())) {
                    brustUmfang = tmp.getBrustUmfang();
                    textViewStandBrust.setText(
                            String.format("Stand %s: %scm", sdf.format(tmp.getDate()), brustUmfang));
                }
                if (tmp.getPoUmfang() > -1
                        && !tmp.getDate().equals(bodyData.getDate())
                        && TextUtils.isEmpty(textViewStandPo.getText())) {
                    poUmfang = tmp.getPoUmfang();
                    textViewStandPo.setText(
                            String.format("Stand %s: %scm", sdf.format(tmp.getDate()), poUmfang));
                }
            }

            dateTextView.setText(sdf.format(bodyData.getDate()));
            weightTextView.setText(String.valueOf(bodyData.getGewicht()));
        }
    }

    @OnClick(R.id.buttonSave)
    void save() {
        try {
            bodyData.setDate(sdf.parse(dateTextView.getText().toString()));
            bodyData.setGewicht(Double.parseDouble(weightTextView.getText().toString()));
            if (!TextUtils.isEmpty(stomachTextView.getText().toString()))
                bodyData.setBauchUmfang(Integer.parseInt(stomachTextView.getText().toString()));
            else
                bodyData.setBauchUmfang(-1);
            if (!TextUtils.isEmpty(rightThighTextView.getText().toString()))
                bodyData.setOberschenkelUmfangRechts(Integer.parseInt(rightThighTextView.getText().toString()));
            else
                bodyData.setOberschenkelUmfangRechts(-1);
            if (!TextUtils.isEmpty(leftThighTextView.getText().toString()))
                bodyData.setOberschenkelUmfangLinks(Integer.parseInt(leftThighTextView.getText().toString()));
            else
                bodyData.setOberschenkelUmfangLinks(-1);
            if (!TextUtils.isEmpty(chestTextView.getText().toString()))
                bodyData.setBrustUmfang(Integer.parseInt(chestTextView.getText().toString()));
            else
                bodyData.setBrustUmfang(-1);
            if (!TextUtils.isEmpty(buttTextView.getText().toString()))
                bodyData.setPoUmfang(Integer.parseInt(buttTextView.getText().toString()));
            else
                bodyData.setPoUmfang(-1);
        } catch (NumberFormatException e) {
            showSnackBar("Bitte valide Werte eingeben!");
        } catch (ParseException e) {
            showSnackBar("Das eingegebene Datum ist falsch formatiert!");
        }
        bodyDataDao.update(bodyData);
        showSnackBar("Daten wurden geändert");
    }

    @OnClick(R.id.textViewDatum)
    void setDate() {
        // Use the current date as the default date in the picker
        Calendar cal = Calendar.getInstance();
        cal.setTime(bodyData.getDate());
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showSnackBar(String snackBarString) {
        Snackbar.make(coordinatorLayoutDetails, snackBarString, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        dateTextView.setText(sdf.format(cal.getTime()));
    }
}
