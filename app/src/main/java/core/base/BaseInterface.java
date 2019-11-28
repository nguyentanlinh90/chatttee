package core.base;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.teecoin.base.TCConfirmListener;
import com.teecoin.base.TCDecisionListener;


public interface BaseInterface {

    void onBindView();

    AppCompatActivity getActiveActivity();


    void showLoadingDialog();

    void hideLoadingDialog();

    void showAlertDialog(int id, String title, String message,
                         String confirm, TCConfirmListener confirm_listener, Object onWhat);

    void showDecisionDialog(int id, String title, String message, String yes, String no, String cancel,
                            TCDecisionListener decision_listener, Object onWhat);

    void showForceLoginDialog();

    @AnimRes
    int getEnterAnimation();

    @AnimRes
    int getExitAnimation();

    void onBaseDestroyView();

    void registerSingleClick(View... views);

    void unregisterSingleClick(View... views);

    void registerSingleClick(@IdRes int... ids);

    void unregisterSingleClick(@IdRes int... ids);

    SingleClick getSingleClick();

    SingleClick getSingleClick(Object object);

}
