package core.view;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import com.teecoin.utils.EnumMgr;

import java.util.ArrayList;

public abstract class ViewPagerAdapter<T> extends PagerAdapter implements RecycleListener {

    protected final ArrayList<T> items;
    protected final RecycleListener<T> listener;
    protected final LayoutInflater inflater;

    public ViewPagerAdapter(LayoutInflater inflater, ArrayList<T> items, RecycleListener<T> listener) {
        this.inflater = inflater;
        this.listener = listener;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void onItemClick(View view, Object item, int position, EnumMgr.ClickType clickType) {
        Object holder = view.getTag();
        if (holder instanceof ItemViewHolder) {
            if (listener != null)
                listener.onItemClick(view, (T) ((ItemViewHolder) holder).getData(), ((ItemViewHolder) holder).getAdapterPosition(), EnumMgr.ClickType.None);
        }
    }

}
