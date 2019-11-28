package com.teecoin.feature.reviewSystem.vendorDetailTabPhotos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.ImagesVendor;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.GetImageVendorRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.ui.GridSpacingItemDecoration;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;


public class VendorDetailTabPhotosScreen extends TCReviewBaseFragment {
    private static final String VENDOR_DETAIL_MODEL = "VENDOR_DETAIL_MODEL";
    private VendorDetailModel detailModel;

    @BindView(R.id.frag_vendor_photo_nested_scroll)
    NestedScrollView nested_scroll;
    @BindView(R.id.frg_vendor_detail_photo_rcv_image)
    TCRecyclerView rcv_image;
    @BindView(R.id.frag_vendor_photo_v_back_top)
    View v_back_top;

    private ArrayList<ImagesVendor> listImages;
    private PhotoVendorAdapter adapter;
    public static VendorDetailTabPhotosScreen newInstance(VendorDetailModel detailModel) {
        VendorDetailTabPhotosScreen screen = new VendorDetailTabPhotosScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, detailModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor_detail_tab_photos, container, false);
    }

    @Override
    public void onBindView() {
        super.onBindView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            detailModel = (VendorDetailModel) bundle.getSerializable(VENDOR_DETAIL_MODEL);
        }

        registerSingleClick(
                R.id.frag_vendor_photo_v_back_top
        );
        setupRecyclerView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && null != rcv_image) {
        //    setupRecyclerView();

        }
    }

    private void setupRecyclerView() {
        listImages = new ArrayList<>();
        adapter =new PhotoVendorAdapter(LayoutInflater.from(getActiveActivity()),listImages, (view, item, position, clickType) -> {
            openDialogDetail(listImages,position);
        });
        rcv_image.setAdapter(adapter);
        rcv_image.addItemDecoration(new GridSpacingItemDecoration(2, 10, false));
        rcv_image.setNextPageIndex(1);
     //   rcv_image.se
        nested_scroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                if(!rcv_image.isCanLoadMore()||rcv_image.isLoading())
                    return;
                if(scrollY>=((v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()))*5/6){
                    getData();
                }
            }
//           if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                TCLog.e("nested_scroll: "+scrollY);
//                if (rcv_image.isCanLoadMore())
//                    new Handler().postDelayed(() -> getData(), 300L);
//            }

        });

        getData();

    }
    private void getData(){
        if(detailModel==null)
            return;
        rcv_image.setLoading(true);
        requestApi(new GetImageVendorRequest(detailModel.getId(), String.valueOf(rcv_image.getNextPageIndex()), "1", new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (requestTarget == ReviewRequestTarget.GET_LIST_IMAGES_VENDOR) {
                    ArrayList<ImagesVendor> list = ((BaseResultsResponseModel) response.getResult()).getResults();
                    if(list!=null&&list.size()>0){
                        listImages.addAll(list);
                        rcv_image.onLoadMoreComplete();
                        if(!TCUtils.isEmpty(((BaseResultsResponseModel) response.getResult()).getNext())){
                            rcv_image.setNextPageIndex(rcv_image.getNextPageIndex()+1);
                            rcv_image.setCanLoadMore(true);
                        }else{
                            rcv_image.setCanLoadMore(false);
                        }

                    }
                }
                rcv_image.setLoading(false);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                rcv_image.setCanMore(false);
                rcv_image.setLoading(false);
            }
        }));

    }
    @Override
    public void onBaseDestroyView() {
        super.displayView();
        unregisterSingleClick(R.id.frag_vendor_photo_v_back_top);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_vendor_photo_v_back_top:
                rcv_image.smoothScrollToPosition(0);
                break;
        }
    }

    private void openDialogDetail(ArrayList<ImagesVendor> listImages,int pos){
        addFragment(PhotoVendorDetailScreen.newInstance(listImages,pos));
    }
}

