package com.example.inventorytracker;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class ItemLocations implements Serializable
{
    public String item_location;

    public ItemLocations(String item_location)
    {
        this.item_location = item_location;
    }

    public ItemLocations()
    {
        this.item_location = "Test";
    }
    @Exclude
    public String getItem_location() {
        return item_location;
    }
    @Exclude
    public void setItem_location(String item_location) {
        this.item_location = item_location;
    }
}
