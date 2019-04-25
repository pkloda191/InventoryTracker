package com.example.inventorytracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class CustomOnItemClickListenerMoreDetails implements AdapterView.OnItemClickListener
{
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //Core.myRef.getRoot().child("items").child(Core.keyList.get(position)).removeValue();
        //Toast.makeText(parent.getContext(), "You clicked " + position, Toast.LENGTH_SHORT).show();
    }
}
