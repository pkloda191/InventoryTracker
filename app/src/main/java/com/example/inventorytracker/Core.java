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

    public static void generateNewTestData()
    {
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 380 (1)", "IT Office", "Missing hard drive"));
        //
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 (1)", "IT Office", "Inventoried 4/2/19"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 (2)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 (3)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 (4)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 (5)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 (6)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 (7)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 390 (8)", "Close Storage", ""));
        //
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3010 (1)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3010 (2)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3010 (3)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3010 (4)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3010 (5)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3010 (6)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3010 (7)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3010 (8)", "Close Storage", ""));
        //
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (1)", "Close Storage", "Imaging for Dr. Litman"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (2)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (3)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (4)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (5)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (6)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (7)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (8)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (9)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (10)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (11)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (12)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (13)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (14)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (15)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (16)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (17)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (18)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (19)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3020 (20)", "Close Storage", ""));
        //
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (1)", "Close Storage", "No hard drive"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (2)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (3)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (4)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (5)", "Desk", "Being worked on"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (6)", "Desk", "Being worked on"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (7)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (8)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (9)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (10)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (11)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (12)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (13)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (14)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (15)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3040 (16)", "Close Storage", ""));
        //
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3050 (1)", "IT Office", "No hard drive"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3050 (2)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3050 (3)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3050 (4)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3050 (5)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3050 (6)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3050 (7)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3050 (8)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3050 (9)", "Close Storage", ""));
        //
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (1)", "IT Office", "Business building"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (2)", "IT Office", "Business building"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (3)", "IT Office", "Business building"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (4)", "IT Office", "Business building"));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (5)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (6)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (7)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (8)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (9)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (10)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (11)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (12)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (13)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_desktop, "Optiplex 3060 (14)", "IT Office", ""));
        //
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (1)", "IT Office", "Waiting to be imaged"));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (2)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (3)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (4)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (5)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (6)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (7)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (8)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (9)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (10)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (11)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (12)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (13)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (14)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (15)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (16)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (17)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (18)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (19)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 (20)", "IT Office", ""));
        //
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5470 (1)", "IT Office", "Waiting to be imaged"));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5470 (2)", "IT Office", "Bad battery"));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5470 (3)", "IT Office", "Repaired"));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5470 (4)", "IT Office", "Shell swapped"));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5470 (5)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5470 (6)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5470 (7)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5470 (8)", "IT Office", ""));
        //
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (1)", "IT Office", "Waiting to be imaged"));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (2)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (3)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (4)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (5)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (6)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (7)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (8)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (9)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (10)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5480 (11)", "IT Office", ""));
        //
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (1)", "IT Office", "Waiting to be imaged"));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (2)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (3)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (4)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (5)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (6)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (7)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (8)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (9)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (10)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (11)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (12)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (13)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (14)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6420 (15)", "IT Office", ""));
        //
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (1)", "IT Office", "Waiting to be imaged"));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (2)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (3)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (4)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (5)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (6)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (7)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (8)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (9)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (10)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (11)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (12)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (13)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (14)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (15)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6430 (16)", "IT Office", ""));
        //
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (1)", "IT Office", "Waiting to be imaged"));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (2)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (3)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (4)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (5)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (6)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (7)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (8)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (9)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (10)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (11)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (12)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (13)", "IT Office", ""));
        addItemDB(new Item(R.drawable.ic_laptop, "Latitude E6440 (14)", "IT Office", ""));
        //
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (1)", "S105", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (2)", "S105", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (3)", "S105", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (4)", "S105", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (5)", "S105", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (6)", "S105", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (7)", "S114D", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (8)", "S114D", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (9)", "S114D", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (10)", "S114D", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (11)", "S114D", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "2015 iMac (12)", "S114D", ""));
        //
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913b (1)", "Close Storage", "Flickering display"));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913b (2)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913b (3)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913b (4)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913b (5)", "Close Storage", ""));
        //
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913t (1)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913t (2)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913t (3)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913t (4)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913t (5)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913t (6)", "Close Storage", ""));
        addItemDB(new Item(R.drawable.ic_monitor, "Monitor P1913t (7)", "Close Storage", ""));
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
        addCategoryDB(new ItemCategories(R.drawable.ic_monitor, "2015 iMac", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_monitor, "Monitor P1913b", Core.numItems));
        addCategoryDB(new ItemCategories(R.drawable.ic_monitor, "Monitor P1913t", Core.numItems));
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
}
