package com.neteru.hermod.activities.ui_utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.neteru.hermod.R;
import com.neteru.hermod.classes.OnSwipeTouchListener;
import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.neteru.hermod.classes.AppUtilities.SAVE_PICTURE_DOWNLOAD_DIRECTORY;
import static com.neteru.hermod.classes.AppUtilities.getFadeInAnimation;
import static com.neteru.hermod.classes.AppUtilities.getFadeOutAnimation;
import static com.neteru.hermod.classes.AppUtilities.manipulateColor;
import static com.neteru.hermod.classes.Constants.EMPTY;

public class ImageViewActivity extends AppCompatActivity {

    public final static String URL = "url";
    public final static String URL_LIST = "url_list";
    public final static String PATH = "path";
    public final static String PATH_LIST = "path_list";
    public final static String URI_STR = "uri";
    public final static String URI_STR_LIST = "uri_list";
    public final static String POSITION = "position";
    public final static String SAVE_MENU = "save_menu";

    private int position;
    private int mPaletteColor;
    private List<String> imgList = new ArrayList<>();
    private TouchImageView touchImageView;
    private ConstraintLayout rootView;
    private Toolbar toolbar;
    private Window window;
    private boolean save;
    private String currentImg;

    private final static int REQUEST_WRITE_PERMISSION = 4320;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.bringToFront();
        appBarLayout.invalidate();

        // Barre d'outils
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Vue image
        touchImageView = findViewById(R.id.touchImageView);

        // Vue racine
        rootView = findViewById(R.id.rootView);

        // Barre d'action
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(EMPTY);

        }

        // Instance de la fenètre
        window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(ImageViewActivity.this,R.color.black));
        }

        if (getIntent() != null){

            // Etat de l'option de sauvegarde
            save = getIntent().getBooleanExtra(SAVE_MENU, true);

            // Position de départ
            position = getIntent().getIntExtra(POSITION, 0);

            if (getIntent().hasExtra(URL)){ // Si URL existe

                loadImage(getIntent().getStringExtra(URL), false);

            }else if(getIntent().hasExtra(PATH)){ // Si PATH existe

                loadImage(getIntent().getStringExtra(PATH), false);

            }else if(getIntent().hasExtra(URI_STR)){ // Si URI existe

                loadImage(getIntent().getStringExtra(URI_STR), true);

            }else if (getIntent().hasExtra(URL_LIST)){ // Si URL existe

                imgList = getIntent().getStringArrayListExtra(URL_LIST);

                if (imgList == null) return;

                loadImage(imgList.get(position), false);

            }else if(getIntent().hasExtra(PATH_LIST)){ // Si PATH existe

                imgList = getIntent().getStringArrayListExtra(PATH_LIST);

                if (imgList == null) return;

                loadImage(imgList.get(position), false);

            }else if(getIntent().hasExtra(URI_STR_LIST)){ // Si URI existe

                imgList = getIntent().getStringArrayListExtra(URI_STR_LIST);

                if (imgList == null) return;

                loadImage(imgList.get(position), true);
            }

        }

        // Disparition du sélecteur d'aperçus au touché
        // Ecouteur de mouvements
        OnSwipeTouchListener onImgSwipeTouchListener = new OnSwipeTouchListener(this){

            @Override
            public void onClick() {
                if (toolbar.getVisibility() == View.VISIBLE){

                    toolbar.setVisibility(View.GONE);
                    toolbar.startAnimation(getFadeOutAnimation(ImageViewActivity.this));

                    /* window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); */

                } else {

                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.startAnimation(getFadeInAnimation(ImageViewActivity.this));

                    /* window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); */

                }

                super.onClick();
            }

            @Override
            public void onSwipeLeft() {

                if (position + 1 >= imgList.size()) return;

                position += 1;

                touchImageView.setVisibility(View.GONE);
                touchImageView.startAnimation(getFadeOutAnimation(ImageViewActivity.this));

                loadImage(imgList.get(position), getIntent().hasExtra(URI_STR_LIST));

                touchImageView.setVisibility(View.VISIBLE);
                touchImageView.startAnimation(getFadeInAnimation(ImageViewActivity.this));

                super.onSwipeLeft();
            }

            @Override
            public void onSwipeRight() {

                if (position - 1 < 0) return;

                position -= 1;

                touchImageView.setVisibility(View.GONE);
                touchImageView.startAnimation(getFadeOutAnimation(ImageViewActivity.this));

                loadImage(imgList.get(position), getIntent().hasExtra(URI_STR_LIST));

                touchImageView.setVisibility(View.VISIBLE);
                touchImageView.startAnimation(getFadeInAnimation(ImageViewActivity.this));

                super.onSwipeRight();
            }

        };

        touchImageView.setOnTouchListener(onImgSwipeTouchListener);

    }

    private void loadImage(String source, boolean parse){

        currentImg = source;

        Glide
                .with(this)
                .asBitmap()
                .load(parse ? Uri.parse(source) : source)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startPostponedEnterTransition();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startPostponedEnterTransition();
                        }
                        if (resource != null) {
                            Palette p = Palette.from(resource).generate();
                            // Use generated instance
                            mPaletteColor = p.getMutedColor(ContextCompat.getColor(ImageViewActivity.this, R.color.black));

                            customizeComponentColor();
                        }
                        return false;
                    }
                })
                .into(touchImageView);

    }

    private void customizeComponentColor() {

        // Adaptation de la barre de status aux versions supérieures à LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(manipulateColor(mPaletteColor, 0.32f));
        }

        // Couleur d'arrière-plan
        rootView.setBackgroundColor(mPaletteColor);
    }

    private void loadImageRes(){

        Glide.with(this)
                .asBitmap()
                .load(getIntent().hasExtra(URI_STR_LIST) ? Uri.parse(currentImg) : currentImg)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImage(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

    }

    private void saveImage(Bitmap bitmap){

        String imageFileName = "IMG_"+ Calendar.getInstance().getTimeInMillis()+".jpg";
        File storageDir = new File(SAVE_PICTURE_DOWNLOAD_DIRECTORY);
        boolean success = true;

        if (!storageDir.exists()){success = storageDir.mkdirs();}

        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            try {

                OutputStream fOutput = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutput);
                fOutput.close();

                galleryAddPic(imageFile.getAbsolutePath());
                Toast.makeText(ImageViewActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(ImageViewActivity.this, R.string.error, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (save){
            getMenuInflater().inflate(R.menu.save_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_save) {
            ActivityCompat.requestPermissions(ImageViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                loadImageRes();

            } else {
                Toast.makeText(ImageViewActivity.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
