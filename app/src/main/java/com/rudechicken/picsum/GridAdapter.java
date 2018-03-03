package com.rudechicken.picsum;

/**
 * Created by Rakesh on 16-12-2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private String[] imageUrls;
    private Context context;
    private int width;

    GridAdapter(Context context, String[] imageUrls, int width) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.width = width;
    }

    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridAdapter.ViewHolder holder, int position) {
        Picasso.with(context)
                .load(imageUrls[position])
                .placeholder(R.drawable.ic_default)
                .error(R.drawable.ic_error)
                .resize(width, width)
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return imageUrls.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;

        ViewHolder(View view) {
            super(view);
            itemImage = (ImageView) view.findViewById(R.id.item_image);
        }
    }
}
