package com.teecoin.feature.walletSystem.orderDetail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseFragment;
import butterknife.BindView;

public class ZoomOutQRCodeScreen extends TCBaseFragment {
    private static String BITMAP_BYTES = "BITMAP_BYTES";
    @BindView(R.id.ll_container)
    View ll_container;
    @BindView(R.id.image)
    ImageView image;

    public static ZoomOutQRCodeScreen getInstance(byte[] bitmapBytes) {
        ZoomOutQRCodeScreen screen = new ZoomOutQRCodeScreen();
        Bundle bundle = new Bundle();
        bundle.putByteArray(BITMAP_BYTES, bitmapBytes);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zoom_out_qr, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            byte[] byteArray = bundle.getByteArray(BITMAP_BYTES);
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            image.setImageBitmap(bmp);
        }
        ll_container.setOnClickListener(v -> finishFragment());
    }

    private void finishFragment() {
        ((TCMainActivity) getActiveActivity()).setClockScreen(false);
        getActiveActivity().onBackPressed();
    }

}
