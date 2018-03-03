package com.rudechicken.picsum;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureActivity extends AppCompatActivity {

    String imageUrl, pathName;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageUrl = getIntent().getStringExtra("url");
        String[] splitUrls = imageUrl.split("/");
        pathName = Environment.getExternalStorageDirectory().getPath()
                + "/" + splitUrls[splitUrls.length - 1].split("=")[1] + ".jpeg";

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int w = (int) (displayMetrics.widthPixels / 1.25);

        imageView = findViewById(R.id.image);
        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_default)
                .error(R.drawable.ic_error)
                .resize(w, w)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Snackbar.make(imageView, "Error getting Image!!", Snackbar.LENGTH_LONG).show();
                    }
                });

        FloatingActionButton fabDown = new FloatingActionButton(this);
        fabDown.setTitle("Wallpaper");
        fabDown.setSize(FloatingActionButton.SIZE_MINI);
        fabDown.setIconDrawable(getDrawable(R.drawable.ic_download));
        fabDown.setOnClickListener(view -> {
            downloadImage(this, imageUrl);
            Snackbar.make(view, "Picture Downloaded!!", Snackbar.LENGTH_LONG).show();
        });

        FloatingActionButton fabWall = new FloatingActionButton(this);
        fabWall.setTitle("Wallpaper");
        fabWall.setIconDrawable(getDrawable(R.drawable.ic_wallpaper));
        fabWall.setOnClickListener(view -> {
            try {
                /* Custom Wallpaper Set
                Uri imgUri = Uri.parse(pathName);
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(imgUri, "image/jpeg");
                intent.putExtra("mimeType", "image/jpeg");
                startActivity(Intent.createChooser(intent, "Set as:"));
                */
                Bitmap bMap = BitmapFactory.decodeFile(pathName);
                WallpaperManager m = WallpaperManager.getInstance(this);
                try {
                    m.setBitmap(bMap);
                    Snackbar.make(view, "Wallpaper Changed!!", Snackbar.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Snackbar.make(view, "Image not downloaded!!", Snackbar.LENGTH_LONG)
                        .setAction("Download", v -> {
                            fabDown.callOnClick();
                        }).show();
            }
        });

        FloatingActionsMenu fabMenu = findViewById(R.id.fab_menu);
        fabMenu.addButton(fabDown);
        fabMenu.addButton(fabWall);

    }

    public static void downloadImage(Context context, String url) {
        Picasso.with(context)
                .load(url)
                .into(getTarget(url));
    }

    private static Target getTarget(final String url) {
        return new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(() -> {
                    String[] splitUrls = url.split("/");
                    String pathName = Environment.getExternalStorageDirectory().getPath()
                            + "/" + splitUrls[splitUrls.length - 1].split("=")[1] + ".jpeg";
                    File file = new File(pathName);
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        ostream.flush();
                        ostream.close();
                    } catch (IOException e) {
                        Log.e("IOException", e.getLocalizedMessage());
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

}
