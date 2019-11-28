package com.teecoin.javastellarsdk.stellar;

import com.teecoin.javastellarsdk.stellar.model.StellarPayment;

public interface TransactionListener {

    void onReceiveTransaction(StellarPayment stellarPayment);
}
