package com.hajjhackathon.noqta.noqta;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {
    Context mcontext;
    CameraSource visioncam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = getApplication();


        if (checkCameraHardware(mcontext)) {
            ImageDetection(true);

        }

    }

    //check if the device if it has a camera
    private boolean checkCameraHardware(Context context) {
        if (mcontext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void ImageDetection(boolean focus) {
        //esraa,1) we defined the text recognetion
        TextRecognizer textrecognizer = new TextRecognizer.Builder(mcontext).build();

        //2)check the text recognetion
        if (!textrecognizer.isOperational()) {

            //3) check the reason why it's failed
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, "Your mobie is run out of storage", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(mcontext, "Failed to initialize the app try again ", Toast.LENGTH_SHORT).show();

        } else {
            //3)define the camera source to make realtime view for the text recognizer
            visioncam = new CameraSource.Builder(mcontext, textrecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(getWindow().getDecorView().getWidth(), getWindow().getDecorView().getHeight())
                    .setAutoFocusEnabled(focus).build();
//get the value after vision cloud responce

        }


    }
}
