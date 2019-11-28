package com.teecoin.ui;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class TCCustomDurationScroller extends Scroller {

    private double scrollFactor = 1;

    public TCCustomDurationScroller(Context context) {
        super(context);
    }

    public TCCustomDurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public void setScrollDurationFactor(double scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int) (duration * scrollFactor));
    }
}
