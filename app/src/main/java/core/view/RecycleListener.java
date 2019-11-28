package core.view;

import android.view.View;

import com.teecoin.utils.EnumMgr;

public interface RecycleListener<T> {

    void onItemClick(View view, T item, int position, EnumMgr.ClickType clickType);
}
