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

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureActivity extends AppCompatActivity {

    String imageUrl, pathName;
    ImageView imageView;
    public static int height, width;
    FloatingActionButton fabDown, fabWall;

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
        width = (int) (displayMetrics.widthPixels);
        height = (int) (displayMetrics.heightPixels);

        imageView = findViewById(R.id.image);
        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_default)
                .error(R.drawable.ic_error)
                .resize(width, width)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Snackbar.make(imageView, "Error getting Image!!", Snackbar.LENGTH_LONG).show();
                    }
                });

        fabDown = findViewById(R.id.fab_down);
        fabDown.setOnClickListener(view -> {
            downloadImage(this, imageUrl, "2160");
            Snackbar.make(view, "Picture Downloaded!!", Snackbar.LENGTH_SHORT).show();
        });

        fabWall = findViewById(R.id.fab_wall);
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
                    Snackbar.make(view, "Wallpaper Changed!!", Snackbar.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Snackbar.make(view, "Image not downloaded!!", Snackbar.LENGTH_LONG)
                        .setAction("Download", v -> fabDown.callOnClick()).show();
            }
        });

        FloatingActionMenu fabMenu = findViewById(R.id.fab_menu);
        fabMenu.setHapticFeedbackEnabled(true);
    }

    public static void downloadImage(Context context, String url, String imageRes) {
        String[] urlParts = url.split("/");
        String urlNew = urlParts[0] + "//" + urlParts[2]
                + "/" + imageRes + "/" + imageRes + "/"
                + urlParts[5];
        Log.e("Rak", urlNew);

        //FIXME Saved only if clicked twice
        Picasso.with(context)
                .load(urlNew)
                .into(getTarget(urlNew));
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
                        Log.e("Rak", "Saved to: " + pathName);
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
