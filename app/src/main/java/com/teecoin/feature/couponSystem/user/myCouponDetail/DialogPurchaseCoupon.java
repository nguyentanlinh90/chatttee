package com.teecoin.feature.couponSystem.user.myCouponDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.model.general.FeeConfigModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class DialogPurchaseCoupon extends BottomSheetDialogFragment {
    @BindView(R.id.dialog_coupon_user_confirm_purchase_coupon_tv_name_coupon)
    TextView tv_name_coupon;
    //    @BindView(R.id.dialog_coupon_user_confirm_purchases_coupon_tv_tec)
//    TextView tv_tec;
    @BindView(R.id.dialog_coupon_user_confirm_purchases_coupon_tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.dialog_coupon_user_confirm_purchase_coupon_iv_loading)
    ImageView ivLoading;
    @BindView(R.id.dialog_coupon_user_confirm_purchases_coupon_tv_confirm)
    TextView tv_confirm;

    private FeeConfigModel feeConfigModel;
    private Context context;
    private GetCouponListener couponListener;
    private CouponDetailModel couponDetailModel;
    private long currTimeOpen;
    private long timeLoad;
    private LoadPriceInterface loadPriceInterface;

    private CountDownTimer countDownTimer;

    private boolean isReload = false;

    public DialogPurchaseCoupon(Context context, CouponDetailModel couponDetailModel, GetCouponListener couponListener, long currTimeOpen, long timeLoad, LoadPriceInterface loadPriceInterface) {
        this.context = context;
        this.couponListener = couponListener;
        this.couponDetailModel = couponDetailModel;
        this.currTimeOpen = currTimeOpen;
        this.timeLoad = timeLoad;
        this.loadPriceInterface = loadPriceInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_coupon_user_confirm_purchases_coupon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });
        feeConfigModel = RealmController.getInstance().getData(FeeConfigModel.class);
        Glide.with(context).asGif().load(R.raw.ic_gift_loading).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(false).into(ivLoading);
        fillData();
        onClick();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        countDownTimer.cancel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void fillData() {
        if (!TCUtils.isEmpty(couponDetailModel.getUsd_price())) {
//            int indexOfTecAmount = TCUtils.getString(R.string.coupon_purchase_coupon_with_tec).indexOf("%s");
            double totalTec = couponDetailModel.getCalculateToTecNoFeeAmount()
                    + couponDetailModel.getFeeAmount()
                    + ((TCMainActivity) context).getBasic_fee_amount();

            String tecAmountString = TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, totalTec);
//            String fullPurchaseMessage = String.format(TCUtils.getString(R.string.coupon_purchase_coupon_with_tec), tecAmountString);

            startTimer(tecAmountString);

//            SpannableString styledText = TCUtils.getStyledText(fullPurchaseMessage, R.style.TCTextViewGoldBold, indexOfTecAmount, indexOfTecAmount + tecAmountString.length());
//            tv_name_coupon.setText(styledText, TextView.BufferType.SPANNABLE);
        }
    }

    private void startTimer(String amount) {
        if (!isReload) {
            timeLoad = timeLoad - (System.currentTimeMillis() - currTimeOpen); // when open dialog timer will be delay, so need minus time delay
        }
        countDownTimer = new CountDownTimer(timeLoad, TCConstant.ONE_SECOND_IN_MILLISECOND) {

            public void onTick(long millisUntilFinished) {
                timeLoad = millisUntilFinished;
                tv_name_coupon.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.purchase_with_s_tec_in_ss), amount, String.valueOf(timeLoad / TCConstant.ONE_SECOND_IN_MILLISECOND))));
            }

            public void onFinish() {
                loadPriceInterface.onLoad();
            }
        };

        countDownTimer.start();
    }

    private void onClick() {
        tv_cancel.setOnClickListener(v -> {
            countDownTimer.cancel();
            dismiss();
        });
        tv_confirm.setOnClickListener(v -> {
            couponListener.onConfirm();
            countDownTimer.cancel();
            dismiss();
        });
    }

    public void reLoadPrice(CouponDetailModel couponModel) {
        isReload = true;
        couponDetailModel = couponModel;
        timeLoad = TCConstant.TIME_COUPON_RELOAD;
        fillData();
    }
}
