package com.augmentis.ayp.crimin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

 public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        Log.d(CrimeListFragment.TAG, "On create activity");

        FragmentManager fm = getSupportFragmentManager();

        Fragment f = fm.findFragmentById(R.id.fragment_container); // first time null

        if ( f == null ){
            f = onCreateFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
            Log.d(CrimeListFragment.TAG, "Fragment is created");
        }else {
            Log.d(CrimeListFragment.TAG, "Fragment have already been created");
        }



    }

    protected abstract Fragment onCreateFragment();
}
