package com.example.inventorytracker;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class CustomOnItemClickListener implements AdapterView.OnItemClickListener
{
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Core.spinnerIndex = position;
        String name = parent.getItemAtPosition(position).toString();
        //name = name.substring(0,3);
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
        //Toast.makeText(parent.getContext(), position + "test", Toast.LENGTH_SHORT).show();
    }
}
