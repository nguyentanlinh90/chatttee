package core.view;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.teecoin.R;

public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadMoreViewHolder(View itemView) {
        super(itemView);
//        ButterKnife.bind(this, itemView);
        progressBar = itemView.findViewById(R.id.view_recycle_view_load_more_icon_pb_load_more);
    }

    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    public void setEnable(boolean b) {
        itemView.setEnabled(b);
    }
}
