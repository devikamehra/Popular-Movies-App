package awe.devikamehra.popularmoviesapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import awe.devikamehra.popularmoviesapp.R;
import awe.devikamehra.popularmoviesapp.ui.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    public static boolean isBigScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), "list")
                .commit();

        if (findViewById(R.id.container_detail) != null){
            isBigScreen = true;
        }

    }

    public void setToolbar(Toolbar toolbar){
        setSupportActionBar(toolbar);
    }

    public FragmentManager getFragManager(){
        return getSupportFragmentManager();
    }

    public static boolean isBigScreen() {
        return isBigScreen;
    }
}
