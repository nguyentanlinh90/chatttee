package com.teecoin.feature.reviewSystem.reviewShopResult;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.couponSystem.user.coupon.UserCouponScreen;
import com.teecoin.feature.reviewSystem.shareSocial.ShareReviewManager;
import com.teecoin.model.general.FeeConfigModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ReviewShopResultScreen extends TCReviewBaseFragment {
    private static final String HAD_FILE = "HadFile";
    private static final String CAN_GET_REVIEW_REWARD = "CAN_GET_REVIEW_REWARD";
//    @BindView(R.id.ll_back)
//    View ll_back;
    @BindView(R.id.frg_review_shop_result_ll_review_without_reward_back)
    View ll_review_without_reward_back;

    @BindView(R.id.frag_review_result_tv_get_more_deals)
    TextView tv_get_more_deals;

    @BindView(R.id.frag_review_result_tv_description)
    TextView tv_description;

    @BindView(R.id.frag_user_review_detail_tv_share_fb)
    TextView tv_share_fb;
    @BindView(R.id.frag_user_review_detail_tv_share_instagram)
    TextView tv_share_instagram;

    @BindView(R.id.frg_review_shop_result_ll_layout_review_with_reward)
    View ll_layout_review_with_reward;

    // facebook
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;


    public static ReviewShopResultScreen getInstance(boolean hadFiles, boolean can_get_review_reward) {
        ReviewShopResultScreen screen = new ReviewShopResultScreen();
        Bundle bundle = new Bundle();
        bundle.putBoolean(HAD_FILE, hadFiles);
        bundle.putBoolean(CAN_GET_REVIEW_REWARD, can_get_review_reward);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_shop_result, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        showFooter();
        showTabMenuBottom();

    }


    @Override
    public void onBindView() {
        initFacebook();
        onClick();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    backPressed();
                    return true;
                }
            }
            return false;
        });

        tv_description.setText(Html.fromHtml(TCUtils.getString(R.string.write_review_result_content)));

    }
    private void onClick() {
//        ll_back.setOnClickListener(v -> backPressed());
        tv_share_fb.setOnClickListener(v -> ShareReviewManager.shareToFacebook(shareDialog, "http://www.grain-traders.com/menu/"));
        registerSingleClick(R.id.frg_review_shop_result_ll_review_without_reward_back,R.id.frag_review_result_tv_get_more_deals);
    }
    private void backPressed() {
        finishWithResult(RESULT_OK, new Intent());
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_review_result_tv_get_more_deals:
                replaceFragment(UserCouponScreen.getInstance(), true);
                break;
            case R.id.frg_review_shop_result_ll_review_without_reward_back:
                backPressed();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frg_review_shop_result_ll_review_without_reward_back,R.id.frag_review_result_tv_get_more_deals);
    }

    private void initFacebook() {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                // share success
                TCUtils.showToast("Share successfull");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
}
