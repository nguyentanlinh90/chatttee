package core.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialog extends Dialog implements SingleClick.SingleClickListener {

    private Unbinder unbinder;
    private SingleClick singleClick;

    public BaseDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        unbinder = ButterKnife.bind(this, view);
        super.setContentView(view);
        initContentView();

    }


    @Override
    public void setContentView(View view) {
        unbinder = ButterKnife.bind(this, view);
        super.setContentView(view);
        initContentView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        unbinder = ButterKnife.bind(this, view);
        super.setContentView(view, params);
        initContentView();

    }
    private void init() {
        //getWindow().setGravity(Gravity.CENTER);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //setCancelable(false);
        //setCanceledOnTouchOutside(false);
    }

    protected abstract void initContentView();
    @Override
    public void onDetachedFromWindow() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDetachedFromWindow();
    }

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

    public final void unregisterSingleClick(View... views) {
        for (View view : views)
            if (view != null) {
                view.setOnClickListener(null);
            }
    }

    public final void registerSingleClick(@IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null && !Utils.isExceptionalView(view)) {
                view.setOnClickListener(null);
                view.setOnClickListener(getSingleClick());
            }
        }
    }

    public void unregisterSingleClick(@IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(null);
                view.setOnTouchListener(null);
            }
        }
    }

    public final SingleClick getSingleClick() {
        if (singleClick == null) {
            singleClick = new SingleClick();
            singleClick.setListener(this);
        }
        return singleClick;
    }

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