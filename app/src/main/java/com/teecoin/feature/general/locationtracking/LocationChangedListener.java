package com.teecoin.feature.general.locationtracking;

import android.location.Location;

public interface LocationChangedListener {
    void onLocationChanged(Location location);
}