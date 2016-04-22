package awe.devikamehra.popularmoviesapp;

import awe.devikamehra.popularmoviesapp.models.ListOfMovies;
import awe.devikamehra.popularmoviesapp.models.Response;
import awe.devikamehra.popularmoviesapp.models.Videos;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Devika on 11-02-2016.
 */
public interface IMDBService {

    public final static String PAGES_URL_PART = "page";
    public final static String API_KEY_URL_PART = "api_key";

    @GET("movie/popular")
    Call<ListOfMovies> getListOfPopularMovies(@Query(PAGES_URL_PART) long pages, @Query(API_KEY_URL_PART) String apiKey);

    @GET("movie/top_rated")
    Call<ListOfMovies> getListOfTopRatedMovies(@Query(PAGES_URL_PART) long pages, @Query(API_KEY_URL_PART) String apiKey);

    @GET("movie/{id}/reviews")
    Call<Response> getMovieReviews(@Path("id") Long id, @Query(API_KEY_URL_PART) String apiKey);

    @GET("movie/{id}/videos")
    Call<Videos> getMovieVideos(@Path("id") Long id, @Query(API_KEY_URL_PART) String apiKey);
}
