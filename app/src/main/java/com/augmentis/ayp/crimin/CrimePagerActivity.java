package com.augmentis.ayp.crimin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimePagerActivity extends SingleFragmentActivity {

    private static final String IS_ON_ADD_NEW_CRIME = "IS_ON_ADD_NEW_CRIME" ;

    private UUID _crimeId;
    private boolean _isNewCrime;

    @Override
    protected Fragment onCreateFragment() {
        _crimeId = (UUID) getIntent().getSerializableExtra(CRIME_ID);
        _isNewCrime = (boolean) getIntent().getSerializableExtra(IS_ON_ADD_NEW_CRIME);
        return CrimeFragment.newInstance(_crimeId, _isNewCrime);
    }

    protected static final String CRIME_ID = "crimePagerActivity.crimeId";

    public static Intent newIntent(Context activity, UUID id, boolean isOnAddNewCrime) {
        Intent intent = new Intent(activity, CrimePagerActivity.class);
        intent.putExtra(CRIME_ID, id);
        intent.putExtra(IS_ON_ADD_NEW_CRIME, isOnAddNewCrime);
        return intent;
    }
}