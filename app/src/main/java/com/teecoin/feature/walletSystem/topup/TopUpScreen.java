package com.teecoin.feature.walletSystem.topup;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.CryptoModel;
import com.teecoin.model.walletsystem.TopUpPostModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.CryptoListRequest;
import com.teecoin.myapi.apirequest.walletsystem.CryptoRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import butterknife.BindView;

public class TopUpScreen extends TCWalletBaseFragment {
    @BindView(R.id.frg_top_up_et_amount)
    EditText etAmount;

    @BindView(R.id.frg_top_up_tv_kyc)
    TextView tvKYC;

    @BindView(R.id.frg_top_up_list)
    TCRecyclerView recyclerView;

    private TopUpAdapter adapter;
    private ArrayList<CryptoModel> cryptoModelList;

    public static TopUpScreen getInstance() {
        return new TopUpScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_up, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.top_up));
        try {
            hideFooter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindView() {
        getActiveActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getListCrypto();

        initList();

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String amount = s.toString();
                if (s.length() > 0) {
                    /*if (!TCUtils.checkLimitDecimalPlaces(s.toString(), TCConstant.LIMIT_DECIMAL_THREE)) {
                        amount = amount.substring(0, amount.length() - 1);
                        etAmount.setText(amount);
                        etAmount.setSelection(etAmount.getText().length());
                    }*/

                    adapter.calculateRate(TCUtils.convertToDouble(amount));

                } else {
                    adapter.calculateRate(0);
                }
            }
        });

        tvKYC.setOnClickListener(view -> addFragment(WebViewScreen.getInstance(TCConstant.TOP_UP_PROFILE_VERIFY, TCUtils.getString(R.string.user_profile_verification))));
    }

    private void initList() {

        cryptoModelList = new ArrayList<>();

        adapter = new TopUpAdapter(LayoutInflater.from(getActiveActivity()), cryptoModelList, (view, item, position, clickType) -> {

            if (clickType == EnumMgr.ClickType.ShowWallet) {

                TopUpPostModel topUpPostModel = new TopUpPostModel(item.getCrypto_type());
                getAddressWallet(topUpPostModel, position);

            } else if (clickType == EnumMgr.ClickType.ShowQrCode) {
                new DialogShowQrCode(getActiveActivity(), item.getCrypto_name(), item.getAddress()).show();

            } else if (clickType == EnumMgr.ClickType.CopyAddress) {
                TCUtils.copyStringToClipboard(item.getAddress());
                Toast.makeText(getActiveActivity(), R.string.wallet_address_copied, Toast.LENGTH_LONG).show();
            } else if (clickType == EnumMgr.ClickType.ShareTopUp) {
                TCUtils.shareLinkVendor(item.getAddress());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getListCrypto() {
        requestApi(new CryptoListRequest(new APIResponseListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ArrayList<CryptoModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
                if (list != null && list.size() > 0) {
//                    tvKYC.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.note_top_up),
//                            list.get(0).getTec_topup_threshold(), TCUtils.getString(R.string.tee_coin_symbol))));
                    //hard code to 10,000 USD
                    tvKYC.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.note_top_up), "10,000")));

                    cryptoModelList.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }

    private void getAddressWallet(TopUpPostModel topUpPostModel, int position) {
        requestApi(new CryptoRequest(topUpPostModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getResult() != null) {
                    CryptoModel cryptoModel = (CryptoModel) response.getResult();
                    adapter.showAddress(position, cryptoModel);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }
}
