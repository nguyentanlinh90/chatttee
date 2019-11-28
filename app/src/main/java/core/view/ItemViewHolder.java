package core.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class ItemViewHolder <T> extends RecyclerView.ViewHolder {
    private T data;

    public ItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    public void setEnable(boolean b) {
        itemView.setEnabled(b);
    }
}
