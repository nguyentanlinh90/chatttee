package com.teecoin.feature.reviewSystem.location;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.teecoin.model.reviewsystem.PlaceAutoCompleteModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class PlaceArrayAdapter extends ArrayAdapter<PlaceAutoCompleteModel> implements Filterable {
    public static ArrayList<PlaceAutoCompleteModel> mResultList;
    //    private static final String TAG = "PlaceArrayAdapter: ";
    private GoogleApiClient mGoogleApiClient;
    private AutocompleteFilter mPlaceFilter;

    public PlaceArrayAdapter(Context context, int resource, AutocompleteFilter filter) {
        super(context, resource);
        mPlaceFilter = filter;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            mGoogleApiClient = null;
        } else {
            mGoogleApiClient = googleApiClient;
        }
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public PlaceAutoCompleteModel getItem(int position) {
        return mResultList.get(position);
    }

    public ArrayList<PlaceAutoCompleteModel> getPredictions(CharSequence constraint) {
        if (mGoogleApiClient != null) {
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    null, mPlaceFilter);
            // Wait for predictions, set the timeout.
            AutocompletePredictionBuffer autocompletePredictions = results.await(30, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(getContext(), "Error: " + status.toString(), Toast.LENGTH_SHORT).show();
                autocompletePredictions.release();
                return null;
            }

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new PlaceAutoCompleteModel(prediction.getPlaceId(), prediction.getFullText(null)));
            }
            // Buffer release
            autocompletePredictions.release();
            return resultList;
        }
        return null;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    // Query the autocomplete API for the entered constraint
                    mResultList = getPredictions(constraint);
                    if (mResultList != null) {
                        // Results
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, updateTo the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
