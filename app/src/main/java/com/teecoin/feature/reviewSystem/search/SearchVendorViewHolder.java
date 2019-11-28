package com.teecoin.feature.reviewSystem.search;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.SuggestItemModel;
import core.view.ItemViewHolder;

public class SearchVendorViewHolder extends ItemViewHolder<SuggestItemModel> {

    @BindView(R.id.item_suggest_tv_suggest)
    TextView tvSuggest;

    public SearchVendorViewHolder(View itemView) {
        super(itemView);
    }

}
