package com.teecoin.feature.reviewSystem.searchLocation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.teecoin.utils.TCLog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {

    private OnBitmapDownloadedListener listener;

    public DownloadImagesTask(OnBitmapDownloadedListener listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        return download_Image(strings[0]);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        listener.setBitmap(result);
    }

    private Bitmap download_Image(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            TCLog.d("linhnt", "Error getting the image from server : " + e.getMessage());
        }
        return bm;
    }
}