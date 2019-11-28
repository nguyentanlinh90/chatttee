package com.teecoin.feature.general.updateAccount;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.teecoin.R;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.ProfileModel;
import com.teecoin.model.general.Vendor;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralGetProfileAccountRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletUpdateAccountRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class UpdateAccountScreen extends TCBaseFragment implements APIResponseListener {


    @BindView(R.id.frg_update_user_view_name)
    View vName;

    @BindView(R.id.frg_update_shop_view_shop_name)
    View vShopName;

    @BindView(R.id.fragment_update_info_view_bank)
    View vBank;
    @BindView(R.id.fragment_update_info_v_info_bank)
    View vInfobank;


    @BindView(R.id.fragment_update_info_v_acc_number)
    View vAccountNumber;

    @BindView(R.id.fragment_update_info_view_dob)
    View vDOB;

    @BindView(R.id.fragment_update_info_view_note)
    View vNote;

    @BindView(R.id.fragment_update_info_view_gender)
    View vGender;

    @BindView(R.id.fragment_update_info_view_email)
    View vEmail;


    @BindView(R.id.fragment_update_info_tv_header_phone)
    TextView tv_header_phone;
    @BindView(R.id.fragment_update_info_view_phone)
    View vPhone;

    @BindView(R.id.fragment_update_info_view_all_info)
    View vAllInfo;

    //------

    @BindView(R.id.fragment_update_info_v_user_name)
    View vUserName;

    @BindView(R.id.fragment_update_info_et_user_name)
    EditText etUserName;

    @BindView(R.id.fragment_update_info_iv_clear_user_name)
    ImageView ivClearUserName;

    @BindView(R.id.fragment_update_info_tv_error_name)
    TextView tvErrorName;

    @BindView(R.id.fragment_update_info_v_email)
    View vInfoEmail;


    @BindView(R.id.fragment_update_info_tv_bank)
    TextView tv_bank;

    @BindView(R.id.fragment_update_info_tv_error_bank)
    TextView tvErrorBank;



    @BindView(R.id.fragment_update_info_tv_account_number)
    TextView tvAccontNumber;

    @BindView(R.id.fragment_update_info_et_email)
    EditText etEmail;

    @BindView(R.id.fragment_update_info_iv_clear_email)
    ImageView ivClearEmail;

    @BindView(R.id.fragment_update_info_tv_error_email)
    TextView tvErrorEmail;

    @BindView(R.id.fragment_update_info_et_country_code)
    EditText etCountryCode;

    @BindView(R.id.fragment_update_info_et_phone_number)
    EditText etPhoneNumber;

    @BindView(R.id.fragment_update_info_v_dob)
    View vDob;

    @BindView(R.id.fragment_update_info_tv_dob)
    TextView tvDob;

    @BindView(R.id.fragment_update_info_tv_male)
    TextView tvMale;

    @BindView(R.id.fragment_update_info_tv_female)
    TextView tvFemale;

    @BindView(R.id.frg_update_shop_et_shop_name)
    EditText et_shop_name;

    @BindView(R.id.frg_update_shop_et_notes)
    EditText et_notes;

    @BindView(R.id.frg_update_shop_iv_remove_shop_name)
    ImageView iv_remove_shop_name;

    @BindView(R.id.frg_update_shop_iv_remove_notes)
    ImageView iv_remove_notes;

    @BindView(R.id.ll_bt_update)
    LinearLayout bt_update;

    private String gender = EnumMgr.EnumGender.Unknown.getValue();

    private AccountModel accountModel;
    private Vendor vendor;

    public static UpdateAccountScreen getInstance() {
        return new UpdateAccountScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_info, container, false);
    }

    @Override
    public void onBaseResume() {

        showHeader();

        showButtonBackToolbar();

        showFooter();

      //  showMenuNextBottom();

        updateTitleHeader(isAppUser() ? TCUtils.getString(R.string.account_shop_shop_info_title) : TCUtils.getString(R.string.shop_information));
    }

    @Override
    public void onBindView() {



        fillData();

        initView();

        registerSingleClick(R.id.fragment_update_info_v_dob, R.id.fragment_update_info_tv_male,
                R.id.fragment_update_info_tv_female, R.id.ll_bt_update);
    }

    private void initView() {

        changeUI();

        TCUtils.editTextTextChange(etUserName, ivClearUserName, tvErrorName, vUserName, null);

        TCUtils.setBgWhenFocusView(etUserName, vUserName, vInfoEmail, vDob);
        if(isAppUser()){
            TCUtils.editTextTextChange(etEmail, ivClearEmail, tvErrorEmail, vInfoEmail, null);
            TCUtils.setBgWhenFocusView(etEmail, vInfoEmail, vUserName, vDob);
        }


        etPhoneNumber.setEnabled(false);


        TCUtils.showHideRemoveIconInEditText(et_notes, iv_remove_notes);
        requestApi(new GeneralGetProfileAccountRequest(isAppUser()? GeneralRequestTarget.GET_PROFILE_USER:GeneralRequestTarget.GET_PROFILE_SHOP, this));

    }

    @SuppressLint("SetTextI18n")
    private void fillData() {
        accountModel = RealmController.getInstance().getAccount();
        vendor =RealmController.getInstance().getData(Vendor.class);
        if (accountModel != null) {

            String name = "";

            if (!TCUtils.isEmpty(accountModel.getFull_name())) {

                name = accountModel.getFull_name();

            } else {

                if (!TCUtils.isEmpty(accountModel.getFirst_name())) {
                    name = accountModel.getFirst_name();
                }

                if (!TCUtils.isEmpty(accountModel.getLast_name())) {
                    name = name + " " + accountModel.getLast_name();
                }
            }

            etUserName.setText(name);

            if (!TCUtils.isEmpty(accountModel.getEmail())) {
                etEmail.setText(accountModel.getEmail());
            }

            etPhoneNumber.setText(!TCUtils.isEmpty(accountModel.getPhone()) ? accountModel.getPhone() : "");

            if (!TCUtils.isEmpty(accountModel.getCountry_code())) {
                etCountryCode.setText(accountModel.getCountry_code());
            } else {
                if(isAppUser()){
                    Phonenumber.PhoneNumber phoneNumberProto;
                    try {
                        phoneNumberProto = PhoneNumberUtil.getInstance().parse(accountModel.getPhone(), "SG");
                        if (phoneNumberProto != null)
                            etCountryCode.setText(phoneNumberProto.getCountryCode() + "");
                    } catch (NumberParseException e) {
                        e.printStackTrace();
                    }
                }

            }

            if (!TCUtils.isEmpty(accountModel.getDob())) {
                tvDob.setText(TCDateUtility.convertToCurrentTimeZoneDate(accountModel.getDob(),
                        TCDateUtility.DateFormatDefinition.YYYY_MM_DD_, TCDateUtility.DateFormatDefinition.DD_MM_YYYY));
            }

            if (null != accountModel.getGender()) {
                setBgGender(accountModel.getGender());
            }

            if (!TCUtils.isEmpty(accountModel.getName())) {
                et_shop_name.setText(accountModel.getName());
            }

            if (!TCUtils.isEmpty(accountModel.getNotes())) {
                et_notes.setText(accountModel.getNotes());
            }
        }
        if(vendor!=null&&!isAppUser()){
            etEmail.setText(vendor.getEmail());
            et_shop_name.setText(!TCUtils.isEmpty(vendor.getName())?vendor.getName():"");
            tv_bank.setText(!TCUtils.isEmpty(vendor.getBank_name())?vendor.getBank_name():"");
            tvAccontNumber.setText(!TCUtils.isEmpty(vendor.getBank_account_number())?vendor.getBank_account_number():"");
            //etPhoneNumber.setText(!TCUtils.isEmpty(vendor.getPhone())?vendor.getPhone():"");
            if(!TCUtils.isEmpty(vendor.getPhone())){
                TCUtils.handelShowPhoneNumver(vendor.getPhone(),etCountryCode,etPhoneNumber);

            }
        }

    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);

        switch (v.getId()) {

            case R.id.fragment_update_info_v_dob:

                if (etEmail.hasFocus()) etEmail.clearFocus();

                if (etUserName.hasFocus()) etUserName.clearFocus();

                vDob.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gold_border_5_radius));

                vUserName.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gray_border_5_radius));

                TCDateUtility.openCalendar(tvDob, TCDateUtility.DateFormatDefinition.DD_MM_YYYY);

                break;

            case R.id.fragment_update_info_tv_male:
                setBgGender(EnumMgr.EnumGender.Male.getValue());
                break;
            case R.id.fragment_update_info_tv_female:
                setBgGender(EnumMgr.EnumGender.Female.getValue());
                break;

            case R.id.ll_bt_update:

                doUpdate();

                break;
        }
    }

    private void setBgGender(String type) {

        gender = type;

        tvMale.setBackground(TCUtils.getDrawable(
                EnumMgr.EnumGender.Male.getValue().equals(type) ?
                        R.drawable.bg_white_solid_gold_border_5_radius
                        : R.drawable.bg_gray_solid_no_border_5_radius));

        tvFemale.setBackground(TCUtils.getDrawable(
                EnumMgr.EnumGender.Female.getValue().equals(type) ?
                        R.drawable.bg_white_solid_gold_border_5_radius
                        : R.drawable.bg_gray_solid_no_border_5_radius));

    }

    private void doUpdate() {

        AccountModel accountModelPost = new AccountModel();

        if (TCUtils.isEmpty(etUserName.getText().toString())) {

            etUserName.requestFocus();

            return;
        } else {
            accountModelPost.setFull_name(etUserName.getText().toString());

            accountModel.setFull_name(etUserName.getText().toString());

        }

        if (TCUtils.isEmpty(etEmail.getText().toString())) {

            etEmail.requestFocus();

            return;
        } else {

            accountModelPost.setEmail(etEmail.getText().toString());

            accountModel.setEmail(etEmail.getText().toString());
        }

        if (!TCUtils.isEmpty(tvDob.getText().toString())) {

            accountModelPost.setDob(TCDateUtility.convertToCurrentTimeZoneDate(tvDob.getText().toString(),
                    TCDateUtility.DateFormatDefinition.DD_MM_YYYY, TCDateUtility.DateFormatDefinition.YYYY_MM_DD_));

            accountModel.setDob(accountModelPost.getDob());

        }

        if (!TCUtils.isEmpty(gender)) {
            accountModelPost.setGender(gender);
            accountModel.setGender(gender);
        }

        if (!isAppUser()) {

            if (TCUtils.isEmpty(et_shop_name.getText().toString())) {

                et_shop_name.setError(TCUtils.getString(R.string.validate_enter_shop));

                return;

            } else {

                accountModelPost.setName(et_shop_name.getText().toString());

                accountModel.setName(et_shop_name.getText().toString());
            }

            if (!TCUtils.isEmpty(et_notes.getText().toString())) {

                accountModelPost.setName(et_notes.getText().toString());

                accountModel.setName(et_notes.getText().toString());
            }
        }

        accountModelPost.setUuid(accountModel.getUuid());

        requestApi(new WalletUpdateAccountRequest(accountModelPost, this));

    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

        if (requestTarget == WalletRequestTarget.UPDATE_USER_ACCOUNT
                || requestTarget == WalletRequestTarget.UPDATE_SHOP_ACCOUNT) {

            RealmController.getInstance().updateAccountModel(accountModel);

            hideKeyBoardEditText();

            handleBackPressed();

            showDialogMessageAlert(EnumMgr.EnumAlert.Success.getValue(),
                    TCUtils.getString(R.string.your_information_have_been_successfully_updated));

        }
        else if (requestTarget == GeneralRequestTarget.GET_PROFILE_USER||requestTarget == GeneralRequestTarget.GET_PROFILE_SHOP) {
            ProfileModel profileModel = (ProfileModel) response.getResult();
            if (profileModel != null) {
                AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
                accountModel.updateProfile(profileModel);
                RealmController.getInstance().updateAccountModel(accountModel);
                if(profileModel.getVendor()!=null){
                    RealmController.getInstance().updateVendor(profileModel.getVendor());
                }
            }

            fillData();
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

        if (requestTarget == WalletRequestTarget.UPDATE_SHOP_ACCOUNT || requestTarget == WalletRequestTarget.UPDATE_USER_ACCOUNT) {

            if (null != errorModel.getErrorMessage() && TCUtils.isEmpty(errorModel.getErrorMessage())) {

                handleBackPressed();

                showDialogMessageAlert(EnumMgr.EnumAlert.UnSuccess.getValue(), errorModel.getErrorMessage());

            }
        }
    }
    private void changeUI(){
        vName.setVisibility(isAppUser()?View.VISIBLE:View.GONE);
        vShopName.setVisibility(!isAppUser()?View.VISIBLE:View.GONE);
        vBank.setVisibility(!isAppUser()?View.VISIBLE:View.GONE);
        vAccountNumber.setVisibility(!isAppUser()?View.VISIBLE:View.GONE);
        vDOB.setVisibility(isAppUser()?View.VISIBLE:View.GONE);
        vAllInfo.setVisibility(!isAppUser()?View.VISIBLE:View.GONE);
      //  vNote.setVisibility(!isAppUser()?View.VISIBLE:View.GONE);
        tv_header_phone.setVisibility(isAppUser()?View.VISIBLE:View.GONE);
        vGender.setVisibility(isAppUser()?View.VISIBLE:View.GONE);
        bt_update.setVisibility(isAppUser()?View.VISIBLE:View.GONE);
    }
}
