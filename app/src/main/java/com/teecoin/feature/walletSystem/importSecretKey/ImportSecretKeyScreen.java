package com.teecoin.feature.walletSystem.importSecretKey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCFailDialog;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.signupaccount.SignUpAccountEmailScreen;
import com.teecoin.feature.walletSystem.recoveryPassword.InputPasswordScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.walletsystem.CheckSecretResponseModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletCheckPublicKeyExistRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ImportSecretKeyScreen extends TCWalletBaseFragment implements APIResponseListener {
    /*@BindView(R.id.frg_import_secret_key_cb_agree)
    CheckBox cb_agree;
    @BindView(R.id.frg_import_secret_key_ll_terms)
    View tv_terms;*/

    @BindView(R.id.fragment_import_wallet_v_input)
    View vInput;

    @BindView(R.id.frg_import_secret_key_et_secret)
    EditText etInputSecret;

    @BindView(R.id.frg_import_secret_key_iv_clear_secret)
    ImageView ivClearSecret;

    @BindView(R.id.fragment_log_in_tv_error_input_secret)
    TextView tvErrorSecret;

    /*@BindView(R.id.frg_import_secret_key_tv_agree_terms)
    TextView tv_agree_terms;*/

    AccountModel accountModel;

    public static ImportSecretKeyScreen getInstance() {
        return new ImportSecretKeyScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_import_wallet, container, false);
    }

    @Override
    public void onBaseResume() {

        showHeader();

        showButtonBackToolbar();

        updateTitleHeader(TCUtils.getString(R.string.text_import_existing_wallet).toUpperCase());

        showFooter();

        showCancelNextBottomView();

        setTextForCancelBottomView(TCUtils.getString(R.string.text_cancel));
        setTextForNextBottomView(TCUtils.getString(R.string.text_next));
    }

    @Override
    public void onBindView() {

        accountModel = new AccountModel();
//        tv_agree_terms.setText(Html.fromHtml(TCUtils.getString(R.string.public_secret_key_screen_agree_to_the_term_of_use)));

        // TCUtils.editTextTextChange(etInputSecret, ivClearSecret, tvErrorSecret, vInput, null);

        etInputSecret.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvErrorSecret.setVisibility(s.length() > 0 ? View.INVISIBLE : View.VISIBLE);
                vInput.setBackground(s.length() > 0 ? TCUtils.getDrawable(R.drawable.bg_transparent_solid_gold_border_5_radius) :
                        TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

            }
        });
        showKeyboard(etInputSecret);


    }

    private void AgreeTermCheck(boolean isChecked) {
        if (isChecked) {
            TCAppFlyerTrackingEvent.getInstance().trackImportWalletCheckedTos();
        }
    }

    private void onClick() {
//        tv_terms.setOnClickListener(v -> openTermsScreen());
//        cb_agree.setOnCheckedChangeListener((v, isChecked) -> AgreeTermCheck(isChecked));
    }


    public void checkSecretKey() {

        if (TCUtils.isEmpty(etInputSecret.getText().toString())) {

            tvErrorSecret.setVisibility(View.VISIBLE);

            return;

        }
        checkPublicKey();
    }

    public void checkPublicKey() {
//        showLoadingDialog();
        String publicKey = stellarGetPublicKey(etInputSecret.getText().toString().trim());

        if (!TCUtils.isEmpty(publicKey)) {

            accountModel.setPublic_key(publicKey);

            requestApi(new WalletCheckPublicKeyExistRequest(accountModel.getPublic_key(), this));

        } else {
//            hideLoadingDialog();
//            showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_warning), TCUtils.getString(R.string.import_wallet_warning_secret_key_wrong), TCUtils.getString(R.string.text_ok), null, null);
            showPopupFail();
        }
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

        if (requestTarget == WalletRequestTarget.CHECK_SECRET_KEY_EXIST) {
            CheckSecretResponseModel checkSecretResponseModel = (CheckSecretResponseModel) response.getResult();
            if (checkSecretResponseModel.isIs_registered()) {
                accountModel.setSecret_key(etInputSecret.getText().toString().trim());
                addFragment(InputPasswordScreen.getInstance(accountModel, etInputSecret.getText().toString().trim()));
            } else {
                accountModel.setSecret_key(etInputSecret.getText().toString());
                TCAppFlyerTrackingEvent.getInstance().trackCreateWalletInit();
                addFragment(SignUpAccountEmailScreen.getInstance(false, accountModel));
            }
            TCAppFlyerTrackingEvent.getInstance().trackImportWalletSuccess();
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

        //showBaseMessage(errorModel.getErrorMessage());
        showBaseMessage(errorModel.getErrorMessage());

//        if (requestTarget == RequestTarget.CHECK_SECRET_KEY_EXIST) {
//            TCAppFlyerTrackingEvent.getInstance().trackImportWalletNotFound();
//            addFragment(SignUpAccountEmailScreen.getInstance(false, accountModel));
//           // showPopupFail();
//        }
    }

    private void showPopupFail() {

        new TCFailDialog(getActiveActivity(), null, TCUtils.getString(R.string.invalid_secret_key),
                TCUtils.getString(R.string.the_secret_key_entered_is_invalid), TCUtils.getString(R.string.try_again)).show();
    }
}
