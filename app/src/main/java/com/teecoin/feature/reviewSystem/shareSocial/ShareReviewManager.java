package com.teecoin.feature.reviewSystem.shareSocial;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.teecoin.utils.TCUtils;

import java.io.File;
import java.util.List;

import static core.base.BaseApplication.getActiveActivity;

public class ShareReviewManager {
    public static void shareToFacebook(ShareDialog shareDialog, String link) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    public static void shareToInstagram(String mediaPath, String message) {
        Intent instagramIntent = new Intent(Intent.ACTION_SEND);
        instagramIntent.setType("image/*");
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);
        instagramIntent.putExtra(Intent.EXTRA_STREAM, uri);
        if (!TCUtils.isEmpty(message)) {
            instagramIntent.putExtra(Intent.EXTRA_TEXT, message);
        }
        instagramIntent.setPackage("com.instagram.android");
        PackageManager packManager = getActiveActivity().getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(instagramIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.instagram.android")) {
                instagramIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            getActiveActivity().startActivity(instagramIntent);
        } else {
            Toast.makeText(getActiveActivity(), "Instagram App is not installed", Toast.LENGTH_LONG).show();
        }
    }
}
