package com.example.inventorytracker;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ViewCategoriesFragment extends ListFragment {
    ListView lv;
    View myView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.view_inventory, container, false);
        Core.categoriesAdapter = new CategoryCustomArrayAdapter(getActivity(), Core.itemCategoriesList);
        setListAdapter(Core.categoriesAdapter);
        Core.categoriesAdapter.notifyDataSetChanged();
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Core.itemList.removeAll(Core.itemList);
        Core.fragmentManager = getFragmentManager();
        lv = getListView();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                updateQuantities();
                Core.categoriesAdapter.notifyDataSetChanged();
            }
        }, 1000); //update the count 1 sec after activity is created otherwise the arrays are null
        lv.setOnItemClickListener(new CustomOnItemClickListener());
    }

    public void updateQuantities()
    {
        int itemCount = 0;
        for (int i = 0; i < Core.itemCategoriesList.size(); i++)
        {
            String nameToCompareTo = Core.itemCategoriesList.get(i).toString();
            //nameToCompareTo = nameToCompareTo.substring(0,3);

            for (int j = 0; j < Core.allItems.size(); j++)
            {
                if (Core.allItems.get(j).toString().contains(nameToCompareTo))
                {
                    itemCount++;
                }
            }
            Core.itemCategoriesList.get(i).setQuantity(itemCount);
            itemCount = 0;
        }
    }
}
