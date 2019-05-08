package com.example.inventorytracker;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class ItemCategories implements Serializable
{
    public int mImageDrawable;
    public String category_name;
    public int quantity;

    public ItemCategories(int mImageDrawable, String category_name, int quantity)
    {
        this.mImageDrawable = mImageDrawable;
        this.category_name = category_name;
        this.quantity = quantity;
    }

    public ItemCategories()
    {
        this.mImageDrawable = R.drawable.ic_laptop;
        this.category_name = "Test";
        this.quantity = 1;
    }

    @Override
    public String toString() {
        return this.getCategory_name();
    }
    @Exclude
    public int getmImageDrawable() {
        return mImageDrawable;
    }
    @Exclude
    public void setmImageDrawable(int mImageDrawable) {
        this.mImageDrawable = mImageDrawable;
    }
    @Exclude
    public String getCategory_name() {
        return category_name;
    }
    @Exclude
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
    @Exclude
    public int getQuantity() {
        return quantity;
    }
    @Exclude
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
