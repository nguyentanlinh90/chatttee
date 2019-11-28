package core.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

//import com.rey.material.widget.ProgressView;
import com.teecoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingDialog extends ProgressDialog {

//    private ProgressView dialog;

    @BindView(R.id.rlt_progress_view_base)
    View rlt_progress_view_base;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_view_base);
        ButterKnife.bind(this);
        //TODO
//        dialog = (ProgressView)findViewById(R.id.progress_view_base);
    }

    public void showLoading(){
        rlt_progress_view_base.setVisibility(View.VISIBLE);
    }

    public void closeLoading(){
        rlt_progress_view_base.setVisibility(View.GONE);
    }

}
