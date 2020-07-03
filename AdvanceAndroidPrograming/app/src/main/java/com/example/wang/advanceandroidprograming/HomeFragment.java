package com.example.wang.advanceandroidprograming;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class HomeFragment extends Fragment {
    private static final String ARG_HOME_ID = "home_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private Home h;
    private EditText mNameEditText;
    private EditText mSerialEditText;
    private EditText mValueEditText;
    private  Button mDateButton;
    private  Button  mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onHomeUpdated(Home home);
    }

    public static HomeFragment newInstance(UUID homeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_HOME_ID, homeId);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID homeId = (UUID) getArguments().getSerializable(ARG_HOME_ID);

        h = HomeStorage.get(getActivity()).getItem(homeId);

        mPhotoFile = HomeStorage.get(getActivity()).getPhotoFile(h);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //name to settext and display on screen when text is changed
        mNameEditText = (EditText) v.findViewById(R.id.nameEditText);
        mNameEditText.setText(h.getName());
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                h.setName(s.toString());
                updateHome();

            }
            @Override
            public void afterTextChanged(Editable s) {
            }});
        //value to settext and display on screen when text is changed
        mValueEditText = (EditText) v.findViewById(R.id.valueEditText);
        Log.d("???" +  Integer.toString(h.getValue()).equals(0),"check");

        if( h.getValue() == 0){
            mValueEditText.setText("");
        }else{
            mValueEditText.setText(Integer.toString(h.getValue()));
        }



        mValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

              try{
                    h.setValue(Integer.parseInt(s.toString()));
                  updateHome();
                }catch(NumberFormatException c){
                  Toast.makeText(getActivity(),"Please input a number for value!", Toast.LENGTH_SHORT).show();
                    c.printStackTrace();
              }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }});

        mSerialEditText = (EditText) v.findViewById(R.id.serialEditText);
        mSerialEditText.setText(h.getSerial());
        mSerialEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                h.setSerial(s.toString());
                updateHome();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }});


        mPhotoButton = (ImageButton) v.findViewById(R.id.imageButton);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null ;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.example.wang.advanceandroidprograming.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.imageView);
        updatePhotoView();
        //report
        mReportButton = (Button) v.findViewById(R.id.report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,msgSender());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.home_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });





        //date button
        mDateButton = (Button) v.findViewById(R.id.dateButton);
        updateDate();

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(h.getDate());
                dialog.setTargetFragment(HomeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        return v;




    }

    @Override
    public void onPause() {
        super.onPause();
        HomeStorage.get(getActivity())
                .updateItem(h);
    }

    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            h.setDate(date);
            updateDate();
        }else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.example.wang.advanceandroidprograming.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updateHome() {
        HomeStorage.get(getActivity()).updateItem(h);
        mCallbacks.onHomeUpdated(h);
    }

    private void updateDate() {
        mDateButton.setText(h.getDate().toString());
    }







    private String msgSender() {
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, h.getDate()).toString();
        Resources res = getResources();
         String report = getString(R.string.home_report,
                h.getName(), dateString,h.getSerial(),h.getValue());
        return report;

    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

}

