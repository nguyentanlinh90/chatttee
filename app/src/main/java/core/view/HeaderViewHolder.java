package core.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class HeaderViewHolder<T> extends RecyclerView.ViewHolder {

    private T data;

    public HeaderViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
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
}
