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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddLocationFragment extends Fragment implements View.OnClickListener
{
    View myView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.add_location, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button addLocationButton = (Button) getActivity().findViewById(R.id.add_location_button);
        addLocationButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0); //closes keyboard if opening nav drawer
        }

        FragmentManager fragmentManager = getFragmentManager();
        EditText itemLocationET = (EditText)getActivity().findViewById(R.id.item_location_ET);
        String itemCategory = itemLocationET.getText().toString();
        ItemLocations location = new ItemLocations(itemCategory);
        Core.addLocationDB(location);
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewCategoriesFragment()).addToBackStack("tag").commit();
        Toast.makeText(getActivity(), "Location added", Toast.LENGTH_SHORT).show();
    }
}
