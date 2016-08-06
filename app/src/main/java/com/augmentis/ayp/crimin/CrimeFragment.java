package com.augmentis.ayp.crimin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.augmentis.ayp.crimin.model.Crime;
import com.augmentis.ayp.crimin.model.CrimeLab;
import com.augmentis.ayp.crimin.model.PictureUtils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Tanaphon on 7/18/2016.
 */
public class CrimeFragment extends Fragment {

    private static final String CRIME_ID = "CrimeFragment.CRIME_ID";
    private static final String DIALOG_DATE = "CrimeFragment.DIALOG_DATE";
    private static final String DIALOG_TIME = "CrimeFragment.DIALOG_TIME";
    private static final int REQUEST_DATE = 43692;
    private static final int REQUEST_TIME = 43697;
    private static final int REQUEST_CONTACT_SUSPECT = 43334;
    private static final int REQUEST_CAPTURE_PHOTO = 37254;
    private static final int REQUEST_FULL_PICTURE = 32454;
    private static final String DIALOG_PHOTO = "CrimeFragment.DIALOG_PHOTO";
    private String suspectContactName;
    private String suspectContactNumber;

    private static final String IS_ON_ADD_NEW_CRIME = "IS_ON_ADD_NEW_CRIME";
    private static final int REQUEST_CALL_ACTION = 32467;


    private File photoFile;
    private Crime crime;
    private EditText editTextCrime;
    private Button crimeDateButton;
    private Button crimeTimeButton;
    private Button crimeReportButton;
    private CheckBox crimeSolvedCheckbox;
    private Button crimeSuspectButton;
    private Button crimeCallSuspectButton;
    private ImageButton photoButton;
    private ImageView photoView;
    private CallBacks callBacks;
    int nextCrimePos;

    public CrimeFragment() {
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeId);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    // Callback
    public interface CallBacks {
        void onCrimeUpdated(Crime crime);
        void onCrimeDeleted();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBacks = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBacks = (CallBacks) context;
    }

    private void reloadCrimeFromDB(){
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
        crime = crimeLab.getCrimeById(crimeId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        reloadCrimeFromDB();
        if(crime != null)
            photoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(crime);
    }

    private void editTextCrimeSetting() {
        editTextCrime.setText(crime.getTitle());
        editTextCrime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void crimeDateButtonSetting() {
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
    }

    private void crimeTimeButtonSetting() {
        crimeTimeButton.setText(crime.getSimpleTimeFormat(crime.getCrimeDate()));
        crimeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                TimePickerFragment timePickerFragment =
                        TimePickerFragment.newInstance(crime.getCrimeDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(fm, DIALOG_TIME);
            }
        });
    }

    private void crimeSolvedCheckboxSetting() {
        crimeSolvedCheckbox.setChecked(crime.isSolved());
        crimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    crime.setSolved(isChecked);
                    updateCrime();
                }
                callBacks.onCrimeUpdated(crime);
            }
        });
    }

    private void crimeReportButtonSetting() {
        crimeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); // MIME type
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReported());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));

                i = Intent.createChooser(i, getString(R.string.send_report));

                startActivity(i);
            }
        });
    }

    private void crimeSuspectButtonSetting() {
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

        crimeSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT_SUSPECT);
            }
        });

        if (crime.getSuspect() != null) {
            crimeSuspectButton.setText(crime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();

        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            crimeSuspectButton.setEnabled(false);
        }
    }

    private void crimeCallSuspectButtonSetting() {
        crimeCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSuspectWrapper();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
                nextCrimePos = crimeLab.getCrimePosById(crime.getId());
                crimeLab.deleteCrime(crime.getId());
                callBacks.onCrimeDeleted();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_fragment_menu, menu);
    }


    private void photoViewSetting() {
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                PhotoViewDialogFragment photoViewDialogFragment =
                        PhotoViewDialogFragment.newInstance(photoFile);
                photoViewDialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_FULL_PICTURE);
                photoViewDialogFragment.show(fm, DIALOG_PHOTO);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        photoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        photoView = (ImageView) v.findViewById(R.id.crime_photo);
        photoViewSetting();

        editTextCrime = (EditText) v.findViewById(R.id.crime_title);
        editTextCrimeSetting();

        crimeDateButton = (Button) v.findViewById(R.id.crime_date);
        crimeDateButtonSetting();

        crimeTimeButton = (Button) v.findViewById(R.id.crime_time);
        crimeTimeButtonSetting();

        crimeSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSolvedCheckboxSetting();

        crimeReportButton = (Button) v.findViewById(R.id.crime_report);
        crimeReportButtonSetting();

        crimeSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        crimeSuspectButtonSetting();

        crimeCallSuspectButton = (Button) v.findViewById(R.id.button_call_suspect_crime);
        crimeCallSuspectButtonSetting();

        final Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);

        final Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = photoFile != null
                && captureImageIntent.resolveActivity(getActivity().getPackageManager()) != null;

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(photoFile);
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImageIntent, REQUEST_CAPTURE_PHOTO);
            }
        });

        // update photo changing
        updatePhotoView();

        return v;
    }

    // check self permission whether it has perm or not
    private void callSuspectWrapper() {
        int hasWriteCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (hasWriteCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_ACTION);
            return;
        }
        callSuspect();
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) {
            photoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(),
                    getActivity());

            photoView.setImageBitmap(bitmap);
        }
    }

    private void callSuspect() {
        try {
            Intent intentCall = new Intent(Intent.ACTION_CALL);
            intentCall.setData(Uri.parse("tel:" + suspectContactNumber));
            startActivityForResult(intentCall, REQUEST_CALL_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_ACTION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callSuspect();
                } else {
                    callSuspect();
                    Toast.makeText(getActivity(), R.string.denied_permission_to_call,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                return;
        }
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
            updateCrime();
        }
        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            crime.setCrimeDate(date);
            crimeTimeButton.setText(crime.getSimpleTimeFormat(crime.getCrimeDate()));
            updateCrime();
        }
        if (requestCode == REQUEST_CONTACT_SUSPECT) {
            if (data != null) {
                Uri contactUri = data.getData();
                String[] queryFields = new String[]{
                        ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                Cursor c = getActivity()
                        .getContentResolver()
                        .query(contactUri
                                , queryFields
                                , null
                                , null
                                , null);

                try {
                    if (c.getCount() == 0) {
                        return;
                    }
                    c.moveToFirst();
                    suspectContactName = c.getString(0);
                    suspectContactNumber = c.getString(1);
                    String suspect = suspectContactName + ": " + suspectContactNumber;
                    Log.d("ggwp", "suspect is ---> " + suspect);
                    crime.setSuspect(suspect);
                    crimeSuspectButton.setText(suspect);
                } finally {
                    c.close();
                }
            }
        }

        if (requestCode == REQUEST_CAPTURE_PHOTO) {
            updatePhotoView();
        }
    }

    public Crime getNextCrime(){
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        Crime nextCrime = null;
        try {
            nextCrime= crimeLab.getCrimes().get(nextCrimePos);
        }finally {
            try {
                if(nextCrime == null)
                    nextCrime = crimeLab.getCrimes().get(0);
            } finally {
                return nextCrime;
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void updateCrime() {
        CrimeLab.getInstance(getActivity()).updateCrime(crime);
        callBacks.onCrimeUpdated(crime);
    }

    private String getCrimeReported() {
        String solvedString = null;

        if (crime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, crime.getCrimeDate()).toString();

        String suspect = crime.getSuspect();

        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_with_suspect);
        }

        String report = getString(R.string.crime_report, crime.getTitle(), dateString, solvedString, suspect);
        return report;
    }
}