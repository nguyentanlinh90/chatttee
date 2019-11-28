package core.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class FooterViewHolder <T> extends RecyclerView.ViewHolder {

    private T data;

    public FooterViewHolder(View view) {
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