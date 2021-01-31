package com.example.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.flixster.DetailActivity;
import com.example.flixster.MainActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Movie> movies;
    private final int BASIC = 0;
    private final int POPULAR = 1;
    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case BASIC:
                View mView = inflater.inflate(R.layout.item_movie,parent,false);
                viewHolder = new ViewHolderBasic(mView);
                break;
            case POPULAR:
                View view = inflater.inflate(R.layout.item_popular_movie,parent,false);
                viewHolder = new ViewHolderPopular(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        Movie movie = movies.get(position);
        switch (holder.getItemViewType()){
            case BASIC:
                ViewHolderBasic viewHolderBasic = (ViewHolderBasic) holder;
                viewHolderBasic.bind(movie);
                break;
            case POPULAR:
                ViewHolderPopular viewHolderPopular = (ViewHolderPopular) holder;
                viewHolderPopular.bind(movie);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(movies.get(position).getRating() > 5.0)
            return POPULAR;
        return BASIC;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolderPopular extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        ProgressBar progressBar;
        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPlayIcon;

        public ViewHolderPopular(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            progressBar = itemView.findViewById(R.id.prBar);
            container = itemView.findViewById(R.id.container);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPlayIcon = itemView.findViewById(R.id.ivPlayIcon);
        }

        public void bind(Movie movie) {
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                tvTitle.setText(movie.getTitle());
                tvOverview.setText(movie.getOverview());
            }
            int radius = 30;
            int margin = 10;
            progressBar.setVisibility(View.VISIBLE);
            ivPlayIcon.setVisibility(View.GONE);
            Glide.with(context)
                    .load(movie.getBackdropPath())
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            ivPlayIcon.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            ivPlayIcon.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(ivPoster);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)context, (View)ivPoster, "poster");
                    context.startActivity(i, options.toBundle());
                }
            });
        }
    }

    public class ViewHolderBasic extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ProgressBar progressBar;
        RelativeLayout container;

        public ViewHolderBasic(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            progressBar = itemView.findViewById(R.id.prBar);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            }
            else {
                imageUrl = movie.getPosterPath();
            }
            int radius = 30;
            int margin = 10;

            progressBar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(imageUrl)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(ivPoster);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    Pair<View, String> p1 = Pair.create((View) tvTitle, "title");
                    Pair<View, String> p2 = Pair.create((View) ivPoster, "poster");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)context, p1, p2);
                    context.startActivity(i, options.toBundle());
                }
            });
        }
    }
}
