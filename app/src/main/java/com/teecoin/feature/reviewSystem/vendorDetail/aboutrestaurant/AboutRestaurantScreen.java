package com.teecoin.feature.reviewSystem.vendorDetail.aboutrestaurant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.model.reviewsystem.ImageModel;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import core.view.RecycleListener;

public class AboutRestaurantScreen extends TCReviewBaseFragment implements RecycleListener<ImageModel> {
    private static final String VENDOR_DETAIL_MODEL = "VENDOR_DETAIL_MODEL";

    @BindView(R.id.frg_vendor_detail_about_restaurant_iv_selected_image)
    ImageView iv_selected_image;

//    @BindView(R.id.frg_vendor_detail_about_restaurant_tv_description)
//    TextView tv_description;

    @BindView(R.id.frg_vendor_detail_about_restaurant_rcv_image_list)
    TCRecyclerView rcv_image_list;

//    @BindView(R.id.frg_vendor_detail_about_restaurant_v_more)
//    View v_more;


    private VendorDetailModel detailModel;

    public static AboutRestaurantScreen newInstance(VendorDetailModel detailModel) {
        AboutRestaurantScreen screen = new AboutRestaurantScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, detailModel);
        screen.setArguments(bundle);
        return screen;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor_detail_about_restaurant, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.about_restaurant));
    }

    @Override
    public void onBindView() {
        if (getArguments() != null) {
            detailModel = (VendorDetailModel) getArguments().getSerializable(VENDOR_DETAIL_MODEL);
            if (detailModel.getImages() != null && detailModel.getImages().size() > 0) {
                Glide.with(getActiveActivity()).load(
                        TCUtils.isEmpty(detailModel.getImages().get(0).getUrl()) ?
                                TCUtils.getDrawable(R.drawable.ic_logo_chattee)
                                : detailModel.getImages().get(0).getUrl())
                        .apply(TCUtils.optionsSquareImage()).into(iv_selected_image);

                rcv_image_list.setAdapter(new
                        AboutRestaurantImageAdapter(
                        LayoutInflater.from(getActiveActivity()), detailModel.getImages(), this));
            }

//            if (!TCUtils.isEmpty(detailModel.getDescription())) {
//                tv_description.setText(detailModel.getDescription().substring(0, 20));
//                v_more.setOnClickListener(v -> tv_description.setText(detailModel.getDescription()));
//                v_more.setVisibility(detailModel.getDescription().length() <= 20 ? View.INVISIBLE : View.VISIBLE);
//            } else {
//                v_more.setVisibility(View.INVISIBLE);
//            }
        }
    }

    @Override
    public void onItemClick(View view, ImageModel item, int position, EnumMgr.ClickType clickType) {
        Glide.with(getActiveActivity()).load(item.getUrl())
                .apply(TCUtils.optionsSquareImage()).into(iv_selected_image);
    }
}