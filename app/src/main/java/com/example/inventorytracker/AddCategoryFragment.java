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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddCategoryFragment extends Fragment implements View.OnClickListener
{
    private Spinner imageSpinner;
    int itemImage;
    View myView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.add_category, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button addCategoryButton = (Button) getActivity().findViewById(R.id.add_category_button);
        addCategoryButton.setOnClickListener(this);
        addImageSelectionOnSpinner();
    }

    public void onClick(View v)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0); //closes keyboard if opening nav drawer
        }

        FragmentManager fragmentManager = getFragmentManager();
        EditText itemCategoryET = (EditText)getActivity().findViewById(R.id.item_category_ET);
        String itemCategory = itemCategoryET.getText().toString();
        setItemImage();
        ItemCategories category = new ItemCategories(itemImage, itemCategory, 0);
        Core.addCategoryDB(category);
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewCategoriesFragment()).addToBackStack("tag").commit();
        Toast.makeText(getActivity(), "Category added", Toast.LENGTH_SHORT).show();
    }

    public void addImageSelectionOnSpinner()
    {
        imageSpinner = (Spinner) getActivity().findViewById(R.id.icon_spinner_addpage);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Computer");
        list.add("Laptop");
        list.add("Monitor");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSpinner.setAdapter(dataAdapter);
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
