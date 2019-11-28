package core.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.teecoin.base.TCConfirmListener;
import com.teecoin.base.TCDecisionListener;

public abstract class BaseFragment extends android.support.v4.app.Fragment implements BaseInterface, SingleClick.SingleClickListener {

    private BaseActivity activeActivity;
    private int finishedResultCode = -1;
    private Intent finishedResult;
    private int requestCode;
    private boolean isFinishedWithResult = false;

    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCELED = 2;
    private SingleClick singleClick = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public AppCompatActivity getActiveActivity() {
        return BaseApplication.getActiveActivity();
    }

    @Override
    public void showLoadingDialog() {
        if (getActivity() != null && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).showLoadingDialog();
        else if (getActiveActivity() != null && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity()).showLoadingDialog();
    }

    @Override
    public void hideLoadingDialog() {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).hideLoadingDialog();
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity())
                    .hideLoadingDialog();
    }

    @Override
    public void showAlertDialog(int id, String title, String message, String confirm, TCConfirmListener confirm_listener, Object onWhat) {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).showAlertDialog(id, title, message, confirm, confirm_listener, onWhat);
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity())
                    .showAlertDialog(id, title, message, confirm, confirm_listener, onWhat);
        else
            activeActivity.showAlertDialog(id, title, message, confirm, confirm_listener, onWhat);
    }

    @Override
    public void showDecisionDialog(int id, String title, String message, String no, String cancel, String yes, TCDecisionListener decision_listener, Object onWhat) {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).showDecisionDialog(id, title, message, no,
                    cancel, yes, decision_listener, onWhat);
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity())
                    .showDecisionDialog(id,
                            title, message, no, cancel, yes, decision_listener, onWhat);
        else
            activeActivity.showDecisionDialog(id,
                    title, message, no, cancel, yes, decision_listener, onWhat);
    }

    @Override
    public void showForceLoginDialog() {

    }

    @IdRes
    public final int getMainContainerId() {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            return ((BaseActivity) getActivity())
                    .getFragmentContainerResId();
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            return ((BaseActivity) getActiveActivity())
                    .getFragmentContainerResId();
        else
            return activeActivity.getFragmentContainerResId();
    }

    protected final void addFragment(BaseFragment fragment) {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).addFragment(getMainContainerId(), fragment);
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity()).addFragment(getMainContainerId(), fragment);
        else
            activeActivity.addFragment(getMainContainerId(), fragment);
    }

    protected final void addFragment(@IdRes int containerId, BaseFragment fragment) {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).addFragment(containerId, fragment);
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity()).addFragment(containerId, fragment);
        else
            activeActivity.addFragment(containerId, fragment);
    }

    protected final void replaceFragment(BaseFragment fragment, boolean clearStack) {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).replaceFragment(getMainContainerId(), fragment, clearStack);
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity()).replaceFragment(getMainContainerId(), fragment, clearStack);
        else
            activeActivity.replaceFragment(getMainContainerId(), fragment, clearStack);
    }

    protected final void popFragment() {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).popFragment(getMainContainerId());
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity()).popFragment(getMainContainerId());
        else
            activeActivity.popFragment(getMainContainerId());
    }

    protected final BaseFragment getTopFragment() {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            return ((BaseActivity) getActivity()).getTopFragment(getMainContainerId());
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            return ((BaseActivity) getActiveActivity()).getTopFragment(getMainContainerId());
        else
            return activeActivity.getTopFragment(getMainContainerId());
    }

    protected final void refreshFragment(BaseFragment fragment) {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).refreshFragment(fragment);
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity()).refreshFragment(fragment);
        else
            activeActivity.refreshFragment(fragment);
    }

//    protected final BaseFragment getLastFragment() {
//        if (getActivity() != null
//                && getActivity() instanceof BaseActivity)
//            return ((BaseActivity) getActivity()).getLastFragment();
//        else if (getActiveActivity() != null
//                && getActiveActivity() instanceof BaseActivity)
//            return ((BaseActivity) getActiveActivity()).getLastFragment();
//        else
//            return activeActivity.getLastFragment();
//    }

    protected final void addFragmentForResult(int requestCode, BaseFragment toFragment) {
        if (getActivity() != null
                && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).addFragmentForResult(getMainContainerId(), requestCode, toFragment);
        else if (getActiveActivity() != null
                && getActiveActivity() instanceof BaseActivity)
            ((BaseActivity) getActiveActivity()).addFragmentForResult(getMainContainerId(), requestCode, toFragment);
        else
            activeActivity.addFragmentForResult(getMainContainerId(), requestCode, toFragment);
    }

//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//        if (getActivity() != null)
//            getActivity().startActivityForResult(intent, requestCode);˜
//            activeActivity.startActivityForResult(intent, requestCode);
//    }

    protected final void finishWithResult(int resultCode, Intent result) {
        isFinishedWithResult = true;
        finishedResultCode = resultCode;
        finishedResult = result;
        popFragment();
    }

    public int getFinishedResultCode() {
        return finishedResultCode;
    }

    public void setFinishedResultCode(int finishedResultCode) {
        this.finishedResultCode = finishedResultCode;
    }

    public Intent getFinishedResult() {
        return finishedResult;
    }

    public void setFinishedResult(Intent finishedResult) {
        this.finishedResult = finishedResult;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public boolean isFinishedWithResult() {
        return isFinishedWithResult;
    }

    public void setFinishedWithResult(boolean finishedWithResult) {
        isFinishedWithResult = finishedWithResult;
    }

    @Override
    public int getEnterAnimation() {
        return 0;
    }

    @Override
    public int getExitAnimation() {
        return 0;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activeActivity = null;
    }

    @Override
    public void onDestroyView() {
        onBaseDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            activeActivity = (BaseActivity) activity;
        }
    }

    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {

    }

    @Override
    public final void registerSingleClick(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(null);
                if (!Utils.isExceptionalView(view)) {
                    view.setOnClickListener(getSingleClick());
                }
            }
        }
    }

    @Override
    public final void unregisterSingleClick(View... views) {
        for (View view : views)
            if (view != null) {
                view.setOnClickListener(null);
            }
    }

    @Override
    public final void registerSingleClick(@IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null && !Utils.isExceptionalView(view)) {
                view.setOnClickListener(null);
                view.setOnClickListener(getSingleClick());
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

    protected final View findViewById(@IdRes int id) {
        if (getView() != null) {
            return getView().findViewById(id);
        }
        return null;
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

    @CallSuper
    @Override
    public void onSingleClick(View v, Object object) {
//        TCLog.d("hung button1:" + v.getResources().getResourceEntryName(v.getId()));
    }

}
