package com.teecoin.feature.general.changeLanguage;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class LanguageListAdapter extends RecycleAdapter<LanguageModel> {

    LanguageListAdapter(LayoutInflater inflater, ArrayList<LanguageModel> items, RecycleListener<LanguageModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return LanguageHolder.class;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.view_item_general_language;
    }

    @Override
    protected void bindItemView(ItemViewHolder<LanguageModel> holder, LanguageModel data, int position) {
        if (holder instanceof LanguageHolder) {

            LanguageHolder viewHolder = (LanguageHolder) holder;

            //viewHolder.tv_language_name.setText(TCUtils.getCountryLocal(data.getLanguageCode()));
            //  viewHolder.tv_language_name.setText(data.getLanguageName());
            updateTextView(viewHolder.tv_language_name, data);
            viewHolder.iv_language_selected.setBackground(data.isSelected() ?
                    TCUtils.getDrawable(R.drawable.ic_oval_check_box_selected)
                    : TCUtils.getDrawable(R.drawable.ic_oval_check_box));
        }

    }

    private void updateTextView(TextView textView, LanguageModel model) {
        ArrayList<LanguageModel> list = LanguageModel.getListLanguages();
        if (list != null) {
            for (LanguageModel languageModel : list) {
                if (model.getLanguageCode().equals(languageModel.getLanguageCode())) {
                    textView.setText(languageModel.getLanguageName());
                    return;
                }
            }
        } else {
            textView.setText(model.getLanguageName());
        }
    }


}
