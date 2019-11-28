package com.teecoin.realmdb;


import android.util.Log;

import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.AppBannerModel;
import com.teecoin.model.general.EventsModel;
import com.teecoin.model.general.PaymentThresholdModel;
import com.teecoin.model.general.TransactionConfigModel;
import com.teecoin.model.general.Vendor;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;


    public RealmController() {
        this.realm = Realm.getDefaultInstance();
    }

    public synchronized static RealmController getInstance() {
        if (instance == null)
            instance = new RealmController();
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm istance
    public void refresh() {
        realm.refresh();
    }

    public ArrayList<TransactionDetailModel> getTransactionList(boolean getAll) {
        RealmResults<TransactionDetailModel> models = realm.where(TransactionDetailModel.class)
                .findAllSorted(TransactionDetailModel.CREATED_COLUMN, Sort.DESCENDING);
        if (models != null) {
            if (getAll)
                return new ArrayList<>(models);
            else
                return new ArrayList<>(models.size() > TCConstant.WALLET_MAX_TRANSACTION_SIZE ?
                        models.subList(0, TCConstant.WALLET_MAX_TRANSACTION_SIZE) : models);
        } else {
            return null;
        }
    }

    public ArrayList<TransactionDetailModel> getTransactionListWithoutTipAndReview(boolean getAll) {
        RealmResults<TransactionDetailModel> models = realm.where(TransactionDetailModel.class)
                .notEqualTo(TransactionDetailModel.TYPE_COLUMN, EnumMgr.TransactionType.Review.getValue())
                .notEqualTo(TransactionDetailModel.TYPE_COLUMN, EnumMgr.TransactionType.Tip.getValue())
                .findAllSorted(TransactionDetailModel.CREATED_COLUMN, Sort.DESCENDING);
        if (models != null) {
            if (getAll)
                return new ArrayList<>(models);
            else
                return new ArrayList<>(models.size() > TCConstant.WALLET_MAX_TRANSACTION_SIZE ?
                        models.subList(0, TCConstant.WALLET_MAX_TRANSACTION_SIZE) : models);
        } else {
            return null;
        }
    }

    public ArrayList<TransactionDetailModel> getTransactionListLimited(int page) {
        RealmResults<TransactionDetailModel> models = realm.where(TransactionDetailModel.class)
                .findAllSorted(TransactionDetailModel.CREATED_COLUMN, Sort.DESCENDING);
        //Log.e("models.size() ",models.size()+"");
        if (models != null) {
            if ((page * TransactionDetailModel.LIMIT + TransactionDetailModel.LIMIT) < models.size()) {
                return new ArrayList<>(models.subList(page * TransactionDetailModel.LIMIT, page * TransactionDetailModel.LIMIT + TransactionDetailModel.LIMIT));
            } else if ((page * TransactionDetailModel.LIMIT + TransactionDetailModel.LIMIT) == models.size()) {
                return new ArrayList<>(models.subList(page * TransactionDetailModel.LIMIT, models.size()));
            } else {
                // Log.e("models.size()-(page*LIMIT+LIMIT)",(page*LIMIT+LIMIT)-models.size()+"");
                if ((page * TransactionDetailModel.LIMIT + TransactionDetailModel.LIMIT) - models.size() <= TransactionDetailModel.LIMIT) {
                    return new ArrayList<>(models.subList(page * TransactionDetailModel.LIMIT, models.size()));
                } else {
                    Log.e("null ", "null---------");
                    return null;
                }
            }
        } else {
            return null;
        }

    }

    public ArrayList<TransactionDetailModel> getTransactionListLimitedWithoutTipAndReview(int pageIndex) {
        RealmResults<TransactionDetailModel> models = realm.where(TransactionDetailModel.class)
                .notEqualTo(TransactionDetailModel.TYPE_COLUMN, EnumMgr.TransactionType.Review.getValue())
                .notEqualTo(TransactionDetailModel.TYPE_COLUMN, EnumMgr.TransactionType.Tip.getValue())
                .findAllSorted(TransactionDetailModel.CREATED_COLUMN, Sort.DESCENDING);
        if (models != null) {
            if (pageIndex + TransactionDetailModel.LIMIT < models.size()) {
                return new ArrayList<>(models.subList(pageIndex, pageIndex + TransactionDetailModel.LIMIT));
            } else if (pageIndex < models.size()) {
                return new ArrayList<>(models.subList(pageIndex, models.size()));
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public <T extends RealmObject> void insertData(T realmObj) {
        if (realm != null) {
            realm.beginTransaction();
            try {
                realm.copyToRealm(realmObj);
                realm.commitTransaction();
            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                realm.cancelTransaction();
            }
        }
    }

    public <T extends RealmObject> void deleteAllRecordsAndInsertNewData(T realmObj) {
        if (realm != null) {
            realm.beginTransaction();
            try {
                realm.delete(realmObj.getClass());
                realm.copyToRealm(realmObj);
                realm.commitTransaction();
            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                realm.cancelTransaction();
            }
        }
    }

    public <T extends RealmObject> void insertData(T realmObj, String queryColumn, String queryValue) {
        if (realm != null) {
            realm.beginTransaction();
            try {
//                T haveData = (T)getData(realmObj.getClass(),queryColumn,queryValue);
//                TCLog.d("hung insertData haveData="+haveData);
                if (!isExist(realmObj.getClass(), queryColumn, queryValue)) {
                    realm.copyToRealm(realmObj);
                    realm.commitTransaction();
                } else {
                    realm.cancelTransaction();
                }
            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                realm.cancelTransaction();
            }
        }
    }

    public void updateAccountModel(AccountModel accountModel) {
        if (realm != null) {
            realm.beginTransaction();
            AccountModel dbAccountModel = realm.where(AccountModel.class)
                    .equalTo(AccountModel.PRIMARY_KEY, accountModel.getPublic_key()).findFirst();
            if (dbAccountModel != null) {
                dbAccountModel.update(accountModel);
            } else {
                realm.copyToRealm(accountModel);
            }
            realm.commitTransaction();
        }
    }
    public void updateVendor(Vendor vendor) {
        if (realm != null) {
            realm.beginTransaction();
            Vendor update = realm.where(Vendor.class)
                    .equalTo(Vendor.ID, vendor.getId()).findFirst();
            if (update != null) {
                update.update(vendor);
            } else {
                realm.copyToRealm(vendor);
            }
            realm.commitTransaction();
        }
    }

    public void updateTransactionDetail(TransactionDetailModel transaction) {
        if (realm != null) {
            realm.beginTransaction();
            TransactionDetailModel dbTransaction = realm.where(TransactionDetailModel.class)
                    .equalTo(TransactionDetailModel.PRIMARY_KEY, transaction.getTransaction_hash()).findFirst();
            if (dbTransaction != null) {
                dbTransaction.update(transaction);
            } else {
                realm.copyToRealm(transaction);
            }
            realm.commitTransaction();
        }
    }

    public void updateTransactionConfig(TransactionConfigModel transactionConfigModel) {
        if (realm != null) {
            realm.beginTransaction();
            TransactionConfigModel dbTransactionModel = realm.where(TransactionConfigModel.class)
                    .equalTo(TransactionConfigModel.PRIMARY_KEY, transactionConfigModel.getPublicKey()).findFirst();
            if (dbTransactionModel != null) {
                dbTransactionModel.update(transactionConfigModel);
            } else {
                realm.copyToRealm(transactionConfigModel);
            }
            realm.commitTransaction();
        }
    }

    public void updatePaymentThreshold(PaymentThresholdModel paymentThreshold) {
        if (realm != null) {
            realm.beginTransaction();
            PaymentThresholdModel dbTPaymentThresholdModel = realm.where(PaymentThresholdModel.class)
                    .equalTo(PaymentThresholdModel.PRIMARY_KEY, paymentThreshold.getPublicKey()).findFirst();
            if (dbTPaymentThresholdModel != null) {
                dbTPaymentThresholdModel.update(paymentThreshold);
            } else {
                realm.copyToRealm(paymentThreshold);
            }
            realm.commitTransaction();
        }
    }

    public void updateDeviceToken(AccountModel account) {
        if (realm != null) {
            realm.executeTransaction(realm -> {
                AccountModel accountModel = realm.where(AccountModel.class).equalTo(AccountModel._ID, account.get_id()).findFirst();
                if (accountModel != null) {
                    accountModel.setDevice_token(account.getDevice_token());
                }

            });
        }
    }

    public void updateBalanceAccount(AccountModel account) {
        if (realm != null) {
            realm.executeTransaction(realm -> {
                AccountModel accountModel = realm.where(AccountModel.class).equalTo(AccountModel._ID, account.get_id()).findFirst();
                if (accountModel != null) {
                    accountModel.setBalance(account.getBalance());
                }
            });
        }
    }

    public void updateFieldCoinBackTransactionDetailModel(String transaction_hash, boolean isCoinBack) {
        if (realm != null) {
            realm.beginTransaction();
            TransactionDetailModel dbTransactionDetailModel = realm.where(TransactionDetailModel.class)
                    .equalTo(TransactionDetailModel.PRIMARY_KEY, transaction_hash).findFirst();
            if (dbTransactionDetailModel != null) {
                TransactionDetailModel newTransac = realm.copyFromRealm(dbTransactionDetailModel);
                newTransac.setIs_coinback_success(isCoinBack);
                dbTransactionDetailModel.updateRow(newTransac);
                realm.commitTransaction();
            } else {
                realm.cancelTransaction();
            }
        }
    }

    public void updateFieldIsReviewSuccessTransactionDetailModel(String transaction_id, boolean isReview) {

        if (realm != null) {
            realm.beginTransaction();

            TransactionDetailModel dbTransactionDetailModel = realm.where(TransactionDetailModel.class)
                    .equalTo(TransactionDetailModel.TRANSACTION_ID, transaction_id).findFirst();
            if (dbTransactionDetailModel != null) {

                TransactionDetailModel newTransac = realm.copyFromRealm(dbTransactionDetailModel);
                newTransac.setIs_review_success(isReview);
                dbTransactionDetailModel.updateRow(newTransac);
                realm.commitTransaction();
            } else {
                realm.cancelTransaction();
            }
        }
    }

    public <T extends RealmObject> T getData(Class<T> realmObjCls) {
        if (realm != null) {
            return realm.where(realmObjCls).findFirst();
        }
        return null;
    }

    public <T extends RealmObject> T getData(Class<T> realmObjCls, String queryColumn, String queryValue) {
        if (realm != null) {
            return realm.where(realmObjCls).equalTo(queryColumn, queryValue).findFirst();
        }
        return null;
    }

    public <T extends RealmObject> boolean isExist(Class<T> realmObjCls, String queryColumn, String queryValue) {
        return realm != null && realm.where(realmObjCls).equalTo(queryColumn, queryValue).findFirst() != null;
    }

    public void deleteData() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll(); // delete all realm objects
        realm.commitTransaction();//commit realm changes
    }

    public void close() {
        if (realm != null) {
            realm.close();
        }
    }

    public AccountModel getAccount() {
        return new AccountModel(RealmController.getInstance().getData(AccountModel.class));
    }

    public String getPublicKey() {
        return getAccount().getPublic_key();
    }

    public String getBalance() {
        return TCUtils.isEmpty(getAccount().getBalance()) ? "0" : getAccount().getBalance();
    }

    public boolean destinationIsMine(String destination) {
        if (TCUtils.isEmpty(destination))
            return false;
        return getAccount().getPublic_key().equals(destination);
    }
    public ArrayList<EventsModel> getListEvent() {
        RealmResults<EventsModel> models = realm.where(EventsModel.class).findAll();
        if (models != null) {
            return new ArrayList<>(models);
        } else {
            return null;
        }
    }

    public ArrayList<AppBannerModel> getListAppBanner() {
        RealmResults<AppBannerModel> models = realm.where(AppBannerModel.class).findAll();
        if (models != null) {
            return new ArrayList<>(models);
        } else {
            return null;
        }
    }

    public <T extends RealmObject> void deleteTable(Class<T> realmObjCls) {
        if (realm != null) {
            realm.beginTransaction();
            try {
                realm.delete(realmObjCls);
                realm.commitTransaction();
            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                realm.cancelTransaction();
            }
        }
    }

    public <T extends RealmObject> ArrayList<T> getListData(Class<T> realmObjCls) {
        Iterable<T> models = realm.where(realmObjCls).findAll();
        if (models != null) {
            return new ArrayList<>(realm.copyFromRealm(models));
        } else {
            return null;
        }
    }
}
