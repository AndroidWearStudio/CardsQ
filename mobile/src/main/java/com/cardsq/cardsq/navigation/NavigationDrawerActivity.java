package com.cardsq.cardsq.navigation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cardsq.cardsq.Accomplishment.AccomplishmentFragment;
import com.cardsq.cardsq.R;
import com.cardsq.cardsq.cardlist.CardListFragment;
import com.cardsq.cardsq.database.Factory;
import com.cardsq.cardsq.decklist.DeckListFragment;
import com.cardsq.cardsq.entity.Deck;
import com.cardsq.cardsq.setting.MainSettingActivity;

import java.util.ArrayList;

public class NavigationDrawerActivity extends Activity
        implements DeckListFragment.OnFragmentInteractionListener,
        CardListFragment.OnFragmentInteractionListener,AccomplishmentFragment.OnFragmentInteractionListener{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private FragmentManager fragmentManager;


    private ArrayList<NavigationDrawerListItem> navDrawerItems;
    private NavigationDrawerListAdapter adapter;

    public ListView getmDrawerList() {
        return mDrawerList;
    }

    public String[] getNavMenuTitles() {
        return navMenuTitles;
    }

    public ArrayList<NavigationDrawerListItem> getNavDrawerItems() {
        return navDrawerItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_list);
        fragmentManager = getFragmentManager();
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.navigation_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.navigation_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_list);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavigationDrawerListItem>();

        // adding navigation drawer items to array
        // My Heap, Will add a counter here
        navDrawerItems.add(new NavigationDrawerListItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1), true, "16"));
        // My Accomplishments, Will add a counter here
        navDrawerItems.add(new NavigationDrawerListItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, "10"));
        // My Creations, Will add a counter here
        navDrawerItems.add(new NavigationDrawerListItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, "2"));
        // Collections, Will add a counter here
        navDrawerItems.add(new NavigationDrawerListItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "19"));
        // Recommendations
        navDrawerItems.add(new NavigationDrawerListItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot
        navDrawerItems.add(new NavigationDrawerListItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavigationDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setOnItemClickListener(new NavigationDrawerClickListener());

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                openMainSetting();
            case R.id.action_about:
                //TODO: about page
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Implement interface of @{link CardListFragment}
     * */
    public void onFragmentInteraction(String id){

    }

    /**
     * Implement interface of @{link DeckListFragment}
     * Run when user click the list in @{link DeckListFragment}
     * */
    @Override
    public void onFragmentInteraction(Deck deck) {
        displayDeckView(deck);
    }


    private class NavigationDrawerClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayDeckView(Deck deck){
        CardListFragment fragment = CardListFragment.newInstance(deck.getCardList());
        if(fragment != null){
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment);
            //add previous fragment to backstack so that you can go back when press back button
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            setTitle(deck.getTitle());
        }else{
            Log.e(this.getLocalClassName(), "Error in creating fragment");
        }
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = CardListFragment.newInstance(Factory.getCardList());
                break;
            case 1:
                fragment = AccomplishmentFragment.newInstance();
                break;
            case 2:
                fragment = DeckListFragment.newInstance(Factory.getDeckList());
                break;
            case 3:
                fragment = DeckListFragment.newInstance(Factory.getDeckList());
                break;
            case 4:
                // fragment = new PagesFragment();
                break;
            case 5:
                // fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e(this.getLocalClassName(), "Error in creating fragment");
        }
    }


    public void openMainSetting(){
        Intent intent = new Intent(NavigationDrawerActivity.this, MainSettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
