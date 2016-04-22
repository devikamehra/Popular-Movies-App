package awe.devikamehra.popularmoviesapp.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
import com.rey.material.widget.Button;

import org.parceler.Parcels;

import awe.devikamehra.popularmoviesapp.MyApp;
import awe.devikamehra.popularmoviesapp.R;
import awe.devikamehra.popularmoviesapp.models.MovieGist;
import awe.devikamehra.popularmoviesapp.ui.activities.ReviewActivity;
import awe.devikamehra.popularmoviesapp.ui.activities.YoutubeActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    // TODO: Rename and change types of parameters
    private MovieGist movieGist;

    @Bind(R.id.movie_name)
    TextView nameTextView;

    @Bind(R.id.movie_user_rating)
    TextView ratingTextView;

    @Bind(R.id.movie_plot_summary)
    TextView plotTextView;

    @Bind(R.id.movie_release_date)
    TextView releaseDateTextView;

    @Bind(R.id.header)
    ImageView header;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;

    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Bind(R.id.button_read_reviews)
    Button readReviewsButton;

    @Bind(R.id.button_watch_trailer)
    Button watchTrailerButton;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movieGist Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(MovieGist movieGist) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, Parcels.wrap(movieGist));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieGist = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            getActivity().getActionBar().setDisplayShowHomeEnabled(true);
        }



        populateData();
        showReviews();
        showTrailer();
        floatingActionButton.setImageDrawable(new IconDrawable(getContext(), MaterialIcons.md_share));
        shareFab();
        return view;
    }

    private void shareFab() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String share = getString(R.string.share_begin) +
                        movieGist.getTitle() +
                        getString(R.string.share_middle);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, share);
                intent.setType(getString(R.string.share_intent_format));
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
            }
        });
    }

    private void showReviews() {
        readReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putLong(ReviewActivity.MOVIE_ID_TEXT, movieGist.getId());
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra(ReviewActivity.MOVIE_ID_TEXT, bundle);
                startActivity(intent);
            }
        });
    }

    private void showTrailer() {

        watchTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong(YoutubeActivity.MOVIE_ID_VIDEO, movieGist.getId());
                Intent intent = new Intent(getActivity(), YoutubeActivity.class);
                intent.putExtra(YoutubeActivity.MOVIE_ID_VIDEO, bundle);
                startActivity(intent);
            }
        });

    }

    private void populateData() {
        setToolbarText(movieGist.getTitle());
        Glide.with(MyApp.getContext()).load(MyApp.getContext().getResources().getString(R.string.image_base_url)
                + movieGist.getPosterPath())
                .asBitmap()
                .placeholder(android.R.drawable.picture_frame)
                .error(android.R.drawable.stat_notify_error)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {
                            //header.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            header.setImageBitmap(resource);
                        }
                    }
                });

        nameTextView.setText(movieGist.getTitle());
        ratingTextView.setText(movieGist.getVoteAverage() + " / 10");
        releaseDateTextView.setText(movieGist.getReleaseDate());
        plotTextView.setText(movieGist.getPlotSummary());
    }

    private void setToolbarText(final String string) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(string);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
    }
}