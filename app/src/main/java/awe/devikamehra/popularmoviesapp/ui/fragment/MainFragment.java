package awe.devikamehra.popularmoviesapp.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;

import awe.devikamehra.popularmoviesapp.MyApp;
import awe.devikamehra.popularmoviesapp.R;
import awe.devikamehra.popularmoviesapp.models.ListOfMovies;
import awe.devikamehra.popularmoviesapp.models.MovieGist;
import awe.devikamehra.popularmoviesapp.ui.RecyclerViewUtils.EndlessRecyclerOnScrollListener;
import awe.devikamehra.popularmoviesapp.ui.RecyclerViewUtils.SimpleGridDecoration;
import awe.devikamehra.popularmoviesapp.ui.activities.MainActivity;
import awe.devikamehra.popularmoviesapp.ui.adapter.MoviesListAdapter;
import awe.devikamehra.popularmoviesapp.ui.adapter.SortOrderAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    public static final String MOVIE_LIST = "movie_list";
    public static final String MOVIE_LIST_TYPE = "movie_list_type";

    @Bind(R.id.movie_list)
    RecyclerView recyclerView;

    ArrayList<MovieGist> moviesDetail = new ArrayList<>();
    ArrayList<String> sortOrder = new ArrayList<>();

    @Bind(R.id.sort_order_spinner)
    Spinner sortSpinner;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.loading_text)
    TextView loadingText;

    GridLayoutManager gridLayoutManager;
    FrameLayout frameLayoutDetail;

    public static boolean bigScreen = false;

    /**
     * 1 - Popular
     * 2 - Top Rated
     * 3 - Favourite
     */
    public static int type = 1;

    int maximum = 1;
    int currentPage = 1;

    Realm realm;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            moviesDetail = Parcels.unwrap(savedInstanceState.getParcelable(MOVIE_LIST));
            type = savedInstanceState.getInt(MOVIE_LIST_TYPE);
            Log.d("size", moviesDetail.size() + "");
        }
        realm = Realm.getDefaultInstance();
        if (getArguments() != null) {
          /*
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        */
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_LIST, Parcels.wrap(moviesDetail));
        outState.putInt(MOVIE_LIST_TYPE, type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_base, container, false);
        ButterKnife.bind(this, view);
        if (MainActivity.isBigScreen()){
            frameLayoutDetail = (FrameLayout)view.findViewById(R.id.container_detail);
            bigScreen = true;
        }
        ((MainActivity) getActivity()).setToolbar(toolbar);
        if (bigScreen){
            gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        }else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        }
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SimpleGridDecoration(getContext()));
        Log.d("size in onCreateView", moviesDetail.size() + "");

        manageSpinner();
        return view;
    }

    private void manageSpinner() {
        sortOrder.add(getString(R.string.most_popular_text));
        sortOrder.add(getString(R.string.high_rating_text));
        sortOrder.add(getString(R.string.favourites));

        sortSpinner.setAdapter(new SortOrderAdapter(getActivity(), sortOrder));
        if (type == 1){
            sortSpinner.setSelection(0);
        }else if (type == 2){
            sortSpinner.setSelection(1);
        }else if (type == 3){
            sortSpinner.setSelection(2);
        }
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    setPopularMovie(true, 1);
                    type = 1;
                } else if (i == 1) {
                    setTopRatedMovie(true, 1);
                    type = 2;
                } else {
                    setFavouriteMovie();
                    type = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setFavouriteMovie(){
        moviesDetail.clear();
        Log.d("s", realm.distinct(MovieGist.class, "id").size() + "");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                moviesDetail.addAll(realm.where(MovieGist.class).findAll());
            }
        });
        Log.d("size fav", moviesDetail.size() + "");
        recyclerDataSetting(moviesDetail);
    }

    public void setPopularMovie(final boolean clearData, long i){
        MyApp.getImdbService().getListOfPopularMovies(i, getString(R.string.api_key))
                .enqueue(new Callback<ListOfMovies>() {
                    @Override
                    public void onResponse(final Response<ListOfMovies> response, Retrofit retrofit) {
                        Log.d("success", response.message() + response.isSuccess() + response.errorBody());
                        maximum = response.body().getTotalPages();
                        updateUI(clearData, response);
                    }


                    @Override
                    public void onFailure(Throwable t) {
                        loadingText.setText(getResources().getString(R.string.error_msg));
                        t.printStackTrace();
                    }
                });

    }

    public void setTopRatedMovie(final boolean clearData, long i){
        MyApp.getImdbService().getListOfTopRatedMovies(i, getString(R.string.api_key))
                .enqueue(new Callback<ListOfMovies>() {
                    @Override
                    public void onResponse(final Response<ListOfMovies> response, Retrofit retrofit) {
                        maximum = response.body().getTotalPages();
                        updateUI(clearData, response);
                    }


                    @Override
                    public void onFailure(Throwable t) {
                        loadingText.setText(getResources().getString(R.string.error_msg));
                        t.printStackTrace();
                    }
                });

    }

    private void updateUI(boolean clearData, Response<ListOfMovies> response) {
        if (loadingText.getVisibility() == View.GONE) {
            loadingText.setVisibility(View.VISIBLE);
        }
        Log.d("Tag in call", moviesDetail.size() + "");
        if (moviesDetail.size() != 0 && clearData) {
            moviesDetail.clear();
        }
        moviesDetail.addAll(response.body().getDetailsArrayList());
        recyclerDataSetting(moviesDetail);

        if (loadingText.getVisibility() == View.VISIBLE) {
            loadingText.setVisibility(View.GONE);
        }
    }

    public void recyclerDataSetting(final ArrayList<MovieGist> md){
        MoviesListAdapter adapter = new MoviesListAdapter(getActivity(), md);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore() {
                if (currentPage <= maximum) {
                    currentPage++;
                    if (type == 1) {
                        setPopularMovie(false, currentPage);
                    } else if (type == 2) {
                        setTopRatedMovie(false, currentPage);
                    }
                }
            }
        });

    }

    public static int getType() {
        return type;
    }

    public static void setType(int type) {
        MainFragment.type = type;
    }

    public static boolean isBigScreen() {
        return bigScreen;
    }

    public static void setBigScreen(boolean bigScreen) {
        MainFragment.bigScreen = bigScreen;
    }

}


