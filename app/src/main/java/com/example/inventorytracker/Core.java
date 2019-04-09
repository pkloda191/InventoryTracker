package com.example.inventorytracker;

import android.widget.ListView;
import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Core
{
    public static ItemCustomArrayAdapter itemAdapter;
    public static ArrayList<Item> itemList = new ArrayList<>();
    private static int numItems = 0;
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference("items");

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
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Item item = ds.getValue(Item.class);
                    Core.addItemLocal(item);
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
