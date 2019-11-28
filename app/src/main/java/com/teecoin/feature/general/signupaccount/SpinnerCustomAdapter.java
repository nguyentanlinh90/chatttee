package com.teecoin.feature.general.signupaccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.teecoin.R;

import java.util.ArrayList;
import java.util.Locale;

public class SpinnerCustomAdapter extends ArrayAdapter<String> {

    private ArrayList<String> countryCodeList;

    public SpinnerCustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.countryCodeList = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getDropDownCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getSelectedCustomView(position, convertView, parent);
    }

    private View getSelectedCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(R.layout.spinner_custom_view, parent, false);
        TextView label = row.findViewById(R.id.spinner_custom_view_tv_name);
        int callingCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryCodeList.get(position));
        label.setText(String.format("+%d", callingCode));
        return row;
    }


    private View getDropDownCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(R.layout.spinner_custom_view, parent, false);
        TextView label = row.findViewById(R.id.spinner_custom_view_tv_name);
        Locale loc = new Locale("", countryCodeList.get(position));
        int callingCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryCodeList.get(position));
        label.setText(String.format("%s +(%d)", loc.getDisplayCountry(), callingCode));
        return row;
    }
}