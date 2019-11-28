package com.teecoin.feature.reviewSystem.vendorReview;

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
import com.teecoin.model.reviewsystem.ReviewVendorDetailModel;
import com.teecoin.ui.SimpleRatingBar;
import com.teecoin.utils.DateTimeAgo;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ReviewDetailScreen extends TCReviewBaseFragment {
    private static final String REVIEW_VENDOR_DETAIL_MODEL = "REVIEW_VENDOR_DETAIL_MODEL";
    private static final String POSITION = "POSITION";
    private static final String POSITION_IMAGE = "POSITION_IMAGE";
    private int position = 0;
    private int positionImage = 0;
    private ReviewVendorDetailModel reviewVendorDetailModel;

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

    public static ReviewDetailScreen newInstance(ReviewVendorDetailModel reviewVendorDetailModel,int position,int positionImage) {// from vendor detail
        ReviewDetailScreen screen = new ReviewDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(REVIEW_VENDOR_DETAIL_MODEL, reviewVendorDetailModel);
        bundle.putSerializable(POSITION, position);
        bundle.putSerializable(POSITION_IMAGE, positionImage);
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
            reviewVendorDetailModel = (ReviewVendorDetailModel) bundle.getSerializable(REVIEW_VENDOR_DETAIL_MODEL);
            position = bundle.getInt(POSITION);
            positionImage = bundle.getInt(POSITION_IMAGE);
        }


        //todo: zoom image, check SliderImageAndVideoDetail
        checkData();
        registerSingleClick(R.id.frag_review_top_review_ll_like,R.id.frg_photo_detail_iv_close,R.id.dialog_view_vendor_tv_more);
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
    private void checkData(){
        if(reviewVendorDetailModel!=null){
            if(reviewVendorDetailModel.getGoogleReviewModel()==null){
                if(TCUtils.isEmpty(reviewVendorDetailModel.getTitle())){
                    tv_title.setVisibility(View.GONE);
                }else{
                    tv_title.setText(reviewVendorDetailModel.getTitle());
                }
                if (reviewVendorDetailModel.getAuthor() != null) {
                    tv_name_user.setText(!TCUtils.isEmpty(reviewVendorDetailModel.getAuthor().getFull_name()) ? reviewVendorDetailModel.getAuthor().getFull_name() : "");
                    Glide.with(getActiveActivity()).load(
                            TCUtils.isEmpty(reviewVendorDetailModel.getAuthor().getAvatar()) ?
                                    TCUtils.getDrawable(R.drawable.ic_avatar_user_gold) :
                                    reviewVendorDetailModel.getAuthor().getAvatar())
                            .apply(RequestOptions.circleCropTransform()
                                    .placeholder(TCUtils.getDrawable(R.drawable.ic_avatar_user_gold))
                                    .error(TCUtils.getDrawable(R.drawable.ic_avatar_user_gold)))
                            .into(iv_avatar_user);
                }
                rb_rating.setRating(Float.parseFloat(reviewVendorDetailModel.getRating()));
                tv_content.setText(reviewVendorDetailModel.getComment());
                TCUtils.getLineContenReview(tv_content,tv_read_more);
                //rb_rating.isIndicator(f);

                tv_total_like.setText(reviewVendorDetailModel.getLike_count());
                tv_total_like.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                int like = Integer.parseInt(reviewVendorDetailModel.getLike_count());
                tv_like.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                tv_like.setText(like > 1 ? TCUtils.getString(R.string.review_likes) : TCUtils.getString(R.string.review_like));

                tv_tip_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, reviewVendorDetailModel.getTip_amount()));
                tv_tip_amount.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                tv_tec.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                if (!TCUtils.isEmpty(reviewVendorDetailModel.getCreated())) {
                    tv_time_ago.setText(DateTimeAgo.timeAgo(
                            TCDateUtility.toDate(reviewVendorDetailModel.getCreated(),
                                    TCDateUtility.TCTimeZone.GMT,
                                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_SSSSSS).getTime()));
                }
                if(reviewVendorDetailModel.getImages()!=null){
                    if(reviewVendorDetailModel.getImages().size()>0){
                        SlideImageReviewDetailAdapter adapter =new SlideImageReviewDetailAdapter(getActiveActivity(),reviewVendorDetailModel.getImages(),true,false);
                        view_pager.setAdapter(adapter);
                        if(positionImage<reviewVendorDetailModel.getImages().size()){
                            view_pager.setCurrentItem(positionImage);
                        }
                        view_pager.setVisibility(View.VISIBLE);
                    }else{
                        view_pager.setVisibility(View.GONE);
                    }

                }else{
                    view_pager.setVisibility(View.GONE);
                }

            }else{
                view_pager.setVisibility(View.GONE);
                ll_like_tip.setVisibility(View.GONE);
                ll_content_review.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.GONE);
                tv_content.setText(reviewVendorDetailModel.getGoogleReviewModel().getText());
                TCUtils.getLineContenReview(tv_content,tv_read_more);
               tv_name_user.setText(reviewVendorDetailModel.getGoogleReviewModel().getAuthor_name());
                rb_rating.setRating(Float.parseFloat(reviewVendorDetailModel.getGoogleReviewModel().getRating()));
                tv_time_ago.setText(String.format(TCUtils.getString(R.string.review_reviewed_format_time), reviewVendorDetailModel.getGoogleReviewModel().getRelative_time_description()));

                Glide.with(getActiveActivity()).
                        load(reviewVendorDetailModel.getGoogleReviewModel().getProfile_photo_url()).apply(TCUtils.optionsCircleImageAvatar()).into(iv_avatar_user);

            }
        }
    }
    private void checkLikeAndTip(){
        boolean tipBefore = false;
        if (!TCUtils.isEmpty(reviewVendorDetailModel.getTip_of_user())) {
            float tip = Float.parseFloat(reviewVendorDetailModel.getTip_of_user());
            if (tip > 0)
                tipBefore = true;
        }
        if (!TCUtils.isMyselfReview(reviewVendorDetailModel.getAuthor().getPublic_key())) {
            showPopupTip(tipBefore, reviewVendorDetailModel.getAuthor().getPublic_key(), reviewVendorDetailModel.getId(), position);
        } else {
            ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.tip_message_yourself));
        }
        if(!reviewVendorDetailModel.isIs_like()){
            likeReview(reviewVendorDetailModel);
        }

    }
    private void showPopupTip(boolean tipBefore, String destinationPublicKey, String commentId, int pos) {
        DialogTipVendor dialogTipVendor = new DialogTipVendor(getActiveActivity(), tipBefore, destinationPublicKey, commentId, amount -> {
            if (amount > 0) {
                if (reviewVendorDetailModel.getTip_amount() != null) {
                    float tip_amount = Float.parseFloat(reviewVendorDetailModel.getTip_amount());
                    reviewVendorDetailModel.setTip_amount((amount + tip_amount) + "");
                    tv_tip_amount.setText(String.valueOf(amount+tip_amount));
                }
                if (reviewVendorDetailModel.getTip_of_user() != null) {
                    float tip_user = Float.parseFloat(reviewVendorDetailModel.getTip_of_user());
                    reviewVendorDetailModel.setTip_of_user((amount + tip_user) + "");
                }

            }
        });
        dialogTipVendor.show();
        dialogTipVendor.setCanceledOnTouchOutside(false);
    }
    public void updateLikeAndTipPhoto(){
        if (reviewVendorDetailModel!=null) {
            if(!TCUtils.isEmpty(reviewVendorDetailModel.getId())){
                if (reviewVendorDetailModel.isIs_like()) {
                    return;
                }
                reviewVendorDetailModel.setIs_like(true);
                if (reviewVendorDetailModel.getLike_count() != null) {
                    int like_count = Integer.parseInt(reviewVendorDetailModel.getLike_count());
                    if (like_count >= 0) {
                        like_count += 1;
                        reviewVendorDetailModel.setLike_count(like_count + "");
                        tv_total_like.setText(String.valueOf(like_count));
                    }
                }
            }
        }
    }
}
