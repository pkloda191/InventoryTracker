package com.example.inventorytracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class ViewInventoryFragment extends ListFragment
{
    ListView lv;
    View myView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.view_inventory, container, false);
        Core.itemAdapterNameLocation = new ItemCustomArrayAdapterNameLocation(getActivity(), Core.itemList);
        setListAdapter(Core.itemAdapterNameLocation);
        Core.itemAdapterNameLocation.notifyDataSetChanged();
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Core.fragmentManager = getFragmentManager();
        lv = getListView();
        lv.setOnItemClickListener(new CustomOnItemClickListenerMoreDetails()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                super.onItemClick(parent, view, position, id);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Add the buttons
                Item selectedItem = (Item) parent.getItemAtPosition(position);
                builder.setTitle(selectedItem.getItem_name());
                builder.setMessage(selectedItem.getLocation());
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //user wants to edit the item
                        //display another dialog box?
                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Core.myRef.getRoot().child("items").child(Core.keyList.get(position)).removeValue();
                        Core.itemList.remove(position);
                        Core.itemAdapterNameLocation.notifyDataSetChanged();
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
                    public void onShow(DialogInterface arg0)
                    {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00574B"));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00574B"));
                    }
                });
                dialog.show();
            }
        });
    }
}
