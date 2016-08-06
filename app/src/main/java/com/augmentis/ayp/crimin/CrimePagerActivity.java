package com.augmentis.ayp.crimin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.augmentis.ayp.crimin.model.Crime;

import java.util.UUID;

public class CrimePagerActivity extends SingleFragmentActivity implements CrimeFragment.CallBacks {

    private static final String IS_ON_ADD_NEW_CRIME = "IS_ON_ADD_NEW_CRIME" ;

    private UUID _crimeId;
    private boolean _isNewCrime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

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

    @Override
    public void onCrimeUpdated(Crime crime) {
        // TODO I'll see what can i do here
    }

    @Override
    public void onCrimeDeleted() {
        finish();
    }
}