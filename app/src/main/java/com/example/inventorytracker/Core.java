package com.example.inventorytracker;

import android.widget.ListView;
import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Core
{
    public static ItemCustomArrayAdapter itemAdapter;
    public static ArrayList<Item> itemList = new ArrayList<>();
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
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Item itemInDatabase = ds.getValue(Item.class);
                    Core.addItemLocal(itemInDatabase);
                }
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
            optiplexListItemAmount++;
        }
        else if(item.getItem_name().contains("Latitude"))
        {
            Core.latitudeList.add(item);
            latitudeListItemAmount++;
        }
        else
        {
            Core.monitorList.add(item);
            monitorListItemAmount++;
        }
        Core.itemList.add(item);
        //Core.theBallerStrings[Core.numBallers] = ba.toString();
        Core.numItems++;
    }

    public static void addItemDB(Item item)
    {
        Core.writeItemToFirebase(item);
    }
/*
    public static String getBallers()
    {
        return ArrayList.toString(itemList);
    }
    */
}
