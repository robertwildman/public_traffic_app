package org.S1509388.gcu.TrafficApp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FragmentListener
{
    /*Icons from
        https://www.flaticon.com/free-icon/cone_827670?term=traffic%20cone&page=1&position=8
        https://www.flaticon.com/free-icon/car-collision_65788?term=accident&page=1&position=73
        under construction by Georgiana Ionescu from the Noun Project
    */

    private TextView rawDataDisplay;
    private String result;
    private ProgressBar progressBar;
    private Button startButton;
    public Collection c;
    private MapFragment fragmentMap;
    private SearchFragment fragmentSearch;
    private String settings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentMap = new MapFragment();
        fragmentSearch = new SearchFragment();
        c = new Collection();
        new DownloadTrafficData(this,c,fragmentMap).execute();
        fragmentMap.addPlannedRoadworkers(c.getPlannedrws());
        fragmentMap.addCurrentRoadworks(c.getCurrentrws());
        fragmentMap.addIncidents(c.getIncidents());

        //rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        progressBar =  (ProgressBar) findViewById(R.id.progress);
       // startButton = (Button)findViewById(R.id.startButton);
        // startButton.setOnClickListener(this);

        settings = "TFFFF";
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map_container, fragmentMap)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.search_container, fragmentSearch)
                    .commit();
            return;
        }else
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map_container, fragmentMap)
                    .commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map_container, fragmentMap)
                        .commit();
            }
        });
        fragmentMap.addPlannedRoadworkers(c.getPlannedrws());
        fragmentMap.addCurrentRoadworks(c.getCurrentrws());
        fragmentMap.addIncidents(c.getIncidents());



    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miRefresh:
                new DownloadTrafficData(this,c,fragmentMap).execute();
                return true;
            case R.id.miSearch:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map_container, fragmentSearch)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View aview)
    {
        new DownloadTrafficData(this,c,fragmentMap).execute();
    }

    public void onMapSettingsSent(String values)
    {
        Log.e("MyTag", "Received from Settings" + values);
        fragmentMap.updateSettings(values);
    }


    @Override
    public Collection getCollection() {
        return this.c;
    }
} // End of MainActivity