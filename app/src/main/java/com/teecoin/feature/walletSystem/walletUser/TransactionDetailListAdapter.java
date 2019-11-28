package com.teecoin.feature.walletSystem.walletUser;

import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class TransactionDetailListAdapter extends RecycleAdapter<TransactionDetailModel> {

    private RecycleListener<TransactionDetailModel> listener;


    public TransactionDetailListAdapter(LayoutInflater inflater, ArrayList<TransactionDetailModel> items, RecycleListener<TransactionDetailModel> listener) {
        super(inflater, items, listener);
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return TransactionDetailViewHolder.class;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_last_transaction;
    }

    @Override
    protected void bindItemView(ItemViewHolder<TransactionDetailModel> holder, TransactionDetailModel data, int position) {
        if (holder instanceof TransactionDetailViewHolder) {
            TransactionDetailViewHolder viewHolder = (TransactionDetailViewHolder) holder;
            if (data.getType().equals(EnumMgr.TransactionType.TransferMoney.getValue())) {
                viewHolder.tv_name.setText(
                        TCUtils.isYourselfASenderMoney(data.getSource()) ?
                                data.getReceiver() : data.getSender());
            } else if (data.getType().equals(EnumMgr.TransactionType.Tip.getValue())) {
                if (RealmController.getInstance().destinationIsMine(data.getDestination())) {
                    if (data.getSender().equals("System")) {
                        viewHolder.tv_name.setText(TCUtils.isUserApp() ? data.getShop_name() : data.getReceiver());
                    } else {
                        viewHolder.tv_name.setText(TCUtils.isUserApp() ? data.getSender() : data.getReceiver());
                    }
                } else {
                    viewHolder.tv_name.setText(TCUtils.isUserApp() ? data.getReceiver() : data.getShop_name());
                }

            } else {
                viewHolder.tv_name.setText(TCUtils.isUserApp() ? data.getShop_name() : data.getSender());
            }
            if (!TCUtils.isEmpty(data.getCreated())) {
                viewHolder.tv_day.setText(TCDateUtility.convertToCurrentTimeZoneDate(data.getCreated(),
                        TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                        TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HH_MM)));
            }

            if (!TCUtils.isEmpty(data.getAmount())) {
                viewHolder.tv_coin.setText(String.format(TCUtils.getString(R.string.string_format_1),
                        TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, data.getAmount()), TCUtils.getString(R.string.tee_coin_symbol)));
            }

            viewHolder.iv_icon.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_40);
            viewHolder.iv_icon.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_40);


            if (!TCUtils.isEmpty(data.getIcon())) {
                Glide.with(getActiveActivity()).load(data.getIcon()).into(viewHolder.iv_icon);
                if (!TCUtils.isEmpty(data.getStatus()) && data.getStatus().equals(TransactionDetailModel.TransactionDetailStatus.FAILED.name())) {
                    viewHolder.iv_icon.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_34);
                    viewHolder.iv_icon.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_34);
                    //  viewHolder.iv_icon.setLayoutParams(marginLayoutParams);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(R.dimen.fs_34, R.dimen.fs_34);
//              //      params.setMargins(15, 1, 1, 1);
                    // viewHolder.iv_icon.setLayoutParams(params);
                    viewHolder.iv_icon.requestLayout();

                }
            } else {
                setIcon(data, viewHolder);
            }


            if (data.getType().equals(EnumMgr.TransactionType.Checkin.getValue())) {
                viewHolder.iv_icon.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_32);
                viewHolder.iv_icon.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_32);
            }

        }
    }

    public void updateStatusReview(String transaction_id) {
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getTransaction_id().equals(transaction_id)) {
                    items.get(i).setIs_review_success(true);
                    notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    private void setIcon(TransactionDetailModel data, TransactionDetailViewHolder viewHolder) {
        if (data.getType().equals(EnumMgr.TransactionType.TransferMoney.getValue())) {
            viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(
                    TCUtils.isYourselfASenderMoney(data.getSource()) ?
                            R.drawable.ic_coin_out : R.drawable.ic_coin_in));
        } else if (data.getType().equals(EnumMgr.TransactionType.CouponPurchase.getValue())) {
            viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(TCUtils.isUserApp() ?
                    R.drawable.ic_coin_out : R.drawable.ic_gift_gold));
        } else if (data.getType().equals(EnumMgr.TransactionType.Payment.getValue())) {
            viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(TCUtils.isUserApp() ?
                    R.drawable.ic_coin_out : R.drawable.ic_gift_gold));
        } else if (data.getType().equals(EnumMgr.TransactionType.Tip.getValue())) {
            viewHolder.iv_icon.setImageDrawable(data.getDestination().equals(RealmController.getInstance().getPublicKey()) ? TCUtils.getDrawable(R.drawable.ic_gift_gold) : TCUtils.getDrawable(R.drawable.ic_coin_out));

        } else if (data.getType().equals(EnumMgr.TransactionType.Checkin.getValue())) {
            if (TCUtils.isUserApp()) {
                viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_coupon_checkin));
                viewHolder.iv_icon.getLayoutParams().width = TCUtils.getDimension(R.dimen.fs_32);
                viewHolder.iv_icon.getLayoutParams().height = TCUtils.getDimension(R.dimen.fs_32);
                viewHolder.iv_icon.requestLayout();
            } else {
                viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(TCUtils.isUserApp() ?
                        R.drawable.ic_gift_gold : R.drawable.ic_coin_out));
            }
        } else {
            viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(TCUtils.isUserApp() ?
                    R.drawable.ic_gift_gold : R.drawable.ic_coin_out));
        }

        if (!TCUtils.isEmpty(data.getStatus()) && data.getStatus().equals(TransactionDetailModel.TransactionDetailStatus.FAILED.name())) {
            viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_transaction_failed));
            //   viewHolder.iv_icon.setPadding(15,0,15,0);
            viewHolder.iv_icon.requestLayout();
        }
    }
}
