package com.teecoin.feature.reviewSystem.favourite;

import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.PostVendorFavoriteModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserSubmitFavoriteRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static com.teecoin.utils.TCUtils.setCuisineList;
import static com.teecoin.utils.TCUtils.setStatusTimeVendor;
import static core.base.BaseApplication.getActiveActivity;

public class FavoriteAndRecentAdapter extends RecycleAdapter<VendorModel> {

    private VendorModel vendorModelDeleted;
    private int positionDeleted = -1;
    private int width;
    private int height;
    FavoriteAndRecentAdapter(LayoutInflater inflater, ArrayList<VendorModel> items, RecycleListener<VendorModel> listener) {
        super(inflater, items, listener);
        width = TCScreenSize.getScreenFavouriteWidth(TCScreenSize.getWidth(getActiveActivity()));
        height =TCScreenSize.getScreenFavouriteHeight(TCScreenSize.getWidth(getActiveActivity()));
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return FavoriteRecentViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_review_main_single_category;
    }

    @Override
    protected void bindItemView(ItemViewHolder<VendorModel> holder, VendorModel data, int position) {

        if (holder instanceof FavoriteRecentViewHolder) {

            FavoriteRecentViewHolder viewHolder = (FavoriteRecentViewHolder) holder;
//            viewHolder.ivImage.getLayoutParams().width = width * 2;
//            viewHolder.ivImage.getLayoutParams().height = height * 2;
            viewHolder.rl_logo.getLayoutParams().width = width;
            viewHolder.rl_logo.requestLayout();
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(data.getFeaturedImage()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : data.getFeaturedImage()).into(viewHolder.ivImage);

            viewHolder.ivFavoriteVendor.setSelected(data.isFavorite());

            viewHolder.tvName.setText(data.getName());

            //viewHolder.materialRatingBar.setRating(data.getRating());
            viewHolder.materialRatingBar.setRating(TCUtils.roundRating(data.getRating()));

            TCUtils.setTextReviewCount(data.getReviewCount(), viewHolder.tvReview);

            viewHolder.tvPriceRange.setText(TCUtils.isEmpty(data.getAvgPrice()) ? "" : data.getAvgPrice() + " ・ ");

            viewHolder.tvDistance.setText(String.format(TCUtils.getString(R.string.distance_to_vendor),
                    TCUtils.formatMoney(TCConstant.ONE_DECIMAL_FORMAT, data.getDistance())));

//            setCuisineList(data, viewHolder.tvCuisine);

            if (null != data.getCuisineList() && 0 < data.getCuisineList().size()) {
                viewHolder.tvCuisine.setVisibility(View.VISIBLE);
                StringBuilder cuisineTypeBuilder = new StringBuilder();
                for (int i = 0; i < data.getCuisineList().size(); i++) {
                    cuisineTypeBuilder.append(data.getCuisineList().get(0)).append(", ");
                }
                viewHolder.tvCuisine.setText(cuisineTypeBuilder.substring(0, cuisineTypeBuilder.length() - 2));
            } else {
                //viewHolder.tvCuisine.setVisibility(View.GONE);
                setCuisineList(data, viewHolder.tvCuisine);
            }

            setStatusTimeVendor(data, viewHolder.tvOpenOrClose, viewHolder.tvOpenOrCloseTime, viewHolder.tvDot);

            viewHolder.ivIsCoupon.setVisibility(data.isHaveCatalogueCoupon() ? View.VISIBLE : View.GONE);

            viewHolder.ivIsCash.setVisibility(data.isHaveCashVoucher() ? View.VISIBLE : View.GONE);

            // todo
            //viewHolder.iv_direction.setOnClickListener(v -> TCUtils.showToast("todo"));
              viewHolder.iv_direction.setVisibility(View.VISIBLE);
              viewHolder.view_tec_check_in.setVisibility(View.GONE);
            viewHolder.iv_direction.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_23);
            viewHolder.iv_direction.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_23);

            viewHolder.ivWriteReviewVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_WriteReview));

            viewHolder.ivShareVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_Share));

            viewHolder.ivFavoriteVendor.setOnClickListener(v -> listener.onItemClick(v, data, position, EnumMgr.ClickType.Vendor_Favourite));
            viewHolder.iv_direction.setOnClickListener(v -> {
                if(!TCUtils.isEmpty(data.getDistance())){
                    TCUtils.gotoDirection(TCUtils.vendorGetLocation(data.getLocation()));
                }
            });
        }

    }

    void setStateIconFavorite(int position, ImageView ivFavorite, boolean isFavorite) {
        items.get(position).setFavorite(isFavorite);
        if(ivFavorite!=null){
            ivFavorite.setSelected(isFavorite);
        }
        //  notifyItemChanged(position);

    }

    void deleteItem(int position) {

        vendorModelDeleted = items.get(position);

        positionDeleted = position;

        showUndoSnackBar(positionDeleted);

        submitFavorite(false);

        items.remove(position);

        notifyItemRemoved(position);

    }

    private void showUndoSnackBar(int pos) {

        View view = getActiveActivity().findViewById(R.id.fragment_favorite_recent_view_seek_bar);

        Snackbar snackbar = Snackbar.make(view, items.get(pos).getName() + " " + TCUtils.getString(R.string.deleted), Snackbar.LENGTH_LONG);

        final FrameLayout snackBarView = (FrameLayout) snackbar.getView();

        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();

        params.height = TCUtils.getDimension(R.dimen.fs_58);

        snackBarView.getChildAt(0).setLayoutParams(params);

        snackbar.setAction(TCUtils.getString(R.string.undo), v -> undoDelete());

        snackbar.show();

    }

    private void undoDelete() {

        items.add(positionDeleted, vendorModelDeleted);
        notifyItemInserted(positionDeleted);

        submitFavorite(true);

    }

    private void submitFavorite(boolean isFavorite) {
        PostVendorFavoriteModel postVendorFavoriteModel
                = new PostVendorFavoriteModel(vendorModelDeleted.getId(), isFavorite);
        ((TCMainActivity) getActiveActivity()).requestApi(new ReviewUserSubmitFavoriteRequest(postVendorFavoriteModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
            }
        }));
    }


}
