package org.S1509388.gcu.TrafficApp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.S1509388.gcu.TrafficApp.Models.Incident;
import org.S1509388.gcu.TrafficApp.Models.Roadwork;

import java.security.cert.CollectionCertStoreParameters;
import java.util.Date;
import java.text.CollationElementIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class SearchFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, RadioGroup.OnCheckedChangeListener {
    private View mView;
    private EditText fromDate,toDate,etroadin;
    private RadioGroup rgDate,rgRoads;
    private RadioButton rbCT,rbSelect,rbSelectedRoads,rbAllRoads;
    private TableLayout tlDate;
    private LinearLayout llRoadname;
    private CheckBox cbIn,cbCurrentrw,cbPlannedrw;
    private Collection c;
    private MapFragment fragmentMap;
    private Button bSearchSub;
    private FragmentListener fl;

    public SearchFragment(){ }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            fl = (FragmentListener) getActivity();
            c = fl.getCollection();
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
        mView = inflater.inflate(R.layout.search_fragment, container, false);
        fromDate = mView.findViewById(R.id.etFromDate);
        toDate = mView.findViewById(R.id.etToDate);
        rgDate = mView.findViewById(R.id.rgDate);
        rbCT = mView.findViewById(R.id.rbCT);
        rbSelect = mView.findViewById(R.id.rbSelect);
        tlDate = mView.findViewById(R.id.tlDate);
        cbIn = mView.findViewById(R.id.cbIn);
        cbCurrentrw = mView.findViewById(R.id.cbCurrentRw);
        cbPlannedrw = mView.findViewById(R.id.cbPlannedRw);
        bSearchSub = mView.findViewById(R.id.bSearchSub);
        llRoadname = mView.findViewById(R.id.llRoadname);
        rbSelectedRoads = mView.findViewById(R.id.rbSelectedRoads);
        rbAllRoads = mView.findViewById(R.id.rbAllRoads);
        rgRoads = mView.findViewById(R.id.rgRoads);
        etroadin = mView.findViewById(R.id.etRoadin);
        rgRoads.setOnCheckedChangeListener(this);
        rgDate.setOnCheckedChangeListener(this);
        fromDate.setOnTouchListener(this);
        toDate.setOnTouchListener(this);
        bSearchSub.setOnClickListener(this);
        rbAllRoads.toggle();
        rbCT.toggle();
        cbPlannedrw.toggle();
        cbIn.toggle();
        cbCurrentrw.toggle();
        //navButton = (Button)mView.findViewById(R.id.settingsButton);
        //afragment = (SupportMapFragment)v.findViewById(R.id.map);
        //navButton.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        //Start will all data
        LinkedList<Roadwork> searchcurrentrw = c.getCurrentrws();
        LinkedList<Roadwork> searchplannedrw = c.getPlannedrws();
        LinkedList<Incident> searchincidents = c.getIncidents();
        Log.e("mytag",String.valueOf(c.getCurrentrws().size()));
        //Checking if all the roads or typed one
        if(rbSelectedRoads.isChecked())
        {
            String input = etroadin.getText().toString();
            searchcurrentrw = c.getroadworksbyname(searchcurrentrw,input) ;
            searchplannedrw = c.getroadworksbyname(searchplannedrw,input);
            searchincidents = c.getincidentsbyname(searchincidents,input);
        }
        //Checking Date
        if(rbSelect.isChecked())
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date start = null;
            Date end = null;
            try {
               start = formatter.parse(fromDate.getText().toString());
               end = formatter.parse(toDate.getText().toString());
               searchcurrentrw = c.getoadworksbydate(searchcurrentrw, start,end);
               searchplannedrw = c.getoadworksbydate(searchplannedrw, start,end);
               searchincidents = c.getincidentsbydate(searchincidents, start,end);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        //Dealing with the date inputted
        //Filter pass on the data wanted,
        if(cbIn.isChecked())
        {
            c.setSavedincidents(searchincidents);
        }else
        {
            c.setSavedincidents(null);
        }
        if(cbCurrentrw.isChecked())
        {
            c.setSavedcurrentrws(searchcurrentrw);
        }else
        {
            c.setSavedcurrentrws(null);
        }
        if(cbPlannedrw.isChecked())
        {
            c.setSavedplannedrws(searchplannedrw);
        }else
        {
            c.setSavedplannedrws(null);
        }
        //Handling the searching!
        fragmentMap = new MapFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.map_container, fragmentMap)
                .commit();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v == fromDate && MotionEvent.ACTION_UP == event.getAction())
        {
            DialogFragment newFragment = new DatePickerFragment(fromDate);
            newFragment.show(getFragmentManager(), "datePicker");
        }else if(v == toDate && MotionEvent.ACTION_UP == event.getAction())
        {
            DialogFragment newFragment = new DatePickerFragment(toDate);
            newFragment.show(getFragmentManager(), "datePicker");
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(rbCT == mView.findViewById(checkedId))
        {
            tlDate.setVisibility(View.GONE);
        }else if(rbSelect == mView.findViewById(checkedId))
        {
            tlDate.setVisibility(View.VISIBLE);
        }
        else if(rbSelectedRoads == mView.findViewById(checkedId))
        {
            llRoadname.setVisibility(View.VISIBLE);
        }
        else if(rbAllRoads == mView.findViewById(checkedId))
        {
            llRoadname.setVisibility(View.GONE);
        }
    }
}
