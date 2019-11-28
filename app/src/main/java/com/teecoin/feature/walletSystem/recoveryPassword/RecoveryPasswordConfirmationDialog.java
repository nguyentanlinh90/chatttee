package com.teecoin.feature.walletSystem.recoveryPassword;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.model.general.AccountModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.KeyStoreUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class RecoveryPasswordConfirmationDialog extends TCBaseDialog {

    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.ll_bt_cancel)
    View ll_bt_cancel;
    @BindView(R.id.ll_bt_ok)
    View ll_bt_ok;
    @BindView(R.id.et_pass)
    EditText et_pass;
    @BindView(R.id.iv_remove)
    ImageView iv_remove;

    private EnumMgr.TransactionType type;
    private RecoveryPasswordConfirmationListener listener;
    private Context mContext;

    public RecoveryPasswordConfirmationDialog(Context context, EnumMgr.TransactionType type, RecoveryPasswordConfirmationListener listener) {
        super(context);
        this.mContext = context;
        this.type = type;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_enter_password);
    }

    @Override
    protected void initContentView() {
        iv_close.setVisibility(View.INVISIBLE);
        String text = TCUtils.getString(
                type == EnumMgr.TransactionType.CoinBack ?
                        R.string.dialog_passcode_confirm_text_coinback :
                        type == EnumMgr.TransactionType.Reward ?
                                R.string.dialog_passcode_confirm_text_reward :
                                R.string.dialog_passcode_confirm_text_payment);

        tv_message.setText(text);

        dialog.setOnKeyListener((dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);
    }

    @Override
    protected void onViewClick() {
        ll_bt_cancel.setOnClickListener(v -> passCodeCancel());
        ll_bt_ok.setOnClickListener(v -> checkPassword());
        TCUtils.showHideRemoveIconInEditText(et_pass, iv_remove);
    }

    private void passCodeCancel() {
        dismiss();
        listener.onPasswordConfirmationCancel();
    }

    private void checkPassword() {
        String recoveryPass = et_pass.getText().toString().trim();
        if (TCUtils.isEmpty(recoveryPass)) {
            et_pass.setError(TCUtils.getString(R.string.dialog_passcode_confirm_required_password));
        } else {
            AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
            String decryptedPass = KeyStoreUtility.getInstance().decryptString(accountModel.getPassword());
            if (recoveryPass.equals(decryptedPass)) {
                dismiss();
                listener.onPasswordConfirmationSuccess();
            } else {
                ((TCMainActivity) mContext).showAlertDialog(View.NO_ID,
                        TCUtils.getString(R.string.text_alert),
                        TCUtils.getString(R.string.incorrect_recovery_password),
                        TCUtils.getString(R.string.text_ok),
                        null, null);
            }
        }
    }

}
