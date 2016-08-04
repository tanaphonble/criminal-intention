package com.augmentis.ayp.crimin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.augmentis.ayp.crimin.model.Crime;
import com.augmentis.ayp.crimin.model.CrimeLab;

import java.util.List;


/**
 * Created by Tanaphon on 7/18/2016.
 */
public class CrimeListFragment extends Fragment {

    private static final int REQUEST_UPDATED_CRIME = 300;
    private static final String SUBTITLE_VISIBLE_STATE = "SUBTITLE_VISIBLE";
    private RecyclerView _crimeRecyclerView;
    private CrimeAdapter _adapter;
    private TextView _textViewSuggestAdd;

    protected static final String TAG = "CRIME_LIST";

    private boolean _subtitleVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        _crimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);
        _crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _textViewSuggestAdd = (TextView) v.findViewById(R.id.text_view_suggest_add_first_crime);

        if (savedInstanceState != null) {
            _subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE_STATE);
        }
        updateUI();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (_subtitleVisible) {
            menuItem.setTitle(R.string.hide_subtitle);
        } else {
            menuItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId(), true);
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                _subtitleVisible = !_subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (_adapter == null) {
            _adapter = new CrimeAdapter(crimes);
            _crimeRecyclerView.setAdapter(_adapter);
        } else {
            _adapter.setCrimes(crimeLab.getCrimes());
            _adapter.notifyDataSetChanged();
        }

        if (CrimeLab.getInstance(getActivity()).getCrimes().size() == 0 )
            _textViewSuggestAdd.setVisibility(View.VISIBLE);
        else {
            _textViewSuggestAdd.setVisibility(View.INVISIBLE);
        }
        updateSubtitle();
    }


    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        if (!_subtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Resume list");
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView _titleTextView;
        public TextView _dateTextView;
        public CheckBox _solvedCheckBox;
        int _position;

        Crime _crime;

        public CrimeHolder(View itemView) {
            super(itemView);
            _titleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            _solvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
            _dateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);

            itemView.setOnClickListener(this);
        }

        public void bind(final Crime crime, int position) {
            _crime = crime;
            _position = position;
            _titleTextView.setText(_crime.getTitle());
            _solvedCheckBox.setChecked(_crime.isSolved());
            _solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    _crime.setSolved(isChecked);
                    CrimeLab.getInstance(getActivity()).updateCrime(_crime);
                }
            });
            _dateTextView.setText(_crime.getCrimeDate().toString());
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), _crime.getId(), false);
            startActivity(intent);
        }
    }


    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> _crimes;

        public CrimeAdapter(List<Crime> crimes) {
            _crimes = crimes;
        }

        protected void setCrimes(List<Crime> crimes) {
            _crimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(v);
        }


        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = _crimes.get(position);
            holder.bind(crime, position);
        }

        @Override
        public int getItemCount() {
            return _crimes.size();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUBTITLE_VISIBLE_STATE, _subtitleVisible);
    }
}