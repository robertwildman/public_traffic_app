package org.S1509388.gcu.TrafficApp;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.S1509388.gcu.TrafficApp.Models.Incident;
import org.S1509388.gcu.TrafficApp.Models.Roadwork;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MarkerWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;
    private Collection c;
    private DateFormat dateFormatterDate = new SimpleDateFormat("EEE MMM dd yyyy HH:mm", Locale.ENGLISH);
    public MarkerWindowAdapter(Activity context,Collection c){
        this.context = context;
        this.c = c;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.markerwindow, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
        TextView tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
        TextView tvOther = (TextView) view.findViewById(R.id.tvOther);
        String[] markerinfo = marker.getSnippet().split(",");
        System.out.println("Issue");
        if(markerinfo[0].equalsIgnoreCase("Roadworkc"))
        {
            //Roadwork Current class
            Roadwork rw = c.getcurrentrwsbyid(Integer.parseInt(markerinfo[1]));
            if(rw.getStartDate() != null && rw.getEndDate() != null)
            {
                tvStartDate.setText("Start Date: " + dateFormatterDate.format(rw.getStartDate()));
                tvEndDate.setText("End Date: "  + dateFormatterDate.format(rw.getEndDate()));
                tvOther.setText("Other Infomation: " +rw.getDescription());
            }else
            {
                System.out.println(rw.getStartDate().toString());
                System.out.println(rw.getEndDate().toString());
            }
            tvTitle.setText("Current Roadworks: " + marker.getTitle());
        }else if(markerinfo[0].equalsIgnoreCase("Roadworkp"))
        {
            //Roadwork Planned class
            Roadwork rw = c.getplannedrwsbyid(Integer.parseInt(markerinfo[1]));
            if(rw.getStartDate() != null && rw.getEndDate() != null )
            {
                tvStartDate.setText("Start Date: " + dateFormatterDate.format(rw.getStartDate()));
                tvEndDate.setText("End Date: "  + dateFormatterDate.format(rw.getEndDate()));
                tvOther.setText("Other Infomation: " +rw.getDescription());
            }
            tvTitle.setText("Planed Roadworks: " + marker.getTitle());
        }else
        {
            //Incident Class
            Incident ic = c.getincidentbyid(Integer.parseInt(markerinfo[1]));
            tvStartDate.setText("Start Date: " + ic.getTimePublished().toString());
            tvOther.setText("Other Infomation: " +ic.getDescription());
            tvTitle.setText("Incident" + marker.getTitle());
        }


        return view;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}