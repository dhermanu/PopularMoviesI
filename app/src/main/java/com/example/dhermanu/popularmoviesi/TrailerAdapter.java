package com.example.dhermanu.popularmoviesi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dhermanu.popularmoviesi.Interface.ItemClickListener;
import com.example.dhermanu.popularmoviesi.Model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dhermanu on 5/28/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        public TextView trailerTitle, trailerSite, trailerQuality;
        public static ImageView trailerImage;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerTitle = (TextView) itemView.findViewById(R.id.list_item_forecast_movie);
            trailerSite = (TextView) itemView.findViewById(R.id.trailerSite);
            trailerQuality = (TextView) itemView.findViewById(R.id.trailerQuality);
            trailerImage = (ImageView) itemView.findViewById(R.id.trailerImage);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getPosition(), false);
        }
    }

    private List<Trailer> mTrailer;
    public Context mContext;

    public TrailerAdapter(List<Trailer> trailers, Context context){
        mTrailer = trailers;
        mContext = context;
    }


    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View trailerView = inflater.inflate(R.layout.list_item_movie, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(trailerView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = mTrailer.get(position);
        TextView trailerTitle = holder.trailerTitle;
        TextView trailerQuality = holder.trailerQuality;
        TextView trailerSite = holder.trailerSite;
        ImageView trailerImage = holder.trailerImage;
        Context context = ViewHolder.trailerImage.getContext();

        trailerTitle.setText(trailer.getName());
        trailerQuality.setText("Quality: " + trailer.getSize() +"p");
        trailerSite.setText("Site: " + trailer.getSite());

        final String BASE_URL = "http://img.youtube.com/vi/";
        final String TRAILER_KEY = trailer.getKey();
        final String IMAGE_QUALITY = "default.jpg";

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TRAILER_KEY)
                .appendPath(IMAGE_QUALITY)
                .build();

        Picasso
                .with(context)
                .load(buildUri.toString())
                .fit()
                .into(trailerImage);

        holder.setClickListener(new ItemClickListener(){
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                String URL = "http://www.youtube.com/watch?v=" + mTrailer.get(position).getKey();
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));

                if (sendIntent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(sendIntent);
                }

                else {
                    Toast.makeText(mContext, "No Media Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailer.size();
    }
}