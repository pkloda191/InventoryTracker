package com.example.inventorytracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class OptiplexInventoryFragment extends ListFragment
{
    ListView lv;
    View myView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.view_inventory, container, false);
        Core.itemAdapterNameLocation = new ItemCustomArrayAdapterNameLocation(getActivity(), Core.optiplexList);
        setListAdapter(Core.itemAdapterNameLocation);
        Core.itemAdapterNameLocation.notifyDataSetChanged();
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        lv = getListView();
        lv.setOnItemClickListener(new CustomOnItemClickListenerMoreDetails());
    }
}
