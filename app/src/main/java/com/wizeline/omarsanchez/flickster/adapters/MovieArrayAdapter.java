package com.wizeline.omarsanchez.flickster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wizeline.omarsanchez.flickster.Activities.DetailActivity;
import com.wizeline.omarsanchez.flickster.R;
import com.wizeline.omarsanchez.flickster.models.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by omarsanchez on 3/9/17.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private final int TYPE_LOW = 0;
    private final int TYPE_HIGH = 1;


    static class ViewHolder {
        @Nullable
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @Nullable
        @BindView(R.id.tvCaption)
        TextView tvOverView;
        @BindView(R.id.ivMovieImage)
        ImageView ivImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }


    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //get data position
        Movie movie = getItem(position);

        int type = getItemViewType(position);
        ViewHolder viewHolder = null;
        switch (type) {
            case TYPE_LOW:

                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.item_movie, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);

                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }


                break;
            case TYPE_HIGH:
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.item_popular_movie, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                break;
        }

        if (viewHolder != null) {
            //populate data
            if (viewHolder.tvTitle != null) {
                viewHolder.tvTitle.setText(movie.getOriginalTitle());
            }
            if (viewHolder.tvOverView != null) {
                viewHolder.tvOverView.setText(movie.getOverview());
            }
            //clear out image from convertView
            viewHolder.ivImage.setImageResource(0);
            Picasso.with(getContext()).load(movie.getImagePath()).placeholder(android.R.drawable.picture_frame).transform(new RoundedCornersTransformation(5, 0)).into(viewHolder.ivImage);
            viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), DetailActivity.class);
                    i.putExtra(DetailActivity.EXTRA_MOVIE, getItem(position));
                    getContext().startActivity(i);
                }
            });

        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getPopularity().ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return Movie.Popularity.values().length;
    }
}
