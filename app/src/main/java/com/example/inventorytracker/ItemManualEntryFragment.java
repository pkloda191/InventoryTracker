package com.example.inventorytracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemManualEntryFragment extends Fragment implements View.OnClickListener
{
    private Spinner spinner;
    int itemImage;
    View myView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.item_manual_entry, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Button addItemButton = (Button)getActivity().findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(this);
        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();
    }

    public void onClick(View v)
    {
        FragmentManager fragmentManager = getFragmentManager();
        EditText itemNameET = (EditText)getActivity().findViewById(R.id.itemNameET);
        EditText itemLocationET = (EditText)getActivity().findViewById(R.id.itemLocationET);
        EditText itemQuantityET = (EditText)getActivity().findViewById(R.id.itemQuantityET);

        String itemName = itemNameET.getText().toString();
        String itemLocation = itemLocationET.getText().toString();
        int itemQuantity = Integer.parseInt(itemQuantityET.getText().toString());
        setItemImage();
        Item item = new Item(itemImage, itemName, itemLocation, itemQuantity);
        Core.addItemDB(item);
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewInventoryFragment()).addToBackStack("tag").commit();
    }

    public void addItemsOnSpinner()
    {
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Computer");
        list.add("Laptop");
        list.add("Monitor");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection()
    {
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener()); //this class passes the value of the image needed for the list
    }

    public void setItemImage()
    {
        if (Core.imageToUseForItem == 0)
        {
            itemImage = R.drawable.ic_desktop;
        }
        else if (Core.imageToUseForItem == 1)
        {
            itemImage = R.drawable.ic_laptop;
        }
        else
        {
            itemImage = R.drawable.ic_monitor;
        }
    }
}