package com.teecoin.javastellarsdk.stellar;

import com.teecoin.javastellarsdk.stellar.model.StellarPayment;

import org.glassfish.jersey.media.sse.EventSource;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Server;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import java.util.Timer;


public class MyStreamDataTimer extends Timer {

    public static final int PERIOD = 60000;
    public static final int DELAY =0; // 10000;
    private EventSource eventSource;
    private TransactionListener transactionListener;
    private StellarPayment stellarPayment;

    public MyStreamDataTimer() {
        super();
    }

    public MyStreamDataTimer(final String publicKey, final String lastToken, TransactionListener transactionListener) {
        super();
        this.transactionListener = transactionListener;
//        if(eventSource != null){
//            eventSource.close();
//            eventSource=null;
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                eventSource = getPaymentHistory(publicKey, lastToken);
//            }
//        }).start();

//        new AsyncTimer().execute(publicKey,lastToken);
    }

    public void streamData(String publicKey, String lastToken) {
        if(eventSource != null){
            StellarLog.d("hung timer EventSource.isOpen=" +eventSource.isOpen());
        }else{
            StellarLog.d("hung timer EventSource.isOpen="+eventSource);
        }
        if (eventSource != null && !eventSource.isOpen()) {
//            eventSource.close(10, TimeUnit.SECONDS);
//            eventSource = null;
            if(!isExistThread()) {
                StellarLog.d(String.format("hung start stream again publicKey=%s, lastToken=%s", publicKey, lastToken));
                eventSource = getPaymentHistory(publicKey, lastToken);
            }
        }else if(eventSource == null){
            if(!isExistThread()) {
                StellarLog.d(String.format("hung start stream publicKey=%s, lastToken=%s", publicKey, lastToken));
                eventSource = getPaymentHistory(publicKey, lastToken);
            }
        }
//        else{
//            if(eventSource != null) {
//                StellarLog.d(String.format("hung closing stream "));
//                eventSource.close();
//                StellarLog.d(String.format("hung closed stream "));
//            }
//        }
    }

    private EventSource getPaymentHistory(final String publicKey, String lastToken) {
        Server server = new Server(StellarConstant.getHorizonServerURL());
        final KeyPair account = KeyPair.fromAccountId(publicKey);
        final PaymentsRequestBuilder paymentsRequest =
                server.payments().forAccount(account);

        if (!StellarUtils.isEmpty(lastToken)) {
            paymentsRequest.cursor(lastToken);
        } else {
            paymentsRequest.cursor("now");
        }

        eventSource = paymentsRequest
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
                                    if (stellarPayment != null) {
                                        if (!stellarPayment.getLastToken().equals(payment.getPagingToken())
                                                && !stellarPayment.getTransactionHash().equals(payment.getTransactionHash())) {
                                            StellarLog.d("hung stream logic2=true");
                                            stellarPayment.setFromAccountId(payment.getFrom().getAccountId());
                                            stellarPayment.setAmount(payment.getAmount());
                                            stellarPayment.setLastToken(payment.getPagingToken());
                                            stellarPayment.setTransactionHash(payment.getTransactionHash());
                                            transactionListener.onReceiveTransaction(stellarPayment);
                                        } else {
                                            StellarLog.d("hung stream logic2=false");
                                        }
                                    } else {
                                        StellarLog.d("hung stream logic3=true");
                                        stellarPayment = new StellarPayment();
                                        stellarPayment.setFromAccountId(payment.getFrom().getAccountId());
                                        stellarPayment.setAmount(payment.getAmount());
                                        stellarPayment.setLastToken(payment.getPagingToken());
                                        stellarPayment.setTransactionHash(payment.getTransactionHash());
                                        transactionListener.onReceiveTransaction(stellarPayment);
                                    }
                                }
                            }
                        }
                    }
                });
        return eventSource;
    }

    @Override
    public void cancel() {
//        if (eventSource != null) {
////            eventSource.close();
//            eventSource.close(10, TimeUnit.SECONDS);
//            eventSource = null;
//        }
        super.cancel();
//        System.gc();
    }

    public static boolean isExistThread() {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState() == Thread.State.RUNNABLE) {
                if (t.getName().contains("jersey-sse-event-source")) {
                    StellarLog.d("isExistThread Thread name=" + t.getName()+" isAlive=" + t.isAlive() + " isInterrupt=" + t.isInterrupted());
                    return t.isAlive() && !t.isInterrupted();
                }
            }
        }
        return false;
    }
//    public void aaa() {
//        for (Thread t : Thread.getAllStackTraces().keySet()) {
//            if (t.getState() == Thread.State.RUNNABLE) {
//                if(t.getName().contains("jersey-sse-event-source")){
//                    StellarLog.d("Thread name="+t.getName());
//                    t.interrupt();
//                    StellarLog.d("Thread isAlive="+t.isAlive()+" isInterrupt="+t.isInterrupted());
//                }
//            }
//        }
//
//    }

//    private class AsyncTimer extends AsyncTask<String,Void,EventSource>{
//
//        @Override
//        protected EventSource doInBackground(String... str) {
//            String publicKey=str[0];
//            String token=str[1];
//            return eventSource = getPaymentHistory(publicKey,token);
//        }
//
//        @Override
//        protected void onPostExecute(EventSource e) {
//            super.onPostExecute(e);
//            eventSource = e;
//        }
//    }
}