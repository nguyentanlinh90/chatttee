package core.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.base.TCDecisionListener;

import butterknife.BindView;
import core.base.BaseDialog;
import core.base.Utils;


public class GeneralDialog extends BaseDialog {

    private final String title;
    private final String message;
    private final String yes;
    private final String no;
    private final String cancel;
    private final int id;
    @BindView(R.id.general_dialog_bt_yes)
    View general_dialog_bt_yes;
    @BindView(R.id.general_dialog_bt_no)
    View general_dialog_bt_no;
    @BindView(R.id.general_dialog_bt_cancel)
    View general_dialog_bt_cancel;
    @BindView(R.id.general_dialog_tv_yes)
    TextView general_dialog_tv_yes;
    @BindView(R.id.general_dialog_tv_no)
    TextView general_dialog_tv_no;
    @BindView(R.id.general_dialog_tv_cancel)
    TextView general_dialog_tv_cancel;
    @BindView(R.id.general_dialog_tv_title)
    TextView general_dialog_tv_title;
    @BindView(R.id.general_dialog_tv_message)
    TextView general_dialog_tv_message;
    private Object onWhat;
    private TCDecisionListener decision_listener;
    private TCConfirmListener confirm_listener;

    public GeneralDialog(Context context, int id, String title, String message, String no, String cancel, String yes,
                         TCDecisionListener decision_listener, Object onWhat) {
        super(context);
        this.id = id;
        this.message = message;
        this.title = title;
        this.yes = yes;
        this.no = no;
        this.cancel = cancel;
        this.onWhat = onWhat;
        this.decision_listener = decision_listener;
    }

    public GeneralDialog(Context context, int id, String title, String message, String confirm,
                         TCConfirmListener confirm_listener, Object onWhat) {
        super(context);
        this.id = id;
        this.message = message;
        this.title = title;
        this.yes = confirm;
        this.no = null;
        this.cancel = null;
        this.onWhat = onWhat;
        this.confirm_listener = confirm_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_general);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initContentView() {
        registerOnClickAction();

        if (general_dialog_tv_title != null) {
            if (!Utils.isEmpty(title))
                general_dialog_tv_title.setText(title);
            else
                general_dialog_tv_title.setVisibility(View.GONE);
        }

        if (general_dialog_tv_message != null) {
            if (!Utils.isEmpty(message))
                general_dialog_tv_message.setText(message);
            else
                general_dialog_tv_message.setVisibility(View.GONE);
        }

        if (general_dialog_bt_yes != null) {
            if (!Utils.isEmpty(yes)) {
                if (general_dialog_tv_yes != null)
                    general_dialog_tv_yes.setText(yes);
            } else {
                general_dialog_bt_yes.setVisibility(View.GONE);
            }
        }

        if (general_dialog_bt_no != null) {
            if (!Utils.isEmpty(no)) {
                if (general_dialog_tv_no != null)
                    general_dialog_tv_no.setText(no);
            } else {
                general_dialog_bt_no.setVisibility(View.GONE);
            }
        }


        if (general_dialog_bt_cancel != null) {
            if (!Utils.isEmpty(cancel)) {
                if (general_dialog_tv_cancel != null)
                    general_dialog_tv_cancel.setText(cancel);
            } else {
                general_dialog_bt_cancel.setVisibility(View.GONE);
            }
        }
    }



    private void registerOnClickAction(){
        general_dialog_tv_yes.setOnClickListener(v -> positiveButtonClick());
        general_dialog_bt_yes.setOnClickListener(v -> positiveButtonClick());

        general_dialog_tv_no.setOnClickListener(v -> negativeButtonClick());
        general_dialog_bt_no.setOnClickListener(v -> positiveButtonClick());

        general_dialog_tv_cancel.setOnClickListener(v -> neutralButtonClick());
        general_dialog_bt_cancel.setOnClickListener(v -> neutralButtonClick());
    }

    private void neutralButtonClick() {
        dismiss();
        if (decision_listener != null){
            decision_listener.onNeutralButtonClicked(id, onWhat);
        }
    }

    private void negativeButtonClick() {
        dismiss();
        if (decision_listener != null){
            decision_listener.onNegativeButtonClicked(id, onWhat);
        }
    }

    private void positiveButtonClick() {
        dismiss();
        if (decision_listener != null)
            decision_listener.onPositiveButtonClicked(id, onWhat);
        if (confirm_listener != null){
            confirm_listener.onConfirmed(id, onWhat);
        }
    }
}
