package com.teecoin.feature.general.secretKey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.base.TCBaseFragment;
import com.teecoin.model.general.AccountModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.SecretKeyEncryption;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class SecretKeyScreen extends TCBaseFragment {
    @BindView(R.id.fragment_secret_key_tv_key)
    TextView tvKey;
    @BindView(R.id.tv_bt_left)
    TextView tvCopy;

    public static SecretKeyScreen getInstance() {
        return new SecretKeyScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_secret_key, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
    }

    @Override
    public void onBindView() {

        updateTitleHeader(TCUtils.getString(R.string.text_secret_key).toUpperCase());

        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);

        if (accountModel != null) {

            tvKey.setText(SecretKeyEncryption.decrypt(accountModel.getSecret_key()));

        }

        tvCopy.setText(TCUtils.getString(R.string.text_copy_address));

        registerSingleClick(tvCopy);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.tv_bt_left:
                doCopy();
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(tvCopy);
    }

    private void doCopy() {

        if (!tvKey.getText().toString().isEmpty()) {

            TCUtils.copyStringToClipboard(tvKey.getText().toString());

            Toast.makeText(getActiveActivity(), TCUtils.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();

        }

    }
}
