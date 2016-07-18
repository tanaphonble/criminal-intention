package com.augmentis.ayp.crimin;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;

/**
 * Created by Tanaphon on 7/18/2016.
 */
public class CrimeFragment extends Fragment {

    private Crime crime;

    private EditText editText;
    private Button crimeDateButton;
    private CheckBox crimeSolvedCheckbox;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crime = new Crime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        editText = (EditText) v.findViewById(R.id.crime_title);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
                //Log.d(CrimeActivity.TAG, crime.getTitle());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        crimeDateButton = (Button) v.findViewById(R.id.crime_data);

        crimeDateButton.setText(crime.getSimpleDateFormat(crime.getCrimeDate()));
        crimeDateButton.setEnabled(false);

        crimeSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                Log.d(CrimeActivity.TAG, "Crime:" + crime.toString());
            }
        });

        return v;
    }
}
