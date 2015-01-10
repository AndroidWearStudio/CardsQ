package com.cardsq.cardsq.setting;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cardsq.cardsq.R;
import com.cardsq.cardsq.notification.SendDataToWearActivity;

/**
 * Activity
 *
 * Created by lzhu on 1/10/15.
 */
public class MainSettingActivity extends Activity implements SetReviewTimeFragment.OnFragmentInteractionListener,
        MainSettingFragment.OnFragmentInteractionListener {

    String [] settingTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        Fragment fragment = new MainSettingFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.setting_menu, fragment).commit();
        setTitle("Settings");
        settingTitles = getResources().getStringArray(R.array.main_setting_list);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onFragmentInteraction(int position) {
        //display a setting fragment according to the parameter passed from MainSettingFragment
        displayView(position);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }



    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new SetReviewTimeFragment();
                break;
            case 1:
                //fragment = new SendToWearFragment();
                Intent intent = new Intent(this, SendDataToWearActivity.class);
                startActivity(intent);
                break;
            case 2:
                //fragment = new PhotosFragment();
                break;
            case 3:
                //fragment = new CommunityFragment();
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
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.setting_menu, fragment).commit();
            setTitle(settingTitles[position]);
        } else {
            // error in creating fragment
            Log.e("MainSettingActivity", "Error in creating fragment");
        }
    }



}
