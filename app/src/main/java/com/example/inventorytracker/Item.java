package com.example.inventorytracker;
import com.google.firebase.database.Exclude;
import java.io.Serializable;

public class Item implements Serializable
{
    public int mImageDrawable;
    public String item_name;
    public String location;
    public String notes;

    public Item(int mImageDrawable, String item_name, String location, String notes)
    {
        this.mImageDrawable = mImageDrawable;
        this.item_name = item_name;
        this.location = location;
        this.notes = notes;
    }

    public Item(int mImageDrawable, String item_name, String location)
    {
        this.mImageDrawable = mImageDrawable;
        this.item_name = item_name;
        this.location = location;
    }

    public Item()
    {
        this.mImageDrawable = R.drawable.ic_laptop;
        this.item_name = "Test";
        this.location = "Test";
        this.notes = "";
    }

    @Override
    public String toString() {
        return this.getItem_name();
    }
    @Exclude
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
        return location;
    }
    @Exclude
    public void setLocation(String location) {
        this.location = location;
    }
    @Exclude
    public void setmImageDrawable(int mImageDrawable) {
        this.mImageDrawable = mImageDrawable;
    }
    @Exclude
    public String getNotes() {
        return notes;
    }
    @Exclude
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
