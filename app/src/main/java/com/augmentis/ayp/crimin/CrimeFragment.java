package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Tanaphon on 7/18/2016.
 */
pu blic class CrimeFragment extends Fragment {

    private static final String CRIME_ID = "CrimeFragment.CRIME_ID";
    private static final String DIALOG_DATE = "CrimeFragment.DIALOG_DATE";
    private static final int REQUEST_DATE = 43692;
    private static final String IS_ON_ADD_NEW_CRIME = "IS_ON_ADD_NEW_CRIME";

    private Crime crime;

    private EditText editText;
    private Button crimeDateButton;
    private CheckBox crimeSolvedCheckbox;

    private Button deleteCrimeButton;

    private boolean isOnAddNewCrime;


    public CrimeFragment() {
    }

    public static CrimeFragment newInstance(UUID crimeId, boolean isOnAdd) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeId);
        args.putBoolean(IS_ON_ADD_NEW_CRIME, isOnAdd);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
        crime = CrimeLab.getInstance(getActivity()).getCrimeById(crimeId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        deleteCrimeButton = (Button) v.findViewById(R.id.button_delete_crime);
        isOnAddNewCrime = getArguments().getBoolean(IS_ON_ADD_NEW_CRIME);
        if (isOnAddNewCrime)
            deleteCrimeButton.setVisibility(View.INVISIBLE);
        else
            deleteCrimeButton.setVisibility(View.VISIBLE);

        editText = (EditText) v.findViewById(R.id.crime_title);
        editText.setText(crime.getTitle());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        deleteCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrimeLab.getInstance(getActivity()).deleteCrimeById(crime.getId());
                getActivity().finish();
            }
        });

        crimeDateButton = (Button) v.findViewById(R.id.crime_data);
        crimeDateButton.setText(crime.getSimpleDateFormat(crime.getCrimeDate()));
        crimeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///
                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment =
                        DatePickerFragment.newInstance(crime.getCrimeDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });

        crimeSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSolvedCheckbox.setChecked(crime.isSolved());
        crimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                Log.d(CrimeListFragment.TAG, "Crime:" + crime.toString());
            }
        });

        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            crime.setCrimeDate(date);
            crimeDateButton.setText(crime.getSimpleDateFormat(crime.getCrimeDate()));
        }

    }
}
