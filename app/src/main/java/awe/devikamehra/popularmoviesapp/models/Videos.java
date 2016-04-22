package awe.devikamehra.popularmoviesapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Devika on 19-02-2016.
 */
public class Videos {

    @SerializedName("id")
    public Long id;

    @SerializedName("results")
    public ArrayList<SingleVideo> videos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<SingleVideo> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<SingleVideo> videos) {
        this.videos = videos;
    }
}
