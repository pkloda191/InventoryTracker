package com.example.inventorytracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ItemManualEntryFragment extends Fragment implements View.OnClickListener
{
    View myView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.item_manual_entry, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addItemButton = (Button)getActivity().findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        EditText itemNameET = (EditText)getActivity().findViewById(R.id.itemNameET);
        EditText itemLocationET = (EditText)getActivity().findViewById(R.id.itemLocationET);
        EditText itemQuantityET = (EditText)getActivity().findViewById(R.id.itemQuantityET);

        String itemName = itemNameET.getText().toString();
        String itemLocation = itemLocationET.getText().toString();
        int itemQuantity = Integer.parseInt(itemQuantityET.getText().toString());
        Item item = new Item(R.drawable.ic_laptop, itemName, itemLocation, itemQuantity);
        Core.addItemDB(item);
    }
}