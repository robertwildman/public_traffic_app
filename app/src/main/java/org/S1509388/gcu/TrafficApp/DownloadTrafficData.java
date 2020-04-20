package org.S1509388.gcu.TrafficApp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.S1509388.gcu.TrafficApp.Models.Roadwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class DownloadTrafficData extends AsyncTask<String, Integer, Void> {
    int progress_status;
    private TextView rawDataDisplay;
    private ProgressBar progressBar;
    private MainActivity activity;
    private LinearLayout llProgress;
    private MapFragment fragmentMap;
    private String result;
    private Collection c;
    private String roadworksUrlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String plannedRoadworksUrlSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String incidentsUrlSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    public DownloadTrafficData(MainActivity a,Collection c,MapFragment fragmentMap)
    {
        this.activity = a;
        this.c = c;
        this.fragmentMap = fragmentMap;
        //rawDataDisplay = (TextView) activity.findViewById(R.id.rawDataDisplay);
        progressBar =  (ProgressBar) activity.findViewById(R.id.progress);
        llProgress = (LinearLayout) activity.findViewById(R.id.llProgress);
    }
    @Override
    protected void onPreExecute()
    {
        // update the UI immediately after the task is executed
        super.onPreExecute();
        Toast.makeText(activity,"Invoke onPreExecute()", Toast.LENGTH_SHORT).show();
        llProgress.setVisibility(View.VISIBLE);
        publishProgress(0);

    }

    @Override
    protected Void doInBackground(String... params)
    {
        publishProgress(5);
        c.parseplannedrw(getxmlfromurl(plannedRoadworksUrlSource,10,20,30));
        publishProgress(35);
        c.parsecurrentrw(getxmlfromurl(roadworksUrlSource,40,50,60));
        publishProgress(65);
        c.parseincidents(getxmlfromurl(incidentsUrlSource,70,80,90));
        publishProgress(100);
        return null;
    }

    protected void onProgressUpdate(Integer... values)
    {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);

    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);

        Toast.makeText(activity,
                "Data has been updated", Toast.LENGTH_SHORT).show();
        llProgress.setVisibility(View.GONE);
        fragmentMap.addPlannedRoadworkers(c.getPlannedrws());
        fragmentMap.addCurrentRoadworks(c.getCurrentrws());
        fragmentMap.addIncidents(c.getIncidents());
    }

    private String getxmlfromurl(String url,Integer p1,Integer p2,Integer p3)
    {
        result = "";
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";
        Log.e("MyTag","in run");
        try
        {
            publishProgress(p1);
            Log.e("MyTag","in try");
            aurl = new URL(url);
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            //
            // Throw away the first 2 header lines before parsing
            //
            //
            publishProgress(p2);
            result = in.readLine();
            while ((inputLine = in.readLine()) != null) {
                result = result + inputLine;

            }
            in.close();
            publishProgress(p3);

        }
        catch (IOException ae)
        {
            Log.e("MyTag", "ioexception");
        }
        return result;
    }
}
