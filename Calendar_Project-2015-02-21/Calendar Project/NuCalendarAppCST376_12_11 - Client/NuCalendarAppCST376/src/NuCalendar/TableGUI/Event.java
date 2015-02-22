/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NuCalendar.TableGUI;

//import NCACST376.*;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author MLSSD
 */
public class Event {
    
    //fields
    private String Name;
    private Date StartDate;
    private Date EndDate;
    //changed location to description, everything else remains the same because running out of time
    private String Description;
    private ArrayList<String> AvailableTo;
    //constructor
    public Event(String name, Date startDate, String location, ArrayList<String> availableTo)
    {
        this.Name = name;
        this.StartDate = startDate;
        this.Description = location;
        this.AvailableTo = availableTo;
    }
    //user provided an end date
    public Event(String name, Date startDate, Date endDate, String location, ArrayList<String> availableTo)
    {
        this.Name = name;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.Description = location;
        this.AvailableTo = availableTo;
    }
    //edit the event info
    public void editName(String name)
    {
        this.Name = name;
    }
    
    public void editStartDate(Date date)
    {
        this.StartDate = date;
    }
    
    public void editEndDate(String location)
    {
        this.Description = location;
    }
    public void addUser(String userName)
    {
        if (!AvailableTo.contains(userName))
        {
            this.AvailableTo.add(userName);
        }
    }
    
    public void removeUser(String userName)
    {
        if (AvailableTo.contains(userName))
        {
            this.AvailableTo.remove(userName);
        }
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date EndDate) {
        this.EndDate = EndDate;
    }

    public ArrayList<String> getAvailableTo() {
        return AvailableTo;
    }

    public String getLocation() {
        return Description;
    }

    public String getName() {
        return Name;
    }

    public Date getStartDate() {
        return StartDate;
    }
    public void removeAllUsers()
    {
        this.AvailableTo = new ArrayList<String>();
    }
    public String toString()
    {
        return this.Name + ", " + this.StartDate + ", " + this.EndDate+", " + this.Description;
    }
    
    public static void main(String[] args)
   {
       //This is all for testing purposes.
       EventList e = new EventList();
       Date date = new Date("1994/08/09 02:02:02");
       ArrayList<String> as = new ArrayList<String>();
       as.add("John");
       as.add("Luke");
       Event l = new Event("hello", date, "loc", as);
       e.add(l);
       System.out.println(l.getName());
       
   }
}