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
import android.view.WindowManager;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progress;
    String[] imageUrls;
    int numberOfImages = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int w = (int) (displayMetrics.widthPixels / 2);

        imageUrls = new String[numberOfImages];

        for (int i = 0; i < numberOfImages; i++) {
            imageUrls[i] = "https://picsum.photos/" + w + "/" + w + "/?image=" + pic();
        }

        GridAdapter adapter = new GridAdapter(this, imageUrls, w);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        (view, position) -> {
                            Log.e("Rak", imageUrls[position]);
                            Intent i = new Intent(MainActivity.this, PictureActivity.class);
                            i.putExtra("url", imageUrls[position]);
                            startActivity(i);
                        })
        );

    }

    public int pic() {
        Random rand = new Random();
        return rand.nextInt(1085);
        //TODO Remove Image not found errors
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

}
