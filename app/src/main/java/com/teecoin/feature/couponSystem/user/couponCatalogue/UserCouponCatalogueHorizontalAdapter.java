package com.teecoin.feature.couponSystem.user.couponCatalogue;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueDataModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponCatalogueListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class UserCouponCatalogueHorizontalAdapter extends RecycleAdapter<UserCouponCatalogueModel> {
    private Context context;
    private boolean showViewAll;
    private RecycleListener<UserCouponCatalogueDataModel> userCouponCatalogueDataListener;

    private String typeSort = EnumMgr.SortByTypeCoupon.Coupons.getValue();

    private RecommendCouponsHorizontalAdapter adapter;

    public UserCouponCatalogueHorizontalAdapter(Context context, LayoutInflater inflater, ArrayList<UserCouponCatalogueModel> items,
                                                boolean showViewAll, RecycleListener<UserCouponCatalogueModel> listener,
                                                RecycleListener<UserCouponCatalogueDataModel> userCouponCatalogueDataListener) {
        super(inflater, items, listener);
        this.context = context;
        this.showViewAll = showViewAll;
        this.userCouponCatalogueDataListener = userCouponCatalogueDataListener;
    }

    private String getTypeSort() {
        return typeSort;
    }

    void setTypeSort(String typeSort) {
        this.typeSort = typeSort;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return UserCouponCatalogueHorizontalViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.view_item_coupon_user_catalogue_horizontal_list;
    }

    @Override
    protected void bindItemView(ItemViewHolder<UserCouponCatalogueModel> holder, UserCouponCatalogueModel data, int position) {
        if (holder instanceof UserCouponCatalogueHorizontalViewHolder) {
            UserCouponCatalogueHorizontalViewHolder viewHolder = (UserCouponCatalogueHorizontalViewHolder) holder;

            viewHolder.view_all.setVisibility(showViewAll ? View.VISIBLE : View.GONE);
            viewHolder.iv_icon.setVisibility(showViewAll ? View.GONE : View.VISIBLE);
            viewHolder.tv_name.setText(data.getName());
            viewHolder.tv_all.setText(String.format("%s (%s)", TCUtils.getString(R.string.text_all), data.getCount()));
            viewHolder.rcv_catalogue_data.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.HORIZONTAL, false));
            if (data.getCatalogues() != null && data.getCatalogues().size() > 0) {
                adapter = new RecommendCouponsHorizontalAdapter(LayoutInflater.from(getActiveActivity()), data.getCatalogues(), false,
                        (view, item, pos, clickType) -> {
                            userCouponCatalogueDataListener.onItemClick(view, item, pos, clickType);
                        }
                );
                viewHolder.rcv_catalogue_data.setAdapter(adapter);
                viewHolder.rcv_catalogue_data.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        if (data.getCatalogues().size() < Integer.parseInt(data.getCount())) {

                            ((TCMainActivity) context).requestApi(new CouponUserGetCouponCatalogueListRequest(data.getId(),
                                    TCUtils.paramsToGetCategory((data.getCatalogues().size() / 10) + 1, data.getId(), ((TCMainActivity) context).getLocation(),
                                            ((TCMainActivity) context).getCountryCodeModel().getCountry_code(), getTypeSort(), null),
                                    new APIResponseListener() {
                                        @Override
                                        public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                                            ArrayList<UserCouponCatalogueDataModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
                                            data.getCatalogues().addAll(list);
                                            viewHolder.rcv_catalogue_data.onLoadMoreComplete();
                                        }

                                        @Override
                                        public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                                        }
                                    }));
                        }
                    }

                    @Override
                    public boolean shouldOverrideRefresh() {
                        return false;
                    }
                });
            }
            viewHolder.view_all.setOnClickListener(v -> listener.onItemClick(viewHolder.view_all, data, position, EnumMgr.ClickType.ViewAll));
        }
    }

    void setPrice() {
        if (null != adapter) {
            adapter.notifyDataSetChanged();
            notifyDataSetChanged();
        }
    }
}