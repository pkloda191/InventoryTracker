package com.example.inventorytracker;

import android.os.Bundle;
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
        /*
        int itemCount = 0;
        for (int i = 0; i < Core.itemCategoriesList.size(); i++)
        {
            for (int j = 0; j < Core.allItems.size(); i++)
            {
                String nameToCompareTo = Core.itemCategoriesList.get(i).toString();
                if (nameToCompareTo.contains(Core.allItems.get(j).toString()))
                {
                    itemCount++;
                }
            }
            Core.itemCategoriesList.get(i).setQuantity(itemCount);
            itemCount = 0;
        } how get count to update????
        */
        lv.setOnItemClickListener(new CustomOnItemClickListener());
    }
}
