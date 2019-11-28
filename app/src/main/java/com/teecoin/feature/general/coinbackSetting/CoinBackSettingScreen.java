package com.teecoin.feature.general.coinbackSetting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.teecoin.R;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCSuccessfulDialog;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.Vendor;
import com.teecoin.model.walletsystem.CoinBackPercentageModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopUpdateCoinBackRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.CustomRangeInputFilter;
import com.teecoin.ui.DecimalDigitsInputFilter;
import com.teecoin.ui.SwitchButton;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class CoinBackSettingScreen extends TCBaseFragment {
    @BindView(R.id.frg_coinback_setting_et_coinback)
    EditText et_coin_back;
    @BindView(R.id.frg_coinback_setting_sw_coinback)
    SwitchButton sw_coin_back;
    @BindView(R.id.ll_bt_update)
    View v_update;

    private Vendor vendor;

    public static CoinBackSettingScreen getInstance() {
        return new CoinBackSettingScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coinback_setting, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        updateTitleHeader(TCUtils.getString(R.string.coinback_setting).toUpperCase());
    }

    @Override
    public void onBindView() {
        registerSingleClick(R.id.ll_bt_update);

        vendor = RealmController.getInstance().getData(Vendor.class);
        et_coin_back.setText(TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT, vendor.getCoinback_percentage()));
        et_coin_back.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2), new CustomRangeInputFilter(0, 100)});

        boolean isEnable = TCSharePreferenceManager.getInstance().getBoolean(DataKey.CoinBackSetting);
        sw_coin_back.setChecked(isEnable);
        setUIEditText(isEnable);
        sw_coin_back.setOnCheckedChangeListener((view, isChecked) -> setUIEditText(isChecked));
    }

    private void setUIEditText(boolean isEnable) {
        et_coin_back.setText(isEnable ? TCUtils.formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT, vendor.getCoinback_percentage()) : "0");
        et_coin_back.setEnabled(isEnable);
        et_coin_back.setTextColor(isEnable ? TCUtils.getColor(R.color.c_000000) : TCUtils.getColor(R.color.c_d2d2d2));
        et_coin_back.setBackground(isEnable ? TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius)
                : TCUtils.getDrawable(R.drawable.bg_gray_solid_gray_border_5_radius));
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.ll_bt_update);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        doUpdate();
    }

    private void doUpdate() {
        float coinBackValue = 0;
        if (sw_coin_back.isChecked()) {
            String s = et_coin_back.getText().toString();

            if (TCUtils.isStringNumeric(s)) {
                coinBackValue = Float.parseFloat(s);
                if (coinBackValue < 1 || coinBackValue > 100) {
                    new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.coupon_unsuccessful),
                            TCUtils.getString(R.string.the_minimum_coinback_percentage_must_be_1_and_less_than_100),
                            TCUtils.getString(R.string.text_done)).show();
                    return;
                }
            } else {
                return;
            }
        }

        TCSharePreferenceManager.getInstance().setBoolean(DataKey.CoinBackSetting, sw_coin_back.isChecked());

        CoinBackPercentageModel coinBackPercentageModel = new CoinBackPercentageModel(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, coinBackValue));
        requestApi(new ShopUpdateCoinBackRequest(coinBackPercentageModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {

                    AccountModel accountModel = (AccountModel) response.getResult();
                    if (accountModel.getVendor() != null) {
                        RealmController.getInstance().updateVendor(accountModel.getVendor());

                        new TCSuccessfulDialog(getActiveActivity(),
                                TCUtils.getString(R.string.text_successful),
                                TCUtils.getString(R.string.the_coinback_percentage_has_been_successfully_changed),
                                TCUtils.getString(R.string.text_done), (id, onWhat) -> handleBackPressed()).show();
                    }
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.coupon_unsuccessful),
                        TCUtils.getString(R.string.an_error_has_occurred_and_your_setting_could_not_be_processed_please_try_again_later), TCUtils.getString(R.string.text_done)).show();
            }
        }));
    }
}
