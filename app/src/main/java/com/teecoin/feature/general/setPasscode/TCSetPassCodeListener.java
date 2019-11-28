package com.teecoin.feature.general.setPasscode;

public interface TCSetPassCodeListener {
    void onCloseApp(boolean isClose);

    void onUnClockSuccess(boolean success);

    void onFinishChangePassCode();

    void onRemovePassCode();
}
