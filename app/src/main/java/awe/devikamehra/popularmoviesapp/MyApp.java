package awe.devikamehra.popularmoviesapp;

import android.app.Application;
import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.squareup.okhttp.OkHttpClient;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Devika on 11-02-2016.
 */
public class MyApp extends Application{

    private static MyApp myApp;
    private static IMDBService imdbService;
    private static Context context;
    private static Gson gson;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setMyApp(this);
        context = getApplicationContext();
        Iconify.with(new MaterialModule());

        gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client((new OkHttpClient()))
                .build();

        setImdbService(retrofit.create(IMDBService.class));
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public synchronized static MyApp getMyApp() {
        return myApp;
    }

    private synchronized static void setMyApp(MyApp myApp) {
        MyApp.myApp = myApp;
    }

    public synchronized static IMDBService getImdbService() {
        return imdbService;
    }

    private synchronized static void setImdbService(IMDBService imdbService) {
        MyApp.imdbService = imdbService;
    }

    public static Gson getGson() {
        return gson;
    }
}
