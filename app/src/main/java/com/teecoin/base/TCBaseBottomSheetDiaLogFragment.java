package com.teecoin.base;

import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

public class TCBaseBottomSheetDiaLogFragment extends BottomSheetDialogFragment {


    protected void deselectView(View... views) {
        for (View v : views) {
            v.setSelected(false);
        }
    }
}
