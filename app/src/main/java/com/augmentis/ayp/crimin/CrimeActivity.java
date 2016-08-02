 package com.augmentis.ayp.crimin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    protected static final String CRIME_ID = "crimeActivity.crimeId";

    public static Intent newIntent(Context context, UUID id, int _position){
        Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(CRIME_ID, id);
        return intent;
    }

    @Override
    protected Fragment onCreateFragment() {
        return new CrimeFragment();
    }
}