package com.teecoin.feature.walletSystem.recoveryPassword;

public interface RecoveryPasswordConfirmationListener {

    void onPasswordConfirmationSuccess();
    void onPasswordConfirmationCancel();
}
