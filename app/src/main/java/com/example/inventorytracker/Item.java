package com.example.inventorytracker;
import com.google.firebase.database.Exclude;
import java.io.Serializable;

public class Item implements Serializable
{
    public int mImageDrawable;
    public String item_name;
    public String location;
    public int quantity;

    public Item(int mImageDrawable, String item_name, String location, int quantity)
    {
        this.mImageDrawable = mImageDrawable;
        this.item_name = item_name;
        this.location = location;
        this.quantity = quantity;
    }

    public Item(String item_name, String location, int quantity)
    {
        this.item_name = item_name;
        this.location = location;
        this.quantity = quantity;
    }
    public Item()
    {
        this.mImageDrawable = R.drawable.ic_laptop;
        this.item_name = "Test";
        this.location = "Test";
        this.quantity = 1;
    }

    public int getmImageDrawable() {
        return mImageDrawable;
    }

    @Exclude
    public String getItem_name() {
        return item_name;
    }
    @Exclude
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
    @Exclude
    public String getLocation() {
        return "Located in: "+ location;
    }
    @Exclude
    public void setLocation(String location) {
        this.location = location;
    }
    @Exclude
    public String getQuantity() {
        return "Amount: " + Integer.toString(quantity);
    }
    @Exclude
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
