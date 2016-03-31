package com.codepath.android.navigationdrawerexercise.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.codepath.android.navigationdrawerexercise.R;
import com.codepath.android.navigationdrawerexercise.fragments.FamilyGuyFragment;
import com.codepath.android.navigationdrawerexercise.fragments.FuturamaFragment;
import com.codepath.android.navigationdrawerexercise.fragments.SimpsonsFragment;
import com.codepath.android.navigationdrawerexercise.fragments.SouthParkFragment;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public DrawerLayout mDrawer;
    public ActionBarDrawerToggle drawerToggle;
    public NavigationView nvDrawer;
    public int currentPosition;
    private SharedPreferences sharedPreferences;

    HashMap<Integer, Fragment> classLookupHash = new HashMap<>();
    HashMap<Integer, String> titleLookupHash = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        sharedPreferences = getPreferences(Context.MODE_APPEND);

        classLookupHash.put(0, new FuturamaFragment());
        classLookupHash.put(1, new SimpsonsFragment());
        classLookupHash.put(2, new SouthParkFragment());
        classLookupHash.put(3, new FamilyGuyFragment());

        titleLookupHash.put(0, getString(R.string.futurama));
        titleLookupHash.put(1, getString(R.string.simpsons));
        titleLookupHash.put(2, getString(R.string.south_park));
        titleLookupHash.put(3, getString(R.string.family_guy));

        int curPosition = readInt();

        Fragment frg = classLookupHash.get(curPosition);

        nvDrawer.getMenu().getItem(0).setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, frg).commit();
        setTitle(titleLookupHash.get(curPosition));
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_futurama:
                fragmentClass = FuturamaFragment.class;
                currentPosition = 0;

                break;
            case R.id.nav_simpsons:
                fragmentClass = SimpsonsFragment.class;
                currentPosition = 1;
                break;
            case R.id.nav_south_park:
                fragmentClass = SouthParkFragment.class;
                currentPosition = 2;
                break;
            default:
                fragmentClass = FamilyGuyFragment.class;
                currentPosition = 3;
        }
        writeInt(currentPosition);

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private int readInt(){
        return sharedPreferences.getInt("curFrg", 0);
    }

    private void writeInt(int position){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("curFrg", position);
        editor.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
