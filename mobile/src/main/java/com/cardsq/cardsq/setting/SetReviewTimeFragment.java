package com.cardsq.cardsq.setting;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cardsq.cardsq.R;
import com.cardsq.cardsq.notification.ScheduleClient;
import com.cardsq.cardsq.notification.SendDataToWearActivity;

import java.util.Calendar;

/**
 * Created by lzhu on 1/10/15.
 */
public class SetReviewTimeFragment extends Fragment implements View.OnClickListener {
    // This is a handle so that we can call methods on our service
    private ScheduleClient scheduleClient;
    // This is the date picker used to select the date for our notification
    private TimePicker picker;

    private Button setReviewTimeBtn;

    private OnFragmentInteractionListener mListener;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(getActivity());
        scheduleClient.doBindService();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.set_review_time, container, false);
        // Get a reference to our date picker
        picker = (TimePicker)view.findViewById(R.id.scheduleTimePicker);
        setReviewTimeBtn = (Button)view.findViewById(R.id.setReviewTimeButton);
        setReviewTimeBtn.setOnClickListener(this);
        return view;
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
    public void onClick(View view){
        // Create a new calendar set to the date chosen
        // we set the time to midnight (i.e. the first minute of that day)
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int hour = picker.getCurrentHour();
        int min = picker.getCurrentMinute();
        int sec = 0;
        // c.set(year, month, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, sec);
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification(c);
        // Notify the user what they just did
        Toast.makeText(getActivity(), "Set Daily Review Time: " + hour + ":" + min + ":" + sec, Toast.LENGTH_SHORT).show();

//        Intent intent = new Intent(MainSettingActivity.this, SendDataToWearActivity.class);
//        startActivity(intent);
    }




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SetReviewTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetReviewTimeFragment newInstance() {
        SetReviewTimeFragment fragment = new SetReviewTimeFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    public SetReviewTimeFragment() {
        // Required empty public constructor
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
