package awe.devikamehra.popularmoviesapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import awe.devikamehra.popularmoviesapp.MyApp;
import awe.devikamehra.popularmoviesapp.R;
import awe.devikamehra.popularmoviesapp.models.Videos;
import awe.devikamehra.popularmoviesapp.ui.fragment.YoutubeFragment;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Retrofit;

public class YoutubeActivity extends AppCompatActivity {

    public static final String MOVIE_ID_VIDEO = "movie_id_video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
        }

        MyApp.getImdbService().getMovieVideos(getIntent().getBundleExtra(MOVIE_ID_VIDEO).getLong(MOVIE_ID_VIDEO),
                getString(R.string.api_key))
                .enqueue(new Callback<Videos>() {
                    @Override
                    public void onResponse(retrofit.Response<Videos> response, Retrofit retrofit) {
                        Log.d("Size in Activity", response.body().getVideos().size() + "");
                        Log.d("key", response.body().getVideos().get(0).getKey());
                        if (response.body().getVideos().size() > 0) {
                            FragmentManager manager = getSupportFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.container,
                                            YoutubeFragment.newInstance(response.body().getVideos().get(0).getKey(), ""))
                                    .addToBackStack(null)
                                    .commit();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

    }

}
