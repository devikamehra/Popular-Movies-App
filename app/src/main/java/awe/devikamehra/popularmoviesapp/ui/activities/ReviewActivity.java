package awe.devikamehra.popularmoviesapp.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import awe.devikamehra.popularmoviesapp.MyApp;
import awe.devikamehra.popularmoviesapp.R;
import awe.devikamehra.popularmoviesapp.models.Response;
import awe.devikamehra.popularmoviesapp.ui.adapter.ReviewsViewPagerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Retrofit;

public class ReviewActivity extends AppCompatActivity {

    public static final String MOVIE_ID_TEXT = "movie_id_text";

    @Bind(R.id.container)
    ViewPager viewPager;

    @Bind(R.id.loading_text)
    TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main);
        int width = getResources().getConfiguration().screenWidthDp;
        Log.d("width", width + "");
        if(MainActivity.isBigScreen) {
            assert frameLayout != null;
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(width - 20, 300));
        }
        MyApp.getImdbService().getMovieReviews(getIntent().getBundleExtra(MOVIE_ID_TEXT).getLong(MOVIE_ID_TEXT),
                getString(R.string.api_key))
                .enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(retrofit.Response<Response> response, Retrofit retrofit) {
                        Log.d("Size in Activity", response.body().getResponses().size() + "");
                        if (response.body().getResponses().size() > 0){
                            loadingText.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            viewPager.setAdapter(new ReviewsViewPagerAdapter(getSupportFragmentManager(), response.body()));
                        }else{
                            loadingText.setText(R.string.no_review_text);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                });



    }

}
