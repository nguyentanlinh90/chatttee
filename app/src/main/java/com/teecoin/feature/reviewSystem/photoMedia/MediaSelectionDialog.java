package com.teecoin.feature.reviewSystem.photoMedia;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.model.reviewsystem.MediaModel;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import core.view.RecycleListener;

import static com.teecoin.utils.TCConstant.OPEN_LIBRARY_FROM_USER_REVIEW;

public class MediaSelectionDialog extends TCBaseDialog implements RecycleListener<MediaModel> {
    @BindView(R.id.dialog_album_rv_media_list)
    TCRecyclerView rv_media_list;
    @BindView(R.id.activity_main_iv_bt_done)
    ImageView activity_main_iv_done_toolbar;

    private Activity activity;
    private String type;
    private List<MediaModel> selectedImageList;
    private MediaSelectionListener tcPhotoListener;
    private MediaSelectionAdapter adapter;
    private int selectedImageCount;
    private boolean isAlreadyHadVideo;

    public MediaSelectionDialog(Activity activity, String type, int selectedImageCount, boolean isAlreadyHadVideo, MediaSelectionListener tcPhotoListener) {
        super(activity);
        this.activity = activity;
        this.type = type;
        this.selectedImageList = new ArrayList<>();
        this.selectedImageCount = selectedImageCount;
        this.isAlreadyHadVideo = isAlreadyHadVideo;
        this.tcPhotoListener = tcPhotoListener;
    }

    public static boolean isAlreadyHadVideo(List<MediaModel> mediaList) {
        for (MediaModel mediaModel : mediaList) {
            if (mediaModel.isVideo())
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_album_photo);
    }

    @Override
    protected void initContentView() {
        setFullScreen(true);
        updateTitleBar("IMAGES");
        activity_main_iv_done_toolbar.setOnClickListener(v -> clickDoneButton());
        registerSingleClick(R.id.activity_main_iv_bt_back);
        initRecyclerView();
    }

    @Override
    protected void onViewClick() {

    }

    private void clickDoneButton() {
        tcPhotoListener.onSelected(selectedImageList);
        dismiss();
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.activity_main_iv_bt_back:
                dismiss();
                break;
        }
    }

    private void initRecyclerView() {
        ArrayList<MediaModel> photoMediaList = PictureManager.getImagesPath(activity);
        adapter = new MediaSelectionAdapter(activity, LayoutInflater.from(activity), photoMediaList, this);
        rv_media_list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, MediaModel item, int position, EnumMgr.ClickType clickType) {
        if (type.equals(OPEN_LIBRARY_FROM_USER_REVIEW)) {
            if (!item.isSelected()) {
                if (selectedImageCount < TCConstant.MAX_MEDIA_UPLOAD_FILES) {
                    if (item.isVideo()) {
                        if (isAlreadyHadVideo(selectedImageList) || isAlreadyHadVideo) {
                            Toast.makeText(activity, TCUtils.getString(R.string.you_already_had_one_video_selected), Toast.LENGTH_LONG).show();
                        } else {
                            item.setSelected(!item.isSelected());
                            selectedImageList.add(item);
                            selectedImageCount++;
                        }
                    } else {
                        item.setSelected(!item.isSelected());
                        TCLog.d("hung media=" + item.getPath());
                        selectedImageList.add(item);
                        selectedImageCount++;
                    }
                } else {
                    Toast.makeText(activity, TCUtils.getString(R.string.max_5_photos), Toast.LENGTH_LONG).show();
                }

            } else {
                item.setSelected(!item.isSelected());
                selectedImageList.remove(item);
                selectedImageCount--;
            }
            adapter.notifyItemChanged(position);
        } else {
            //todo choose picture banner when add coupon
        }

        activity_main_iv_done_toolbar.setVisibility(selectedImageList.size() > 0 ? View.VISIBLE : View.GONE);

    }

}
