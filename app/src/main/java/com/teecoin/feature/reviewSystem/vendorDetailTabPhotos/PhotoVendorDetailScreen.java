package com.teecoin.feature.reviewSystem.vendorDetailTabPhotos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.reviewSystem.tip.DialogTipVendor;
import com.teecoin.model.reviewsystem.ImagesVendor;
import com.teecoin.ui.SimpleRatingBar;
import com.teecoin.utils.DateTimeAgo;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class PhotoVendorDetailScreen extends TCReviewBaseFragment  {
    private static final String LIST_PHOTO = "LIST_PHOTO";
    private static final String POSITION = "POSITION";
    private ArrayList<ImagesVendor> listPhoto;
    private int position = 0;

    @BindView(R.id.frg_photo_detail_iv_close)
    ImageView iv_close;
    @BindView(R.id.frg_photo_detail_view_pager)
    ViewPager view_pager;


    @BindView(R.id.frg_photo_detail_ll_content_review)
    View ll_content_review;
    @BindView(R.id.frg_photo_detail_ll_author)
    View ll_author;
    @BindView(R.id.frg_photo_detail_ll_like_tip)
    View ll_like_tip;

    @BindView(R.id.dialog_view_vendor_tv_title)
    TextView tv_title;
    @BindView(R.id.dialog_view_vendor_tv_content)
    TextView tv_content;
    @BindView(R.id.dialog_view_vendor_tv_more)
    TextView tv_read_more;
    @BindView(R.id.dialog_view_comment_iv_avatar_user)
    ImageView iv_avatar_user;
    @BindView(R.id.dialog_view_comment_tv_name_user)
    TextView tv_name_user;
    @BindView(R.id.dialog_view_comment_rb_rating)
    SimpleRatingBar rb_rating;
    @BindView(R.id.dialog_view_comment_tv_time_ago)
    TextView tv_time_ago;
    @BindView(R.id.frag_list_review_tv_total_like)
    TextView tv_total_like;
    @BindView(R.id.frag_list_review_tv_like)
    TextView tv_like;
    @BindView(R.id.frag_list_review_tv_tip_amount)
    TextView tv_tip_amount;
    @BindView(R.id.frag_list_review_tv_tec)
    TextView tv_tec;


    public static PhotoVendorDetailScreen newInstance(ArrayList<ImagesVendor> listImages, int position) {
        PhotoVendorDetailScreen screen = new PhotoVendorDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LIST_PHOTO, listImages);
        bundle.putSerializable(POSITION, position);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_detail_vendor, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideFooter();
        hideHeader();
    }

    @Override
    public void onBindView() {
        super.onBindView();
        super.onBindView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            listPhoto = (ArrayList<ImagesVendor>) bundle.getSerializable(LIST_PHOTO);
            position = bundle.getInt(POSITION);
        }

        //todo: zoom image, check SliderImageAndVideoDetail
        setupViewVideo();
        registerSingleClick(R.id.frag_review_top_review_ll_like,R.id.frg_photo_detail_iv_close,R.id.dialog_view_vendor_tv_more);
    }

    private void setupViewVideo() {
        initPhotoAdapter();
    }
    private void initPhotoAdapter(){
        if(listPhoto==null||listPhoto.size()==0)
            return;

        SlidePhotoVendorDetailAdapter adapter = new SlidePhotoVendorDetailAdapter(getActiveActivity(), listPhoto, true, TCUtils.checkURLisVideo(listPhoto.get(position).getUrl()));
        view_pager.setAdapter(adapter);
        view_pager.setCurrentItem(position);
        checkReviewPhoto(position);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                position =pos;
                checkReviewPhoto(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_review_top_review_ll_like:
                if(!isAppUser())
                    return;
                checkLikeAndTip();
                break;
            case R.id.frg_photo_detail_iv_close:
                handleBackPressed();
                break;
            case R.id.dialog_view_vendor_tv_more:
                if(tv_read_more.getText().toString().equals(TCUtils.getString(R.string.text_more)))
                {
                    TCUtils.showMoreContenReview(tv_content);
                    tv_read_more.setText(TCUtils.getString(R.string.text_less));

                }else{
                    TCUtils.getLineContenReview(tv_content,tv_read_more);
                    tv_read_more.setText(TCUtils.getString(R.string.text_more));
                }

                break;
        }
    }


    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frag_review_top_review_ll_like,R.id.frg_photo_detail_iv_close,R.id.dialog_view_vendor_tv_more);
    }
    private void  checkReviewPhoto(int position){
        if(listPhoto!=null&&listPhoto.size()>0){
          //  if(position<listPhoto.size()){
                ImagesVendor imagesVendor =listPhoto.get(position);
                if(imagesVendor.getReview()!=null){
                    if(!TCUtils.isEmpty(imagesVendor.getReview().getId())){
                        ll_content_review.setVisibility(View.VISIBLE);
                        ll_author.setVisibility(View.VISIBLE);
                        ll_like_tip.setVisibility(View.VISIBLE);
                        if(!TCUtils.isEmpty(imagesVendor.getReview().getTitle())){
                            tv_title.setText(imagesVendor.getReview().getTitle());
                            tv_title.setVisibility(View.VISIBLE);
                        }else{
                            tv_title.setVisibility(View.GONE);
                        }
                        if(imagesVendor.getReview().getAuthor()!=null){
                            tv_name_user.setText(!TCUtils.isEmpty(imagesVendor.getReview().getAuthor().getFull_name()) ? imagesVendor.getReview().getAuthor().getFull_name() : "");
                            Glide.with(getActiveActivity()).load(
                                    TCUtils.isEmpty(imagesVendor.getReview().getAuthor().getAvatar()) ?
                                            TCUtils.getDrawable(R.drawable.ic_avatar_user_gold) :
                                            imagesVendor.getReview().getAuthor().getAvatar())
                                    .apply(RequestOptions.circleCropTransform()
                                            .placeholder(TCUtils.getDrawable(R.drawable.ic_avatar_user_gold))
                                            .error(TCUtils.getDrawable(R.drawable.ic_avatar_user_gold)))
                                    .into(iv_avatar_user);

                        }
                        if (!TCUtils.isEmpty(imagesVendor.getReview().getCreated())) {
                            tv_time_ago.setText(DateTimeAgo.timeAgo(
                                    TCDateUtility.toDate(imagesVendor.getReview().getCreated(),
                                            TCDateUtility.TCTimeZone.GMT,
                                            TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_SSSSSS).getTime()));
                        }
                        tv_content.setText(imagesVendor.getReview().getComment());
                        TCUtils.getLineContenReview(tv_content,tv_read_more);
                        rb_rating.setRating(imagesVendor.getReview().getRating());
                        tv_total_like.setText(imagesVendor.getReview().getLike_count());
                        tv_total_like.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                        tv_like.setTextColor(TCUtils.getColor(R.color.c_ffffff));

                        tv_tip_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, imagesVendor.getReview().getTip_amount()));
                        tv_tip_amount.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                        tv_tec.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                    }else{
                        ll_content_review.setVisibility(View.GONE);
                        ll_author.setVisibility(View.GONE);
                        ll_like_tip.setVisibility(View.GONE);
                    }
                }else{
                    ll_content_review.setVisibility(View.GONE);
                    ll_author.setVisibility(View.GONE);
                    ll_like_tip.setVisibility(View.GONE);
                }

            }
        //}

    }
    private void checkLikeAndTip(){
        ImagesVendor imagesVendor =listPhoto.get(position);
        if(imagesVendor!=null&imagesVendor.getReview()!=null){
            boolean tipBefore = false;
            if (!TCUtils.isEmpty(imagesVendor.getReview().getTip_of_user())) {
                float tip = Float.parseFloat(imagesVendor.getReview().getTip_of_user());
                if (tip > 0)
                    tipBefore = true;
            }
            if (!TCUtils.isMyselfReview(imagesVendor.getReview().getAuthor().getPublic_key())) {
                showPopupTip(tipBefore, imagesVendor.getReview().getAuthor().getPublic_key(), imagesVendor.getReview().getId(), position);
            } else {
                ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.tip_message_yourself));
            }
            if(!imagesVendor.getReview().isIs_like()){
                likeImageReview(imagesVendor,position);
            }

        }
    }
    private void showPopupTip(boolean tipBefore, String destinationPublicKey, String commentId, int pos) {
        DialogTipVendor dialogTipVendor = new DialogTipVendor(getActiveActivity(), tipBefore, destinationPublicKey, commentId, amount -> {
            if (amount > 0) {
                ImagesVendor imagesVendor=listPhoto.get(pos);
                if (imagesVendor.getReview().getTip_amount() != null) {
                    float tip_amount = Float.parseFloat(imagesVendor.getReview().getTip_amount());
                    imagesVendor.getReview().setTip_amount((amount + tip_amount) + "");
                    tv_tip_amount.setText(String.valueOf(amount+tip_amount));
                }
                if (imagesVendor.getReview().getTip_of_user() != null) {
                    float tip_user = Float.parseFloat(imagesVendor.getReview().getTip_of_user());
                    imagesVendor.getReview().setTip_of_user((amount + tip_user) + "");
                }

            }
        });
        dialogTipVendor.show();
        dialogTipVendor.setCanceledOnTouchOutside(false);
    }
    public void updateLikeAndTipPhoto(int position){
        if (position < listPhoto.size()) {
            ImagesVendor imagesVendor =listPhoto.get(position);
            if(listPhoto.get(position).getReview()!=null){
                if(!TCUtils.isEmpty(imagesVendor.getReview().getId())){
                    if (imagesVendor.getReview().isIs_like()) {
                        return;
                    }
                    imagesVendor.getReview().setIs_like(true);
                    if (imagesVendor.getReview().getLike_count() != null) {
                        int like_count = Integer.parseInt(imagesVendor.getReview().getLike_count());
                        if (like_count >= 0) {
                            like_count += 1;
                            imagesVendor.getReview().setLike_count(like_count + "");
                            tv_total_like.setText(String.valueOf(like_count));
                        }
                    }
                }
            }

        }
    }

}
