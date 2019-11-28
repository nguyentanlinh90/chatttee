package com.teecoin.feature.reviewSystem.photoMedia;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.teecoin.model.reviewsystem.MediaModel;

import java.io.File;
import java.util.ArrayList;


public class PictureManager {

    private static final String MP4_EXTENDED_FILE = ".mp4";

    public static ArrayList<MediaModel> getImagesPath(Context activity) {
        ArrayList<MediaModel> listOfAllImages = new ArrayList<MediaModel>();
        String[] thumbColumns = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
        String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Files.FileColumns.MEDIA_TYPE};
        String[] whereArgs = {"image/jpeg", "image/png", "image/jpg", "video/mp4"};

        Uri uri = MediaStore.Files.getContentUri("external");

        String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?  ";

        Cursor cursor = activity.getContentResolver().query(uri, columns, where, whereArgs, orderBy);

        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            File file = new File(cursor.getString(column_index_data));
            if (file.exists() && !file.getPath().contains("/Android/data/")) {//skip all media file in /Android/data/
                listOfAllImages.add(new MediaModel(
                        cursor.getString(column_index_data), false,
                        cursor.getString(column_index_data).toLowerCase().contains(MP4_EXTENDED_FILE)));
            }
        }
        return listOfAllImages;
    }
}
