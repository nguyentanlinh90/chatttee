package com.teecoin.model.general;

import com.teecoin.model.TeeCoinModel;

import java.util.ArrayList;
import java.util.Map;


public class FireBaseUploadedVideoModel extends TeeCoinModel {

    private ArrayList<FireBaseMediaModel> fireBaseMediaModels;

    public FireBaseUploadedVideoModel(ArrayList<Map<String, Object>> mediaModels, String uuid) {
        this.fireBaseMediaModels = new ArrayList<>();
        for (int i = 0; i < mediaModels.size(); i++) {
            FireBaseMediaModel appBannerModel = new FireBaseMediaModel(
                    String.valueOf(mediaModels.get(i).get(FireBaseMediaModel.ID)),
                    (String) mediaModels.get(i).get(FireBaseMediaModel.THUMBNAIL),
                    (String) mediaModels.get(i).get(FireBaseMediaModel.URL));
            this.fireBaseMediaModels.add(appBannerModel);
        }
    }

    public ArrayList<FireBaseMediaModel> getFireBaseMediaModels() {
        return fireBaseMediaModels;
    }
}
