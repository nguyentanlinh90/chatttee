package com.teecoin.stellar;

import com.teecoin.javastellarsdk.stellar.StellarProcess;
import com.teecoin.javastellarsdk.stellar.TCGoogleAnalyticEventTrackingListener;
import com.teecoin.javastellarsdk.stellar.exceptions.DestinationNotFoundException;
import com.teecoin.javastellarsdk.stellar.model.StellarPayment;

import org.stellar.sdk.Memo;
import org.stellar.sdk.Transaction;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import rx.Observable;

public class StellarObserver {

    private static StellarObserver instance;

    public synchronized static StellarObserver getInstance() {
        if (instance == null)
            instance = new StellarObserver();
        return instance;
    }

    public Observable<Transaction> getTransaction(
            String sourceSecretSeed,
            String destinationPublicKey,
            String teeCoinAmount,
            String teeCoinFee,
            Memo memo) {
        return makeObservable(getTransactionCallable(sourceSecretSeed, destinationPublicKey, teeCoinAmount, teeCoinFee,memo));
    }

    public Observable<Transaction> getTransaction(
            String sourceSecretSeed,
            String paidAmount,
            String feeAmount,
            String basicFeeAmount,
            String paidAmountWallet,
            String feeAmountWallet,
            String basicFeeAmountWallet) {
        return makeObservable(getTransactionCallable(sourceSecretSeed, paidAmount, feeAmount, basicFeeAmount,paidAmountWallet,feeAmountWallet,basicFeeAmountWallet));
    }

    public Observable<String> getBalance(String publicKey) {
        return makeObservable(getBalanceCallable(publicKey));
    }

    public Observable<Boolean> isAccountTrustedWithTeeCoin(String publicKey, TCGoogleAnalyticEventTrackingListener eventListener) {
        return makeObservable(isAccountTrustedWithTeeCoinCallable(publicKey, eventListener));
    }

    public Observable<Boolean> trustTeeCoin(String sourceSecretSeed, TCGoogleAnalyticEventTrackingListener eventListener) {
        return makeObservable(trustTeeCoinCallable(sourceSecretSeed, eventListener));
    }

    public Observable<Boolean> submitTransaction(Transaction transaction) {
        return makeObservable(submitTransactionCallable(transaction));
    }

    public Observable<ArrayList<StellarPayment>> getStellarPayment(String accountId, String lastToken) {
        return makeObservable(getStellarPaymentCallable(accountId, lastToken));
    }

    public Observable<ArrayList<StellarPayment>> getStellarPayment(String transactionHash) {
        return makeObservable(getStellarPaymentCallable(transactionHash));
    }

    private Callable<Boolean> submitTransactionCallable(Transaction transaction) {
        return () -> StellarProcess.submitPayment(transaction);
    }

    private Callable<Boolean> trustTeeCoinCallable(String sourceSecretSeed, TCGoogleAnalyticEventTrackingListener eventListener) {
        return () -> {
            try {
                return StellarProcess.changeTrust("", sourceSecretSeed, eventListener);
            } catch (DestinationNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    private Callable<String> getBalanceCallable(String publicKey) {
        return () -> {
            StellarProcess stellarProcess = new StellarProcess();
            return stellarProcess.getAccountBalance(publicKey);
        };
    }

    private Callable<Boolean> isAccountTrustedWithTeeCoinCallable(String publicKey, TCGoogleAnalyticEventTrackingListener eventListener) {
        return () -> {
            StellarProcess stellarProcess = new StellarProcess();
            return stellarProcess.isAccountTrustedWithTeeCoin(publicKey, eventListener);
        };
    }


    private Callable<Transaction> getTransactionCallable(
            String sourceSecretSeed,
            String destinationPublicKey,
            String teeCoinAmount,
            String teeCoinFee,
            Memo memo) {
        return () -> StellarProcess.getTransactionWithCommission(
                sourceSecretSeed,
                destinationPublicKey,
                teeCoinAmount,
                teeCoinFee,
                memo);
    }

    //    String paidAmount,
//    String feeAmount,
//    String baseFeeAmount,
    private Callable<Transaction> getTransactionCallable(
            String sourceSecretSeed,
            String paidAmount,
            String feeAmount,
            String baseFeeAmount,
            String paidAmountWallet,
            String feeAmountWallet,
            String baseFeeAmounttWallet
            ) {
        return () -> StellarProcess.getTransactionWithCommission(
                sourceSecretSeed,
                paidAmount,
                feeAmount,
                baseFeeAmount,
                paidAmountWallet,
                feeAmountWallet,
                baseFeeAmounttWallet
        );
    }

    private Callable<ArrayList<StellarPayment>> getStellarPaymentCallable(String accountId, String lastToken) {
        return () -> StellarProcess.getStellarPayment(accountId, lastToken);
    }

    private Callable<ArrayList<StellarPayment>> getStellarPaymentCallable(String transactionHash) {
        return () -> StellarProcess.getStellarPayment(transactionHash);
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                subscriber -> {
                    try {
                        subscriber.onNext(func.call());
                        subscriber.onCompleted();
                    } catch (Exception ex) {
                        subscriber.onError(ex);
                    }
                });
    }
}
