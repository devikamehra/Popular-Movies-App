package awe.devikamehra.popularmoviesapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import awe.devikamehra.popularmoviesapp.models.Videos;
import awe.devikamehra.popularmoviesapp.ui.fragment.YoutubeFragment;

/**
 * Created by Devika on 15-04-2016.
 */
public class VideoViewPagerAdapter extends FragmentStatePagerAdapter {

    Videos videos;

    public VideoViewPagerAdapter(FragmentManager fm, Videos videos) {
        super(fm);
        this.videos = videos;
    }

    @Override
    public Fragment getItem(int position) {
        return YoutubeFragment.newInstance(videos.getVideos().get(position).getKey(), "");
    }

    @Override
    public int getCount() {

        Log.d("Size in Adapter", videos.getVideos().size() + "");
        return videos.getVideos().size();
    }
}
