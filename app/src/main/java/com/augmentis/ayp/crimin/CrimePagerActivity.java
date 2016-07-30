package com.augmentis.ayp.crimin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String IS_ON_ADD_NEW_CRIME = "IS_ON_ADD_NEW_CRIME" ;

    private ViewPager _viewPager;
    private List<Crime> _crimes;
    private UUID _crimeId;

    private boolean _isNewCrime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        _crimeId = (UUID) getIntent().getSerializableExtra(CRIME_ID);

        _viewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        _crimes = CrimeLab.getInstance(this).getCrimes();

        _isNewCrime = (boolean) getIntent().getSerializableExtra(IS_ON_ADD_NEW_CRIME);

        FragmentManager fm = getSupportFragmentManager();

        _viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = _crimes.get(position);
                Fragment f = CrimeFragment.newInstance(crime.getId(), _isNewCrime);
                return f;
            }

            @Override
            public int getCount() {
                return _crimes.size();
            }
        });

        int position = CrimeLab.getInstance(this).getCrimePositionById(_crimeId);
        _viewPager.setCurrentItem(position);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(CrimeListFragment.TAG, "Crime Pager Activity: on pause");
    }

    protected static final String CRIME_ID = "crimePagerActivity.crimeId";
    /**
     * Create an intent for sending intent to this class
     *
     * @param activity intent from this activity
     * @param id       crime uuid
     * @return intent that for starting a new activity
     */
    public static Intent newIntent(Context activity, UUID id, boolean isOnAddNewCrime) {
        Intent intent = new Intent(activity, CrimePagerActivity.class);
        intent.putExtra(CRIME_ID, id);
        intent.putExtra(IS_ON_ADD_NEW_CRIME, isOnAddNewCrime);
        return intent;
    }

}