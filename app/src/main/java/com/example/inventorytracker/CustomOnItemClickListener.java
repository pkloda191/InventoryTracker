package com.example.inventorytracker;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class CustomOnItemClickListener implements AdapterView.OnItemClickListener
{
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String name = parent.getItemAtPosition(position).toString();
        name = name.substring(0,3);
        //Toast.makeText(parent.getContext(), "" + Core.itemCategoriesList.size(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < Core.allItems.size(); i++)
        {
            String nameToCompareTo = Core.allItems.get(i).toString();
            if (nameToCompareTo.contains(name))
            {
                Core.itemList.add(Core.allItems.get(i));
            }
        }
        Core.fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewInventoryFragment()).addToBackStack("tag").commit();
        /*
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
        */
        //Toast.makeText(parent.getContext(), position + "test", Toast.LENGTH_SHORT).show();
    }
}
