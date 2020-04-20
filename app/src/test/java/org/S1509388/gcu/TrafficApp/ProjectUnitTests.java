package org.S1509388.gcu.TrafficApp;

import org.S1509388.gcu.TrafficApp.Models.Incident;
import org.S1509388.gcu.TrafficApp.Models.Roadwork;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ProjectUnitTests {
    private Collection c;
    private LinkedList<Roadwork> tmprw = new LinkedList<Roadwork>();
    private LinkedList<Incident> tmpic = new LinkedList<Incident>();
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH/mm");
    @Before
    public void create_collect()
    {
        c = new Collection();
        assertNotNull(c);
    }
    @Before
    public void create_roadwork()
    {
        Roadwork rw1 = new Roadwork(0);
        rw1.setTitle("First Roadwork - M74");
        rw1.setDescription("My Description");
        try {
            rw1.setStartDate(df.parse("20/04/2020 00/00"));
            rw1.setEndDate(df.parse("23/04/2020 00/00"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        Roadwork rw2 = new Roadwork(0);
        rw2.setTitle("Second Roadwork - M72");
        rw2.setDescription("My Description");
        try {
            rw1.setStartDate(df.parse("19/04/2020 00/00"));
            rw1.setEndDate(df.parse("27/04/2020 00/00"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        Roadwork rw3 = new Roadwork(0);
        rw3.setTitle("Third Roadwork - A74");
        rw3.setDescription("My Description");
        try {
            rw3.setStartDate(df.parse("30/04/2020 00/00"));
            rw3.setEndDate(df.parse("02/05/2020 00/00"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        Roadwork rw4 = new Roadwork(0);
        rw4.setTitle("Fourth Roadwork - B5034");
        rw4.setDescription("My Description");
        try {
            rw4.setStartDate(df.parse("01/07/2020 00/00"));
            rw4.setEndDate(df.parse("20/08/2020 00/00"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        tmprw.add(rw1);
        tmprw.add(rw2);
        tmprw.add(rw3);
        tmprw.add(rw4);
        assertEquals(tmprw.size(),4);
    }
    @Test
    public void add_roadworks_current()
    {
        LinkedList<Roadwork> tmp = new LinkedList<Roadwork>();
        tmp.add(tmprw.get(0));
        tmp.add(tmprw.get(1));
        c.setSavedcurrentrws(tmp);
        assertEquals(c.getSavedcurrentrws().size(),2);
    }
    @Test
    public void add_roadwork_planned()
    {
        LinkedList<Roadwork> tmp = new LinkedList<Roadwork>();
        tmp.add(tmprw.get(2));
        tmp.add(tmprw.get(3));
        c.setSavedplannedrws(tmp);
        assertEquals(c.getSavedplannedrws().size(),2);
    }
    @Before
    public void create_incidents()
    {

        Incident ic = new Incident(0);
        ic.setTitle("First Incident - M8");
        ic.setDescription("Decription of incident");
        try {
            ic.setTimePublished(df.parse("20/04/2020 18/30"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        Incident ic1 = new Incident(1);
        ic1.setTitle("Second Incident - M9");
        ic1.setDescription("Decription of incident");
        try {
            ic1.setTimePublished(df.parse("20/04/2020 19/30"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        tmpic.add(ic);
        tmpic.add(ic1);
        assertEquals(tmpic.size(),2);
    }
    @Test
    public void add_incidents()
    {
        c.setSavedincidents(tmpic);
        assertEquals(c.getSavedincidents().size(),2);
    }
    @After
    public void roadworks_filter_date()
    {
        try {
            assertEquals(1,c.getoadworksbydate(c.getSavedplannedrws(),df.parse("30/04/2020 00/00"),df.parse("02/05/2020 00/00")).size());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    @After
    public void incidents_filter_date()
    {
        try {
            assertEquals(c.getincidentsbydate(c.getSavedincidents(),df.parse("20/04/2020 00/00"),df.parse("02/05/2020 00/00")).size(),2);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    @After
    public void roadworks_filter_name()
    {
        assertEquals(1,c.getroadworksbyname(c.getSavedcurrentrws(),"M74").size());
    }
    @After
    public void incidents_filter_name()
    {
        assertEquals(1,c.getincidentsbyname(c.getSavedincidents(),"M8").size());
    }


}