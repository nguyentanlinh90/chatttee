package com.teecoin.utils;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.teecoin.base.TCApplication;
import com.teecoin.feature.reviewSystem.userReviewShop.CropVideoListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CameraUtils {

    public static Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }
    public static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TeeCoinPIC");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

    //---
    public static File createImageFileWith() throws IOException {
        final String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "JPEG_" + timestamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pics");
        storageDir.mkdirs();
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    public static File createImageFileWith(String fileExtension) throws IOException {
        final String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "JPEG_" + timestamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pics");
        storageDir.mkdirs();
        return File.createTempFile(imageFileName, "." + fileExtension, storageDir);
    }

    public static File createVideoFileWith() throws IOException {
        final String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "MP4_" + timestamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "videos");
        storageDir.mkdirs();
        return File.createTempFile(imageFileName, ".mp4", storageDir);
    }

    public static void deleteMediaFiles() {
        try {
            File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pics");
            File videoDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "videos");
            for (File image : imageDir.listFiles()) {
                image.delete();
                //Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media. .Playlists.EXTERNAL_CONTENT_URI, i);
                //TCApplication.getActiveActivity().getContentResolver().delete(Uri.fromFile(image), null, null);
            }
            for (File video : videoDir.listFiles()) {
                video.delete();
            }
        } catch (Exception e) {
            TCLog.d("hung deleteMediaFiles exception=" + e.getMessage());
        }
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static void cropVideoWithSquareFrame(String videoFilePath, String cropSize, CropVideoListener cropVideoListener) {
        String cropVideoFilePath = videoFilePath.replace(".mp4", "crop.mp4");
        String[] cmd = {
                "-i",
                videoFilePath,
                "-filter:v",
                String.format("crop=%s:%s:0:0", cropSize, cropSize),
                "-codec:v",
                "libx264",
                "-profile:v",
                "high",
                "-preset",
                "ultrafast",
                "-c:a", "copy",
                cropVideoFilePath
        };
        final FFmpeg ffmpeg = FFmpeg.getInstance(TCApplication.getActiveActivity());
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onSuccess() {
                    try {
                        ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                            @Override
                            public void onStart() {
                                TCLog.d("hung: onStart");
                            }

                            @Override
                            public void onProgress(String message) {
                                TCLog.d("hung: onProgress:" + message);
                            }

                            @Override
                            public void onFailure(String message) {
                                TCLog.d("hung: onFailure:" + message);
                                if (cropVideoListener != null)
                                    cropVideoListener.onCropFail(message);
                            }

                            @Override
                            public void onSuccess(String message) {
                                TCLog.d("hung: onSuccess:" + message);
//                                if(cropVideoListener != null)
//                                    cropVideoListener.onCropFinish(cropVideoFilePath);
                            }

                            @Override
                            public void onFinish() {
                                TCLog.d("hung: onFinish");
                                if (cropVideoListener != null)
                                    cropVideoListener.onCropFinish(cropVideoFilePath);
                            }
                        });
                    } catch (FFmpegCommandAlreadyRunningException e) {
                        TCLog.d("hung: FFmpegCommandAlreadyRunningException:" + e.getMessage());
                        if (cropVideoListener != null)
                            cropVideoListener.onCropFail(e.getMessage());
                    }
                }

            });
        } catch (FFmpegNotSupportedException e) {
            TCLog.d("hung FFmpegNotSupportedException:" + e.getMessage());
            if (cropVideoListener != null)
                cropVideoListener.onCropFail(e.getMessage());
        }
    }
}
