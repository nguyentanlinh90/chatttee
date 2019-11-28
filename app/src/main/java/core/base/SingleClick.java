package core.base;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;

public class SingleClick implements View.OnClickListener {

    private SingleClickListener listener;
    private LastClick lastClick;
    private Handler executor = new Handler();
    private Runnable delay;
    private boolean isExecutingClick = false;
    private Object object;

    public SingleClick() {
    }

    public SingleClick(SingleClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        long currentTimestamp = SystemClock.uptimeMillis();
        if (lastClick != null) {
            if (listener != null
                    && !(currentTimestamp - lastClick.getTimeStamp() <= Utils.INTERVAL_CLICK) && !isExecutingClick) {
                triggerClick(v);
            }

        } else {
            if (listener != null && !isExecutingClick)
                triggerClick(v);
        }
        lastClick = new LastClick(currentTimestamp);
    }

    private void triggerClick(final View v) {
        isExecutingClick = true;
        executor.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSingleClick(v, SingleClick.this.object);
                isExecutingClick = false;
            }
        }, Utils.RIPPLE_EFFECT_DELAY);
    }

    public SingleClickListener getListener() {
        return listener;
    }

    public void setListener(SingleClickListener listener) {
        this.listener = listener;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * public interface<br>
     * <b>SingleClickListener</b><br>
     * <br>
     * <b>Class Overview</b> <br>
     * <br>
     * Used for receiving notifications from the <code>SingleClick</code> when
     * event click of a single component is fired.<br>
     * <br>
     * <b>Summary</b>
     */
    public interface SingleClickListener {
        /**
         * <b>Specified by:</b> onSingleClick(...) in SingleClickListener <br>
         * <br>
         * This is called immediately after the click event is being fired
         * within the pre-defined minimum interval time.
         *
         * @param v The view is being clicked
         */
        void onSingleClick(View v, Object object);
    }

    private class LastClick {
        private final long timeStamp;

        public LastClick(long timeStamp) {
            super();
            this.timeStamp = timeStamp;
        }

        /**
         * @return the timeStamp
         */
        public long getTimeStamp() {
            return timeStamp;
        }

    }
}