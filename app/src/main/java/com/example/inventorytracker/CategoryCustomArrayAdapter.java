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

public class CategoryCustomArrayAdapter extends ArrayAdapter<ItemCategories>
{
    private Context mContext;
    private List<ItemCategories> categoryList = new ArrayList<>();

    public CategoryCustomArrayAdapter(@NonNull Context context, ArrayList<ItemCategories> list)
    {
        super(context,0 , list);
        this.mContext = context;
        this.categoryList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
        {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_array_adapter,parent,false);
        }

        ItemCategories currentCategory = categoryList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.imageView_item);
        image.setImageResource(currentCategory.getmImageDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        name.setText(currentCategory.getCategory_name());

        TextView quantity = (TextView) listItem.findViewById(R.id.textView_quantity);
        quantity.setText("Amount: " + currentCategory.getQuantity());

        return listItem;
    }

    public ItemCategories getItem(int position){
        return categoryList.get(position);
    }
}
