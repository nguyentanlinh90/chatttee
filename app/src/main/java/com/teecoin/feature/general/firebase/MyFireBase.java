package com.teecoin.feature.general.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.teecoin.BuildConfig;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.general.popup.NewVersionAppNotificationPopup;
import com.teecoin.feature.general.popup.UpdateAppListener;
import com.teecoin.feature.reviewSystem.userReviewShop.UserReviewShopScreen;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.AppBannerModel;
import com.teecoin.model.general.AppConfigModel;
import com.teecoin.model.general.CouponsConfigModel;
import com.teecoin.model.general.FeeConfigModel;
import com.teecoin.model.general.FireBaseUploadedVideoModel;
import com.teecoin.model.general.FloatingIconModel;
import com.teecoin.model.general.GuidelineModel;
import com.teecoin.model.general.LatestAppVersion;
import com.teecoin.model.general.TransactionConfigModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.retrofit.URL;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import core.base.BaseFragment;

public class MyFireBase {

    private static final String FIRE_BASE_CLOUD_DOCUMENT = "app_config";
    private static final String FIRE_BASE_CLOUD_UPLOADED_VIDEO = "upload";
    private static final String FIRE_BASE_CLOUD_INVOICES = "invoices";
    private static final String FIRE_BASE_CLOUD_EXCHANGE_RATE = "exchange_rates";
    private Context mContext;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<GuidelineModel> guidelineModels;

    public MyFireBase(Context mContext) {
        this.mContext = mContext;
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        streamAppConfig(firebaseFirestore);
    }

    private void streamAppConfig(FirebaseFirestore db) {
        if (db != null) {
            db.collection(URL.getFireBaseCloudServer())
                    .document(FIRE_BASE_CLOUD_DOCUMENT)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {

                            AppConfigModel appConfigModel = new AppConfigModel(snapshot.getData());
                            FeeConfigModel feeConfigModel = appConfigModel.getFeeConfigModel();
                            String exchangeRate = appConfigModel.getExchangeRate();
                            //String googleKey = appConfigModel.getGoogle_key();

                            TCSharePreferenceManager.getInstance().setString(DataKey.TopUpCountry, appConfigModel.getTec_topup_country());

                            RealmController.getInstance().deleteAllRecordsAndInsertNewData(feeConfigModel);

                            if (!TCUtils.isEmpty(exchangeRate) && ((TCMainActivity) mContext).isAlreadyLogin()) {
                                AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
                                TransactionConfigModel transactionConfigModel = new TransactionConfigModel(RealmController.getInstance().getData(TransactionConfigModel.class));
                                transactionConfigModel.setCoinExchange(accountModel.getRate());
                                transactionConfigModel.setCoinBackRewardRate(accountModel.getReturn_rate());
                                transactionConfigModel.setMinAmount(accountModel.getMin_amount());
                                transactionConfigModel.setMaxPaymentRate(transactionConfigModel.getMaxPaymentRate());
                                transactionConfigModel.setCode(accountModel.getCurrency_code() != null ? accountModel.getCurrency_code() : TransactionConfigModel.DEFAULT_CODE);
                                transactionConfigModel.setSymbol(accountModel.getCurrency_symbol() != null ? accountModel.getCurrency_symbol() : TransactionConfigModel.DEFAULT_SYMBOL);
                                transactionConfigModel.setPublicKey(accountModel.getPublic_key());
                                transactionConfigModel.setCoinExchange(TCUtils.convertToDouble(exchangeRate));

                                accountModel.setRate(TCUtils.convertToDouble(exchangeRate));
                                RealmController.getInstance().updateAccountModel(accountModel);
                                RealmController.getInstance().updateTransactionConfig(transactionConfigModel);
                            }

                            // get banner
                            ArrayList<AppBannerModel> listBanner = appConfigModel.getAppBannerModelList();
                            if (listBanner != null && listBanner.size() > 0) {
                                RealmController.getInstance().deleteTable(AppBannerModel.class);
                                for (AppBannerModel appBannerModel : listBanner) {
                                    RealmController.getInstance().insertData(appBannerModel);
                                }
                            }

                            // get guide line
                            guidelineModels = appConfigModel.getGuidelineModelArrayList();

//                            if (!TCUtils.isEmpty(googleKey)) {
//                                GoogleKeyConfigModel googleKeyConfigModel = new GoogleKeyConfigModel(googleKey);
//                                RealmController.getInstance().deleteTable(GoogleKeyConfigModel.class);
//                                RealmController.getInstance().insertData(googleKeyConfigModel);
//                            }

                            // todo for release 03/10/2019
                            CouponsConfigModel couponsConfigModel = appConfigModel.getCoupons();
                            if (couponsConfigModel != null) {
                                if (couponsConfigModel.getFloating_icon() != null) {
                                    FloatingIconModel floatingIconModel = couponsConfigModel.getFloating_icon();
                                    if (floatingIconModel != null) {
                                        feeConfigModel.setImageFloating(floatingIconModel.getImage());
                                        feeConfigModel.setUrlFloating(floatingIconModel.getUrl());
                                        feeConfigModel.setTitleFloating(floatingIconModel.getTitle());
                                        RealmController.getInstance().deleteAllRecordsAndInsertNewData(feeConfigModel);
                                    }
                                }

                            }
                        }
                    });
            DocumentReference docRef = db
                    .collection(URL.getFireBaseCloudServer())
                    .document(FIRE_BASE_CLOUD_UPLOADED_VIDEO);

            docRef.addSnapshotListener((snapshot, e) -> {
                if (snapshot != null && snapshot.exists() && snapshot.getData() != null) {
                    BaseFragment topFragment = ((TCMainActivity) mContext).getTopFragment();
                    if (topFragment instanceof UserReviewShopScreen) {
                        String uuid = ((UserReviewShopScreen) topFragment).getVideoUuid();
                        ArrayList<Map<String, Object>> mediaModels =
                                (ArrayList<Map<String, Object>>) snapshot.getData().get(uuid);
                        if (mediaModels != null && mediaModels.size() > 0) {
                            FireBaseUploadedVideoModel uploadedVideoModel =
                                    new FireBaseUploadedVideoModel(mediaModels, uuid);

                            ((UserReviewShopScreen) topFragment).finishUploadVideo(uploadedVideoModel);
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(uuid, FieldValue.delete());
                            docRef.update(updates);
                        }
                    }
                }
            });
        }
    }

    public void getAppConfig(AccountModel accountModel, GetAppConfigListener appConfigListener) {
        if (firebaseFirestore != null) {
            DocumentReference docRef = firebaseFirestore.collection(URL.getFireBaseCloudServer()).document(FIRE_BASE_CLOUD_DOCUMENT);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists() && document.getData() != null) {
                            AppConfigModel appConfigModel = new AppConfigModel(document.getData());
                            FeeConfigModel feeConfigModel = appConfigModel.getFeeConfigModel();
                            RealmController.getInstance().deleteAllRecordsAndInsertNewData(feeConfigModel);
                            accountModel.setRate(TCUtils.convertToDouble(appConfigModel.getExchangeRate()));
                            RealmController.getInstance().updateAccountModel(accountModel);

                            TransactionConfigModel transactionConfigModel = new TransactionConfigModel(
                                    accountModel.getRate(),
                                    accountModel.getReturn_rate(),
                                    accountModel.getMin_amount(),
                                    TCConstant.DEFAULT_MAX_PAYMENT_RATE,
                                    accountModel.getCurrency_code(),
                                    accountModel.getCurrency_symbol(),
                                    accountModel.getPublic_key()
                            );
                            RealmController.getInstance().insertData(transactionConfigModel);

                            ArrayList<AppBannerModel> listBanner = appConfigModel.getAppBannerModelList();
                            if (listBanner != null && listBanner.size() > 0) {
                                RealmController.getInstance().deleteTable(AppBannerModel.class);
                                for (AppBannerModel appBannerModel : listBanner) {
                                    RealmController.getInstance().insertData(appBannerModel);
                                }
                            }
                            // todo for release 03/10/2019
                            CouponsConfigModel couponsConfigModel = appConfigModel.getCoupons();
                            if (couponsConfigModel != null) {
                                if (couponsConfigModel.getFloating_icon() != null) {
                                    FloatingIconModel floatingIconModel = couponsConfigModel.getFloating_icon();
                                    if (floatingIconModel != null) {
                                        feeConfigModel.setImageFloating(floatingIconModel.getImage());
                                        feeConfigModel.setUrlFloating(floatingIconModel.getUrl());
                                        feeConfigModel.setTitleFloating(floatingIconModel.getTitle());
                                        RealmController.getInstance().deleteAllRecordsAndInsertNewData(feeConfigModel);
                                    }
                                }

                            }
                            appConfigListener.onGetAppConfigFinish();
                        } else {
                            appConfigListener.onGetAppConfigFinish();
                        }
                    } else {
                        appConfigListener.onGetAppConfigFinish();
                    }
                }
            });
        } else {
            appConfigListener.onGetAppConfigFinish();
        }
    }


    public void checkVersionAppUpdate() {
        if (firebaseFirestore != null) {
            firebaseFirestore.collection(URL.getFireBaseCloudServer())
                    .document(FIRE_BASE_CLOUD_DOCUMENT)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            LatestAppVersion appVersion = new LatestAppVersion(snapshot.getData());
                            if (appVersion.hasNewUpdate()) {
                                showPopupUpdateVersion();
                            }
                        }
                    });
        }
    }

    private void showPopupUpdateVersion() {
        NewVersionAppNotificationPopup dialogUpdate = new NewVersionAppNotificationPopup(mContext, new UpdateAppListener() {
            @Override
            public void onCloseApp() {
                ((TCMainActivity) mContext).finish();
            }

            @Override
            public void gotoUpdate() {
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                }
            }
        });
        dialogUpdate.show();
    }

    public ArrayList<GuidelineModel> getGuidelineModels() {
        return guidelineModels;
    }

    public void invoiceStreaming(String invoice, PaymentListener paymentListener) {// FAILED   SUCCESS
        if (firebaseFirestore != null) {
            DocumentReference docRef = firebaseFirestore.collection(URL.getFireBaseCloudServer()).document(FIRE_BASE_CLOUD_INVOICES);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String status = (String) documentSnapshot.get(invoice);
                        if (!TCUtils.isEmpty(status)) {
                            if (status.equalsIgnoreCase(TCConstant.TAG_SUCCESS_UPPERCASE) || status.equalsIgnoreCase(TCConstant.TAG_FAILED)) {
                                TCLog.d("Tu status payment " + status);
                                Map<String, Object> updates = new HashMap<>();
                                updates.put(invoice, FieldValue.delete());
                                docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        TCLog.d("Tu payment onCompleted");
                                        paymentListener.onPaymentStatus(invoice, status.equalsIgnoreCase(TCConstant.TAG_FAILED));
                                    }
                                });
                            }

                        }
                    } else {
                        TCLog.e(invoice + " not exits ");
                    }
                }
            });

        }
    }

    public void exchangeRateStreaming(String currency, ExchangeRateListener exchangeRateListener) {
        if (firebaseFirestore != null) {
            DocumentReference docRef = firebaseFirestore.collection(URL.getFireBaseCloudServer()).document(FIRE_BASE_CLOUD_EXCHANGE_RATE);
            docRef.addSnapshotListener((documentSnapshot, e) -> {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String rate = (String) documentSnapshot.get(currency);
                    String fee = (String) documentSnapshot.get("fee");
                    String spread = (String) documentSnapshot.get("spread");
                    String basic_fee_amount = (String) documentSnapshot.get("basic_fee_amount");
                    exchangeRateListener.onChange(TCUtils.convertToDouble(rate), TCUtils.convertToDouble(fee),
                            TCUtils.convertToDouble(spread), TCUtils.convertToDouble(basic_fee_amount));
                } else {
                    TCLog.d("linhnt not exits ");
                }
            });
        }
    }

    public interface GetAppConfigListener {
        void onGetAppConfigFinish();
    }

    public interface PaymentListener {
        void onPaymentStatus(String invoice, boolean failed);
    }

    public interface ExchangeRateListener {
        void onChange(double rate, double fee, double spread, double basic_fee_amount);
    }
}
