package org.S1509388.gcu.TrafficApp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.S1509388.gcu.TrafficApp.Models.Incident;
import org.S1509388.gcu.TrafficApp.Models.Roadwork;

import java.io.Serializable;
import java.util.LinkedList;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,View.OnClickListener
{
    private GoogleMap mMap;
    private MapView mMapView;
    private View mView;
    private Collection c;
    private Button navButton;
    private String settings;
    private FragmentListener fl;
    public MapFragment()
    {

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fl = (FragmentListener) context;
            c = fl.getCollection();
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    public void updateSettings(String avalue)
    {
        Log.e("MyTag","In Map Fragment " + avalue);
        settings = new String(avalue);
        Log.e("MyTag","In Map Fragment " + settings);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    public void addPlannedRoadworkers(LinkedList<Roadwork> rw)
    {
        for(Roadwork roadwork : rw)
        {
            System.out.println(roadwork.getLat() + " : " + roadwork.getLong());
            LatLng pos = new LatLng(roadwork.getLat(), roadwork.getLong());
            mMap.addMarker(new MarkerOptions().position(pos).title(roadwork.getTitle()).snippet(String.valueOf("Roadworkp," + roadwork.getCounter())).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cone)));
        }
    }
    public void addCurrentRoadworks(LinkedList<Roadwork> rw)
    {
        for(Roadwork roadwork : rw)
        {
            System.out.println(roadwork.getLat() + " : " + roadwork.getLong());
            LatLng pos = new LatLng(roadwork.getLat(), roadwork.getLong());
            mMap.addMarker(new MarkerOptions().position(pos).title(roadwork.getTitle()).snippet(String.valueOf("Roadworkc," + roadwork.getCounter())).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_roadworks),100,100,false))));

        }
    }
    public void addIncidents(LinkedList<Incident> ics)
    {
        for(Incident ic : ics)
        {
            System.out.println(ic.getLat() + " : " + ic.getLong());
            LatLng pos = new LatLng(ic.getLat(), ic.getLong());
            mMap.addMarker(new MarkerOptions().position(pos).title(ic.getTitle()).snippet(String.valueOf("Incident," + ic.getCounter())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.map_fragment, container, false);
        try {
            fl = (FragmentListener) getActivity();
            c = fl.getCollection();
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
        //navButton = (Button)mView.findViewById(R.id.settingsButton);
        //afragment = (SupportMapFragment)v.findViewById(R.id.map);
        //navButton.setOnClickListener(this);

        return mView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);

        if (mMapView != null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        //Seeing if theres any saved markers
        if(c.getSavedplannedrws() != null)
        {
            addPlannedRoadworkers(c.getSavedplannedrws());
        }
        if(c.getSavedcurrentrws() != null)
        {
            addCurrentRoadworks(c.getSavedcurrentrws());
        }
        if(c.getSavedincidents() != null)
        {
            addIncidents(c.getSavedincidents());
        }


        Log.e("MyTag","In map callback");
        // Add a marker in Glasgow and move the camera
        LatLng glasgow = new LatLng(55.861, -4.25);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(glasgow));
        mMap.setOnInfoWindowClickListener(this);
        CameraPosition glasgowCity = CameraPosition.builder().target(glasgow).zoom(6).bearing(0).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(glasgowCity));
        //Setting up marker window
        MarkerWindowAdapter adapter = new MarkerWindowAdapter(getActivity(),c);
        mMap.setInfoWindowAdapter(adapter);
    }

    @Override
    public void onClick(View v)
    {
        //fragmentSetting = new SettingFragment();

       // FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
       // fragmentTransaction.replace(R.id.displayContainer, fragmentSetting)
                //.commit();
    }

    public void clear() {
        mMap.clear();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), "Info window clicked", Toast.LENGTH_SHORT).show();
    }
}