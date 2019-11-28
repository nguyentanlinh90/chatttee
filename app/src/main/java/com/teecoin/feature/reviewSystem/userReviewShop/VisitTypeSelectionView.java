package com.teecoin.feature.reviewSystem.userReviewShop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.VisitTypeModel;

public class VisitTypeSelectionView extends LinearLayout {

    private VisitTypeModel visitTypeModel;

    public VisitTypeSelectionView(Context context, VisitTypeModel visitTypeModel, VisitTypeSelectionListener listener) {
        super(context);
        this.visitTypeModel = visitTypeModel;
        init(context, listener);
    }


    private void init(Context context, VisitTypeSelectionListener listener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_visit_type_selection_item, this);
        TextView tvName = rootView.findViewById(R.id.view_visit_type_selection_tv_name);
        tvName.setText(visitTypeModel.getName());
        if (listener != null) {
            rootView.setOnClickListener(v -> listener.onVisitTypeSelect(this, this.visitTypeModel));
        }
    }

    public String getVisitTypeValue() {
        return this.visitTypeModel.getValue();
    }
}
