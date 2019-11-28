package com.teecoin.feature.walletSystem.topup;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.walletsystem.CryptoModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class TopUpAdapter extends RecycleAdapter<CryptoModel> {

    private double inputAmountTec = 0;
    private boolean notNotifyCountdown = false;
    private boolean isShowAddress = false;

    TopUpAdapter(LayoutInflater inflater, ArrayList<CryptoModel> items, RecycleListener<CryptoModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return TopupViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_crypto;
    }

    @Override
    protected void bindItemView(ItemViewHolder<CryptoModel> holder, CryptoModel data, int position) {

        if (holder instanceof TopupViewHolder) {

            TopupViewHolder viewHolder = (TopupViewHolder) holder;

            viewHolder.tvTitle.setText(data.getCrypto_name());

            Glide.with(getActiveActivity()).load(data.getIcon()).into(viewHolder.ivIcon);

            if (inputAmountTec == 0) {
                viewHolder.tvExchangeRate.setText(String.format(TCUtils.getString(R.string.top_up_exchange),
                        "*1", data.getCrypto_type(), TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, TCUtils.convertToDouble(data.getPrice_in_tec()))));
            } else {
                double valueCalculate = inputAmountTec / TCUtils.convertToDouble(data.getPrice_in_tec());
                String valueCryptoString = TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, valueCalculate);
                viewHolder.tvExchangeRate.setText(String.format("*%s %s",
                        data.getCrypto_type(), valueCryptoString/*, TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, inputAmountTec)*/));
            }

            if (TextUtils.isEmpty(data.getAddress())) {
                viewHolder.tvShowWallet.setVisibility(View.VISIBLE);
                viewHolder.tvTimer.setVisibility(View.GONE);
                viewHolder.rlAddress.setVisibility(View.GONE);
                viewHolder.tvShowWallet.setOnClickListener(view -> listener.onItemClick(view, data, position, EnumMgr.ClickType.ShowWallet));
            } else {
                viewHolder.tvShowWallet.setVisibility(View.GONE);

                viewHolder.tvTimer.setVisibility(View.VISIBLE);
                if (!notNotifyCountdown || isShowAddress) {
                    isShowAddress = false;
                    new CountDownTimer(data.getRemain_time() * 1000, TCConstant.ONE_SECOND_IN_MILLISECOND) {
                        @SuppressLint("SetTextI18n")
                        public void onTick(long millisUntilFinished) {

//                        long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
//                        millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                            long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                            millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                            millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

//                        if (days > 1) {
//                            viewHolder.tvTimer.setText(days + " days - " + hours + ":" + minutes + ":" + seconds);
//                        } else if (days == 1) {
//                            viewHolder.tvTimer.setText(days + " day - " + hours + ":" + minutes + ":" + seconds);
//                        } else {
//                            viewHolder.tvTimer.setText(hours + ":" + minutes + ":" + seconds);
//                        }
                            @SuppressLint("DefaultLocale")
                            String hms = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                            viewHolder.tvTimer.setText(hms);
                        }

                        public void onFinish() {

                        }
                    }.start();
                }

                viewHolder.rlAddress.setVisibility(View.VISIBLE);
                viewHolder.tvAddressWallet.setText(data.getAddress());

                viewHolder.ivQr.setOnClickListener(view -> listener.onItemClick(view, data, position, EnumMgr.ClickType.ShowQrCode));
                viewHolder.ivCopy.setOnClickListener(view -> listener.onItemClick(view, data, position, EnumMgr.ClickType.CopyAddress));
                viewHolder.ivShare.setOnClickListener(view -> listener.onItemClick(view, data, position, EnumMgr.ClickType.ShareTopUp));

//                viewHolder.vButton.post(() -> viewHolder.tvExchangeRate.getLayoutParams().width = viewHolder.vButton.getMeasuredWidth());
            }
        }
    }

    void showAddress(int pos, CryptoModel cryptoModel) {
        items.get(pos).setAddress(cryptoModel.getAddress());
        items.get(pos).setRemain_time(cryptoModel.getRemain_time());
        isShowAddress = true;
        notifyItemChanged(pos);
    }

    void calculateRate(double tecAmount) {
        inputAmountTec = tecAmount;
        notNotifyCountdown = true;
        notifyDataSetChanged();
    }
}
