package com.teecoin.feature.couponSystem.user.validationCode;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCouponRedeemDataModel;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserRedeemResultScreen;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseResponseModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserCouponValidationCodeScreen extends TCCouponBaseFragment implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    private static final String COUPON_REDEEM_DATA = "COUPON_REDEEM_DATA";
    @BindView(R.id.frg_coupon_user_validation_code_tv_error_qr_code)
    TextView tvErrorQrCode;
    @BindView(R.id.frg_coupon_user_validation_code_iv_shop_avatar)
    ImageView ivShopAvatar;
    @BindView(R.id.frg_coupon_user_validation_code_tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.frg_coupon_user_validation_code_tv_coupon_name)
    TextView tvCouponName;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_1)
    EditText etValidationCode1;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_2)
    EditText etValidationCode2;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_3)
    EditText etValidationCode3;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_4)
    EditText etValidationCode4;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_5)
    EditText etValidationCode5;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_6)
    EditText etValidationCode6;
    @BindView(R.id.frg_coupon_user_validation_code_tv_try_again)
    TextView etValidationTryAgain;
    @BindView(R.id.frg_coupon_user_validation_code_et_validation_code_hidden)
    EditText etValidationCodeHidden;
    Unbinder unbinder;

    boolean isCodeIncorrect = false;
    private UserCouponRedeemDataModel redeemData;

    public static UserCouponValidationCodeScreen newInstance(CouponDetailModel couponDetailModel, boolean isWrongCode) {
        UserCouponValidationCodeScreen screen = new UserCouponValidationCodeScreen();
        Bundle bundle = new Bundle();
        UserCouponRedeemDataModel dataModel = new UserCouponRedeemDataModel(couponDetailModel, isWrongCode);
        bundle.putSerializable(COUPON_REDEEM_DATA, dataModel);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserCouponValidationCodeScreen newInstance(UserCouponRedeemDataModel couponRedeemData, boolean isWrongCode) {
        UserCouponValidationCodeScreen screen = new UserCouponValidationCodeScreen();
        Bundle bundle = new Bundle();
        couponRedeemData.setWrongCode(isWrongCode);
        bundle.putSerializable(COUPON_REDEEM_DATA, couponRedeemData);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserCouponValidationCodeScreen newInstance(CouponCataloguePurchaseResponseModel purchaseResponseModel, boolean isWrongCode) {
        UserCouponValidationCodeScreen screen = new UserCouponValidationCodeScreen();
        Bundle bundle = new Bundle();
        UserCouponRedeemDataModel dataModel = new UserCouponRedeemDataModel(purchaseResponseModel, isWrongCode);
        bundle.putSerializable(COUPON_REDEEM_DATA, dataModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon_user_validation_code, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onBaseResume() {
        showHeader();
        showButtonBackToolbar();
        updateTitleHeader(TCUtils.getString(R.string.validation_code));
        hideFooter();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            redeemData = (UserCouponRedeemDataModel) bundle.getSerializable(COUPON_REDEEM_DATA);
        }
        fillData();
        setFocusedCodeBackground(etValidationCode1);
        setFocus(etValidationCodeHidden);
        showKeyboard(etValidationCodeHidden);
        setCodeListeners();
        if (redeemData != null && redeemData.isWrongCode()) {
            setUIWhenInvalidCode();
        }
    }

    private void fillData() {
        if (redeemData != null) {
            Glide.with(getActiveActivity()).load(TCUtils.isEmpty(redeemData.getVendor().getFeaturedImage()) ?
                    TCUtils.getDrawable(R.drawable.ic_logo_chattee) : redeemData.getVendor().getFeaturedImage()).into(ivShopAvatar);
            tvShopName.setText(redeemData.getVendor().getName());
            tvCouponName.setText(redeemData.getCouponName());
        }
    }

    private void setCodeListeners() {
        etValidationCodeHidden.addTextChangedListener(this);

        etValidationCode1.setOnFocusChangeListener(this);
        etValidationCode2.setOnFocusChangeListener(this);
        etValidationCode3.setOnFocusChangeListener(this);
        etValidationCode4.setOnFocusChangeListener(this);
        etValidationCode5.setOnFocusChangeListener(this);
        etValidationCode6.setOnFocusChangeListener(this);

        etValidationCode1.setOnKeyListener(this);
        etValidationCode2.setOnKeyListener(this);
        etValidationCode3.setOnKeyListener(this);
        etValidationCode4.setOnKeyListener(this);
        etValidationCode5.setOnKeyListener(this);
        etValidationCode6.setOnKeyListener(this);
        etValidationCodeHidden.setOnKeyListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!isCodeIncorrect) {
            setDefaultCodeBackground(etValidationCode1);
            setDefaultCodeBackground(etValidationCode2);
            setDefaultCodeBackground(etValidationCode3);
            setDefaultCodeBackground(etValidationCode4);
            setDefaultCodeBackground(etValidationCode5);
            setDefaultCodeBackground(etValidationCode6);
        }

        if (s.length() == 0) {
            setFocusedCodeBackground(etValidationCode1);
            etValidationCode1.setText("");
            if (isCodeIncorrect) {
                isCodeIncorrect = false;
                setCodeIncorrectCodeBackground(etValidationCode1);
            }
        } else if (s.length() == 1) {
            setFocusedCodeBackground(etValidationCode2);
            etValidationCode1.setText(s.charAt(0) + "");
            etValidationCode2.setText("");
            etValidationCode3.setText("");
            etValidationCode4.setText("");
            etValidationCode5.setText("");
            etValidationCode6.setText("");
            if (etValidationTryAgain.getVisibility() == View.VISIBLE) {
                etValidationTryAgain.setVisibility(View.GONE);
            }
            if (tvErrorQrCode.getVisibility() == View.VISIBLE) {
                tvErrorQrCode.setVisibility(View.GONE);
            }
        } else if (s.length() == 2) {
            setFocusedCodeBackground(etValidationCode3);
            etValidationCode2.setText(s.charAt(1) + "");
            etValidationCode3.setText("");
            etValidationCode4.setText("");
            etValidationCode5.setText("");
            etValidationCode6.setText("");
        } else if (s.length() == 3) {
            setFocusedCodeBackground(etValidationCode4);
            etValidationCode3.setText(s.charAt(2) + "");
            etValidationCode4.setText("");
            etValidationCode5.setText("");
            etValidationCode6.setText("");
        } else if (s.length() == 4) {
            setFocusedCodeBackground(etValidationCode5);
            etValidationCode4.setText(s.charAt(3) + "");
            etValidationCode5.setText("");
            etValidationCode6.setText("");
        } else if (s.length() == 5) {
            setFocusedCodeBackground(etValidationCode6);
            etValidationCode5.setText(s.charAt(4) + "");
            etValidationCode6.setText("");
        } else if (s.length() == 6) {
            etValidationCode6.setText(s.charAt(5) + "");
            hideKeyBoardEditText();
            getActiveActivity().onBackPressed();
            if (redeemData != null && redeemData.getVendorCodeRedeemModel() != null) {
                redeemData.getVendorCodeRedeemModel().setVendor_code(etValidationCodeHidden.getText().toString());
                addFragment(UserRedeemResultScreen.getInstance(redeemData));
            }
        }
    }

    private void setUIWhenInvalidCode() {
        isCodeIncorrect = true;

        setCodeIncorrectCodeBackground(etValidationCode1);
        setCodeIncorrectCodeBackground(etValidationCode2);
        setCodeIncorrectCodeBackground(etValidationCode3);
        setCodeIncorrectCodeBackground(etValidationCode4);
        setCodeIncorrectCodeBackground(etValidationCode5);
        setCodeIncorrectCodeBackground(etValidationCode6);

        etValidationCode1.setText("");
        etValidationCode2.setText("");
        etValidationCode3.setText("");
        etValidationCode4.setText("");
        etValidationCode5.setText("");
        etValidationCode6.setText("");

        etValidationCodeHidden.setText("");

        etValidationTryAgain.setVisibility(View.VISIBLE);

        tvErrorQrCode.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.frg_coupon_user_validation_code_et_validation_code_1:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_2:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_3:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_4:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_5:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;

            case R.id.frg_coupon_user_validation_code_et_validation_code_6:
                if (hasFocus) {
                    setFocus(etValidationCodeHidden);
                    showKeyboard(etValidationCodeHidden);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.frg_coupon_user_validation_code_et_validation_code_hidden:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (etValidationCodeHidden.getText().length() == 6)
                            etValidationCode6.setText("");
                        else if (etValidationCodeHidden.getText().length() == 5)
                            etValidationCode5.setText("");
                        else if (etValidationCodeHidden.getText().length() == 4)
                            etValidationCode4.setText("");
                        else if (etValidationCodeHidden.getText().length() == 3)
                            etValidationCode3.setText("");
                        else if (etValidationCodeHidden.getText().length() == 2)
                            etValidationCode2.setText("");
                        else if (etValidationCodeHidden.getText().length() == 1)
                            etValidationCode1.setText("");

                        if (etValidationCodeHidden.length() > 0)
                            etValidationCodeHidden.setText(etValidationCodeHidden.getText().subSequence(0, etValidationCodeHidden.length() - 1));

                        return true;
                    }

                    break;
                default:
                    return false;
            }
        }

        return false;
    }

    public void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private void setDefaultCodeBackground(EditText editText) {
        editText.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gray_border_0_radius));
    }

    private void setFocusedCodeBackground(EditText editText) {
        editText.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gold_border_0_radius));
    }

    private void setCodeIncorrectCodeBackground(EditText editText) {
        editText.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_0_radius));
    }
}
