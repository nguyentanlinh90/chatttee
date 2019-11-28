package com.teecoin.feature.reviewSystem.userReviewShop;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nex3z.flowlayout.FlowLayout;
import com.teecoin.R;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.general.popup.MediaOptionDialog;
import com.teecoin.feature.reviewSystem.photoMedia.MediaSelectionDialog;
import com.teecoin.feature.reviewSystem.photoMedia.MediaSelectionListener;
import com.teecoin.feature.reviewSystem.reviewShopResult.ReviewShopResultScreen;
import com.teecoin.feature.walletSystem.paymentThreshold.YesNoListener;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CheckinNotificationResultModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseResponseModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.model.couponsystem.RedeemCodeCouponResultModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.FeeConfigModel;
import com.teecoin.model.general.FireBaseMediaModel;
import com.teecoin.model.general.FireBaseUploadedVideoModel;
import com.teecoin.model.reviewsystem.CommentImageModel;
import com.teecoin.model.reviewsystem.GetStatusReviewModel;
import com.teecoin.model.reviewsystem.GetVendorIdForShop;
import com.teecoin.model.reviewsystem.MediaModel;
import com.teecoin.model.reviewsystem.ReviewFromNotificationModel;
import com.teecoin.model.reviewsystem.ReviewShopModel;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.model.reviewsystem.VendorGetWalletResponseModel;
import com.teecoin.model.reviewsystem.VisitTypeModel;
import com.teecoin.model.walletsystem.PaymentInvoiceModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.GetVisitTypeRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetReviewShopLogoRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetStatusReviewRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetVendorIdRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetWalletVendorRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserUploadCommentImageRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserUploadCommentVideoRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarBusinessProcess;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.CameraUtils;
import com.teecoin.utils.CompressFile;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import core.view.RecycleListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class UserReviewShopScreen extends TCReviewBaseFragment implements RecycleListener<MediaModel>, TCConfirmListener, MediaSelectionListener, APIResponseListener {

    private static final String VENDOR_DETAIL_MODEL = "VendorDetailModel";

    @BindView(R.id.frg_write_review_view_parent)
    NestedScrollView view_parent;
    @BindView(R.id.frg_write_review_tv_coin_receive)
    TextView tv_coin_receive;

    //    @BindView(R.id.share_social_sw_location)
//    Switch sw_location;
//
//    @BindView(R.id.share_social_sw_facebook)
//    Switch sw_facebook;
//
//    @BindView(R.id.share_social_sw_instagram)
//    Switch sw_instagram;
    @BindView(R.id.frag_user_review_rating)
    RatingBar review_rating;
    @BindView(R.id.frag_user_et_comment)
    EditText et_comment;
    @BindView(R.id.frag_user_review_rcv_photo)
    TCRecyclerView rcv_photo;
    @BindView(R.id.frag_user_review_ll_add_photo)
    View ll_add_photo;
    @BindView(R.id.frg_user_review_shop_ll_date_visited)
    View ll_date_visited;
    @BindView(R.id.frg_user_review_shop_tv_date_visited)
    TextView tv_date_visited;
    @BindView(R.id.frg_user_review_shop_fl_visit_type)
    FlowLayout fl_visit_type;
    @BindView(R.id.frg_user_review_shop_tv_minimum_50)
    TextView tv_minimum_50;
    @BindView(R.id.frg_user_review_shop_et_title)
    EditText et_title;
    @BindView(R.id.button_ok_tv_ok)
    TextView tvSubmit;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String textForCheckLength = s.toString().trim();
            textForCheckLength = textForCheckLength.replace(" ", "").replace("\n", "");
            tv_minimum_50.setText(String.format(TCUtils.getString(R.string.text_50_minimum), textForCheckLength.length()));
        }
    };
    private VendorDetailModel vendorDetailModel;
    private ArrayList<MediaModel> listImage;
    private AddImagesHorizontalAdapter adapterImage;
    private String pathCamera;
    private ArrayList<String> listIDImageToSendReView;
    private String uuidVideo;
    private boolean can_get_review_reward;

    public static UserReviewShopScreen getInstance(TransactionDetailModel model) {
        UserReviewShopScreen screen = new UserReviewShopScreen();
        VendorDetailModel vendorDetailModel = new VendorDetailModel(model);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, vendorDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserReviewShopScreen getInstance(PaymentInvoiceModel model) {
        UserReviewShopScreen screen = new UserReviewShopScreen();
        VendorDetailModel vendorDetailModel = new VendorDetailModel(model);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, vendorDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserReviewShopScreen getInstance(VendorDetailModel vendorDetailModel) {
        UserReviewShopScreen screen = new UserReviewShopScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, vendorDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserReviewShopScreen getInstance(ReviewFromNotificationModel reviewFromNotificationModel) {
        UserReviewShopScreen screen = new UserReviewShopScreen();
        VendorDetailModel vendorDetailModel = new VendorDetailModel(reviewFromNotificationModel);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, vendorDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserReviewShopScreen getInstance(CouponDetailModel couponDetailModel) {
        UserReviewShopScreen screen = new UserReviewShopScreen();
        VendorDetailModel vendorDetailModel = new VendorDetailModel(couponDetailModel);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, vendorDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    //review when check in from home wallet
    public static UserReviewShopScreen getInstance(RedeemCodeCouponResultModel userCouponModels) {
        UserReviewShopScreen screen = new UserReviewShopScreen();
        VendorDetailModel vendorDetailModel = new VendorDetailModel(userCouponModels);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, vendorDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    public static UserReviewShopScreen getInstance(CheckinNotificationResultModel checkinNotificationResultModel) {//review when check in from notification
        UserReviewShopScreen screen = new UserReviewShopScreen();
        VendorDetailModel vendorDetailModel = new VendorDetailModel(checkinNotificationResultModel);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, vendorDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    //review when use coupon after purchase
    public static UserReviewShopScreen getInstance(CouponCataloguePurchaseResponseModel purchaseResponseModel) {
        UserReviewShopScreen screen = new UserReviewShopScreen();
        VendorDetailModel vendorDetailModel = new VendorDetailModel(purchaseResponseModel);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, vendorDetailModel);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_review_shop, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        showHeader();
        showButtonBackToolbar();
        hideFooter();

        if (vendorDetailModel != null) {
            updateTitleHeader(vendorDetailModel.getName());
        }
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            vendorDetailModel = (VendorDetailModel) bundle.getSerializable(VENDOR_DETAIL_MODEL);// from vendor detail
            fillData();
        }
        initRecyclerView();
        onClick();
        et_comment.addTextChangedListener(textWatcher);
        tv_minimum_50.setText(String.format(TCUtils.getString(R.string.text_50_minimum), 0));
        tvSubmit.setText(TCUtils.getString(R.string.submit));
        tv_date_visited.setText(TCDateUtility.formatDate(TCDateUtility.getCurrentDate(), TCDateUtility.DateFormatDefinition.YYYY_MM_DD));
        getVisitType();
    }

    private void onClick() {
        ll_add_photo.setOnClickListener(v -> checkPermissionStore());
        registerSingleClick(
                R.id.frg_user_review_shop_ll_date_visited,
                R.id.ll_bt_ok);

//        view_parent.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
//            if (new KeyboardManager().isKeyboardShown(view_parent.getRootView())) {
//                hideFooter();
//            } else {
//                new Handler().postDelayed(() -> {
//                    showFooter();
//                },100L);
//            }
//        });

    }

    private void updateStatusCoinReceive() {
        if (can_get_review_reward) {
            FeeConfigModel feeConfigModel = RealmController.getInstance().getData(FeeConfigModel.class);
            if (feeConfigModel != null) {
                tv_coin_receive.setText(String.format(TCUtils.getString(R.string.user_review_text_receive), feeConfigModel.getReview_reward_amount_attachment()));
                //  tv_coin_receive.setText(String.format("%s %s", TCUtils.getString(R.string.user_review_text_receive), feeConfigModel.getReview_reward_amount()));
            }
        } else {

            tv_coin_receive.setText(TCUtils.getString(R.string.user_review_have_reached_the_limit_of_review_reward));

            new DialogStatusReview(getActiveActivity(), new YesNoListener() {
                @Override
                public void onSubmit() {

                }

                @Override
                public void onCancel() {
                    handleBackPressed();
                }
            }).show();
        }

    }

    private void checkPermissionStore() {
        if (ContextCompat.checkSelfPermission(getActiveActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(TCConstant.CAMERA_WRITE_EXTERNAL_PERMISSIONS, EnumMgr.RequestCode.READ_EXTERNAL_STORAGE.getValue());
        } else if (ContextCompat.checkSelfPermission(getActiveActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (listImage.size() < TCConstant.MAX_MEDIA_UPLOAD_FILES) {
                showOptionSelectionPopup();
            } else {
                showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_warning), TCUtils.getString(R.string.max_5_photos), TCUtils.getString(R.string.text_ok), null, null);
            }
        }
    }

    private void fillData() {
        if (vendorDetailModel != null) {
            updateTitleHeader(vendorDetailModel.getName());
            if (!TCUtils.isEmpty(vendorDetailModel.getId()) && TCUtils.isEmpty(vendorDetailModel.getDestination())) {
                getWalletForVendor(vendorDetailModel.getId());
            } else if (TCUtils.isEmpty(vendorDetailModel.getId()) && !TCUtils.isEmpty(vendorDetailModel.getDestination())) {
                getVendorID(vendorDetailModel.getDestination());
            }

            if (!TCUtils.isEmpty(vendorDetailModel.getId())) {
                getReviewStatus();
            }
        }
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_user_review_shop_ll_date_visited:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActiveActivity(), (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);
                    tv_date_visited.setText(TCDateUtility.formatDate(calendar.getTime(),
                            TCDateUtility.DateFormatDefinition.YYYY_MM_DD));
                }, TCDateUtility.getCurrentYear(),
                        TCDateUtility.getCurrentMonth(),
                        TCDateUtility.getCurrentDayOfMonth());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;
            case R.id.ll_bt_ok:
                validate();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EnumMgr.RequestCode.READ_EXTERNAL_STORAGE.getValue()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showOptionSelectionPopup();
            }
        }
    }

    private void showOptionSelectionPopup() {
        MediaOptionDialog mediaOptionDialog = new MediaOptionDialog(getActiveActivity(), hasVideo(), this);
        mediaOptionDialog.show();
    }

    private void openLibrary() {
        MediaSelectionDialog dialogPhotoMedia = new MediaSelectionDialog(
                getActiveActivity(),
                TCConstant.OPEN_LIBRARY_FROM_USER_REVIEW,
                listImage.size(),
                MediaSelectionDialog.isAlreadyHadVideo(listImage),
                this);
        dialogPhotoMedia.show();
    }

    private void capturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActiveActivity().getPackageManager()) != null) {
            Uri photoURI = null;
            try {
                File photoFile = CameraUtils.createImageFileWith();
                pathCamera = photoFile.getAbsolutePath();
                photoURI = FileProvider.getUriForFile(getActiveActivity(),
                        getString(R.string.file_provider_authority),
                        photoFile);

            } catch (IOException ignored) {

            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(takePictureIntent, EnumMgr.RequestCode.OPEN_CAMERA.getValue());
        }
    }

    private void initRecyclerView() {
        listImage = new ArrayList<>();
        rcv_photo.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapterImage = new AddImagesHorizontalAdapter(getActiveActivity(), LayoutInflater.from(getActiveActivity()), listImage, this);
        rcv_photo.setAdapter(adapterImage);
    }

    @Override
    public void onItemClick(View view, MediaModel item, int position, EnumMgr.ClickType clickType) {
        if (clickType == EnumMgr.ClickType.RemovePhoto) {
            if (listImage != null && listImage.size() > 0) {
                listImage.remove(position);
                adapterImage.notifyDataSetChanged();
            }
            if (listIDImageToSendReView != null && listIDImageToSendReView.size() > 0) {
                listIDImageToSendReView.remove(position);
            }
        }
    }

    @Override
    public void onConfirmed(int id, Object onWhat) {
        if (id == EnumMgr.PhotoMediaFrom.Library.getValue()) {
            openLibrary();
        } else if (id == EnumMgr.PhotoMediaFrom.CapturePhoto.getValue()) {
            capturePhoto();
        } else {
            captureVideo();
        }
    }

    private void getVisitType() {
        requestApi(new GetVisitTypeRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ArrayList<VisitTypeModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
                if (list != null) {
                    fl_visit_type.removeAllViews();
                    for (VisitTypeModel visitType : list) {
                        VisitTypeSelectionView v = new VisitTypeSelectionView(getActiveActivity(),
                                visitType, (v1, visitTypeModel) -> {
                            handleVisitTypeSelection(v1);
                        });
                        fl_visit_type.addView(v);
                    }
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }

    @Override
    public void onSelected(List<MediaModel> selectedImageList) {
        ArrayList<MediaModel> imageList = new ArrayList<>();
        MediaModel videoFile = null;

        for (MediaModel mediaModel : selectedImageList) {
            if (!mediaModel.isVideo()) {
                imageList.add(mediaModel);
            } else {
                videoFile = mediaModel;
            }
        }

        listImage.addAll(imageList);
        adapterImage.notifyDataSetChanged();
        rcv_photo.scrollToPosition(listImage.size() - 1);
        if (imageList.size() > 0)
            uploadImageList(imageList);

        if (videoFile != null) {
            if (TCUtils.isVideoFileSizeLargeThan50Mb(new File(videoFile.getPath()))) {
                showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_alert),
                        TCUtils.getString(R.string.you_can_not_upload_file_larger_than_50_mb),
                        TCUtils.getString(R.string.text_ok),
                        null,
                        null);
                showLoading(false);
            } else {
                uploadVideo(videoFile.getPath());
            }
        }
    }

    private void uploadImageList(ArrayList<MediaModel> imageList) {
        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[imageList.size()];
        for (int i = 0; i < imageList.size(); i++) {
            File file = new File(imageList.get(i).getPath());
            file = CompressFile.getCompressedImageFile(file, getContext());
            if (file != null) {
                surveyImagesParts[i] = MultipartBody.Part.createFormData(
                        TCConstant.MEDIA_FIELD, file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file));
            }
        }
        requestApi(new ReviewUserUploadCommentImageRequest(surveyImagesParts, this));
    }

    private void captureVideo() {
        Intent intent = new Intent(getActiveActivity(), CaptureVideoActivity.class);
        startActivityForResult(intent, EnumMgr.RequestCode.CAPTURE_VIDEO.getValue());
    }


    private void getWalletForVendor(String vendorID) {
        requestApi(new ReviewUserGetWalletVendorRequest(vendorID, this));
    }

    public void validate() {
        if (review_rating.getRating() == 0) {
            showBaseMessage(TCUtils.getString(R.string.user_review_waiting_rating));
            return;
        }
        if (TCUtils.isEmpty(et_comment.getText().toString().trim())) {
            showBaseMessage(TCUtils.getString(R.string.user_review_waiting_comment));
            return;
        }

        String textForCheckLength = et_comment.getText().toString().trim();
        textForCheckLength = textForCheckLength.replace(" ", "").replace("\n", "");
        if (textForCheckLength.length() < TCConstant.REVIEW_MIN_CHARACTERS) {
            showBaseMessage(TCUtils.getString(R.string.user_review_your_review_min_characters_count));
            return;
        }
//        if (listIDImageToSendReView == null || listIDImageToSendReView.size() == 0) {
//            showBaseMessage(TCUtils.getString(R.string.user_review_waiting_photo));
//            return;
//        }
        //TCLog.e("listImage 1"+listImage.size());
        startReview();
    }

    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_USER_REVIEW_SHOP_RESULT.getValue() && finishedResultCode == RESULT_OK) {
            finishWithResult(RESULT_OK, new Intent());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EnumMgr.RequestCode.OPEN_CAMERA.getValue() && resultCode == Activity.RESULT_OK) {
            String path = pathCamera;
            File file = new File(path);
            file = CompressFile.getCompressedImageFile(file, getContext());
            if (file != null) {
                listImage.add(new MediaModel(path, true, false));
                adapterImage.notifyDataSetChanged();
                rcv_photo.scrollToPosition(listImage.size() - 1);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[1];
                surveyImagesParts[0] = MultipartBody.Part.createFormData(TCConstant.MEDIA_FIELD, file.getName(), surveyBody);
                requestApi(new ReviewUserUploadCommentImageRequest(surveyImagesParts, this));
            } else {
                showBaseMessage("Can not process media file, please try again");
            }
        } else if (requestCode == EnumMgr.RequestCode.CAPTURE_VIDEO.getValue() && resultCode == Activity.RESULT_OK) {
            showLoading(true);
            TCLog.d("hung onActivityResult file:" + data.getStringExtra(CaptureVideoActivity.VIDEO_PATH_INTENT));
            CameraUtils.cropVideoWithSquareFrame(
                    data.getStringExtra(CaptureVideoActivity.VIDEO_PATH_INTENT),
                    data.getStringExtra(CaptureVideoActivity.CROP_VIDEO_SIZE_INTENT),
                    new CropVideoListener() {
                        @Override
                        public void onCropFinish(String cropVideoPath) {
                            TCLog.d("hung onCropFinish: " + cropVideoPath);
                            uploadVideo(cropVideoPath);
                        }

                        @Override
                        public void onCropFail(String message) {
                            TCLog.d("hung onCropFail: " + message);
                            showLoading(false);
                        }
                    });
        }
    }

    private void getLinkImageShopFromTransaction(String destination) {
        requestApi(new ReviewUserGetReviewShopLogoRequest(destination, this));
    }

    private void getVendorID(String public_Key) {
        requestApi(new ReviewUserGetVendorIdRequest(public_Key, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response != null) {
                    GetVendorIdForShop data = (GetVendorIdForShop) (response).getResult();
                    if (data.getId() != null && !TCUtils.isEmpty(data.getId())) {
                        vendorDetailModel.setId(data.getId());
                        getReviewStatus();
                    }
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }

    private void getReviewStatus() {
        requestApi(new ReviewUserGetStatusReviewRequest(vendorDetailModel.getId(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response != null) {
                    GetStatusReviewModel data = (GetStatusReviewModel) (response).getResult();
                    if (data != null) {
                        can_get_review_reward = data.isCan_get_review_reward();
                        if (!can_get_review_reward)
                            updateStatusCoinReceive();
                    }
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }

    public void startReview() {

        AccountModel accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        String sourcePublicKey = accountModel.getPublic_key();
        ReviewShopModel reviewShopModel;

        if (TCUtils.isEmpty(vendorDetailModel.getId())) {
            reviewShopModel = new ReviewShopModel(
                    et_comment.getText().toString(),
                    vendorDetailModel.getTransaction_id(),
                    String.valueOf(review_rating.getRating()),
                    listIDImageToSendReView,
                    sourcePublicKey,
                    vendorDetailModel.getDestination(),
                    TCConstant.TEE_COIN_FOR_REVIEW,
                    false,//sw_instagram.isChecked(),
                    false,//sw_facebook.isChecked(),
                    et_title.getText().toString(),
                    getSelectedVisitTypes(),
                    tv_date_visited.getText().toString()
            );
        } else {
            reviewShopModel = new ReviewShopModel(
                    et_comment.getText().toString(),
                    vendorDetailModel.getId(),
                    vendorDetailModel.getTransaction_id(),
                    String.valueOf(review_rating.getRating()),
                    listIDImageToSendReView,
                    sourcePublicKey,
                    vendorDetailModel.getDestination(),
                    TCConstant.TEE_COIN_FOR_REVIEW,
                    false,//sw_instagram.isChecked(),
                    false,//sw_facebook.isChecked(),
                    et_title.getText().toString(),
                    getSelectedVisitTypes(),
                    tv_date_visited.getText().toString()
            );
        }
        showLoading(true);
        StellarBusinessProcess.getInstance().startReview(reviewShopModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                showLoading(false);
                addFragmentForResult(EnumMgr.RequestCode.GOTO_USER_REVIEW_SHOP_RESULT.getValue(),
                        ReviewShopResultScreen.getInstance(listImage.size() > 0, can_get_review_reward));
                if (!TCUtils.isEmpty(vendorDetailModel.getTransaction_id())) {
                    RealmController.getInstance().updateFieldIsReviewSuccessTransactionDetailModel(vendorDetailModel.getTransaction_id(), true);
                }
                CameraUtils.deleteMediaFiles();
                //   hideLoadingDialog();
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                TCAppFlyerTrackingEvent.getInstance().trackReviewWriteReviewSubmitFail();
                showLoading(false);
                showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_warning), errorModel.getErrorMessage(), TCUtils.getString(R.string.text_ok), null, null);
            }
        });
    }

    public String getVideoUuid() {
        return uuidVideo;
    }

    public void finishUploadVideo(FireBaseUploadedVideoModel uploadedVideoModel) {
        if (uploadedVideoModel.getFireBaseMediaModels() != null && uploadedVideoModel.getFireBaseMediaModels().size() > 0) {
            if (listIDImageToSendReView == null)
                listIDImageToSendReView = new ArrayList<>();
            for (FireBaseMediaModel fireBaseMediaModel : uploadedVideoModel.getFireBaseMediaModels()) {
                listIDImageToSendReView.add(fireBaseMediaModel.getId());
            }
        }
        TCLog.d("hung finish upload video");
        showLoading(false);
    }

    public void uploadVideo(String videoPath) {
        showLoading(true);
        File videoFile = new File(videoPath);
        if (TCUtils.isVideoFileSizeLargeThan50Mb(videoFile)) {
            showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_alert),
                    TCUtils.getString(R.string.you_can_not_upload_file_larger_than_50_mb),
                    TCUtils.getString(R.string.text_ok),
                    null,
                    null);
            showLoading(false);
            return;
        }
        MultipartBody.Part[] surveyMediaParts = new MultipartBody.Part[2];
        surveyMediaParts[0] = MultipartBody.Part.createFormData(
                TCConstant.MEDIA_FIELD,
                videoFile.getName(),
                RequestBody.create(MediaType.parse("video/*"), videoFile));

        File thumbnailImageFile;
        try {
            thumbnailImageFile = CameraUtils.createImageFileWith();
            Bitmap thumbnail = TCUtils.createVideoThumbnail(videoPath);
            FileOutputStream out = new FileOutputStream(thumbnailImageFile);
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            surveyMediaParts[1] = MultipartBody.Part.createFormData(
                    TCConstant.MEDIA_FIELD,
                    thumbnailImageFile.getName(),
                    RequestBody.create(MediaType.parse("image/*"), thumbnailImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        listImage.add(new MediaModel(videoPath, true, true));
        adapterImage.notifyDataSetChanged();
        rcv_photo.scrollToPosition(listImage.size() - 1);
        uuidVideo = CameraUtils.generateUUID();
        requestApi(new ReviewUserUploadCommentVideoRequest(surveyMediaParts, uuidVideo, this));
    }

    private boolean hasVideo() {
        if (listImage != null && listImage.size() > 0) {
            for (MediaModel mediaModel : listImage) {
                if (mediaModel.isVideo()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.UPLOAD_COMMENT_IMAGE) {
            if (listIDImageToSendReView == null)
                listIDImageToSendReView = new ArrayList<>();
            ArrayList<CommentImageModel> commentImageModelList = ((BaseResultsResponseModel<CommentImageModel>) response.getResult()).getResults();
            for (int i = 0; i < commentImageModelList.size(); i++) {
                listIDImageToSendReView.add((commentImageModelList.get(i).getId()));
            }
        } else if (requestTarget == ReviewRequestTarget.GET_WALLET_VENDOR) {
            VendorGetWalletResponseModel data = (VendorGetWalletResponseModel) response.getResult();
            if (data != null) {
                vendorDetailModel.setDestination(data.getPublic_key());
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        showLoading(false);
        if (requestTarget == ReviewRequestTarget.UPLOAD_COMMENT_IMAGE) {
            showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_alert),
                    errorModel.getErrorMessage(),
                    TCUtils.getString(R.string.text_ok), null, null);
        }
    }

    private ArrayList<String> getSelectedVisitTypes() {
        ArrayList<String> selectedVisitTypeValue = new ArrayList<>();
        for (int index = 0; index < fl_visit_type.getChildCount(); ++index) {
            View childView = fl_visit_type.getChildAt(index);
            if (childView instanceof VisitTypeSelectionView) {
                VisitTypeSelectionView visitTypeSelectionView = (VisitTypeSelectionView) childView;
                if (visitTypeSelectionView.isSelected()) {
                    selectedVisitTypeValue.add(visitTypeSelectionView.getVisitTypeValue());
                }
            }
        }
        return selectedVisitTypeValue;
    }

    private void handleVisitTypeSelection(VisitTypeSelectionView selectedView) {
        for (int index = 0; index < fl_visit_type.getChildCount(); ++index) {
            View childView = fl_visit_type.getChildAt(index);
            if (childView instanceof VisitTypeSelectionView) {
                VisitTypeSelectionView visitTypeSelectionView = (VisitTypeSelectionView) childView;
                visitTypeSelectionView.setSelected(false);
            }
        }
        selectedView.setSelected(true);
    }

}
