package awe.devikamehra.popularmoviesapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import awe.devikamehra.popularmoviesapp.models.Response;
import awe.devikamehra.popularmoviesapp.ui.fragment.ReviewFragment;

/**
 * Created by Devika on 15-04-2016.
 */
public class ReviewsViewPagerAdapter extends FragmentStatePagerAdapter {

    Response response;

    public ReviewsViewPagerAdapter(FragmentManager fm, Response response) {
        super(fm);
        this.response = response;
    }

    @Override
    public Fragment getItem(int position) {
        return ReviewFragment.newInstance(response.getResponses().get(position).getAuthor(),
                response.getResponses().get(position).getContent());
    }

    @Override
    public int getCount() {

        Log.d("Size in Adapter", response.getResponses().size() + "");
        return response.getResponses().size();
    }
}
