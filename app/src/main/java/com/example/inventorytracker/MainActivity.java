package com.example.inventorytracker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(this);

        //sets the first page to be the login page / inventory - way of getting around array adapter error
        FragmentTransaction preLoadInventoryPage = getSupportFragmentManager().beginTransaction();
        preLoadInventoryPage.replace(R.id.content_frame, new ViewInventoryFragment()).commit();
        FragmentTransaction LoadCategoryPage = getSupportFragmentManager().beginTransaction();
        LoadCategoryPage.replace(R.id.content_frame, new ViewCategoriesFragment()).commit();
        FragmentTransaction loginPage = getSupportFragmentManager().beginTransaction();
        loginPage.replace(R.id.content_frame, new LoginPage()).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialogBox();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null && getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); //closes keyboard if opening nav drawer
                }
            }
        };
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Core.generateTestData(5);
        //Core.generateNewTestData();
        //Core.generateCategories();
        //Core.generateLocations();
        Core.listenForDatabaseChanges();
        Core.listenForCategoryChanges();
        Core.listenForLocationChanges();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_all) {
            Core.itemList.removeAll(Core.itemList);
            for (int i = 0; i < Core.allItems.size(); i++)
            {
                Core.itemList.add(Core.allItems.get(i));
            }
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewInventoryFragment()).addToBackStack("tag").commit();
            return true;
        }
        else if(id == R.id.action_search)
        {
            for (int i = 0; i < Core.allItems.size(); i++)
            {
                System.out.println("*** " + Core.allItems.get(i).toString());
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText userSearchET = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            userSearchET.setLayoutParams(lp);
            userSearchET.setHint("Item name");
            userSearchET.setPaddingRelative(65,30,0,0);
            userSearchET.setBackground(null);

            alert.setTitle("Search for an item");
            alert.setView(userSearchET);
            alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    Core.itemList.removeAll(Core.itemList);
                    String searchText = userSearchET.getText().toString().toLowerCase();
                    for (int i = 0; i < Core.allItems.size(); i++)
                    {
                        String nameToCompareTo = Core.allItems.get(i).toString().toLowerCase();
                        if (nameToCompareTo.contains(searchText))
                        {
                            Core.itemList.add(Core.allItems.get(i));
                        }
                    }
                    Core.fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewInventoryFragment()).addToBackStack("tag").commit();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //do nothing
                }
            });

            final AlertDialog dialog = alert.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0)
                {
                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00574B"));
                    dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00574B"));
                }
            });
            dialog.show();
        }
        else if (id == R.id.action_sort_by) {
            //Collections.sort(Core.itemList);
            //Collections.sort(Core.itemList, Collections.reverseOrder());
            //Core.itemAdapterNameLocation.notifyDataSetChanged();
            Core.sortIndex = false;
            final AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Sort by (1/2)");
            String[] types = {"Ascending", "Descending"};
            b.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //DIALOGCEPTION
                    dialog.dismiss();
                    switch(which){
                        case 0:
                            //sort ascending
                            b.setTitle("Sort by (2/2)");
                            String[] types = {"Name", "Location"};
                            b.setItems(types, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                    switch(which){
                                        case 0:
                                            //sort by name
                                            Collections.sort(Core.itemList);
                                            Core.itemAdapterNameLocation.notifyDataSetChanged();
                                            break;
                                        case 1:
                                            //sort by location
                                            Core.sortIndex = true;
                                            Collections.sort(Core.itemList);
                                            Core.itemAdapterNameLocation.notifyDataSetChanged();
                                            break;
                                    }
                                }
                            });
                            b.show();
                            break;
                        case 1:
                            //sort descending
                            b.setTitle("Sort by (2/2)");
                            String[] types2 = {"Name", "Location"};
                            b.setItems(types2, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                    switch(which){
                                        case 0:
                                            //sort by name
                                            Collections.sort(Core.itemList, Collections.reverseOrder());
                                            Core.itemAdapterNameLocation.notifyDataSetChanged();
                                            break;
                                        case 1:
                                            //sort by location
                                            Core.sortIndex = true;
                                            Collections.sort(Core.itemList, Collections.reverseOrder());
                                            Core.itemAdapterNameLocation.notifyDataSetChanged();
                                            break;
                                    }
                                }
                            });
                            b.show();
                            break;
                    }
                }
            });
            b.show();
        }

        else if (id == R.id.action_sign_out)
        {
            Core.auth.getInstance().signOut();
            Core.fragmentManager.beginTransaction().replace(R.id.content_frame, new LoginPage()).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    FragmentManager fragmentManager = getSupportFragmentManager();
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inventory) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewCategoriesFragment()).addToBackStack("tag").commit();
        } else if (id == R.id.nav_additem) {
            makeDialogBox(); //does the same as the fab button
        } else if (id == R.id.nav_print) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new PrintQRCodeFragment()).addToBackStack("tag").commit();
        } else if (id == R.id.nav_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SettingsFragment()).addToBackStack("tag").commit();
        } else if (id == R.id.nav_help) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HelpPageFragment()).addToBackStack("tag").commit();
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutPageFragment()).addToBackStack("tag").commit();
        } else if (id == R.id.nav_add_category) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AddCategoryFragment()).addToBackStack("tag").commit();
        } else if (id == R.id.nav_add_location) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AddLocationFragment()).addToBackStack("tag").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void makeDialogBox()
    {
        final CharSequence[] items = {
                "Scan QR Code", "Manual Item Entry"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Input an item");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item)
            {
                // Do something with the selection
                if (item == 0)
                {
                    //open qr code scanning fragment
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new QrCodeScanningFragment()).addToBackStack("tag").commit();
                }
                else
                {
                    //open manual entry fragment
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new ItemManualEntryFragment()).addToBackStack("tag").commit();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
