package com.example.inventorytracker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Core extends Fragment
{
    public static ItemCustomArrayAdapter itemAdapter;
    public static CategoryCustomArrayAdapter categoriesAdapter;
    public static ItemCustomArrayAdapterNameLocation itemAdapterNameLocation;
    final static ArrayList<String> keyList = new ArrayList<>();
    public static FragmentManager fragmentManager;
    public static ArrayList<Item> itemList = new ArrayList<>(); //view inventory fragment after categories
    public static ArrayList<Item> allItems = new ArrayList<>(); //will need this for QR code item detection; if already exists, present one dialog box, else new entry box
    public static ArrayList<ItemCategories> itemCategoriesList = new ArrayList<>();
    public static ArrayList<ItemLocations> itemLocationList = new ArrayList<>();
    public static int optiplexListItemAmount;
    public static int latitudeListItemAmount;
    public static int monitorListItemAmount;
    public static int numItems = 0;
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference("items");
    public static DatabaseReference myRefCategories = database.getReference("categories");
    public static DatabaseReference myRefLocations = database.getReference("locations");
    public static FirebaseAuth auth;
    public static int imageToUseForItem;
    public static String itemEditName = null;

    public static void listenForDatabaseChanges()
    {
        //async listener
        ValueEventListener prListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Get Post object and use the values to update the UI
                Core.numItems = 0;
                Core.allItems.removeAll(allItems);
                Core.keyList.removeAll(keyList);
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Item itemInDatabase = ds.getValue(Item.class);
                    Core.keyList.add(ds.getKey());
                    Core.addItemLocal(itemInDatabase);
                }
                Core.itemAdapterNameLocation.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                // Getting Post failed, log a message
                System.out.println("loadPost:onCancelled " + databaseError.toException());
            }

        };
        Core.myRef.addValueEventListener(prListener);
    }

    public static void listenForCategoryChanges()
    {
        //async listener
        ValueEventListener prListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Core.itemCategoriesList.removeAll(itemCategoriesList);
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ItemCategories itemCategoryInDatabase = ds.getValue(ItemCategories.class);
                    //Core.keyList.add(ds.getKey());
                    Core.addItemCategoryLocal(itemCategoryInDatabase);
                }
                Core.categoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                // Getting Post failed, log a message
                System.out.println("loadPost:onCancelled " + databaseError.toException());
            }
        };
        Core.myRefCategories.addValueEventListener(prListener);
    }

    public static void listenForLocationChanges()
    {
        //async listener
        ValueEventListener prListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Core.itemLocationList.removeAll(itemLocationList);
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ItemLocations itemLocationInDatabase = ds.getValue(ItemLocations.class);
                    //Core.keyList.add(ds.getKey());
                    Core.addLocationLocal(itemLocationInDatabase);
                }
                //Core.categoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                // Getting Post failed, log a message
                System.out.println("loadPost:onCancelled " + databaseError.toException());
            }
        };
        Core.myRefLocations.addValueEventListener(prListener);
    }


    public static void writeItemToFirebase(Item item)
    {
        //static context
        Core.myRef.push().setValue(item);
    }

    public static void writeCategoryToFirebase(ItemCategories category)
    {
        Core.myRefCategories.push().setValue(category);
    }

    public static void writeLocationToFirebase(ItemLocations location)
    {
        Core.myRefLocations.push().setValue(location);
    }

    public static void addItemLocal(Item item)
    {
        /*
        if (item.getItem_name().contains("Optiplex"))
        {
            //Core.optiplexList.add(item);
            Core.optiplexListItemAmount++;
        }
        else if(item.getItem_name().contains("Latitude"))
        {
            //Core.latitudeList.add(item);
            Core.latitudeListItemAmount++;
        }
        else if(item.getItem_name().contains("Monitor"))
        {
            //Core.monitorList.add(item);
            Core.monitorListItemAmount++;
        }
        else
        {
            System.out.println("nothing");
        }
        */
        Core.allItems.add(item);
        Core.numItems++;
    }

    public static void addItemCategoryLocal(ItemCategories category)
    {
        Core.itemCategoriesList.add(category);
    }

    public static void addLocationLocal(ItemLocations location)
    {
        Core.itemLocationList.add(location);
    }

    public static void addItemDB(Item item)
    {
        Core.writeItemToFirebase(item); //this is called in the item creation page or when hard coding items
    }

    public static void addCategoryDB(ItemCategories category)
    {
        Core.writeCategoryToFirebase(category); //this is called in the admin page when creating a category or when hard coding categories
    }

    public static void addLocationDB(ItemLocations location)
    {
        Core.writeLocationToFirebase(location);
    }

    public static void generateTestData(int numTimes)
    {
        for(int i = 1; i < numTimes; i++) {
            addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 " + "("+ i +")", "S104", 1));
            addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 " + "("+ i +")", "S103", 1));
            addItemDB(new Item(R.drawable.ic_monitor, "Dell Monitor " + "("+ i +")", "IT Office", 1));
        }
    }

    public static void generateCategories()
    {
        addCategoryDB(new ItemCategories(R.drawable.ic_desktop, "Optiplex Desktops", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_laptop, "Latitude Laptops", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_monitor, "Monitors", Core.numItems));

        //Core.itemList.add(new Item(R.drawable.ic_desktop, "Optiplex Desktops", Core.optiplexListItemAmount));
        //Core.itemList.add(new Item(R.drawable.ic_laptop, "Latitude Laptops", Core.latitudeListItemAmount));
        //Core.itemList.add(new Item(R.drawable.ic_monitor, "Monitors", Core.monitorListItemAmount));
    }

    public static void generateLocations()
    {
        addLocationDB(new ItemLocations("Close Storage"));
        addLocationDB(new ItemLocations("Far Storage"));
        addLocationDB(new ItemLocations("IT Office"));
    }
/*
    public static String getBallers()
    {
        return ArrayList.toString(itemList);
    }
    */
}
