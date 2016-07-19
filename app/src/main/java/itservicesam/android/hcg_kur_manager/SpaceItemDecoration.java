package itservicesam.android.hcg_kur_manager;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by armin on 17.07.2016.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //get resources
        Resources r = parent.getResources();
        float marginBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, r.getDisplayMetrics());
        float marginBottomLastItem = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        float marginTopFirstItem = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

        if (parent.getChildAdapterPosition(view) != state.getItemCount() - 1) {
            outRect.bottom = Math.round(marginBottom);
        } else {
            outRect.bottom = Math.round(marginBottomLastItem);
        }

        if(parent.getChildAdapterPosition(view) == 0)
            outRect.top = Math.round(marginTopFirstItem);
    }
}
