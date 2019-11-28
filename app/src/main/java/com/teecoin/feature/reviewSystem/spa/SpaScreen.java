package com.teecoin.feature.reviewSystem.spa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;

public class SpaScreen extends TCReviewBaseFragment {
    public static SpaScreen getInstance() {
        return new SpaScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spa, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
    }

    @Override
    public void onBindView() {


    }
}