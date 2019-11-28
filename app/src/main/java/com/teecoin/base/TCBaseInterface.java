package com.teecoin.base;

import com.teecoin.myapi.apirequest.APIBaseRequest;

import core.base.BaseInterface;

public interface TCBaseInterface extends BaseInterface {

    void requestApi(APIBaseRequest apiBaseRequest);

    boolean isAppUser();
//
//    boolean isChatTeeApp();


}
