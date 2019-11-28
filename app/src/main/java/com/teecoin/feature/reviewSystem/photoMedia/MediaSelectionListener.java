package com.teecoin.feature.reviewSystem.photoMedia;

import com.teecoin.model.reviewsystem.MediaModel;

import java.util.List;

public interface MediaSelectionListener {
    void onSelected(List<MediaModel> selectedImageList);
}
