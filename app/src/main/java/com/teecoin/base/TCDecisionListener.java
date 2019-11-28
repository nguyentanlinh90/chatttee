package com.teecoin.base;

public interface TCDecisionListener {

    void onPositiveButtonClicked(int id, Object onWhat);

    void onNegativeButtonClicked(int id, Object onWhat);

    void onNeutralButtonClicked(int id, Object onWhat);

}
