package com.teecoin.stellar;

public interface StellarResponseListener<T> {

    void onStellarSuccess(T t);

    void onStellarFail(Throwable t);
}
