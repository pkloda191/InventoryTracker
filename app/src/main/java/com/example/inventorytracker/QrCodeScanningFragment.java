package com.example.inventorytracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QrCodeScanningFragment extends Fragment
{
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    public static boolean qrCodeItemFound = false;
    boolean itemFound = false;
    View myView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.qr_code_scanning, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        surfaceView = (SurfaceView) view.findViewById(R.id.camerapreview);
        textView = (TextView) view.findViewById(R.id.textView);
        barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector).setRequestedPreviewSize(1000, 1000).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                        // MY_PERMISSIONS_REQUEST_CAMERA is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    try {
                        cameraSource.start(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>()
        {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections)
            {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textView.setText(qrCodes.valueAt(0).displayValue);
                            QrCodeScanningFragment.qrCodeItemFound = true;
                            cameraSource.stop();
                            for (int i = 0; i < Core.allItems.size(); i++)
                            {
                                if (Core.allItems.get(i).getItem_name().contains(qrCodes.valueAt(0).displayValue))
                                {
                                    //item was found
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    // Add the buttons
                                    final String qrCodeNameResult = qrCodes.valueAt(0).displayValue;
                                    builder.setTitle("Item found in database\nDo you want to update it?");
                                    builder.setMessage(qrCodeNameResult);
                                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Core.itemEditName = qrCodeNameResult;
                                            Core.fragmentManager.beginTransaction().replace(R.id.content_frame, new ItemManualEntryFragment()).addToBackStack("tag").commit();
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                            //do nothing
                                        }
                                    });
                                    // Create the AlertDialog
                                    final AlertDialog dialog = builder.create();
                                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface arg0) {
                                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00574B"));
                                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00574B"));
                                        }
                                    });
                                    dialog.show();
                                    itemFound = true;
                                    break;
                                }
                            }
                            if (itemFound == false)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                // Add the buttons
                                final String qrCodeNameResult = qrCodes.valueAt(0).displayValue;
                                builder.setTitle("Item not found in database\nDo you want to add it?");
                                builder.setMessage(qrCodeNameResult);
                                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Core.itemEditName = qrCodeNameResult;
                                        Core.fragmentManager.beginTransaction().replace(R.id.content_frame, new ItemManualEntryFragment()).addToBackStack("tag").commit();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        //do nothing
                                    }
                                });
                                // Create the AlertDialog
                                final AlertDialog dialog = builder.create();
                                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface arg0) {
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00574B"));
                                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00574B"));
                                    }
                                });
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });
    }
}

