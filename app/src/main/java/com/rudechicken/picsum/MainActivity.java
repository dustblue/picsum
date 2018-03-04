package com.rudechicken.picsum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progress;
    String[] imageUrls;
    int numberOfImages = 10;
    public static boolean[] ifError;
    GridAdapter adapter;
    int w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        w = (int) (displayMetrics.widthPixels);

        imageUrls = new String[numberOfImages];
        ifError = new boolean[numberOfImages];

        refreshImages();
        adapter = new GridAdapter(this, imageUrls, w);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        (view, position) -> {

                            if (!ifError[position]) {
                                Log.e("Rak", imageUrls[position]);
                                Intent i = new Intent(MainActivity.this, PictureActivity.class);
                                i.putExtra("url", imageUrls[position]);
                                startActivity(i);
                            } else {
                                imageUrls[position] = "https://picsum.photos/" + w + "/" + w + "/?image=" + getRandomPic();
                                adapter.notifyDataSetChanged();
                            }
                        })
        );

    }

    public void refreshImages() {
        for (int i = 0; i < numberOfImages; i++) {
            ifError[i] = false;
            imageUrls[i] = "https://picsum.photos/" + w + "/" + w + "/?image=" + getRandomPic();
        }
    }

    public static int getRandomPic() {
        Random rand = new Random();
        return rand.nextInt(1085);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> MainActivity.super.onBackPressed())
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int color;

        switch (item.getItemId()) {
            case R.id.refresh:
                refreshImages();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
