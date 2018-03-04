package com.rudechicken.picsum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.rudechicken.picsum.MainActivity.getRandomPic;

public class AboutActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;

        imageView = (ImageView) findViewById(R.id.about_image);
        Picasso.with(this)
                .load("https://picsum.photos/" + width * 2 + "/" + width * 2 + "/?blur&image=" + getRandomPic())
                .placeholder(R.drawable.ic_default)
                .error(R.drawable.ic_error)
                .resize(width, width)
                .into(imageView);

        TextView textView = (TextView) findViewById(R.id.about_text);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "Images from:\n" + "<a href='http://picsum.photos'>picsum.photos</a>";
        textView.setText(Html.fromHtml(text, 0));

        TextView ipsum = findViewById(R.id.ipsum_text);
        TextView picsum = findViewById(R.id.picsum_text);
        ipsum.bringToFront();
        picsum.bringToFront();
    }
}
