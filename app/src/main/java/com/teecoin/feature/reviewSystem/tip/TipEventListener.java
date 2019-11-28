package com.teecoin.feature.reviewSystem.tip;

import com.teecoin.model.reviewsystem.TipResponseModel;

public interface TipEventListener {

    void onNext(TipModel tipModel);

    void onBack();

    void onClose();

    void onOkNext();

    void tipSuccess(TipResponseModel tipResponseModel);

    void tipFail(String errorMessage);
}
