package com.example.inventorytracker;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Core extends Fragment
{
    public static ItemCustomArrayAdapter itemAdapter;
    public static ItemCustomArrayAdapterNameLocation itemAdapterNameLocation;
    final static ArrayList<String> keyList = new ArrayList<>();
    public static FragmentManager fragmentManager;
    public static ArrayList<Item> itemList = new ArrayList<>();
    public static ArrayList<Item> allItems = new ArrayList<>(); //will need this for QR code item detection; if already exists, present one dialog box, else new entry box
    public static ArrayList<Item> optiplexList = new ArrayList<>();
    public static ArrayList<Item> latitudeList = new ArrayList<>();
    public static ArrayList<Item> monitorList = new ArrayList<>();
    public static int optiplexListItemAmount;
    public static int latitudeListItemAmount;
    public static int monitorListItemAmount;
    private static int numItems = 0;
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference("items");
    public static FirebaseAuth auth;
    public static int imageToUseForItem;

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
                Core.itemList.removeAll(itemList);
                Core.optiplexList.removeAll(optiplexList);
                Core.latitudeList.removeAll(latitudeList);
                Core.monitorList.removeAll(monitorList);
                Core.allItems.removeAll(allItems);
                Core.optiplexListItemAmount = 0;
                Core.latitudeListItemAmount = 0;
                Core.monitorListItemAmount = 0;
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Item itemInDatabase = ds.getValue(Item.class);
                    Core.keyList.add(ds.getKey());
                    Core.addItemLocal(itemInDatabase);
                }
                Core.itemList.add(new Item(R.drawable.ic_desktop, "Optiplex Desktops", Core.optiplexListItemAmount));
                Core.itemList.add(new Item(R.drawable.ic_laptop, "Latitude Laptops", Core.latitudeListItemAmount));
                Core.itemList.add(new Item(R.drawable.ic_monitor, "Monitors", Core.monitorListItemAmount));
                Core.itemAdapter.notifyDataSetChanged();
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

    public static void writeItemToFirebase(Item item)
    {
        //static context
        Core.myRef.push().setValue(item);
    }

    public static void addItemLocal(Item item)
    {
        if (item.getItem_name().contains("Optiplex"))
        {
            Core.optiplexList.add(item);
            Core.optiplexListItemAmount++;
        }
        else if(item.getItem_name().contains("Latitude"))
        {
            Core.latitudeList.add(item);
            Core.latitudeListItemAmount++;
        }
        else if(item.getItem_name().contains("Monitor"))
        {
            Core.monitorList.add(item);
            Core.monitorListItemAmount++;
        }
        else
        {
            System.out.println("nothing");
        }
        Core.allItems.add(item);
        Core.numItems++;
    }

    public static void addItemDB(Item item)
    {
        Core.writeItemToFirebase(item);
    }

    public static void generateTestData(int numTimes)
    {
        for(int i = 1; i < numTimes; i++) {
            addItemDB((new Item(R.drawable.ic_desktop, "Optiplex 390 " + "("+ i +")", "S104", 1)));
            addItemDB(new Item(R.drawable.ic_laptop, "Latitude E5450 " + "("+ i +")", "S103", 1));
            addItemDB(new Item(R.drawable.ic_monitor, "Dell Monitor " + "("+ i +")", "IT Office", 1));
        }
    }
/*
    public static String getBallers()
    {
        return ArrayList.toString(itemList);
    }
    */
}
