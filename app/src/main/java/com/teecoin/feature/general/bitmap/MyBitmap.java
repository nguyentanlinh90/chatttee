package com.teecoin.feature.general.bitmap;

import com.teecoin.model.couponsystem.BitMapSelectModel;
import com.teecoin.model.couponsystem.BitMapUnSelectModel;

import java.util.ArrayList;

public class MyBitmap {

    private ArrayList<BitMapSelectModel> bitMapSelect;
    private ArrayList<BitMapUnSelectModel> bitMapUnSelect;

    public MyBitmap() {
        bitMapSelect = new ArrayList<>();
        bitMapUnSelect = new ArrayList<>();
    }

    public ArrayList<BitMapSelectModel> getBitMapSelect() {
        return bitMapSelect;
    }

    public void setBitMapSelect(ArrayList<BitMapSelectModel> bitMapSelect) {
        this.bitMapSelect = bitMapSelect;
    }

    public ArrayList<BitMapUnSelectModel> getBitMapUnSelect() {
        return bitMapUnSelect;
    }

    public void setBitMapUnSelect(ArrayList<BitMapUnSelectModel> bitMapUnSelect) {
        this.bitMapUnSelect = bitMapUnSelect;
    }
}
