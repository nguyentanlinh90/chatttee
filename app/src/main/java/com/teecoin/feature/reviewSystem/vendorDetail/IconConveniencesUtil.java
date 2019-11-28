package com.teecoin.feature.reviewSystem.vendorDetail;

import android.graphics.drawable.Drawable;

import com.teecoin.base.TCApplication;
import com.teecoin.utils.TCConstant;

public class IconConveniencesUtil {
//    private List<ListIconConveniences> iconList;
    //    public  ListIconConveniences(String name, Drawable drawable) {
//        this.name = name;
//        this.drawable = drawable;
//    }

//    private void initIconList(){
//        iconList = new ArrayList<>();
//        iconList.add(new ListIconConveniences("ic_convenience_air_conditioner",getDrawable("ic_convenience_air_conditioner")));
//        iconList.add(new ListIconConveniences("ic_convenience_booking",getDrawable("ic_convenience_booking")));
//        iconList.add(new ListIconConveniences("ic_convenience_buffet",getDrawable("ic_convenience_buffet")));
//        iconList.add(new ListIconConveniences("ic_convenience_car_parking",getDrawable("ic_convenience_car_parking")));
//        iconList.add(new ListIconConveniences("ic_convenience_delivery",getDrawable("ic_convenience_delivery")));
//        iconList.add(new ListIconConveniences("ic_convenience_do_not_bother",getDrawable("ic_convenience_do_not_bother")));
//        iconList.add(new ListIconConveniences("ic_convenience_fireplace",getDrawable("ic_convenience_fireplace")));
//        iconList.add(new ListIconConveniences("ic_convenience_free",getDrawable("ic_convenience_free")));
//        iconList.add(new ListIconConveniences("ic_convenience_handicapped",getDrawable("ic_convenience_handicapped")));
//        iconList.add(new ListIconConveniences("ic_convenience_karaoke",getDrawable("ic_convenience_karaoke")));
//        iconList.add(new ListIconConveniences("ic_convenience_membership_card",getDrawable("ic_convenience_membership_card")));
//        iconList.add(new ListIconConveniences("ic_convenience_orders",getDrawable("ic_convenience_orders")));
//        iconList.add(new ListIconConveniences("ic_convenience_outdoor_seating",getDrawable("ic_convenience_outdoor_seating")));
//        iconList.add(new ListIconConveniences("ic_convenience_payment_by_card",getDrawable("ic_convenience_payment_by_card")));
//        iconList.add(new ListIconConveniences("ic_convenience_private_dining",getDrawable("ic_convenience_private_dining")));
//        iconList.add(new ListIconConveniences("ic_convenience_projectors",getDrawable("ic_convenience_projectors")));
//        iconList.add(new ListIconConveniences("ic_convenience_serves_alcohol",getDrawable("ic_convenience_serves_alcohol")));
//        iconList.add(new ListIconConveniences("ic_convenience_sing_a_song",getDrawable("ic_convenience_sing_a_song")));
//        iconList.add(new ListIconConveniences("ic_convenience_take_away",getDrawable("ic_convenience_take_away")));
//        iconList.add(new ListIconConveniences("ic_convenience_tip_for_employee",getDrawable("ic_convenience_tip_for_employee")));
//        iconList.add(new ListIconConveniences("ic_convenience_tivi",getDrawable("ic_convenience_tivi")));
//        iconList.add(new ListIconConveniences("ic_convenience_toy",getDrawable("ic_convenience_toy")));
//        iconList.add(new ListIconConveniences("ic_convenience_wifi",getDrawable("ic_convenience_wifi")));
//
//        iconList.add(new ListIconConveniences("ic_convenience_android_default",getDrawable("ic_convenience_android_default")));// default
//    }


    public static boolean checkIconExits(String name) {
//        for(ListIconConveniences icon: iconList){
//            if(name.equals(icon.name)){
//                return true;
//            }
//        }
//        return false;

        int check = TCApplication.getActiveActivity().getResources()
                .getIdentifier(name,
                        TCConstant.DRAWABLE,
                        TCApplication.getActiveActivity().getPackageName());
        return check > 0;
    }

    public static Drawable getDrawable(String name) {
        int resourceId = TCApplication.getActiveActivity().getResources().getIdentifier(name, TCConstant.DRAWABLE, TCApplication.getActiveActivity().getPackageName());
        return TCApplication.getActiveActivity().getResources().getDrawable(resourceId);
    }

}
