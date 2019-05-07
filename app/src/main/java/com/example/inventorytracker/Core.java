package com.example.inventorytracker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Core extends Fragment
{
    public static CategoryCustomArrayAdapter categoriesAdapter;
    public static ItemCustomArrayAdapterNameLocation itemAdapterNameLocation;
    final static ArrayList<String> keyList = new ArrayList<>();
    public static FragmentManager fragmentManager;
    public static ArrayList<Item> itemList = new ArrayList<>(); //view inventory fragment after categories, filters to show appropriate items
    public static ArrayList<Item> allItems = new ArrayList<>(); //will need this for QR code item detection; if already exists, present one dialog box, else new entry box
    public static Map<String, String> itemKeyMap = new HashMap<String, String>();
    public static ArrayList<ItemCategories> itemCategoriesList = new ArrayList<>();
    public static ArrayList<ItemLocations> itemLocationList = new ArrayList<>();
    public static int numItems = 0;
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference("items");
    public static DatabaseReference myRefCategories = database.getReference("categories");
    public static DatabaseReference myRefLocations = database.getReference("locations");
    public static FirebaseAuth auth;
    public static int imageToUseForItem;
    public static String itemEditName = null;
    public static String itemNotes = "";
    public static int spinnerIndex = 0;
    public static int locationIndex = 0;
    public static boolean sortIndex = false;
    public static Object itemClicked;
    public static ItemCategories itemClicked2;

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
                Core.itemKeyMap.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Item itemInDatabase = ds.getValue(Item.class);
                    Core.keyList.add(ds.getKey());
                    Core.addItemLocal(itemInDatabase);
                    itemKeyMap.put(itemInDatabase.getItem_name(), ds.getKey());
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
            addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 " + "("+ i +")", "Close Storage", "test notes"));
            addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 " + "("+ i +")", "Far Storage", "test notes"));
            addItemDB(new Item(R.drawable.ic_monitor, "Dell Monitor " + "("+ i +")", "IT Office", "test notes"));
        }
    }

    public static void generateCategories()
    {
        //addCategoryDB(new ItemCategories(R.drawable.ic_desktop, "Optiplex Desktops", Core.numItems));
        //addCategoryDB(new ItemCategories(R.drawable.ic_laptop, "Latitude Laptops", Core.numItems));
        //addCategoryDB(new ItemCategories(R.drawable.ic_monitor, "Monitors", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_desktop, "Optiplex 380", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_desktop, "Optiplex 390", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_desktop, "Optiplex 3010", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_desktop, "Optiplex 3020", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_desktop, "Optiplex 3040", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_desktop, "Optiplex 3050", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_desktop, "Optiplex 3060", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_laptop, "Latitude E5450", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_laptop, "Latitude E5470", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_laptop, "Latitude E5480", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_laptop, "Latitude E6420", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_laptop, "Latitude E6430", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_laptop, "Latitude E6440", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_monitor, "iMac", Core.numItems));


    }

    public static void generateLocations()
    {
        addLocationDB(new ItemLocations("Close Storage"));
        addLocationDB(new ItemLocations("Far Storage"));
        addLocationDB(new ItemLocations("IT Office"));
        addLocationDB(new ItemLocations("Desk"));
        addLocationDB(new ItemLocations("S105"));
        addLocationDB(new ItemLocations("S107"));
        addLocationDB(new ItemLocations("S114D"));
    }
/*
    public static String getBallers()
    {
        return ArrayList.toString(itemList);
    }
    */
}
