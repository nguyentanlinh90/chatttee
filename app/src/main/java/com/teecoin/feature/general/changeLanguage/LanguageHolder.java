package com.teecoin.feature.general.changeLanguage;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class LanguageHolder extends ItemViewHolder<LanguageModel> {

    @BindView(R.id.view_item_general_language_tv_language_name)
    TextView tv_language_name;

    @BindView(R.id.view_item_general_language_iv_language_selected)
    ImageView iv_language_selected;

    public LanguageHolder(View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
    }
}
