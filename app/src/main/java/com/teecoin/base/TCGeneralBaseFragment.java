package com.teecoin.base;

import com.teecoin.R;
import com.teecoin.feature.general.terms.TermsOfServiceScreen;
import com.teecoin.feature.walletSystem.paymentThreshold.InputPasswordDialog;
import com.teecoin.feature.walletSystem.paymentThreshold.InputPasswordListener;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import core.base.BaseFragment;

public class TCGeneralBaseFragment extends TCBaseFragment {

    @Override
    public void onBindView() {

    }

    public boolean checkHasPasscode() {
        return !TCUtils.isEmpty(TCSharePreferenceManager.getInstance().getString(DataKey.Passcode));
    }
    public void openTermsScreen(){
        new TermsOfServiceScreen(getActiveActivity(),TCUtils.getString(R.string.terms_of_use), TCConstant.URL_TERMS).show();
    }

    public void inputPassword(BaseFragment fragment, EnumMgr.TypeDialog typeDialog) {
        InputPasswordDialog dialog = new InputPasswordDialog(getActiveActivity(), typeDialog.getValue(), new InputPasswordListener() {
            @Override
            public void onSubmit(String inputPassword) {
                addFragment(fragment);
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show();
    }

}
