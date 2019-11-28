package com.teecoin.feature.general.changeLanguage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCGeneralBaseFragment;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletUpdateAccountRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

public class ChangeLanguageScreen extends TCGeneralBaseFragment implements RecycleListener<LanguageModel> {

    @BindView(R.id.frg_change_language_rcv_languages)
    TCRecyclerView rcv_languages;

    LanguageListAdapter adapter;
    ArrayList<LanguageModel> languageModels;

    public static ChangeLanguageScreen getInstance() {
        return new ChangeLanguageScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_language, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.change_language));
        showButtonBackToolbar();
        showFooter();
       // showMenuNextBottom();
    }

    @Override
    public void onBindView() {
        fillData();
        initClickEvent();
    }

    private void fillData() {
        languageModels = ((TCMainActivity) getActiveActivity()).getLanguageList();
        if (languageModels != null && languageModels.size() > 0) {
            rcv_languages.setLayoutManager(new LinearLayoutManager(
                    getActiveActivity(), LinearLayoutManager.VERTICAL, false));
            adapter = new LanguageListAdapter(getLayoutInflater(), languageModels, this);
            rcv_languages.setAdapter(adapter);
        }
    }

    private void initClickEvent() {

    }

    @Override
    public void onItemClick(View view, LanguageModel item, int position, EnumMgr.ClickType clickType) {
        for (LanguageModel model : languageModels) {
            model.setSelected(false);
        }
        item.setSelected(true);
        TCSharePreferenceManager.getInstance().setString(DataKey.SelectedLanguageCode, item.getLanguageCode());
        TCUtils.changeLanguage(item.getLocaleLanguageCode(), item.getLocaleCountryCode());
        refreshFragment(this);
        ((TCMainActivity) getActiveActivity()).refreshBottomLayoutWhenChangeLanguage();
        updateTitleHeader(getString(R.string.change_language));
        updateAccountInfo();
    }

    private void updateAccountInfo() {
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        accountModel.setLanguage(TCUtils.getLanguageCode());
        requestApi(new WalletUpdateAccountRequest(accountModel, false,
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        RealmController.getInstance().updateAccountModel(accountModel);
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                    }
                }));
    }
}

