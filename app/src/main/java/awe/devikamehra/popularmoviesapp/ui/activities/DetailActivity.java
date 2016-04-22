package awe.devikamehra.popularmoviesapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import awe.devikamehra.popularmoviesapp.R;
import awe.devikamehra.popularmoviesapp.models.MovieGist;
import awe.devikamehra.popularmoviesapp.ui.fragment.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    public static String MOVIE_ID = "movie_id";
    private MovieGist movieGist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        movieGist = Parcels.unwrap(getIntent().getBundleExtra(DetailActivity.MOVIE_ID).getParcelable(DetailActivity.MOVIE_ID));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, DetailFragment.newInstance(movieGist), "detail")
                .commit();

    }

    public FragmentManager getFragManager(){
        return getSupportFragmentManager();
    }


}
