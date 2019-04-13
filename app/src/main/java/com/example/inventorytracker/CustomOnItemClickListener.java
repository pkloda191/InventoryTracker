package com.example.inventorytracker;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class CustomOnItemClickListener implements AdapterView.OnItemClickListener
{
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(position == 0)
        {
            Core.fragmentManager.beginTransaction().replace(R.id.content_frame, new OptiplexInventoryFragment()).addToBackStack("tag").commit();
            //Core.myRef.getRoot().child("items").child(Core.keyList.get(position)).removeValue();
        }
        else if(position == 1)
        {
            Core.fragmentManager.beginTransaction().replace(R.id.content_frame, new LatitudeInventoryFragment()).addToBackStack("tag").commit();
        }
        else
        {
            Core.fragmentManager.beginTransaction().replace(R.id.content_frame, new MonitorInventoryFragment()).addToBackStack("tag").commit();
        }
        //Toast.makeText(parent.getContext(), position + "test", Toast.LENGTH_SHORT).show();
    }
}
