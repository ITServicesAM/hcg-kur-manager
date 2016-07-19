package itservicesam.android.hcg_kur_manager;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by armin on 16.07.2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<BodyData> mData;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    OnItemClickListener mOnItemClickListener;
    OnContextItemDeleteClickListener mOnContextItemDeleteClickListener;

    public RecyclerViewAdapter(List<BodyData> mData) {
        this.mData = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        // create ViewHolder
        ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textViewTagesTitel.setText("TAG " + (getItemCount() - position));
        viewHolder.textViewDate.setText(
                sdf.format(mData.get(position).getDate()));
        viewHolder.textViewGewicht.setText(
                String.valueOf(mData.get(position).getGewicht()) + "kg");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewTagesTitel;
        public TextView textViewDate;
        public TextView textViewGewicht;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            textViewTagesTitel = (TextView) itemLayoutView.findViewById(R.id.textViewTagLabel);
            textViewDate = (TextView) itemLayoutView.findViewById(R.id.textViewDayTitle);
            textViewGewicht = (TextView) itemLayoutView.findViewById(R.id.textViewGewichtValue);
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem myActionItem = contextMenu.add("Löschen");
            myActionItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            mOnContextItemDeleteClickListener.onContextItemDeleteClick(getAdapterPosition());
            return true;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnContextItemDeleteClickListener(OnContextItemDeleteClickListener onContextItemDeleteClickListener) {
        mOnContextItemDeleteClickListener = onContextItemDeleteClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnContextItemDeleteClickListener {
        public void onContextItemDeleteClick(int position);
    }
}
