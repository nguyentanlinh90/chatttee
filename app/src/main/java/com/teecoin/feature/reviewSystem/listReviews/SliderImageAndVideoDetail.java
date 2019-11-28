package com.teecoin.feature.reviewSystem.listReviews;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.animation.TCTouchZooImageView;
import com.teecoin.feature.reviewSystem.tip.DialogTipVendor;
import com.teecoin.model.reviewsystem.ImagesVendor;
import com.teecoin.ui.SimpleRatingBar;
import com.teecoin.utils.DateTimeAgo;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import static core.base.BaseApplication.getActiveActivity;


public class SliderImageAndVideoDetail extends PagerAdapter {
    private ArrayList<ImagesVendor> listData;
    private LayoutInflater inflater;
    private Context context;
    private SlideImageListener listener;
    private boolean zoom;
    private TCTouchZooImageView imageView_zoom;
    private boolean from_video;
    private int sizeImageHeight;
    private int sizeImageWidth;



    public SliderImageAndVideoDetail(Context context, ArrayList<ImagesVendor> listImages, boolean zoom, boolean from_video, SlideImageListener listener) {
        this.context = context;
        this.listData = listImages;
        inflater = LayoutInflater.from(context);
        this.zoom = zoom;
        this.from_video = from_video;
        this.listener = listener;
        this.sizeImageWidth = (TCScreenSize.getWidth(getActiveActivity()));
        this.sizeImageHeight = sizeImageWidth;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_images_list_review, view, false);
        view.addView(imageLayout, 0);
        if (imageLayout != null) {
            ImagesVendor item = listData.get(position);
            ImageView imageView = imageLayout.findViewById(R.id.item_list_review_iv_photo);

            VideoView video_view = imageLayout.findViewById(R.id.item_list_review_video_view);
            video_view.setBackgroundColor(Color.BLACK);
            RelativeLayout rl_play_video = imageLayout.findViewById(R.id.item_list_review_rl_play_video);
            RelativeLayout rl_parent_images = imageLayout.findViewById(R.id.rl_parent_images);
            RelativeLayout progress_bar = imageLayout.findViewById(R.id.rl_progress_bar);

            View ll_review =imageLayout.findViewById(R.id.dialog_view_vendor_ll_review);
            View ll_like_tip =imageLayout.findViewById(R.id.frag_review_top_review_ll_like);
            TextView tv_title=imageLayout.findViewById(R.id.dialog_view_vendor_tv_title);
            TextView tv_content=imageLayout.findViewById(R.id.dialog_view_vendor_tv_content);
            ImageView iv_avatar_user=imageLayout.findViewById(R.id.dialog_view_comment_iv_avatar_user);
            TextView tv_name_user=imageLayout.findViewById(R.id.dialog_view_comment_tv_name_user);
            //MaterialRatingBar rb_rating=imageLayout.findViewById(R.id.dialog_view_comment_rb_rating);
            SimpleRatingBar rb_rating = imageLayout.findViewById(R.id.dialog_view_comment_rb_rating);
            TextView tv_time_ago=imageLayout.findViewById(R.id.dialog_view_comment_tv_time_ago);
            TextView tv_total_like=imageLayout.findViewById(R.id.frag_list_review_tv_total_like);
            TextView tv_like=imageLayout.findViewById(R.id.frag_list_review_tv_like);
            TextView tv_tip_amount=imageLayout.findViewById(R.id.frag_list_review_tv_tip_amount);
            TextView tv_tec=imageLayout.findViewById(R.id.frag_list_review_tv_tec);


            progress_bar.setVisibility(View.GONE);
            rl_play_video.setVisibility(View.GONE);
            imageView_zoom = imageLayout.findViewById(R.id.item_list_review_iv_photo_zoom);
            ViewGroup.LayoutParams params = rl_play_video.getLayoutParams();
            params.width = sizeImageWidth;
            params.height = sizeImageHeight;
            rl_play_video.setLayoutParams(params);
            if (zoom) {
                imageView_zoom.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            } else {
                imageView_zoom.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
            Glide.with(context).load(item.getUrl()).apply(TCUtils.optionsSquareImage()).into(zoom ? imageView_zoom : imageView);

            if(item.getReview()!=null){
               // Review
                if(!TCUtils.isEmpty(item.getReview().getId())){
                    ll_review.setVisibility(View.VISIBLE);
                    if(!TCUtils.isEmpty(item.getReview().getTitle())){
                        tv_title.setText(item.getReview().getTitle());
                        tv_title.setVisibility(View.VISIBLE);
                    }else{
                        tv_title.setVisibility(View.GONE);
                    }
                    if(item.getReview().getAuthor()!=null){
                        tv_name_user.setText(!TCUtils.isEmpty(item.getReview().getAuthor().getFull_name()) ? item.getReview().getAuthor().getFull_name() : "");
                        Glide.with(getActiveActivity()).load(TCUtils.isEmpty(item.getReview().getAuthor().getAvatar()) ? TCUtils.getDrawable(R.drawable.ic_avatar_user_gold) : item.getReview().getAuthor().getAvatar()).into(iv_avatar_user);

                    }
                    if (!TCUtils.isEmpty(item.getReview().getCreated())) {
                        tv_time_ago.setText(DateTimeAgo.timeAgo(
                                TCDateUtility.toDate(item.getReview().getCreated(),
                                        TCDateUtility.TCTimeZone.GMT,
                                        TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_SSSSSS).getTime()));
                    }
                    tv_content.setText(item.getReview().getComment());
                    rb_rating.setRating(item.getReview().getRating());
                    tv_total_like.setText(item.getReview().getLike_count());
                    tv_total_like.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                    tv_like.setTextColor(TCUtils.getColor(R.color.c_ffffff));

                    tv_tip_amount.setText(TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, item.getReview().getTip_amount()));
                    tv_tip_amount.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                    tv_tec.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                }else{
                    ll_review.setVisibility(View.GONE);
                }

            }else{
                ll_review.setVisibility(View.GONE);
            }
            ll_like_tip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getReview()!=null){
                        boolean tipBefore = false;
                        if (!TCUtils.isEmpty(item.getReview().getTip_of_user())) {
                            float tip = Float.parseFloat(item.getReview().getTip_of_user());
                            if (tip > 0)
                                tipBefore = true;
                        }
                        if (!TCUtils.isMyselfReview(item.getReview().getAuthor().getPublic_key())) {
                            DialogTipVendor dialogTipVendor = new DialogTipVendor(getActiveActivity(), tipBefore, item.getReview().getAuthor().getPublic_key(), item.getReview().getId(), amount -> {
                                if (amount > 0) {
                                    if (item.getReview().getTip_amount() != null) {
                                        float tip_amount = Float.parseFloat(item.getReview().getTip_amount());
                                        item.getReview().setTip_amount((amount + tip_amount) + "");
                                    }
                                    if (item.getReview().getTip_of_user() != null) {
                                        float tip_user = Float.parseFloat(item.getReview().getTip_of_user());
                                        item.getReview().setTip_of_user((amount + tip_user) + "");
                                    }
                                }
                            });
                            dialogTipVendor.show();
                            dialogTipVendor.setCanceledOnTouchOutside(false);
                        } else {
                            ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.tip_message_yourself));
                        }
                    }

                }
            });
            // TODO CHECK VIEO
//            if (TCUtils.isEmpty(item.getVideo_url())) {
//                rl_parent_images.setVisibility(View.VISIBLE);
//                rl_play_video.setVisibility(View.GONE);
//                if (video_view.isPlaying()) {
//                    video_view.pause();
//                    video_view.stopPlayback();
//                }
//            } else {
//                rl_play_video.setVisibility(View.VISIBLE);
//                rl_parent_images.setVisibility(View.GONE);
//                if (video_view.isPlaying()) {
//                    video_view.pause();
//                    video_view.stopPlayback();
//                }
//                if (from_video) {
//                    from_video = false;
//                    TCUtils.playVideoReview(item.getVideo_url(), video_view, progress_bar, iv_cover_video, iv_play_video);
//                }
//                Glide.with(context).load(item.getUrl()).apply(TCUtils.optionsSquareImage()).into(iv_cover_video);
//            }
//            //iv_play_video.setOnClickListener(v -> playVideo(item,video_view,progress_bar,iv_cover_video,iv_play_video));
//            iv_play_video.setOnClickListener(v -> TCUtils.playVideoReview(item.getVideo_url(), video_view, progress_bar, iv_cover_video, iv_play_video));
//            if (listener != null) {
//                imageView.setOnClickListener(v -> listener.onClickPosition(position));
//                rl_play_video.setOnClickListener(v -> listener.onClickPosition(position));
//                imageView_zoom.setOnTouchListener((v, event) -> {
//                    return false;
//                });
//                imageView_zoom.setOnClickListener(v -> listener.onClickPosition(position));
//            }

//            check info review


        }
        return imageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

}
