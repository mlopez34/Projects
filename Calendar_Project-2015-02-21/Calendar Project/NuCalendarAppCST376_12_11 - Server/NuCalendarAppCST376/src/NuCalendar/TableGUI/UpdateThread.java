/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NuCalendar.TableGUI;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MLSSD
 */
public class UpdateThread extends Thread {
    
    private MainCalendarWindow mcw;
    private Socket client;
    private Scanner ss;
    public UpdateThread(MainCalendarWindow Mcw, Socket s, Scanner fs){
        this.mcw = Mcw;
        this.client = s;
        this.ss = fs;
        //System.out.println("Started UT");
    }
    
    @Override
    public void run(){
        
        while(true)
        {
            if (ss.hasNextLine()){
                String line = ss.nextLine();
                //TODO: accept a message from the server if it starts with UPDATE EVENT, then call mcw.updateEVENT with whatever the info is
                //then repopulate the month.
                System.out.println("UT "+ line);
                if (line.startsWith("ADD OK"))
                {
                    String [] data = line.split(", ");
                    String name = data[1];
                    Date date = new Date(data[2]);
                    Date endDate = new Date(data[3]);
                    String location = data[4];
                    String availableTo = data[5];
                    //split the users
                    String[] splitUsers = availableTo.split(" ");
                    ArrayList<String> tmp = new ArrayList<>();
                    //put the users inside an array
                    for (String s : splitUsers) {
                        tmp.add(s);
                    }
                    //create an event and put it inside the linked list
                    Event e = new Event(name, date, endDate, location, tmp);
                    //create an event from the line
                    
                    
                    //add the event to the local list
                    mcw.SuccessfulEventAdd(e);
                    
                }
                else if (line.startsWith("UPDATE ADD_EVENT"))
                {
                    String [] data = line.split(", ");
                    String name = data[1];
                    Date date = new Date(data[2]);
                    Date endDate = new Date(data[3]);
                    String location = data[4];
                    String availableTo = data[5];
                    //split the users
                    String[] splitUsers = availableTo.split(" ");
                    ArrayList<String> tmp = new ArrayList<>();
                    //put the users inside an array
                    for (String s : splitUsers) {
                        tmp.add(s);
                    }
                    //create an event and put it inside the linked list
                    Event e = new Event(name, date, endDate, location, tmp);
                    //add the event to the local list
                    
                    mcw.SuccessfulEventAdd(e);
                }
                else if (line.startsWith("UPDATE REMOVE_EVENT"))
                {
                    String [] data = line.split(", ");
                    String name = data[1];
                    
                    //add the event to the local list
                    
                    mcw.SuccessfulRemoveEvent(name);
                }
                //mcw.nextMonth();
                
            }
        }
    }
    
}
