package com.teecoin.feature.couponSystem.user.discover;

import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.couponsystem.CountryCodeModel;
import com.teecoin.utils.EnumMgr;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class CountryAdapter extends RecycleAdapter<CountryCodeModel> {
    public CountryAdapter(LayoutInflater inflater, ArrayList<CountryCodeModel> items, RecycleListener<CountryCodeModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return CountryViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_country_discover;
    }

    @Override
    protected void bindItemView(ItemViewHolder<CountryCodeModel> holder, CountryCodeModel data, int position) {
        if (holder instanceof CountryViewHolder) {
            CountryViewHolder viewHolder = (CountryViewHolder) holder;
            viewHolder.cb_country.setText(String.format("%s (%s)", data.getName(), data.getCountry_code()));
            viewHolder.cb_country.setChecked(data.isSelected());

            viewHolder.cb_country.setOnClickListener(v ->
                    listener.onItemClick(viewHolder.cb_country, data, position, EnumMgr.ClickType.Country));
        }
    }
        public void unSelectedItem(){
            for(int pos=0;pos<items.size();pos++){
                if(items.get(pos).isSelected()){
                    items.get(pos).setSelected(false);
                    notifyItemChanged(pos);
                    return;
                }

            }
        }


}
