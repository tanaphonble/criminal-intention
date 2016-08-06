package com.augmentis.ayp.crimin;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.augmentis.ayp.crimin.model.Crime;

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.CallBacks {

    @Override
    protected Fragment onCreateFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime, boolean isNewCrime) {
        if (findViewById(R.id.detail_fragment_ontainer) == null) {
            // single pane
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId(), isNewCrime);
        } else {

//            CrimeFragment currentDetailFragment = (CrimeFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.detail_fragment_ontainer);

//            if (currentDetailFragment != null && currentDetailFragment.getCrimeId().equals(crime.getId())) {

                Fragment newDetailFragment = CrimeFragment.newInstance(crime.getId(), isNewCrime);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_fragment_ontainer, newDetailFragment)
                        .commit();
//            }

//            else {
//                currentDetailFragment.updateUI();
//            }
        }
    }


    @Override
    public void onCrimeUpdated(Crime crime) {
        // Update List
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeDeleted() {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        CrimeFragment detailFragment = (CrimeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_fragment_ontainer);

        listFragment.updateUI();

        getSupportFragmentManager()
                .beginTransaction()
                .detach(detailFragment)
                .commit();
    }
}
