package com.teecoin.feature.general.setPasscode;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import core.dialog.GeneralDialog;

public class SetPasscodeDialog extends TCBaseDialog {
    @BindView(R.id.frg_set_passcode_tv_enter_code)
    TextView tv_enter_code;
    @BindView(R.id.frg_set_passcode_tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.frg_set_passcode_tv_no_match)
    TextView tv_no_match;
    //    -------------
    @BindView(R.id.frg_set_passcode_iv_code_1)
    ImageView iv_code_1;
    @BindView(R.id.frg_set_passcode_iv_code_2)
    ImageView iv_code_2;
    @BindView(R.id.frg_set_passcode_iv_code_3)
    ImageView iv_code_3;
    @BindView(R.id.frg_set_passcode_iv_code_4)
    ImageView iv_code_4;
    //    --------
    @BindView(R.id.frg_set_passcode_tv_num_1)
    TextView tv_num_1;
    @BindView(R.id.frg_set_passcode_tv_num_2)
    TextView tv_num_2;
    @BindView(R.id.frg_set_passcode_tv_num_3)
    TextView tv_num_3;
    @BindView(R.id.frg_set_passcode_tv_num_4)
    TextView tv_num_4;
    @BindView(R.id.frg_set_passcode_tv_num_5)
    TextView tv_num_5;
    @BindView(R.id.frg_set_passcode_tv_num_6)
    TextView tv_num_6;
    @BindView(R.id.frg_set_passcode_tv_num_7)
    TextView tv_num_7;
    @BindView(R.id.frg_set_passcode_tv_num_8)
    TextView tv_num_8;
    @BindView(R.id.frg_set_passcode_tv_num_9)
    TextView tv_num_9;
    @BindView(R.id.frg_set_passcode_tv_num_0)
    TextView tv_num_0;
    //    -----------------
    @BindView(R.id.frg_set_passcode_rl_1)
    RelativeLayout rl_1;
    @BindView(R.id.frg_set_passcode_rl_2)
    RelativeLayout rl_2;
    @BindView(R.id.frg_set_passcode_rl_3)
    RelativeLayout rl_3;
    @BindView(R.id.frg_set_passcode_rl_4)
    RelativeLayout rl_4;
    @BindView(R.id.frg_set_passcode_rl_5)
    RelativeLayout rl_5;
    @BindView(R.id.frg_set_passcode_rl_6)
    RelativeLayout rl_6;
    @BindView(R.id.frg_set_passcode_rl_7)
    RelativeLayout rl_7;
    @BindView(R.id.frg_set_passcode_rl_8)
    RelativeLayout rl_8;
    @BindView(R.id.frg_set_passcode_rl_9)
    RelativeLayout rl_9;
    @BindView(R.id.frg_set_passcode_rl_0)
    RelativeLayout rl_0;
    @BindView(R.id.frg_set_passcode_rl_delete)
    RelativeLayout rl_delete;
    @BindView(R.id.frg_set_pass_code_tv_remove_passcode)
    TextView tv_remove_passcode;


    private TCSetPassCodeListener passCodeListener;
    //---
    private int count = 0;
    private boolean isVerify = false;
    private int countSetPassCode = 0;
    private List<Integer> arrPass = new ArrayList<>();
    private List<Integer> arrVerifyPass = new ArrayList<>();
    private boolean unLockPassCode;
    private boolean changePassCode;
    private boolean verifyOther;// verify Input some Screen, don't need Close App when initClickEvent BackPressed()

    public SetPasscodeDialog(Context context, boolean isUnLock, boolean isChangePassCode, boolean isVerifyOther, TCSetPassCodeListener tcCloseAppListener) {
        super(context);
        this.unLockPassCode = isUnLock;
        this.changePassCode = isChangePassCode;
        this.verifyOther = isVerifyOther;
        this.passCodeListener = tcCloseAppListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_set_passcode);
    }

    @Override
    protected void initContentView() {
        updateTitleBar(TCUtils.getString(R.string.text_set_passcode).toUpperCase());
        setFullScreen(!unLockPassCode);
        if (changePassCode) {
            tv_enter_code.setText(TCUtils.getString(R.string.enter_your_old_passcode));
        }
        tv_remove_passcode.setVisibility(View.GONE);
    }

    @Override
    protected void onViewClick() {
        tv_num_1.setOnClickListener(v -> handleSetCode(tv_num_1, rl_1, 1));
        tv_num_2.setOnClickListener(v -> handleSetCode(tv_num_2, rl_2, 2));
        tv_num_3.setOnClickListener(v -> handleSetCode(tv_num_3, rl_3, 3));
        tv_num_4.setOnClickListener(v -> handleSetCode(tv_num_4, rl_4, 4));
        tv_num_5.setOnClickListener(v -> handleSetCode(tv_num_5, rl_5, 5));
        tv_num_6.setOnClickListener(v -> handleSetCode(tv_num_6, rl_6, 6));
        tv_num_7.setOnClickListener(v -> handleSetCode(tv_num_7, rl_7, 7));
        tv_num_8.setOnClickListener(v -> handleSetCode(tv_num_8, rl_8, 8));
        tv_num_9.setOnClickListener(v -> handleSetCode(tv_num_9, rl_9, 9));
        tv_num_0.setOnClickListener(v -> handleSetCode(tv_num_0, rl_0, 0));

        rl_delete.setOnClickListener(v -> handleSetCodeRemove());
        tv_cancel.setOnClickListener(v -> onCancel());

        dialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onCancel();
                    return true;
                }
                return false;
            }
        });
        tv_remove_passcode.setOnClickListener(v -> showRemovePassCodeDialog());
    }

    private void showRemovePassCodeDialog() {
        new GeneralDialog(getContext(), View.NO_ID,
                TCUtils.getString(R.string.title),
                TCUtils.getString(R.string.remove_passcode_message),
                TCUtils.getString(R.string.text_yes),
                TCUtils.getString(R.string.text_no), "", new TCDecisionListener() {
            @Override
            public void onPositiveButtonClicked(int id, Object onWhat) {
                removePassCode();
            }

            @Override
            public void onNegativeButtonClicked(int id, Object onWhat) {

            }

            @Override
            public void onNeutralButtonClicked(int id, Object onWhat) {
                dismiss();
            }
        }, null).show();
    }

    private void removePassCode() {
        TCSharePreferenceManager.getInstance().clearByKey(DataKey.Passcode);
        if (passCodeListener != null)
            passCodeListener.onRemovePassCode();
        dismiss();
    }

    private void onCancel() {
        if (!verifyOther) {
            passCodeListener.onCloseApp(true);
        }
        passCodeListener.onUnClockSuccess(false);
        dismiss();
    }

    private void handleSetCode(TextView numText, RelativeLayout viewNum, int numValue) {
        setEnableNumber(false);
        tv_no_match.setVisibility(View.INVISIBLE);
        unSelectorNumber();
        numText.setTextColor(TCUtils.getColor(R.color.c_b2973f));
        viewNum.setSelected(true);
        if (!isVerify) {
            arrPass.add(numValue);
        } else {
            arrVerifyPass.add(numValue);
        }
        setCode();
    }

    private void handleSetCodeRemove() {
        unSelectorNumber();
        rl_delete.setSelected(true);
        removeCode();
    }

    private void setCode() {
        count++;
        if (count == 1) {
            iv_code_1.setSelected(true);
            setEnableNumber(true);
        } else if (count == 2) {
            iv_code_2.setSelected(true);
            setEnableNumber(true);
        } else if (count == 3) {
            iv_code_3.setSelected(true);
            setEnableNumber(true);
        } else if (count == 4) {
            iv_code_4.setSelected(true);
            count = 0;
            new Handler().postDelayed(() -> {
                //change code session
                if (changePassCode) {
                    if (countSetPassCode == 0) {
                        if (TCSharePreferenceManager.getInstance().getString(DataKey.Passcode).contentEquals(getPassCodeInput())) {
                            tv_enter_code.setText(TCUtils.getString(R.string.enter_your_new_passcode));
                            unSelectorCode();
                            unSelectorNumber();
                            arrPass.clear();
                            countSetPassCode++;
                            tv_remove_passcode.setVisibility(View.VISIBLE);
                        } else {
                            tv_enter_code.setText(TCUtils.getString(R.string.enter_your_old_passcode));
                            inputAgainPassCode();
                        }
                    } else if (countSetPassCode == 1) {
                        tv_enter_code.setText(TCUtils.getString(R.string.set_passcode_verify_passcode));
                        unSelectorCode();
                        unSelectorNumber();
                        countSetPassCode++;
                        isVerify = true;
                    } else {
                        checkPassCode();
                    }
                } else { //set normal
                    if (!isVerify) {
                        tv_enter_code.setText(TCUtils.getString(R.string.set_passcode_verify_passcode));
                        isVerify = true;
                        isSelect(false);
                        // set code when resume app
                        if (unLockPassCode) {
                            if (TCSharePreferenceManager.getInstance().getString(DataKey.Passcode).contentEquals(getPassCodeInput())) {
                                passCodeListener.onUnClockSuccess(true);
                                dismiss();
                            } else {
                                inputAgainPassCode();
                            }
                        } else {
                            countDownTime();
                        }
                    } else {
                        checkPassCode();
                    }
                }
                setEnableNumber(true);
            }, 400L);
        }
    }

    private String getPassCodeInput() {
        StringBuilder passCodeInputBuilder = new StringBuilder();
        for (int i = 0; i < arrPass.size(); i++) {
            passCodeInputBuilder.append(arrPass.get(i));
        }
        return passCodeInputBuilder.toString();
    }

    private void inputAgainPassCode() {
        tv_enter_code.setText(TCUtils.getString(R.string.set_passcode_enter_your_passcode));
        tv_no_match.setVisibility(View.VISIBLE);
        unSelectorCode();
        unSelectorNumber();
        isSelect(true);
        isVerify = false;
        arrPass.clear();
    }

    private void checkPassCode() {
        if (checkPasscode()) {
            new Handler().postDelayed(() -> {
                Toast.makeText(getContext(), TCUtils.getString(R.string.you_changed_passcode), Toast.LENGTH_SHORT).show();
                savePasscode();
                countSetPassCode = 0;
                if (passCodeListener != null) {
                    passCodeListener.onFinishChangePassCode();
                }
                dismiss();
            }, 500L);

        } else {// pass code no match
            if (!changePassCode) {// check first setting passcode
                tv_no_match.setVisibility(View.VISIBLE);
                unSelectorCode();
                unSelectorNumber();
                arrPass.clear();
                arrVerifyPass.clear();
                isVerify = false;
                count = 0;
                tv_enter_code.setText(TCUtils.getString(R.string.set_passcode_enter_your_passcode));
            } else {// // check change setting passcode
                tv_no_match.setVisibility(View.VISIBLE);
                tv_enter_code.setText(TCUtils.getString(R.string.enter_your_new_passcode));
                arrPass.clear();
                arrVerifyPass.clear();
                isVerify = false;
                count = 0;
                countSetPassCode = 1;
                countDownTime();
            }
        }
    }

    private void removeCode() {
        if (count > 0) {
            if (!isVerify) {
                if (count == 1) {
                    arrPass.remove(0);
                    iv_code_1.setSelected(false);
                } else if (count == 2) {
                    arrPass.remove(1);
                    iv_code_2.setSelected(false);
                } else if (count == 3) {
                    arrPass.remove(2);
                    iv_code_3.setSelected(false);
                }
            } else {
                if (count == 1) {
                    arrVerifyPass.remove(0);
                    iv_code_1.setSelected(false);
                } else if (count == 2) {
                    arrVerifyPass.remove(1);
                    iv_code_2.setSelected(false);
                } else if (count == 3) {
                    arrVerifyPass.remove(2);
                    iv_code_3.setSelected(false);
                }
            }
            count--;
        }
    }

    private void unSelectorCode() {
        iv_code_1.setSelected(false);
        iv_code_2.setSelected(false);
        iv_code_3.setSelected(false);
        iv_code_4.setSelected(false);
    }

    private void unSelectorNumber() {
        tv_num_1.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        tv_num_2.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        tv_num_3.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        tv_num_4.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        tv_num_5.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        tv_num_6.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        tv_num_7.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        tv_num_8.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        tv_num_9.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        tv_num_0.setTextColor(TCUtils.getColor(R.color.c_ffffff));
        rl_1.setSelected(false);
        rl_2.setSelected(false);
        rl_3.setSelected(false);
        rl_4.setSelected(false);
        rl_5.setSelected(false);
        rl_6.setSelected(false);
        rl_7.setSelected(false);
        rl_8.setSelected(false);
        rl_9.setSelected(false);
        rl_0.setSelected(false);
    }

    private void setEnableNumber(boolean isEnable) {
        tv_num_1.setEnabled(isEnable);
        tv_num_2.setEnabled(isEnable);
        tv_num_3.setEnabled(isEnable);
        tv_num_4.setEnabled(isEnable);
        tv_num_5.setEnabled(isEnable);
        tv_num_6.setEnabled(isEnable);
        tv_num_7.setEnabled(isEnable);
        tv_num_8.setEnabled(isEnable);
        tv_num_9.setEnabled(isEnable);
        tv_num_0.setEnabled(isEnable);
    }

    private void countDownTime() {
        new Handler().postDelayed(() -> {
            unSelectorCode();
            unSelectorNumber();
        }, 500L);
    }

    private void isSelect(boolean isSelect) {
        tv_num_0.setEnabled(isSelect);
        tv_num_1.setEnabled(isSelect);
        tv_num_2.setEnabled(isSelect);
        tv_num_3.setEnabled(isSelect);
        tv_num_4.setEnabled(isSelect);
        tv_num_5.setEnabled(isSelect);
        tv_num_6.setEnabled(isSelect);
        tv_num_7.setEnabled(isSelect);
        tv_num_8.setEnabled(isSelect);
        tv_num_9.setEnabled(isSelect);
        rl_delete.setEnabled(isSelect);
    }

    private boolean checkPasscode() {
        return arrPass.equals(arrVerifyPass);
    }

    private void savePasscode() {
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < arrPass.size(); i++) {
            pass.append(arrPass.get(i).toString());
        }
        TCSharePreferenceManager.getInstance().setString(DataKey.Passcode, pass.toString());
    }
}
