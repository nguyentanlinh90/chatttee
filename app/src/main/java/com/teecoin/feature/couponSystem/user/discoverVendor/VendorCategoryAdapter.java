package com.teecoin.feature.couponSystem.user.discoverVendor;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.couponSystem.user.discover.DiscoverVendorAdapter;
import com.teecoin.feature.couponSystem.user.discover.FavouriteAndShareListener;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.reviewsystem.VendorCategoryModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class VendorCategoryAdapter extends RecycleAdapter<VendorCategoryModel> {
    private  DiscoverVendorAdapter    discoverVendorAdapter;
    RecycleListener<VendorModel> vendorModelRecycleListener;
    private boolean checkin;
     public VendorCategoryAdapter(LayoutInflater inflater, ArrayList<VendorCategoryModel> items,boolean checkin, RecycleListener<VendorCategoryModel> listener, RecycleListener<VendorModel> vendorModelRecycleListener) {
        super(inflater, items, listener);
        this.vendorModelRecycleListener =vendorModelRecycleListener;
        this.checkin =checkin;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return VendorCategoryViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_horizontal_list;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindItemView(ItemViewHolder<VendorCategoryModel> holder, VendorCategoryModel data, int position) {
        if (holder instanceof VendorCategoryViewHolder) {

            VendorCategoryViewHolder viewHolder = (VendorCategoryViewHolder) holder;

            viewHolder.tvName.setText(data.getName());

            viewHolder.tvAll.setText(String.format("%s (%s)", TCUtils.getString(R.string.text_all), data.getCount()));

            viewHolder.vAll.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.ViewAll));

               discoverVendorAdapter= new DiscoverVendorAdapter(LayoutInflater.from(getActiveActivity()), data.getVendors(),checkin, (view1, item, position1, clickType) -> {

                if (clickType == EnumMgr.ClickType.Vendor_Favourite) {
                    Fragment fragment =((TCMainActivity)getActiveActivity()).getTopFragment();
                    if(fragment!=null){
                        if(fragment instanceof VendorCategoryScreen){
                            VendorCategoryScreen vendorCategoryScreen = (VendorCategoryScreen) fragment;
                            vendorCategoryScreen.submitFavourite(position1, data.getVendors().get(position1), new FavouriteAndShareListener() {
                                @Override
                                public void submitFavoriteSuccess(int posi, VendorModel vendorModel) {
                                    data.getVendors().get(position1).setFavorite(!data.getVendors().get(position1).isFavorite());
                                    discoverVendorAdapter.notifyItemChanged(position1);
                                    viewHolder.rcvRecommendCoupons.onLoadMoreComplete();

                                }
                            });
                        }
                    }
                } else if (clickType == EnumMgr.ClickType.Vendor_Share) {
                    Fragment fragment =((TCMainActivity)getActiveActivity()).getTopFragment();
                    if(fragment!=null){
                        if(fragment instanceof VendorCategoryScreen){
                            VendorCategoryScreen vendorCategoryScreen = (VendorCategoryScreen) fragment;
                            vendorCategoryScreen.getShareVendor(data.getVendors().get(position1));
                        }
                    }

                } else if (clickType == EnumMgr.ClickType.Vendor_WriteReview) {
                    Fragment fragment =((TCMainActivity)getActiveActivity()).getTopFragment();
                    if(fragment!=null){
                        if(fragment instanceof VendorCategoryScreen){
                            VendorCategoryScreen vendorCategoryScreen = (VendorCategoryScreen) fragment;
                            vendorCategoryScreen.writeReview(data.getVendors().get(position1));
                        }
                    }
                } else {
                    ((TCMainActivity)getActiveActivity()).addFragment(VendorScreen.getInstance(item.getId()));
                }
            });
            viewHolder.rcvRecommendCoupons.setAdapter(discoverVendorAdapter);

        }
    }
}
