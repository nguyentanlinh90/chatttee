package com.teecoin.feature.general.popup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.model.general.EventsModel;
import com.teecoin.utils.EnumMgr;

import java.util.ArrayList;

import butterknife.BindView;

public class PopupEvents extends TCBaseDialog {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.ll_bt_cancel)
    View bt_cancel;
    @BindView(R.id.ll_bt_read_more)
    View bt_read_more;
    private ArrayList<EventsModel> listEvent;
    private EventsModel eventsModel;
    private TCConfirmListener confirmListener;


    public PopupEvents(Context context, ArrayList<EventsModel> eventModel, TCConfirmListener confirmListener) {
        super(context);
        this.listEvent = eventModel;
        this.confirmListener = confirmListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_events);
    }

    @Override
    protected void initContentView() {
        if (listEvent != null && listEvent.size() > 0) {
            eventsModel =listEvent.get(0);
            SlideImageEventsAdapter adapter = new SlideImageEventsAdapter(getContext(), listEvent);
            viewPager.setAdapter(adapter);
            tv_title.setText(eventsModel.getName());

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    eventsModel =listEvent.get(position);
                      tv_title.setText(eventsModel.getName());
                    //  TCLog.e(" . "+eventsModel.getDescription());

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }

    @Override
    protected void onViewClick() {
        bt_cancel.setOnClickListener(v -> dismiss());
        bt_read_more.setOnClickListener(v -> dismiss());
        bt_read_more.setOnClickListener(v -> {
            dismiss();
            confirmListener.onConfirmed(Integer.parseInt(EnumMgr.PushNotification.Referral.getValue()), eventsModel);

        });
    }
}
