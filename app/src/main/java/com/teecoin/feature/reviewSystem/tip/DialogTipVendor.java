package com.teecoin.feature.reviewSystem.tip;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.feature.general.popup.MessageBaseScreen;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.reviewsystem.TipResponseModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class DialogTipVendor extends TCBaseDialog implements TipEventListener {

    private static final int INPUT_TIP_INDEX = 0;
    private static final int CHECK_TIP_INDEX = 1;
    private static final int RESULT_TIP_INDEX = 2;
    private static final int THANK_FOR_LIKE_INDEX = 3;
    @BindView(R.id.dialog_base_rl_parent_view)
    RelativeLayout rl_parent_view;

    private ArrayList<ViewGroup> container;
    private Context mContext;
    private String destinationPublicKey;
    private String commentId;
    private TipCallback tipCallback;

    public DialogTipVendor(Context context, boolean tipBefore, String destinationPublicKey, String commentId, TipCallback tipCallback) {
        super(context);
        this.mContext = context;
        this.destinationPublicKey = destinationPublicKey;
        this.commentId = commentId;
        this.tipCallback = tipCallback;
        container = new ArrayList<>();
        if (tipBefore) {
            container.add(new ThankForLikeAndTipScreen(mContext, this));
        } else {
            container.add(INPUT_TIP_INDEX, new InputTipScreen(mContext, new TipModel(destinationPublicKey, commentId), this));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip_vendor);
    }

    @Override
    protected void initContentView() {
        rl_parent_view.removeAllViews();
        rl_parent_view.addView(container.get(0));
    }

    @Override
    protected void onViewClick() {

    }

    @Override
    public void onNext(TipModel tipModel) {
        if (tipModel == null) {
            container.add(INPUT_TIP_INDEX, new InputTipScreen(mContext, new TipModel(destinationPublicKey, commentId), this));
        } else if (TCUtils.isEmpty(tipModel.getAmount())) {
            container.clear();
            container.add(new ThankForLikeScreen(mContext, this));
            rl_parent_view.removeAllViews();
            rl_parent_view.addView(container.get(0));
            tipCallback.tipAmount(0);
        } else {
            if (TCUtils.convertToDouble(tipModel.getAmount()) <= 0) {
                container.clear();
                container.add(new ThankForLikeScreen(mContext, this));
                rl_parent_view.removeAllViews();
                rl_parent_view.addView(container.get(0));
                tipCallback.tipAmount(0);
            } else {
                if (container.size() <= 1) {
                    container.add(new CheckTipScreen(mContext, tipModel, this));
                } else {
                    container.remove(CHECK_TIP_INDEX);
                    container.add(CHECK_TIP_INDEX, new CheckTipScreen(mContext, tipModel, this));
                }
                rl_parent_view.removeAllViews();
                rl_parent_view.addView(container.get(CHECK_TIP_INDEX));
            }
        }
    }

    @Override
    public void onBack() {
        rl_parent_view.removeAllViews();
        rl_parent_view.addView(container.get(INPUT_TIP_INDEX));
    }

    @Override
    public void onClose() {
        dismiss();
    }

    @Override
    public void onOkNext() {
        container.clear();
        container.add(INPUT_TIP_INDEX, new InputTipScreen(mContext, new TipModel(destinationPublicKey, commentId), this));
        rl_parent_view.removeAllViews();
        rl_parent_view.addView(container.get(0));
    }

    @Override
    public void tipSuccess(TipResponseModel tipResponseModel) {
        Toast.makeText(mContext, TCUtils.getString(R.string.tip_successfully), Toast.LENGTH_LONG).show();
        rl_parent_view.removeAllViews();
        container.clear();
        container.add(new TipResultScreen(mContext, tipResponseModel, this));
        rl_parent_view.addView(container.get(0));
        if (!TCUtils.isEmpty(tipResponseModel.getAmount())) {
            float amount = Float.parseFloat(tipResponseModel.getAmount());
            tipCallback.tipAmount(amount);
        } else {
            tipCallback.tipAmount(0);
        }
        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        accountModel.calculateBalance(-TCUtils.convertToDouble(tipResponseModel.getTotalAmount()));
        RealmController.getInstance().updateAccountModel(accountModel);
    }

    @Override
    public void tipFail(String errorMessage) {
        //Toast.makeText(mContext, "tip fail:" + errorMessage, Toast.LENGTH_LONG).show();
        MessageBaseScreen messageBaseScreen = new MessageBaseScreen(getContext(), errorMessage);
        messageBaseScreen.show();
    }


}
