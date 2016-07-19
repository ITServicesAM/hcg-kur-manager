package itservicesam.android.hcg_kur_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity {

    private BodyDataDao bodyDataDao;
    private BodyData bodyData;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

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
            int liOberScUmfang = bodyData.getOberschenkelUmfangLinks();
            int reOberScUmfang = bodyData.getOberschenkelUmfangRechts();
            int brustUmfang = bodyData.getBrustUmfang();
            int poUmfang = bodyData.getPoUmfang();

            List<BodyData> data = null;
            if (bauchUmfang == -1 || liOberScUmfang == -1 || reOberScUmfang == -1 ||
                    brustUmfang == -1 || poUmfang == -1) {
                data = bodyDataDao.loadAll();
            }

            if (bauchUmfang == -1) {
                for (int i = (data.size() - 1); i >= 0; i--) {
                    BodyData tmp = data.get(i);
                    if (tmp.getBauchUmfang() > -1) {
                        bauchUmfang = tmp.getBauchUmfang();
                        textViewStandBauch.setText(
                                String.format("Stand %s: %scm", sdf.format(tmp.getDate()), bauchUmfang));
                        stomachTextView.setText("");
                        break;
                    }
                }
            } else
                stomachTextView.setText(String.valueOf(bauchUmfang));

            if (liOberScUmfang == -1) {
                for (int i = (data.size() - 1); i >= 0; i--) {
                    BodyData tmp = data.get(i);
                    if (tmp.getOberschenkelUmfangLinks() > -1) {
                        liOberScUmfang = tmp.getOberschenkelUmfangLinks();
                        textViewStandLiOberSc.setText(
                                String.format("Stand %s: %scm", sdf.format(tmp.getDate()), liOberScUmfang));
                        leftThighTextView.setText("");
                        break;
                    }
                }
            } else
                leftThighTextView.setText(String.valueOf(liOberScUmfang));

            if (reOberScUmfang == -1) {
                for (int i = (data.size() - 1); i >= 0; i--) {
                    BodyData tmp = data.get(i);
                    if (tmp.getOberschenkelUmfangRechts() > -1) {
                        reOberScUmfang = tmp.getOberschenkelUmfangRechts();
                        textViewStandReOberSc.setText(
                                String.format("Stand %s: %scm", sdf.format(tmp.getDate()), reOberScUmfang));
                        rightThighTextView.setText("");
                        break;
                    }
                }
            } else
                rightThighTextView.setText(String.valueOf(reOberScUmfang));

            if (brustUmfang == -1) {
                for (int i = (data.size() - 1); i >= 0; i--) {
                    BodyData tmp = data.get(i);
                    if (tmp.getBrustUmfang() > -1) {
                        brustUmfang = tmp.getBrustUmfang();
                        textViewStandBrust.setText(
                                String.format("Stand %s: %scm", sdf.format(tmp.getDate()), brustUmfang));
                        chestTextView.setText("");
                        break;
                    }
                }
            } else
                chestTextView.setText(String.valueOf(brustUmfang));

            if (poUmfang == -1) {
                for (int i = (data.size() - 1); i >= 0; i--) {
                    BodyData tmp = data.get(i);
                    if (tmp.getPoUmfang() > -1) {
                        poUmfang = tmp.getPoUmfang();
                        textViewStandPo.setText(
                                String.format("Stand %s: %scm", sdf.format(tmp.getDate()), poUmfang));
                        buttTextView.setText("");
                        break;
                    }
                }
            } else
                buttTextView.setText(String.valueOf(poUmfang));

            dateTextView.setText(sdf.format(bodyData.getDate()));
            weightTextView.setText(String.valueOf(bodyData.getGewicht()));
        }
    }

    @OnClick(R.id.buttonSave)
    void save() {
        try {
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
            showSnackBar("Bitte valide Werte eingeben");
        }
        bodyDataDao.update(bodyData);
        showSnackBar("Daten wurden geändert");
    }

    private void showSnackBar(String snackBarString) {
        Snackbar.make(coordinatorLayoutDetails, snackBarString, Snackbar.LENGTH_LONG).show();
    }

}
