package com.teecoin.feature.reviewSystem.detailReview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.model.reviewsystem.ImageModel;
import com.teecoin.model.reviewsystem.TipDetailModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetTipHistoryDetailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class UserReviewDetailScreen extends TCReviewBaseFragment {
    private static final String TRANSACTION_DETAIL_MODEL = "TransactionDetailRealmModel";
    private static final String PUSH_NOTIFICATION_MODEL = "PushNotificationModel";
    @BindView(R.id.ll_container)
    View ll_container;
    @BindView(R.id.frg_history_detail_tv_rate)
    TextView tv_rate;
    @BindView(R.id.frg_history_detail_tv_payment_id)
    TextView tv_payment_id;
    @BindView(R.id.frg_history_detail_tv_shop_name)
    TextView tv_shop_name;
    @BindView(R.id.frg_history_detail_tv_date)
    TextView tv_date;
    @BindView(R.id.frg_user_review_tv_content)
    TextView tv_content;
    @BindView(R.id.frag_list_review_iv_image)
    TCRecyclerView frag_list_review_iv_image;
    @BindView(R.id.frag_list_review_list_tip)
    TCRecyclerView list_tip;
    @BindView(R.id.frag_user_review_rating)
    MaterialRatingBar review_rating;
    @BindView(R.id.total_tip)
    TextView total_tip;
    private TransactionDetailModel transModel;
    private ArrayList<ImageModel> listImage;
    private ImageTipAdapter imageAdapter;

    public static UserReviewDetailScreen getInstance(TransactionDetailModel model) {
        UserReviewDetailScreen screen = new UserReviewDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSACTION_DETAIL_MODEL, model);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserReviewDetailScreen getInstance(PushNotificationModel model) {
        UserReviewDetailScreen screen = new UserReviewDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PUSH_NOTIFICATION_MODEL, model);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_review_detail, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        updateTitleHeader(TCUtils.getString(R.string.text_details).toUpperCase());
        showReadAllNotification(false);
    }

    @Override
    public void onBindView() {
        initRecycler();
        Bundle bundle = getArguments();
        if (bundle != null) {
            transModel = (TransactionDetailModel) bundle.getSerializable(TRANSACTION_DETAIL_MODEL);//todo from last transaction
            PushNotificationModel pushNotificationModel = (PushNotificationModel) bundle.getSerializable(PUSH_NOTIFICATION_MODEL);
            if (transModel == null) {
                if (pushNotificationModel != null) {
                    transModel = new TransactionDetailModel();
                    transModel.setTransaction_id(pushNotificationModel.getData().getTransaction_id());
                    if (!TCUtils.isEmpty(pushNotificationModel.getData().getDestination())) {
                        transModel.setDestination(pushNotificationModel.getData().getDestination());
                    }
                }
            }
            if (transModel != null) {
                requestData();
            }
        }
    }
    private void initRecycler() {
        listImage = new ArrayList<>();
        imageAdapter = new ImageTipAdapter(LayoutInflater.from(getActiveActivity()), listImage, (view, item, position, clickType) -> {

        });
        frag_list_review_iv_image.setAdapter(imageAdapter);
    }


    private void requestData() {
        requestApi(new ReviewUserGetTipHistoryDetailRequest(transModel.getTransaction_id(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                TipDetailModel tipDetailModel = (TipDetailModel) response.getResult();
                fillData(tipDetailModel);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_message), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok), null, null);
            }
        }));
    }

    @SuppressLint("SetTextI18n")
    private void fillData(TipDetailModel tipDetailModel) {
        ll_container.setVisibility(View.VISIBLE);

        review_rating.setRating(Float.parseFloat(tipDetailModel.getRating()));
        review_rating.setIsIndicator(true);

        //load image
        ArrayList<ImageModel> list = tipDetailModel.getImages();
        if (list != null && list.size() > 0) {
            listImage.addAll(list);
            imageAdapter.notifyDataSetChanged();
        }
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        tv_shop_name.setText(tipDetailModel.getShopName());

        //registerSingleClick();
        if (tipDetailModel.getVendorModel() != null) {
            tv_shop_name.setOnClickListener(getSingleClick(tipDetailModel.getVendorModel().getId()));
        }

        tv_rate.setVisibility(View.GONE);
        tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(tipDetailModel.getCreated(),
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HYPHEN_HH_MM)));
        tv_content.setText(tipDetailModel.getComment());

        if (RealmController.getInstance().destinationIsMine(transModel.getDestination())) {
            list_tip.setVisibility(View.VISIBLE);
            tv_payment_id.setText(accountModel.getFull_name());
            total_tip.setText(
                    String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                            TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, tipDetailModel.getTip_amount())));
            if (tipDetailModel.getTips() != null && tipDetailModel.getTips().size() > 0) {
                TipListAdapter adapter = new TipListAdapter(LayoutInflater.from(getActiveActivity()), tipDetailModel.getShopName(), tipDetailModel.getTips(), (view, item, position, clickType) -> {
                    // todo:
                });
                list_tip.setLayoutManager(new LinearLayoutManager(
                        getActiveActivity(), LinearLayoutManager.VERTICAL, false));
                list_tip.setAdapter(adapter);
            }

        } else {
            tv_payment_id.setText(!TCUtils.isEmpty(transModel.getReceiver()) ? transModel.getReceiver() : transModel.getShop_name());
            if (!TCUtils.isEmpty(transModel.getAmount())) {
                total_tip.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                        TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, transModel.getAmount())));
            } else {
                total_tip.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                        TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, tipDetailModel.getTip_amount())));
            }
            list_tip.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_history_detail_tv_shop_name:
                addFragment(VendorScreen.getInstance((String) object));
                break;
        }
    }
}
