package com.teecoin.feature.walletSystem.orderDetail;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.TransactionConfigModel;
import com.teecoin.model.walletsystem.PaymentInvoiceModel;
import com.teecoin.model.walletsystem.ShopInvoiceModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCUtils;

public class GenerateQRCodeService extends IntentService {

    public static String GENERATE_QR_CODE_INTENT = "GENERATE_QR_CODE_INTENT";
    public static String GENERATE_QR_CODE_BITMAP_INTENT = "GENERATE_QR_CODE_BITMAP_INTENT";
    public static String GENERATE_QR_CODE_AMOUNT_INTENT = "GENERATE_QR_CODE_AMOUNT_INTENT";
    public static String GENERATE_QR_CODE_INVOICE_ID_INTENT = "GENERATE_QR_CODE_INVOICE_ID_INTENT";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GenerateQRCodeService(String name) {
        super(name);
    }

    public GenerateQRCodeService() {
        super("name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String amount = extras.getString(GENERATE_QR_CODE_AMOUNT_INTENT);
                String invoiceId = extras.getString(GENERATE_QR_CODE_INVOICE_ID_INTENT);
                generateQRCode(amount, invoiceId);
            }
        }
    }

    private void generateQRCode(String amount, String invoiceId) {
        RealmController realmController = new RealmController();
        AccountModel accountModel = realmController.getData(AccountModel.class);
        TransactionConfigModel transactionConfigModel = realmController.getData(TransactionConfigModel.class);
        if (accountModel != null && transactionConfigModel != null) {
            ShopInvoiceModel shopInvoiceModel = new ShopInvoiceModel(accountModel, transactionConfigModel);
            PaymentInvoiceModel paymentInvoiceModel =
                    new PaymentInvoiceModel(invoiceId, TCUtils.convertToDouble(amount), shopInvoiceModel);
            Bitmap bmp = TCUtils.generateQRCode(TCUtils.convertToJson(paymentInvoiceModel));
            setTransactionIntent(bmp);
        } else {
            setTransactionIntent(null);
        }
        realmController.close();
    }

    private void setTransactionIntent(Bitmap bmp) {
        final Intent intent = new Intent(GENERATE_QR_CODE_INTENT);
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        intent.putExtra(GENERATE_QR_CODE_BITMAP_INTENT, bmp);
        broadcastManager.sendBroadcast(intent);
    }
}
