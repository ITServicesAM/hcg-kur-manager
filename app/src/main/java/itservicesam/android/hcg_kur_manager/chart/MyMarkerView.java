package itservicesam.android.hcg_kur_manager.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import butterknife.BindView;
import butterknife.ButterKnife;
import itservicesam.android.hcg_kur_manager.R;

/**
 * Created by armin on 21.07.2016.
 */

public class MyMarkerView extends MarkerView {

    @BindView(R.id.tvContent)
    TextView tvContent;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        ButterKnife.bind(this);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(e.getY()+"kg");
    }

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight()-8;
    }
}
