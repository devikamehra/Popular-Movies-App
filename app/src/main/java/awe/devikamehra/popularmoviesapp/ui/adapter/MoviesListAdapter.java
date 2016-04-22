package awe.devikamehra.popularmoviesapp.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.parceler.Parcels;

import java.util.ArrayList;

import awe.devikamehra.popularmoviesapp.R;
import awe.devikamehra.popularmoviesapp.models.MovieGist;
import awe.devikamehra.popularmoviesapp.ui.activities.DetailActivity;
import awe.devikamehra.popularmoviesapp.ui.activities.MainActivity;
import awe.devikamehra.popularmoviesapp.ui.fragment.DetailFragment;
import awe.devikamehra.popularmoviesapp.ui.fragment.MainFragment;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Devika on 11-02-2016.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ListViewHolder>{

    ArrayList<MovieGist> listOfMovies;
    Activity activity;
    Realm realm;

    public MoviesListAdapter(Activity activity, ArrayList<MovieGist> listOfMovies) {
        this.listOfMovies = listOfMovies;
        this.activity = activity;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item, parent, false);
        return new ListViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {

        holder.nameOfMovie.setText(listOfMovies.get(position).getTitle());

        Glide.with(activity.getBaseContext()).load(activity.getBaseContext().getResources().getString(R.string.image_base_url)
                + listOfMovies.get(position).getPosterPath())
                .asBitmap()
                .placeholder(android.R.drawable.picture_frame)
                .error(android.R.drawable.stat_notify_error)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {
                            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            holder.imageView.setImageBitmap(resource);
                        }
                    }
                });

        final RealmResults<MovieGist> results = realm.where(MovieGist.class).equalTo("id", listOfMovies.get(position).getId()).findAll();
        if (results.size() == 1){
            holder.favourite.setImageDrawable(new IconDrawable(activity.getBaseContext(), MaterialIcons.md_star));
        }

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String share = activity.getBaseContext().getString(R.string.share_begin) +
                        listOfMovies.get(position).getTitle() +
                        activity.getBaseContext().getString(R.string.share_middle);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, share);
                intent.setType(activity.getBaseContext().getString(R.string.share_intent_format));
                activity.getBaseContext().startActivity(Intent.createChooser(intent, activity.getBaseContext().getString(R.string.share)));
            }
        });

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RealmResults<MovieGist> results = realm.where(MovieGist.class).equalTo("id", listOfMovies.get(position).getId()).findAll();
                Log.d("size in adapter", results.size() + "");
                if (results.size() == 1){
                    Log.d("in realm", results.get(0).getTitle());
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            try {
                                results.get(0).removeFromRealm();
                                if (MainFragment.getType() == 3){
                                    notifyItemRemoved(position);
                                    listOfMovies.remove(position);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    holder.favourite.setImageDrawable(new IconDrawable(activity.getBaseContext(), MaterialIcons.md_star_border));
                }else{
                    realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(listOfMovies.get(position));
                            }
                        });
                    Log.d("s", realm.distinct(MovieGist.class, "id").size() + "");
                    holder.favourite.setImageDrawable(new IconDrawable(activity.getBaseContext(), MaterialIcons.md_star));
                }
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainFragment.isBigScreen()){
                    TextView textView = (TextView) activity.findViewById(R.id.empty_view_text);
                    textView.setVisibility(View.GONE);
                    ((MainActivity)activity).getFragManager().beginTransaction()
                            .replace(R.id.container_detail, DetailFragment.newInstance(listOfMovies.get(position)), "detail")
                            .commit();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DetailActivity.MOVIE_ID, Parcels.wrap(listOfMovies.get(position)));
                    Intent intent = new Intent(activity.getBaseContext(), DetailActivity.class);
                    intent.putExtra(DetailActivity.MOVIE_ID, bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.getBaseContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("scroll problem size", listOfMovies.size() + "");
        return listOfMovies.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView nameOfMovie;
        ImageView favourite,share;

        public ListViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            nameOfMovie = (TextView) itemView.findViewById(R.id.movie_name);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
            favourite.setImageDrawable(new IconDrawable(activity.getBaseContext(), MaterialIcons.md_star_border));
            share = (ImageView) itemView.findViewById(R.id.share);
            share.setImageDrawable(new IconDrawable(activity.getBaseContext(), MaterialIcons.md_share));
        }
    }

}
