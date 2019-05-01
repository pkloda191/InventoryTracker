package com.example.inventorytracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.CancellationSignal;
import android.print.PrintAttributes;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.zxing.WriterException;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;


public class PrintQRCodeFragment extends Fragment
{
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;
    boolean generateButtonClicked = false;
    Spinner itemSpinner;

    View myView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.print_qr_code, container, false);
        return myView;
    }

    String TAG = "GenerateQRCode";
    EditText edtValue;
    ImageView qrImage;
    Button start, save;
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        qrImage = (ImageView)getActivity().findViewById(R.id.QR_Image);
        edtValue = (EditText)getActivity().findViewById(R.id.edt_value);
        start = (Button)getActivity().findViewById(R.id.start);
        save = (Button)getActivity().findViewById(R.id.save);
        addItemsOnSpinner();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputValue = edtValue.getText().toString().trim();
                generateButtonClicked = true;
                if (inputValue.length() > 0) {
                    qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, getView().getWidth());
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v(TAG, e.toString());
                    }
                } else {
                    edtValue.setError("Required");
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean save;
                String result;

                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_STORAGE);
                        // MY_PERMISSIONS_REQUEST_CAMERA is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    try {
                        if (generateButtonClicked == false)
                        {
                            Toast.makeText(getActivity(), "Please generate a QR code first", Toast.LENGTH_LONG).show();
                        }
                        else {
                            save = QRGSaver.save(savePath, edtValue.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                            result = save ? "Image Saved" : "Image Not Saved";
                            Toast.makeText(getActivity(), result + " in " + savePath, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void addItemsOnSpinner()
    {
        itemSpinner = (Spinner)getActivity().findViewById(R.id.item_spinner);
        ArrayList<String> list = new ArrayList<String>();
        list.add("No Selection");
        for (int i = 0; i < Core.allItems.size(); i++)
        {
            list.add(Core.allItems.get(i).getItem_name());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinner.setAdapter(dataAdapter);
        itemSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                super.onItemSelected(parent, view, pos, id);
                if (!parent.getItemAtPosition(pos).toString().contains("No Selection"))
                {
                    edtValue.setText(parent.getItemAtPosition(pos).toString());
                }
            }
        });
    }
}

