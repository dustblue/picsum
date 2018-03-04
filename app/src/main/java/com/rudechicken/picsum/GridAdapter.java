package com.rudechicken.picsum;

/**
 * Created by Rakesh on 16-12-2017.
 */

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.rudechicken.picsum.MainActivity.ifError;

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
                .resize(width / 2, width / 2)
                .into(holder.itemImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        ifError[holder.getAdapterPosition()] = false;
                    }

                    @Override
                    public void onError() {
                        ifError[holder.getAdapterPosition()] = true;
                    }
                });
    }

    @Override
    public int getItemCount() {
        return imageUrls.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private ConstraintLayout parent;

        ViewHolder(View view) {
            super(view);
            itemImage = (ImageView) view.findViewById(R.id.item_image);
            parent = (ConstraintLayout) view.findViewById(R.id.grid_layout_item);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 2, width / 2);
            parent.setLayoutParams(params);
        }
    }
}
