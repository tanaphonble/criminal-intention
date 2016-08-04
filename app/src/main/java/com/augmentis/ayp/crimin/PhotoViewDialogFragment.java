package com.augmentis.ayp.crimin;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.augmentis.ayp.crimin.model.PictureUtils;

import java.io.File;

/**
 * Created by Tanaphon on 8/4/2016.
 */
public class PhotoViewDialogFragment extends DialogFragment {

    private static final String CRIME_PHOTO_FILE = "CrimeFragment.CRIME_PHOTO_FILE";
    ImageView photoView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        File photoFile = (File) getArguments().getSerializable(CRIME_PHOTO_FILE);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);
        photoView = (ImageView) v.findViewById(R.id.full_crime_photo);
        Bitmap bitmap = PictureUtils.getScaledBitmap( photoFile.getPath(),
                getActivity());
        photoView.setImageBitmap(bitmap);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Crime Photo");
        builder.setView(v);

        return builder.create();
    }

    public static PhotoViewDialogFragment newInstance(File photoFile) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_PHOTO_FILE, photoFile);
        PhotoViewDialogFragment fragment = new PhotoViewDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
