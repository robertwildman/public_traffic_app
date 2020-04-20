package org.S1509388.gcu.TrafficApp;

import android.util.Log;

import org.S1509388.gcu.TrafficApp.Models.Incident;
import org.S1509388.gcu.TrafficApp.Models.Roadwork;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class Collection {
    private LinkedList<Roadwork> currentrws;
    private LinkedList<Roadwork> plannedrws;
    private LinkedList<Incident> incidents;

    private LinkedList<Roadwork> savedcurrentrws;
    private LinkedList<Roadwork> savedplannedrws;
    private LinkedList<Incident> savedincidents;
    private DateFormat dateFormatterRssPubDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
    private DateFormat dateFormatterDate = new SimpleDateFormat("EEEEE, dd MMM yyyy - HH:mm", Locale.ENGLISH);
    public Collection() {
        currentrws = new LinkedList<Roadwork>();
        plannedrws = new LinkedList<Roadwork>();
        incidents = new LinkedList<Incident>();

        savedcurrentrws = new LinkedList<Roadwork>();
        savedplannedrws = new LinkedList<Roadwork>();
        savedincidents =  new LinkedList<Incident>();

    }
    public void parsecurrentrw(String input)
    {
        parserw(input,currentrws);
    }
    public void parseplannedrw(String input)
    {
        parserw(input,plannedrws);
    }
    public void parseincidents(String input)
    {
        String tmptext = "";
        int counter = 0;
        Incident incident = new Incident(counter);
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new StringReader(input));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_DOCUMENT)
                {

                }else if(eventType == XmlPullParser.START_TAG)
                {

                    if(xpp.getName().equalsIgnoreCase("item"))
                    {
                        counter++;
                        incident = new Incident(counter);
                    }
                }else if(eventType == XmlPullParser.END_TAG)
                {
                    System.out.println(xpp.getName());
                    switch(xpp.getName())
                    {
                        case "item":
                            incidents.add(incident);
                            break;
                        case "title":
                            incident.setTitle(tmptext);
                            break;
                        case "pubDate":
                            try {
                                incident.setTimePublished(dateFormatterRssPubDate.parse(tmptext));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "point":
                            System.out.println("Lets see what the point is: " + tmptext);
                            String[] cords = tmptext.split(" ");
                            System.out.println(cords[0] + " "  + cords[1]);
                            incident.setLat(cords[0]);
                            incident.setLong(cords[1]);
                            break;
                        case "description":
                            incident.setDescription(tmptext);
                            break;
                        default:
                            break;
                    }
                }else if(eventType == XmlPullParser.TEXT)
                {
                    tmptext = xpp.getText();
                }
                eventType = xpp.next();
            }
            System.out.println("End Document");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parserw(String input,LinkedList<Roadwork> llrw)
    {
        String tmptext = "";
        int counter = 0;
        Roadwork rw = new Roadwork(counter);
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new StringReader(input));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_DOCUMENT)
                {

                }else if(eventType == XmlPullParser.START_TAG)
                {

                    if(xpp.getName().equalsIgnoreCase("item"))
                    {
                        counter++;
                        rw = new Roadwork(counter);
                    }
                }else if(eventType == XmlPullParser.END_TAG)
                {
                    System.out.println(xpp.getName());
                    switch(xpp.getName())
                    {
                        case "item":
                            llrw.add(rw);
                            break;
                        case "title":
                            Log.e("Title",tmptext);
                            rw.setTitle(tmptext);
                            break;
                        case "point":
                            System.out.println("Lets see what the point is: " + tmptext);
                            String[] cords = tmptext.split(" ");
                            System.out.println(cords[0] + " "  + cords[1]);
                            rw.setLat(cords[0]);
                            rw.setLong(cords[1]);
                            break;
                        case "description":
                            //Phasing the string into the sections of the infomation
                            //First take the spilt placed on the br tags
                            //Then will take out the word
                            String[] temparray = tmptext.split("<br />");
                            System.out.println(temparray.length);
                            if(temparray.length > 1) {
                                String startdate = temparray[0];
                                String enddate = temparray[1];
                                try {
                                    Date d = dateFormatterDate.parse(startdate.substring(startdate.indexOf(": ") + 2));
                                    rw.setStartDate(d);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Date d = dateFormatterDate.parse(enddate.substring(enddate.indexOf(": ") + 2));
                                    rw.setEndDate(d);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(temparray.length > 2)
                                {
                                    rw.setDescription(temparray[2]);
                                }

                            }else
                            {
                                rw.setDescription(tmptext);
                            }
                            break;
                        default:
                            break;
                    }
                }else if(eventType == XmlPullParser.TEXT)
                {
                    tmptext = xpp.getText();
                }
                eventType = xpp.next();
            }
            System.out.println("End Document");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Roadwork> getCurrentrws() {
        return currentrws;
    }

    public LinkedList<Roadwork> getPlannedrws() {
        return plannedrws;
    }

    public LinkedList<Incident> getIncidents() {
        return incidents;
    }

    public LinkedList<Roadwork> getSavedcurrentrws() {
        return savedcurrentrws;
    }

    public void setSavedcurrentrws(LinkedList<Roadwork> savedcurrentrws) {
        this.savedcurrentrws = savedcurrentrws;
    }

    public LinkedList<Roadwork> getSavedplannedrws() {
        return savedplannedrws;
    }

    public void setSavedplannedrws(LinkedList<Roadwork> savedplannedrws) {
        this.savedplannedrws = savedplannedrws;
    }

    public LinkedList<Incident> getSavedincidents() {
        return savedincidents;
    }

    public void setSavedincidents(LinkedList<Incident> savedincidents) {
        this.savedincidents = savedincidents;
    }

    public Roadwork getplannedrwsbyid(int id)
    {
        for(Roadwork rw : this.getPlannedrws())
        {
            if(rw.getCounter() == id)
            {
                return rw;
            }
        }
        return null;
    }
    public Roadwork getcurrentrwsbyid(int id)
    {
        for(Roadwork rw : this.getCurrentrws())
        {
            if(rw.getCounter() == id)
            {
                return rw;
            }
        }
        return null;
    }
    public Incident getincidentbyid(int id)
    {
        for(Incident ic : this.getIncidents())
        {
            if(ic.getCounter() == id)
            {
                return ic;
            }
        }
        return null;
    }
    public LinkedList<Roadwork> getoadworksbydate(LinkedList<Roadwork> roadsin, Date start, Date end)
    {
        LinkedList<Roadwork> tmp = new LinkedList<Roadwork>();
        for(Roadwork rw: roadsin)
        {
            if(rw.getStartDate() != null || rw.getEndDate() != null)
            {
                if((!rw.getStartDate().before(start) && !rw.getStartDate().after(end)) || !rw.getEndDate().before(end))
                {
                    tmp.add(rw);
                }
            }
        }
        return tmp;
    }
    public LinkedList<Incident> getincidentsbydate(LinkedList<Incident> incidents, Date start, Date end)
    {
        LinkedList<Incident> tmp = new LinkedList<Incident>();
        for(Incident ic: incidents)
        {
            if(ic.getTimePublished() != null) {
                if (!ic.getTimePublished().before(start) && !ic.getTimePublished().after(end)) {
                    tmp.add(ic);
                }
            }
        }
        return tmp;
    }
    public LinkedList<Roadwork> getroadworksbyname(LinkedList<Roadwork> rws, String input)
    {
        LinkedList<Roadwork> tmp = new LinkedList<Roadwork>();
        for(Roadwork rw: rws)
        {
            if(rw.getTitle() != null)
            {
                if(rw.getTitle().contains(input))
                {
                    tmp.add(rw);
                }
            }
        }
        return tmp;
    }
    public LinkedList<Incident> getincidentsbyname(LinkedList<Incident> inc, String input)
    {
        LinkedList<Incident> tmp = new LinkedList<Incident>();
        for(Incident id: inc)
        {
            if(id.getTitle() != null) {
                if (id.getTitle().contains(input)) {
                    tmp.add(id);
                }
            }
        }
        return tmp;
    }
}
