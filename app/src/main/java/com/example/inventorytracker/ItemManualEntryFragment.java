package com.example.inventorytracker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class ItemManualEntryFragment extends Fragment implements View.OnClickListener
{
    private Spinner imageSpinner;
    private Spinner categoriesSpinner;
    private Spinner locationSpinner;
    private int count = 1;
    String categoryName;
    String itemLocation;
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
        EditText itemNameET = (EditText)getActivity().findViewById(R.id.itemNameET);
        EditText itemNotesET = (EditText)getActivity().findViewById(R.id.itemNotesET);
        addItemButton.setOnClickListener(this);
        addImageSelectionOnSpinner();
        addCategoriesOnSpinner();
        addLocationsOnSpinner();
        addListenerOnSpinnerItemSelection();
        if (Core.itemEditName != null) //fill fields if coming from edit button
        {
            itemNameET.setText(Core.itemEditName); //sets the text of the edit text if updating an item
            itemNotesET.setText(Core.itemNotes);
            categoriesSpinner.setSelection(Core.spinnerIndex);
            locationSpinner.setSelection(Core.locationIndex);
            categoriesSpinner.setEnabled(false);
            imageSpinner.setEnabled(false);
            Core.locationIndex = 0;
            Core.itemEditName = null;
        }
    }

    public void onClick(View v)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0); //closes keyboard if opening nav drawer
        }

        FragmentManager fragmentManager = getFragmentManager();
        EditText itemNameET = (EditText)getActivity().findViewById(R.id.itemNameET);
        EditText itemNotesET = (EditText)getActivity().findViewById(R.id.itemNotesET);
        String itemName = itemNameET.getText().toString();
        String notes = itemNotesET.getText().toString();
        setItemImage();
        boolean itemWasUpdated = false;
        for (int i = 0; i < Core.allItems.size(); i++)
        {
                if (Core.allItems.get(i).getItem_name().contentEquals(itemName)) //if item already exists in the database
                {
                    for(Map.Entry m : Core.itemKeyMap.entrySet())
                    {
                        if (m.getKey() == Core.allItems.get(i).getItem_name()) //update it in the database
                        {
                            Core.myRef.child(""+ m.getValue()).child("location").setValue(itemLocation);
                            Core.myRef.child(""+ m.getValue()).child("notes").setValue(notes);
                        }
                    }
                    Core.allItems.get(i).setmImageDrawable(itemImage);
                    Core.allItems.get(i).setLocation(itemLocation);
                    Core.allItems.get(i).setNotes(notes);
                    itemWasUpdated = true;
                    QrCodeScanningFragment.qrCodeItemFound = false;
                    Toast.makeText(getActivity(), "Item Updated", Toast.LENGTH_SHORT).show(); //handling if the name is updated
                }
        }

        if (itemWasUpdated == false)
        {
            if (QrCodeScanningFragment.qrCodeItemFound == true) //does not already exist in database, create new entry from qr code scan
            {
                Item item = new Item(itemImage, itemName, itemLocation);
                Core.addItemDB(item);
                Toast.makeText(getActivity(), "Item added", Toast.LENGTH_SHORT).show();
                QrCodeScanningFragment.qrCodeItemFound = false;
            }
            else
            {
                Item item = new Item(itemImage, categoryName + " " + "(" + count + ")", itemLocation);
                //Item item = new Item(itemImage, firstWordOfCategory + " " + itemName + " " + "(" + count + ")", itemLocation);
                Core.addItemDB(item);
                Toast.makeText(getActivity(), "Item added", Toast.LENGTH_SHORT).show();
            }
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewCategoriesFragment()).addToBackStack("tag").commit();
    }

    public void addImageSelectionOnSpinner()
    {
        imageSpinner = (Spinner) getActivity().findViewById(R.id.icon_spinner);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Computer");
        list.add("Laptop");
        list.add("Monitor");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSpinner.setAdapter(dataAdapter);
    }

    public void addCategoriesOnSpinner()
    {
        categoriesSpinner = (Spinner)getActivity().findViewById(R.id.categories_spinner);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < Core.itemCategoriesList.size(); i++)
        {
            list.add(Core.itemCategoriesList.get(i).getCategory_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(dataAdapter);
        categoriesSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                super.onItemSelected(parent, view, pos, id);
                count = 1;
                String name = parent.getItemAtPosition(pos).toString();
                categoryName = name;
                if(name.contains("Optiplex"))
                {
                    Core.imageToUseForItem = 0;
                }
                else if (name.contains("Latitude"))
                {
                    Core.imageToUseForItem = 1;
                }
                else
                    {
                    Core.imageToUseForItem = 2;
                }

                for (int i = 0; i < Core.allItems.size(); i++)
                {
                    // choose category, write in model name, concactenate number after name
                    if (Core.allItems.get(i).getItem_name().contains(categoryName))
                    {
                        count++; //adds the value of count to the name at the end in ( )
                    }
                }
            }
        });
    }

    public void addLocationsOnSpinner()
    {
        locationSpinner = (Spinner)getActivity().findViewById(R.id.location_spinner);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < Core.itemLocationList.size(); i++)
        {
            list.add(Core.itemLocationList.get(i).getItem_location());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(dataAdapter);
        locationSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                super.onItemSelected(parent, view, pos, id);
                itemLocation = parent.getItemAtPosition(pos).toString();
            }
        });
    }

    public void addListenerOnSpinnerItemSelection()
    {
        imageSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener()); //this class passes the value of the image needed for the list
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