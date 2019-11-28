package com.teecoin.feature.reviewSystem.search;

import android.text.Html;
import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.SuggestItemModel;
import com.teecoin.utils.TCUtils;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import java.util.ArrayList;

public class SearchVendorAdapter extends RecycleAdapter<SuggestItemModel> {

    public SearchVendorAdapter(LayoutInflater inflater, ArrayList<SuggestItemModel> items, RecycleListener<SuggestItemModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return SearchVendorViewHolder.class;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_suggest;
    }

    @Override
    protected void bindItemView(ItemViewHolder<SuggestItemModel> holder, SuggestItemModel data, int position) {

        if (holder instanceof SearchVendorViewHolder) {

            SearchVendorViewHolder viewHolder = (SearchVendorViewHolder) holder;

            if (TCUtils.isEmpty(data.getId())) {
                //when get key search recent
                viewHolder.tvSuggest.setText(data.getOption());
                viewHolder.tvSuggest.setTextColor(TCUtils.getColor(R.color.c_2d2d2d));
            } else {
                viewHolder.tvSuggest.setTextColor(TCUtils.getColor(R.color.c_b2973f));
                viewHolder.tvSuggest.setText(Html.fromHtml(String.format("%s <b><font color = '#000000'>in %s</font></b>",
                        data.getOption(), data.getCategory())));
            }

        }

    }

}
