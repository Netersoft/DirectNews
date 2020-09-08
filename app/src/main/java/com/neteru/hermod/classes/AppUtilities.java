package com.neteru.hermod.classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.neteru.hermod.BuildConfig;
import com.neteru.hermod.R;
import com.thefinestartist.finestwebview.FinestWebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

import static com.neteru.hermod.classes.Constants.EMPTY;

@SuppressWarnings("unused")
public class AppUtilities {

    private final static String SHARE_PICTURE_DOWNLOAD_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/.news";
    public final static String SAVE_PICTURE_DOWNLOAD_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Direct News";

    public static void share(final Context context, final String url, final String title, final String description, String urlToImage){

        if (urlToImage != null && !urlToImage.isEmpty()) {

            final LoadingDialog loadingDialog = new LoadingDialog(context);
            loadingDialog.setMsg(context.getString(R.string.be_patient));
            loadingDialog.show();

            Glide.with(context)
                    .asBitmap()
                    .load(urlToImage)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            File storageDir = new File(SHARE_PICTURE_DOWNLOAD_DIRECTORY);
                            boolean success = true;

                            if (!storageDir.exists()){success = storageDir.mkdirs();}

                            if (success) {
                                File imageFile = new File(storageDir, Calendar.getInstance().getTimeInMillis() + ".jpg");
                                try {

                                    OutputStream fOutput = new FileOutputStream(imageFile);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, fOutput);
                                    fOutput.close();

                                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    File f = new File(imageFile.getAbsolutePath());
                                    Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", f);
                                    mediaScanIntent.setData(contentUri);
                                    context.sendBroadcast(mediaScanIntent);

                                    StringBuilder text = new StringBuilder();

                                    if (title != null){ text.append(title); }

                                    if (description != null){ text.append("\n\n").append(description); }

                                    if (url != null){ text.append("\n\n").append(url); }

                                    loadingDialog.dismiss();

                                    if (contentUri != null) {
                                        // Construct a ShareIntent with link to image
                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, text.toString());
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        shareIntent.setType("image/*");
                                        // Launch sharing dialog for image
                                        context.startActivity(Intent.createChooser(shareIntent, title != null ? title : EMPTY));
                                    } else {
                                        // ...sharing failed, handle error
                                        Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {

                                    e.printStackTrace();
                                    loadingDialog.dismiss();
                                    Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();

                                }
                            }

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        }else {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n\n" + description + "\n\n" + url);
            context.startActivity(Intent.createChooser(shareIntent, title));

        }
    }

    public static void openWebview(Context context, String url){
        new FinestWebView
                .Builder(context)
                .statusBarColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .toolbarColor(context.getResources().getColor(R.color.colorPrimary))
                .showIconClose(true)
                .showIconMenu(true)
                .showSwipeRefreshLayout(true)
                .swipeRefreshColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .showDivider(false)
                .dividerColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .showProgressBar(true)
                .progressBarColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .showUrl(true)
                .showMenuRefresh(true)
                .showMenuFind(false)
                .showMenuShareVia(true)
                .showMenuCopyLink(true)
                .showMenuOpenWith(true)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResRefresh(R.string.refresh)
                .stringResShareVia(R.string.share_via)
                .stringResCopyLink(R.string.copy_link)
                .stringResOpenWith(R.string.open_with)
                .backPressToClose(true)
                .toolbarScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)
                .gradientDivider(false)
                .webViewAppCacheEnabled(true)
                .webViewJavaScriptEnabled(true)
                .urlColorRes(R.color.whitesmoke)
                .titleColorRes(R.color.white)
                .iconDefaultColorRes(R.color.white)
                .show(url);
    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    /*---------------------- Animations ----------------------*/

    public static Animation getFadeInAnimation(Context context){
        return AnimationUtils.loadAnimation(context, R.anim.fade_in);
    }

    public static Animation getFadeOutAnimation(Context context){
        return AnimationUtils.loadAnimation(context, R.anim.fade_out);
    }

}
