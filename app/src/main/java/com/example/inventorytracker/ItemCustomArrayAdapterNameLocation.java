package com.example.inventorytracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemCustomArrayAdapterNameLocation extends ArrayAdapter<Item>
{
    private Context mContext;
    private List<Item> itemList = new ArrayList<>();

    public ItemCustomArrayAdapterNameLocation(@NonNull Context context, ArrayList<Item> list)
    {
        super(context, 0 , list);
        this.mContext = context;
        this.itemList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
        {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_array_adapter_name_location,parent,false);
        }

        Item currentItem = itemList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.imageView_item);
        image.setImageResource(currentItem.getmImageDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        name.setText(currentItem.getItem_name());

        TextView location = (TextView) listItem.findViewById(R.id.textView_location);
        location.setText(("Located in: " + currentItem.getLocation()));

        return listItem;
    }

    public Item getItem(int position){
        return itemList.get(position);
    }
}
