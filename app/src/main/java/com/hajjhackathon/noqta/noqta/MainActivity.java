package com.hajjhackathon.noqta.noqta;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    DatabaseReference database;
    public static final int requestPermissionID = 101;
    Context mcontext;
    CameraSource visioncam;
    SurfaceView CameraView;
    TextView errortext;
    transport t ;




    private boolean checkPlateNumber(String email) {
        String regExpn =
                "^(([0-9]{1,4}))[A-Z]{3}$";
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mcontext = getApplication();

        CameraView = (SurfaceView) findViewById(R.id.surfaceView);
        errortext = (TextView) findViewById(R.id.error);

        database = FirebaseDatabase.getInstance().getReference();



        // if (checkCameraHardware(mcontext)) {
        ImageDetection(true);

        //   }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != requestPermissionID) {
            Log.d("permission result", "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                visioncam.start(CameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(focus).setRequestedFps(2.0f).build();
//get the value after vision cloud responce


            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            CameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        visioncam.start(CameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    visioncam.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textrecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        errortext.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    Log.e("TextBlock item ", item.toString());
                                    stringBuilder.append(item.getValue());
                                    Log.e("TextBlock getValue ", item.getValue().toString());
                                    //stringBuilder.append("\n");
                                    Log.e("TextBlock stringBui", stringBuilder.toString());
                                   }

                                final String plateNumber=stringBuilder.toString().replaceAll("\\s+","");
                                if(checkPlateNumber(plateNumber)){
                                    //search in the database
                                    errortext.setText(stringBuilder.toString());

                                    DatabaseReference ref = database.child("transport");
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot val : dataSnapshot.getChildren()){
                                                //I am not sure what record are you specifically looking for
                                                //This is if you are getting the Key which is the record ID for your Coupon Object
                                                if(val.getKey().contains(plateNumber)){
                                                    //Do what you want with the record
                                                    Log.e("found ", val.getKey());
                                                }else { Log.e("not found ", "not found");
                                                final MediaPlayer notAuthorised=MediaPlayer.create(MainActivity.this,R.raw.smoke);
                                                notAuthorised.start();}

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {


                                        }
                                    });



//                                    String plate = plateNumber;
//                                    database.child("transport").child(plate).child("permissionno").setValue("12347890");
//                                    Log.e("firebase",database.child("transport").child(plate).child("permissionno").setValue("12347890").toString());
//                                    database.child("transport").child(plate).child("makkahentrydate").setValue("3-9-2018");
//                                    database.child("transport").child(plate).child("arafatentriesno").setValue("5");
//                                    database.child("transport").child(plate).child("minaentriesno").setValue("9");
//                                    database.child("transport").child(plate).child("cartype").setValue("bus");

//                                    String transportorder = database.child("orders").push().getKey();
//                                    database.child("order").child(transportorder).child("fromarea").setValue("Al-azizia");
//                                    database.child("order").child(transportorder).child("toarea").setValue("Arafat");
//                                    database.child("order").child(transportorder).child("ordertime").setValue("10:00pm");
//                                    database.child("order").child(transportorder).child("drivername").setValue("Mohammed Khan");
//                                    database.child("order").child(transportorder).child("driverID").setValue("20009111098");
//                                    database.child("order").child(transportorder).child("plateno").setValue(plate);
//                                    database.child("order").child(transportorder).child("companyname").setValue("مؤسسة صالح شاهر الزيني");

                                    System.out.println("Done");
                                }
                                else{
                                    //search for another test to check
                                    System.out.println("Fail");
                                }





//                                myRef.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });


                            }
                        });
                    }
                }
            });
        }
    }
}


