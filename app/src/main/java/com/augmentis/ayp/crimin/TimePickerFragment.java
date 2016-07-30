package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Tanaphon on 7/30/2016.
 */
public class TimePickerFragment extends DialogFragment implements DialogInterface.OnClickListener {
    protected static final String EXTRA_TIME = "EXTRA_TIME";

    public static TimePickerFragment newInstance(Date time_data) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, time_data);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    TimePicker _timePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date time_date = (Date) getArguments().getSerializable(EXTRA_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time_date);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        _timePicker = (TimePicker) v.findViewById(R.id.time_picker_in_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _timePicker.setHour(hour);
            _timePicker.setMinute(minute);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle("Pick Time");
        builder.setPositiveButton(android.R.string.ok, this);
        return builder.create();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hour = _timePicker.getHour();
            int minute = _timePicker.getMinute();
            Date mDate = (Date) getArguments().getSerializable(EXTRA_TIME);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            Date date = new GregorianCalendar(year, month, dayOfMonth, hour, minute).getTime();
            sendResult(Activity.RESULT_OK, date);
        }
    }

    private void sendResult(int resultCode, Date date) {
        if(getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
