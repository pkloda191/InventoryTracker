package com.example.inventorytracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewInventoryFragment extends ListFragment
{
    View myView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.view_inventory, container, false);
        Core.itemAdapter = new ItemCustomArrayAdapter(getActivity(), Core.itemList);
        setListAdapter(Core.itemAdapter);
        Core.itemAdapter.notifyDataSetChanged();
        return myView;
    }
}
