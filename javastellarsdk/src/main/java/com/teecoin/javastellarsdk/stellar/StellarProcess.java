package com.teecoin.javastellarsdk.stellar;

import com.teecoin.javastellarsdk.stellar.exceptions.DestinationNotFoundException;
import com.teecoin.javastellarsdk.stellar.exceptions.TransactionFailException;
import com.teecoin.javastellarsdk.stellar.model.StellarAccount;
import com.teecoin.javastellarsdk.stellar.model.StellarPayment;

import org.stellar.sdk.ChangeTrustOperation;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;
import org.stellar.sdk.PaymentOperation;
import org.stellar.sdk.Server;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.requests.RequestBuilder;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import java.io.IOException;
import java.util.ArrayList;

public class StellarProcess {

    public static StellarAccount create() {
        try {
            KeyPair pair = KeyPair.random();
            StellarLog.d("secretSeed:" + new String(pair.getSecretSeed()));
            StellarLog.d("publicKey:" + pair.getPublicKey().toString());
            StellarLog.d("accountId:" + pair.getAccountId());
            return new StellarAccount(pair);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getPublicKey(String secretSeed) {
        try {
            KeyPair destination = KeyPair.fromSecretSeed(secretSeed);
            if (destination != null) {
                return destination.getAccountId();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
    public static boolean submitPayment(Transaction transaction)
            throws TransactionFailException {

        StellarConstant.useNetWork();

        Server server = new Server(StellarConstant.getHorizonServerURL());
        try {
            SubmitTransactionResponse response = server.submitTransaction(transaction);
            System.out.println(response);
            return response.isSuccess();
        } catch (Exception e) {
            throw new TransactionFailException();
        }
    }

    public static boolean changeTrust(String memo,
                                      String sourceSecretSeed,
                                      TCGoogleAnalyticEventTrackingListener eventListener)
            throws DestinationNotFoundException,
            TransactionFailException {
        if (eventListener != null)
            eventListener.log("1", "start_trust");
        StellarConstant.useNetWork();
        Server server = new Server(StellarConstant.getHorizonServerURL());

        KeyPair destination = KeyPair.fromSecretSeed(sourceSecretSeed);
        if (eventListener != null)
            eventListener.log("2", "getKeyPair_from_secret_key");
        AccountResponse destinationAccount = null;
        try {
            destinationAccount = server.accounts().account(destination);
            if (eventListener != null)
                eventListener.log("3", "getAccount_success");
        } catch (IOException e) {
            if (eventListener != null)
                eventListener.log("3", "getAccount_exception=" + e.getMessage());
            throw new DestinationNotFoundException();
        }

        // Start building the transaction.
        Transaction transaction = new Transaction.Builder(destinationAccount)
                .addOperation(new ChangeTrustOperation.Builder(StellarConstant.getTeeCoinAsset(), StellarConstant.LIMIT_AMOUNT).build())
                .addMemo(Memo.text(memo))
                .build();
        if (eventListener != null)
            eventListener.log("4", "build_a_transaction_for_trust_tee_coin");
        transaction.sign(destination);
        if (eventListener != null)
            eventListener.log("5", "sign_a_transaction_for_trust_tee_coin");
        try {
            SubmitTransactionResponse response = server.submitTransaction(transaction);
            if (eventListener != null)
                eventListener.log("6", "submit_a_transaction_for_trust_tee_coin_with_reponse=" + response.isSuccess());
            StellarLog.d("hungs: Trust Success!");
            return response.isSuccess();
        } catch (Exception e) {
            if (eventListener != null)
                eventListener.log("7", "trust_tee_coin_exception=" + e.getMessage());
            throw new TransactionFailException();
        }

    }


    public String getAccountBalance(String accountId)
            throws DestinationNotFoundException {

        StellarConstant.useNetWork();

        Server server = new Server(StellarConstant.getHorizonServerURL());

        AccountResponse account = null;
        try {
            account = server.accounts().account(KeyPair.fromAccountId(accountId));
        } catch (IOException e) {
            throw new DestinationNotFoundException();
        }
        for (AccountResponse.Balance balance : account.getBalances()) {
            if (balance.getAsset().equals(StellarConstant.getTeeCoinAsset())) {
                return balance.getBalance();
            }
        }
        return "0.0000";
    }

    public boolean isAccountTrustedWithTeeCoin(String accountId, TCGoogleAnalyticEventTrackingListener eventListener) {
        if (eventListener != null)
            eventListener.log("1", "start_check_is_trust");
        StellarConstant.useNetWork();

        Server server = new Server(StellarConstant.getHorizonServerURL());

        AccountResponse account = null;
        try {
            account = server.accounts().account(KeyPair.fromAccountId(accountId));
            if (eventListener != null)
                eventListener.log("2", "getAccount_success");
        } catch (IOException e) {
            if (eventListener != null)
                eventListener.log("2", "getAccount_exception=" + e.getMessage());
//            throw new DestinationNotFoundException();
            return false;
        }
        for (AccountResponse.Balance balance : account.getBalances()) {
            if (balance.getAsset().equals(StellarConstant.getTeeCoinAsset())) {
                if (eventListener != null)
                    eventListener.log("3", "already_trust_teecoin");
                return true;
            }
        }
        if (eventListener != null)
            eventListener.log("4", "not_trust_teecoin_yet");
        return false;
    }

    //    String paidAmount,
//    String feeAmount,
//    String baseFeeAmount,
    public static Transaction getTransactionWithCommission(
            String sourceSecretSeed,
            String destinationPublicKey,
            String paidAmount,
            String feeAmount,
            String baseFeeAmount,
            Memo memo)
            throws DestinationNotFoundException {

        StellarConstant.useNetWork();

        Server server = new Server(StellarConstant.getHorizonServerURL());

        KeyPair sourceAccount = KeyPair.fromSecretSeed(sourceSecretSeed);
        KeyPair destinationAccount = KeyPair.fromAccountId(destinationPublicKey);

        try {
            server.accounts().account(sourceAccount);
        } catch (IOException e) {
            throw new DestinationNotFoundException(sourceAccount.getAccountId());
        }

        try {
            server.accounts().account(destinationAccount);
        } catch (IOException e) {
            throw new DestinationNotFoundException(destinationAccount.getAccountId());
        }

        KeyPair teeCoinMasterAccountId = KeyPair.fromAccountId(StellarConstant.getTeeCoinTransactionBasicFeeAmountAccountId());
        try {
            server.accounts().account(teeCoinMasterAccountId);
        } catch (IOException e) {
            throw new DestinationNotFoundException();
        }

        AccountResponse fromAccount = null;
        try {
            fromAccount = server.accounts().account(sourceAccount);
        } catch (IOException e) {
            throw new DestinationNotFoundException();
        }

        Transaction.Builder builder = new Transaction.Builder(fromAccount)
                .addOperation(new PaymentOperation.Builder(destinationAccount, StellarConstant.getTeeCoinAsset(), paidAmount).build())
                .addMemo(memo);

        if (StellarUtils.convertToDouble(feeAmount) > 0) {
            builder.addOperation(
                    new PaymentOperation.Builder(teeCoinMasterAccountId, StellarConstant.getTeeCoinAsset(), feeAmount).build());
        }
        if (StellarUtils.convertToDouble(baseFeeAmount) > 0) {
            builder.addOperation(
                    new PaymentOperation.Builder(teeCoinMasterAccountId, StellarConstant.getTeeCoinAsset(), baseFeeAmount).build());
        }

        Transaction transaction = builder.build();
        transaction.sign(sourceAccount);
        return transaction;
    }

    public static Transaction getTransactionWithCommission(// todo using for new payment
                                                           String sourceSecretSeed,
                                                           String paidAmount,
                                                           String feeAmount,
                                                           String baseFeeAmount,String PaidAmountWallet,String FeeAmountWallet,String BasicFeeAmountWallet)
            throws DestinationNotFoundException {

        StellarConstant.useNetWork();

        Server server = new Server(StellarConstant.getHorizonServerURL());

        KeyPair sourceAccount = KeyPair.fromSecretSeed(sourceSecretSeed);
        KeyPair destination = null;
        // KeyPair destination = KeyPair.fromSecretSeed("GDPBWDF7HC24C5WKONESEGMWZORK3XOUYKXNJMM6YSONDVWPMJEQTAJW");//
        try {
            server.accounts().account(sourceAccount);
        } catch (IOException e) {
            throw new DestinationNotFoundException(sourceAccount.getAccountId());
        }

//        KeyPair paidAmountAccountId = KeyPair.fromAccountId(StellarConstant.getTeeCoinTransactionPaidAmountAccountId());
//        KeyPair feeAmountAccountId = KeyPair.fromAccountId(StellarConstant.getTeeCoinTransactionFeeAmountAccountId());
//        KeyPair basicAmountAccountId = KeyPair.fromAccountId(StellarConstant.getTeeCoinTransactionBasicFeeAmountAccountId());
        KeyPair paidAmountAccountId = KeyPair.fromAccountId(PaidAmountWallet);
        KeyPair feeAmountAccountId = KeyPair.fromAccountId(FeeAmountWallet);
        KeyPair basicAmountAccountId = KeyPair.fromAccountId(BasicFeeAmountWallet);
        try {
            server.accounts().account(paidAmountAccountId);
            server.accounts().account(feeAmountAccountId);
            server.accounts().account(basicAmountAccountId);
        } catch (IOException e) {
            throw new DestinationNotFoundException();
        }

        AccountResponse fromAccount = null;
        try {
            fromAccount = server.accounts().account(sourceAccount);

        } catch (IOException e) {
            throw new DestinationNotFoundException();
        }

        Transaction.Builder builder = new Transaction.Builder(fromAccount)
                //.addOperation(new PaymentOperation.Builder(sourceAccount, StellarConstant.getTeeCoinAsset(), paidAmount).build())
                ;

        if (StellarUtils.convertToDouble(paidAmount) > 0) {
            builder.addOperation(
                    new PaymentOperation.Builder(paidAmountAccountId, StellarConstant.getTeeCoinAsset(), paidAmount).build());
        }
        if (StellarUtils.convertToDouble(feeAmount) > 0) {
            builder.addOperation(
                    new PaymentOperation.Builder(feeAmountAccountId, StellarConstant.getTeeCoinAsset(), feeAmount).build());
        }
        if (StellarUtils.convertToDouble(baseFeeAmount) > 0) {
            builder.addOperation(
                    new PaymentOperation.Builder(basicAmountAccountId, StellarConstant.getTeeCoinAsset(), baseFeeAmount).build());
        }

        Transaction transaction = builder.build();
        transaction.sign(sourceAccount);
        return transaction;
    }
    public static Transaction getTransactionWithCommission(
            String sourceSecretSeed,
            String destinationPublicKey,
            String teeCoinAmount,
            String teeCoinFee,
            Memo memo)
            throws DestinationNotFoundException {

        StellarConstant.useNetWork();

        Server server = new Server(StellarConstant.getHorizonServerURL());

        KeyPair sourceAccount = KeyPair.fromSecretSeed(sourceSecretSeed);
        KeyPair destinationAccount = KeyPair.fromAccountId(destinationPublicKey);

        try {
            server.accounts().account(sourceAccount);
        } catch (IOException e) {
            throw new DestinationNotFoundException(sourceAccount.getAccountId());
        }

        try {
            server.accounts().account(destinationAccount);
        } catch (IOException e) {
            throw new DestinationNotFoundException(destinationAccount.getAccountId());
        }

        KeyPair teeCoinMasterAccountId = KeyPair.fromAccountId(StellarConstant.getTeeCoinTransactionBasicFeeAmountAccountId());
        try {
            server.accounts().account(teeCoinMasterAccountId);
        } catch (IOException e) {
            throw new DestinationNotFoundException();
        }

        AccountResponse fromAccount = null;
        try {
            fromAccount = server.accounts().account(sourceAccount);
        } catch (IOException e) {
            throw new DestinationNotFoundException();
        }

        Transaction.Builder builder = new Transaction.Builder(fromAccount)
                .addOperation(new PaymentOperation.Builder(destinationAccount, StellarConstant.getTeeCoinAsset(), teeCoinAmount).build())
                .addMemo(memo);

        if (StellarUtils.convertToDouble(teeCoinFee) > 0) {
            builder.addOperation(
                    new PaymentOperation.Builder(teeCoinMasterAccountId, StellarConstant.getTeeCoinAsset(), teeCoinFee).build());
        }

        Transaction transaction = builder.build();
        transaction.sign(sourceAccount);
        return transaction;
    }

    public static ArrayList<StellarPayment> getStellarPayment(String accountId, String lastToken) {
        Server server = new Server(StellarConstant.getHorizonServerURL());
        KeyPair account = KeyPair.fromAccountId(accountId);
        PaymentsRequestBuilder paymentsRequest =
                server.payments().forAccount(account);
        paymentsRequest.order(RequestBuilder.Order.ASC);
        paymentsRequest.limit(10);
        if (!StellarUtils.isEmpty(lastToken)) {
            paymentsRequest.cursor(lastToken);
        } else {
            return null;
        }

        ArrayList<StellarPayment> stellarPayments = new ArrayList<>();
        try {
            ArrayList<OperationResponse> operationResponses = paymentsRequest.execute().getRecords();
            for (OperationResponse operation : operationResponses) {
                if (operation != null && operation instanceof PaymentOperationResponse) {
                    PaymentOperationResponse payment = (PaymentOperationResponse) operation;
                    boolean checkPayment = (payment.getTo().getAccountId().equals(account.getAccountId())
                            || payment.getFrom().getAccountId().equals(account.getAccountId()))
                            && !payment.getTo().getAccountId().equals(StellarConstant.getTeeCoinTransactionBasicFeeAmountAccountId());
                    if (checkPayment) {
                        stellarPayments.add(new StellarPayment(
                                payment.getAmount(),
                                payment.getFrom().getAccountId(),
                                payment.getTransactionHash(),
                                payment.getPagingToken()));
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return stellarPayments;

    }

    public static ArrayList<StellarPayment> getStellarPayment(String transactionHash) {
        Server server = new Server(StellarConstant.getHorizonServerURL());
        PaymentsRequestBuilder paymentsRequest =
                server.payments().forTransaction(transactionHash);

        ArrayList<StellarPayment> stellarPayments = new ArrayList<>();
        try {
            ArrayList<OperationResponse> operationResponses = paymentsRequest.execute().getRecords();
            for (OperationResponse operation : operationResponses) {
                if (operation != null && operation instanceof PaymentOperationResponse) {
                    PaymentOperationResponse payment = (PaymentOperationResponse) operation;
                    stellarPayments.add(new StellarPayment(
                            payment.getAmount(),
                            payment.getFrom().getAccountId(),
                            payment.getTransactionHash(),
                            payment.getPagingToken()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return stellarPayments;
    }

}
