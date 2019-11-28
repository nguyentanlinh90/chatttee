package core.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseAlertDialog;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.notification.FCMHandler;
import com.teecoin.feature.general.welcome.NewWelcomeScreen;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.RegisterNotifyModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.Loading;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import core.dialog.GeneralDialog;
import core.dialog.LoadingDialog;

public abstract class BaseActivity extends AppCompatActivity implements BaseInterface, SingleClick.SingleClickListener {

    HashMap<Integer, ArrayList<String>> containers = new HashMap<>();
    private Unbinder unbinder;
    private SingleClick singleClick;
    private Loading loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.setActiveActivity(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        BaseApplication.setActiveActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.setActiveActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        onBindView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
        onBindView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        unbinder = ButterKnife.bind(this);
        onBindView();
    }

    @Override
    public final AppCompatActivity getActiveActivity() {
        return BaseApplication.getActiveActivity();
    }

    @Override
    public void showLoadingDialog() {
        if (BaseProperties.loadingDialog != null) {
            BaseProperties.loadingDialog.closeLoading();
            BaseProperties.loadingDialog.dismiss();
        }
        BaseProperties.loadingDialog = null;
        if (BaseProperties.loadingDialog == null) {
            BaseProperties.loadingDialog = new LoadingDialog(getActiveActivity(), R.style.dialog_full_transparent_background);
            BaseProperties.loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            BaseProperties.loadingDialog.setIndeterminate(true);
            BaseProperties.loadingDialog.setCancelable(false);
        }
        if (BaseProperties.loadingDialog != null) {
            BaseProperties.loadingDialog.show();
            BaseProperties.loadingDialog.showLoading();
        }
        //showLoading(true);
    }

    @Override
    public void hideLoadingDialog() {
        if (BaseProperties.loadingDialog != null) {
            BaseProperties.loadingDialog.closeLoading();
            BaseProperties.loadingDialog.dismiss();
            BaseProperties.loadingDialog = null;
        }
        //showLoading(false);
    }

    public void showLoading(boolean isShow) {
        if (isShow) {
            if (loading == null)
                loading = new Loading(getActiveActivity());

            loading.show();
        } else {
            if (loading != null) {
                loading.dismiss();
            }
        }
    }

    @Override
    public void showAlertDialog(int id, String title, String message, String confirm, TCConfirmListener confirm_listener, Object onWhat) {
        if (BaseProperties.alertDialog != null)
            BaseProperties.alertDialog.dismiss();
        BaseProperties.alertDialog = null;
        if (BaseProperties.alertDialog == null)
            BaseProperties.alertDialog = new GeneralDialog(getActiveActivity(), id, title, message, confirm, confirm_listener, onWhat);
        if (BaseProperties.alertDialog != null)
            BaseProperties.alertDialog.show();
    }

    @Override
    public void showDecisionDialog(int id, String title, String message, String yes, String no, String cancel, TCDecisionListener decision_listener, Object onWhat) {
        if (BaseProperties.decisionDialog != null)
            BaseProperties.decisionDialog.dismiss();
        BaseProperties.decisionDialog = null;
        if (BaseProperties.decisionDialog == null)
            BaseProperties.decisionDialog = new GeneralDialog(getActiveActivity(), id, title, message, yes, no, cancel, decision_listener, onWhat);
        if (BaseProperties.decisionDialog != null)
            BaseProperties.decisionDialog.show();
    }

    @Override
    public void showForceLoginDialog() {
        if (BaseProperties.forceLoginDialog != null)
            BaseProperties.forceLoginDialog.dismiss();
        BaseProperties.forceLoginDialog = null;
        BaseProperties.forceLoginDialog = new TCBaseAlertDialog(getActiveActivity(), TCUtils.getString(R.string.text_message), TCUtils.getString(R.string.your_session_has_expired_please_login_again),
                TCUtils.getString(R.string.text_ok), (id, onWhat) -> {
            handleLogout();
        });
        BaseProperties.forceLoginDialog.show();
    }

    private void handleLogout() {
        try {
            AccountModel accountModel = RealmController.getInstance().getAccount();
            RegisterNotifyModel notifyModel = new RegisterNotifyModel();
            notifyModel.setToken(accountModel.getDevice_token());
            logout();
        } catch (Exception ignored) {
            ((TCMainActivity) getActiveActivity()).replaceFragment(NewWelcomeScreen.getInstance(), true);
        }
    }

    private void logout() {
        String topUpCountry = TCSharePreferenceManager.getInstance().getString(DataKey.TopUpCountry);
        TCSharePreferenceManager.getInstance().clear();
        //save again top up country
        TCSharePreferenceManager.getInstance().setString(DataKey.TopUpCountry, topUpCountry);
        RealmController.getInstance().deleteData();
        ((TCMainActivity) getActiveActivity()).replaceFragment(NewWelcomeScreen.getInstance(), true);
        ((TCMainActivity) getActiveActivity()).stopTransactionService();
        ((TCMainActivity) getActiveActivity()).stopLocationTracking();
        ((TCMainActivity) getActiveActivity()).hideFooter();
        TCAppFlyerTrackingEvent.getInstance().reset();
        ((TCMainActivity) getActiveActivity()).updateStatusButtonHowToEarnTec(true);
        new FCMHandler(getActiveActivity()).disableFCM();
        new FCMHandler(getActiveActivity()).enableFCM();
    }

    @Override
    public int getEnterAnimation() {
        return 0;
    }

    @Override
    public int getExitAnimation() {
        return 0;
    }


    public abstract int getFragmentContainerResId();

    public void addFragment(@IdRes int containerId, BaseFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        String tag = fragment.getClass().getCanonicalName();
        ArrayList<String> tags = containers.get(containerId);
        if (tags == null) {
            containers.put(containerId, tags = new ArrayList<>());
            tags.add(tag);
            //int anim = fragment.getEnterAnimation();
            //if (anim == -1) {
            // anim = Utils.DEFAULT_ADD_ANIMATION[0];
            //}
            ft.setCustomAnimations(fragment.getEnterAnimation(), 0, 0, 0);
            ft.add(containerId, fragment, tag);
            //ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.commit();

        } else {
            BaseFragment top = getTopFragment(containerId);
            if (top != null)
                top.onPause();
            //animateAddOut(top);
            tags.add(tag);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction
                    .setCustomAnimations(
                            fragment.getEnterAnimation(), 0, 0, 0)
                    .add(containerId, fragment, tag).commit();
        }
    }

    public void replaceFragment(@IdRes int containerId, BaseFragment fragment, boolean clearStack) {
        if (getSupportFragmentManager() != null) {
            String tag = fragment.getClass().getCanonicalName();
            ArrayList<String> tags = containers.get(containerId);
            if (tags != null) {
                if (clearStack) {
                    popAllBackStack(containerId);
                    addFragment(containerId, fragment);
                } else {
                    boolean isExist = false;
                    for (int i = 0; i < tags.size(); ++i) {
                        BaseFragment entry = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tags.get(i));
                        if (entry != null && entry.getClass().getCanonicalName().equals(tag)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (isExist) {
                        if (tags.size() > 1) {
                            BaseFragment top = getTopFragment(containerId);
                            while (!(top != null && top.getClass().getCanonicalName().equals(tag))) {
                                backStack(containerId);
                                top = getTopFragment(containerId);
                            }
                        }
                    } else {
                        if (tags.size() > 1) {
                            BaseFragment top = getTopFragment(containerId);
                            addFragment(containerId, fragment);
                            if (top != null && !Utils.isEmpty(top.getClass().getCanonicalName()))
                                removeFragment(containerId, top.getClass().getCanonicalName());
                        } else {
                            popAllBackStack(containerId);
                            addFragment(containerId, fragment);
                        }
                    }
                }
            } else {
                addFragment(containerId, fragment);
            }
        }
    }

    protected final void removeFragment(@IdRes int containerId, String tag) {
        ArrayList<String> tags = containers.get(containerId);
        if (tags != null) {
            BaseFragment removed = getTopFragment(containerId);
            if (removed != null && removed.getClass().getCanonicalName().equals(tag)) {
                backStack(containerId);
            } else {
                for (int i = 0; i < tags.size(); ++i) {
                    removed = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tags.get(i));
                    if (removed.getClass().getCanonicalName().equals(tag)) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                                .remove(removed);
                        transaction.commit();
                        tags.remove(i);
                        onFragmentRemoved();
                        break;
                    }
                }
            }
        }
    }

    protected final void popAllBackStack(@IdRes int containerId) {
        if (getSupportFragmentManager() != null) {
            try {
                ArrayList<String> tags = containers
                        .get(containerId);
                if (tags != null) {
                    BaseFragment last = getTopFragment(containerId);
                    //animateAddOut(last);
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction();

                    ArrayList<BaseFragment> fragments = new ArrayList<>();

                    for (String tag : tags)
                        fragments.add((BaseFragment) getSupportFragmentManager().findFragmentByTag(tag));

                    for (BaseFragment fragment : fragments) {
                        transaction.remove(fragment);
                    }
                    if (transaction != null) {
                        transaction.commitAllowingStateLoss();
                        String[] removingTagsArray = new String[tags.size()];
                        tags.toArray(removingTagsArray);
                        onFragmentRemoved();
                    }
                    clearStack(containerId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearStack(@IdRes int containerId) {
        ArrayList<String> tags = containers.get(containerId);
        if (tags != null)
            tags.clear();
    }

    public int getBackStackCount() {
        ArrayList<String> tags = containers.get(getFragmentContainerResId());
        if (tags != null)
            return tags.size();
        else
            return 0;
    }

//    public final BaseFragment getTopFragment() {
//        return processGetTopFragment(getFragmentContainerResId());
//    }

    public final BaseFragment getTopFragment(@IdRes int containerId) {
        try {
            ArrayList<String> tags = containers.get(containerId);
            int size;
            if (tags != null && (size = tags.size()) > 0)
                return (BaseFragment) getSupportFragmentManager().findFragmentByTag(tags.get(size - 1));
        } catch (Exception e) {
            // ignore this exception
        }
        return null;
    }

    public void popFragment(@IdRes int containerId) {
        backStack(containerId);
    }

    public final BaseFragment getSecondTopFragment() {
        try {
            ArrayList<String> tags = containers.get(getFragmentContainerResId());
            int size;
            if (tags != null && (size = tags.size()) > 1)
                return (BaseFragment) getSupportFragmentManager().findFragmentByTag(tags.get(size - 2));
        } catch (Exception e) {
            // ignore this exception
        }
        return null;
    }

    protected final void addFragmentForResult(@IdRes int containerId, int requestCode, BaseFragment toFragment) {
        toFragment.setRequestCode(requestCode);
        addFragment(containerId, toFragment);
    }

    public final void backStack(@IdRes int containerId) {
        if (getSupportFragmentManager() != null) {
            ArrayList<String> tags = containers.get(containerId);
            if (tags != null) {
                if (tags.size() <= 1) {
                    finish();
                } else {
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction();
                    BaseFragment removeFragment = getTopFragment(containerId);
                    if (removeFragment != null) {
                        removeFragment.onPause();
                        tags.remove(tags.size() - 1);
                        transaction.remove(removeFragment);
                        transaction.commit();
                    }
                    Intent finishedResult = removeFragment.getFinishedResult();
                    int requestCode = removeFragment.getRequestCode();
                    int finishedResultCode = removeFragment.getFinishedResultCode();
                    BaseFragment fragment = getTopFragment(containerId);
                    if (fragment != null) {
                        if (fragment.getView() != null) {
                            View view = fragment.getView();
                            //animateBackIn(view, fragment.getBackInAnimation());
                        }
                        fragment.onResume();
                        resumeWithResult(fragment, requestCode, finishedResultCode, finishedResult);


                    }
                }
            }
        }
    }

    private void resumeWithResult(BaseFragment callBackFragment, int requestCode, int finishedResultCode, Intent finishedResult) {
        callBackFragment.onPostResumeWithResult(requestCode, finishedResultCode, finishedResult);
    }

    protected void onFragmentAdded() {
        // Override to handle fragment added
    }

    protected void onFragmentRemoved() {
        // Override to handle fragment removed
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null)
            unbinder.unbind();
        if (BaseProperties.loadingDialog != null) {
            BaseProperties.loadingDialog.dismiss();
            BaseProperties.loadingDialog = null;
        }
        if (BaseProperties.alertDialog != null) {
            BaseProperties.alertDialog.dismiss();
            BaseProperties.alertDialog = null;
        }
        if (BaseProperties.decisionDialog != null) {
            BaseProperties.decisionDialog.dismiss();
            BaseProperties.decisionDialog = null;
        }
        popAllBackStack(getFragmentContainerResId());
        super.onDestroy();
    }


    public void refreshFragment(BaseFragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    @Override
    public final void registerSingleClick(View... views) {
        for (View view : views)
            if (view != null) {
                view.setOnClickListener(null);
                view.setOnTouchListener(null);
                if (!Utils.isExceptionalView(view)) {
                    view.setOnClickListener(getSingleClick());
                }
            }
    }

    @Override
    public final void unregisterSingleClick(View... views) {
        for (View view : views)
            if (view != null) {
                view.setOnClickListener(null);
                view.setOnTouchListener(null);
            }
    }

    @Override
    public final void registerSingleClick(@IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(null);
                if (!Utils.isExceptionalView(view)) {
                    view.setOnClickListener(getSingleClick());
                }
            }
        }
    }

    @Override
    public void unregisterSingleClick(@IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(null);
                view.setOnTouchListener(null);
            }
        }
    }

    @Override
    public final SingleClick getSingleClick() {
        if (singleClick == null) {
            singleClick = new SingleClick();
            singleClick.setListener(this);
        }
        return singleClick;
    }

    @Override
    public final SingleClick getSingleClick(Object object) {
        if (singleClick == null) {
            singleClick = new SingleClick();
            singleClick.setListener(this);
        }
        singleClick.setObject(object);
        return singleClick;
    }

    @android.support.annotation.CallSuper
    @Override
    public void onSingleClick(View v, Object object) {

    }

    @Override
    public void onBaseDestroyView() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);//remove super to allow commit and saveInstanceState
    }
}
