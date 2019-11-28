package com.teecoin.javastellarsdk.stellar;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.teecoin.javastellarsdk.stellar.model.StellarPayment;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Server;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

public class TransactionService extends IntentService  {

    public static final int TIMER_REPEAT_CHECK=30000; //30 seconds
    public static final String TRANSACTION_INTENT="TransactionIntent";
    public static final String TRANSACTION_STELLAR_PAYMENT_INTENT="TransactionPaymentIntent";
    public static final String TRANSACTION_STELLAR_PUBLIC_KEY_INTENT ="TransactionPublicKeyIntent";
    public static final String TRANSACTION_LAST_TOKEN_INTENT="TransactionLastTokenIntent";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TransactionService(String name) {
        super(name);
    }

    public TransactionService() {
        super("TransactionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent !=null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                final String publicKey = extras.getString(TRANSACTION_STELLAR_PUBLIC_KEY_INTENT);
                final String lastToken = extras.getString(TRANSACTION_LAST_TOKEN_INTENT);
                getPaymentHistory(publicKey, lastToken);
            }
        }
    }


    @Override
    public void onCreate() {
        StellarLog.d("hung TransactionService onCreate");
        super.onCreate();
    }

    private static final String JERSEY_THREAD_NAME = "jersey-sse-event-source";

    public static boolean isExistThread(String publicKey) {
        boolean isExist = false;
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState() == Thread.State.RUNNABLE) {
                if (t.getName().contains(JERSEY_THREAD_NAME) && t.getName().contains(publicKey)) {
//                    StellarLog.d("isExistThread Thread name=" + t.getName()+" isAlive=" + t.isAlive() + " isInterrupt=" + t.isInterrupted());
//                    return t.isAlive() && !t.isInterrupted();
                    if (t.isAlive() && !t.isInterrupted()) {
                        isExist = true;
                    }
                }
            }
        }
        return isExist;
    }

    public static boolean stopThread() {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState() == Thread.State.RUNNABLE) {
                if (t.getName().contains(JERSEY_THREAD_NAME)) {
                    StellarLog.d("isExistThread Thread name=" + t.getName() + " isAlive=" + t.isAlive() + " isInterrupt=" + t.isInterrupted());
                    if (t.isAlive() && !t.isInterrupted()) {
                        t.interrupt();
                    }
                }
            }
        }
        return false;
    }

    private void setTransactionIntent(StellarPayment stellarPayment) {
        final Intent intent = new Intent(TRANSACTION_INTENT);
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        intent.putExtra(TRANSACTION_STELLAR_PAYMENT_INTENT, stellarPayment);
        broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        StellarLog.d("hung TransactionService onDestroy");
        super.onDestroy();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        StellarLog.d("hung TransactionService onStart");
        super.onStart(intent, startId);
    }

    private void getPaymentHistory(final String publicKey, String lastToken) {
        Server server = new Server(StellarConstant.getHorizonServerURL());
        final KeyPair account = KeyPair.fromAccountId(publicKey);
        PaymentsRequestBuilder paymentsRequest =
                server.payments().forAccount(account);

        if (!StellarUtils.isEmpty(lastToken)) {
            paymentsRequest.cursor(lastToken);
        } else {
            paymentsRequest.cursor("now");
        }

        paymentsRequest
                .stream(new org.stellar.sdk.requests.EventListener<OperationResponse>() {
                    @Override
                    public void onEvent(OperationResponse operation) {
                        StellarLog.d("hung payment:" + operation);
                        if (operation != null) {
                            if (operation instanceof PaymentOperationResponse) {
                                PaymentOperationResponse payment = (PaymentOperationResponse) operation;
                                //get Send or Receive Transaction from/to this account
                                StellarLog.d("hung stream last token:" + payment.getPagingToken());
                                StellarLog.d("hung stream hash:" + payment.getTransactionHash());
                                StellarLog.d("hung stream from:" + payment.getFrom().getAccountId());
                                StellarLog.d("hung stream to:" + payment.getTo().getAccountId());

                                //check from/to is your account
                                //check to account isn't TeeCoin Master Account
                                boolean checkPayment = (payment.getTo().getAccountId().equals(account.getAccountId())
                                        || payment.getFrom().getAccountId().equals(account.getAccountId()))
                                        && !payment.getTo().getAccountId().equals(StellarConstant.getTeeCoinTransactionBasicFeeAmountAccountId());

                                StellarLog.d("hung stream logic1=" + checkPayment);
                                if (checkPayment) {
                                    StellarPayment stellarPayment = new StellarPayment();
                                    stellarPayment.setFromAccountId(payment.getFrom().getAccountId());
                                    stellarPayment.setAmount(payment.getAmount());
                                    stellarPayment.setLastToken(payment.getPagingToken());
                                    stellarPayment.setTransactionHash(payment.getTransactionHash());
                                    setTransactionIntent(stellarPayment);
                                }
                            }
                        }
                    }
                });

    }

}
