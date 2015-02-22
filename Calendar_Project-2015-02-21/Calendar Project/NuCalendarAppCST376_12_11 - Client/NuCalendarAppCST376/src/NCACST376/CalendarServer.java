/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NCACST376;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mruth
 */
public class CalendarServer extends Thread {

    //private static List<String> items = new ArrayList<String>();;
    private Socket client;
    private static File UsersFile;
    private static File EventsFile;
    private boolean loggedIn=false;
    private boolean userIsAdmin=false;
    private OutputStreamWriter out;
    private static UserList usersList;
    private static EventList eventsList;
    private static ArrayList<Socket> allUserSockets;
    private static ArrayList<String> allUsers;
    
    public CalendarServer(Socket client) {
        this.client = client;
        try {
            out = new OutputStreamWriter(client.getOutputStream());
        } catch (IOException ex) {
            
        }
    }
    
    public static void loadUsers(File f) throws FileNotFoundException
    {
        //this will load the users
        Scanner scanner = new Scanner(f);
        
        while (scanner.hasNextLine()) {
            //get the username
            
            String userAsString = scanner.nextLine();
            //get the pass
            String[] userData = userAsString.split(", ");
            String name = userData[0];
            String pass = userData[1];
            boolean admin;
            if (userData[2].compareTo("admin")==0)
            {
                admin = true;
            }
            else{
                admin = false;
            }
            //get the string holding admin...
            //create a user
            User user = new User(name, pass, admin);
            //add user to list...
            usersList.add(user);
            //System.out.println("Added " + user.toString());
        }
        System.out.println("Users: "+usersList.getAllUsers());
        
    }
    
    public static void loadEvents(File f) throws FileNotFoundException
    {
        //this will load the events
        Scanner scanner = new Scanner(f);
        
        while (scanner.hasNextLine()) {
            //get the event
            String eventAsString = scanner.nextLine();
            //get the event data split
            String[] eventData = eventAsString.split(", ");
            String name = eventData[0];
            Date date = new Date(eventData[1]);
            Date endDate = new Date(eventData[2]);
            String location = eventData[3];
            String availableTo = eventData[4];
            //split the users
            String[] splitUsers = availableTo.split(" ");
            ArrayList<String> tmp = new ArrayList<>();
            //put the users inside an array
            for (String s : splitUsers)
            {
                tmp.add(s);
            }
            //create an event and put it inside the linked list
            Event e = new Event(name, date, endDate, location, tmp);
            eventsList.add(e);
        }
        System.out.println("Events: " +eventsList.getAllEvents());
    }
    public String clientRequestEvents(String userName)
    {
        //create an arraylist of events from our linked list
        //ArrayList<Event> tmpListOfEvents = eventsList.FilterEventsForUser(userName);
        ArrayList<Event> tmpListOfEvents = eventsList.getAllEvents();
        //result will be the string we send back to the user
        String result = "";
        for (Event e : tmpListOfEvents) {
            //get name, location, date, and list of available to users from the list
            String eName = e.getName();
            String eLocation = e.getLocation();
            Date eDate = e.getStartDate();
            Date eEndDate = e.getEndDate();
            ArrayList<String> tmpUsersInEvent = e.getAvailableTo();
            String AllUsers = "";
            //format users
            for (String s : tmpUsersInEvent) {
                AllUsers = AllUsers + " " + s;
            }
            //send the events each separated by a semicolon, and each piece of the event separated by comas, spaces separate the available to.
            result = result + eName + ", " + eDate.toString() + ", " + eEndDate.toString()+", "+ eLocation + ", " + AllUsers + ";";
        }
        return result;
    }
    public String clientRequestUsers()
    {
        //get a list of users from linked list
        ArrayList<User> tmpListOfUsers = usersList.getAllUsers();
        String result = "";
        //add all the users names only to result and separate by semicolon
        for (User u : tmpListOfUsers)
        {
            String uName = u.getName();
            result = result + uName+";";
        }
        return result;
    }
    public void outPrintln(String str) {
        //write out to the client
        try{
        out.write(str + "\r\n");
        out.flush();
        }
        catch(Exception e)
        {
            //
        }
    }
    public void UpdatePrint(String str)
    {
        ArrayList<Socket> tempList = new ArrayList<Socket>();
        for (Socket soc : allUserSockets)
        {
            //write an update to all the connected sockets
            try{
                OutputStreamWriter outJr = new OutputStreamWriter(soc.getOutputStream());
                outJr.write(str+"\r\n");
                outJr.flush();
                System.out.println("Sending "+str + " to: "+soc.getInetAddress().toString());
                System.out.println(str);
                //if everything went fine, then the socket is still connected
                tempList.add(soc);
            }
            //TODO: need some way of removing sockets from the list that are not connected
            catch(Exception exx){
                //allUserSockets.remove(soc);
                System.out.println(exx);
            }
        }
        //the list of user sockets connected becomes the templist that is a list of good sockets
        allUserSockets = tempList;
        
    }
    public synchronized static void SaveEvents() throws IOException
    {
        //write all contents of eventsList to events.txt
        BufferedWriter eventsToFile = new BufferedWriter(new FileWriter(EventsFile));
        ArrayList<Event> EL= eventsList.getAllEvents();
        for (Event e: EL)
        {
            ArrayList<String> tmp = e.getAvailableTo();
            String avail = "";
            for (String s : tmp)
            {
                avail = avail + s +" ";
            }
            //format the line to write
            String toWrite = e.getName()+", "+e.getStartDate().toString()+", "+e.getEndDate()+", " +e.getLocation()+", "+ avail;
            eventsToFile.write(toWrite + "\r\n");
            //System.out.println("wrote " + toWrite + " to file");
        }
        System.out.println("Saved Events to file");
        eventsToFile.close();
    }
    
    public synchronized static void SaveUsers() throws IOException
    {
        //TODO: save users to the file when a user is added or removed
    }
    
    @Override
    public void run() {
        
        
            
        try {
            //read from scanner...
            InputStream ins = client.getInputStream();
            Scanner scanner = new Scanner(ins);
            //is there a message to process?
            while (scanner.hasNextLine()) {
                //get the first line
                String command = scanner.nextLine();
                System.out.println("Command: " +command + " "+client.getInetAddress());
                //output will be the message the server replies with
                String output = "";
                
                if (command.compareTo("LOG IN") == 0) {
                    //user requests to log in, next 2 lines are the username and password
                    
                    String usernameToCheck = scanner.nextLine();
                    String passwordToCheck = scanner.nextLine();
                    System.out.println(usernameToCheck+ " "+passwordToCheck);
                    //check if user exists
                    if (usersList.contains(usernameToCheck))
                    {
                        User u = usersList.getUser(usernameToCheck);
                        //check if password exists
                        if (u.getPass().compareTo(passwordToCheck)==0)
                        {
                            System.out.println(client.getInetAddress().toString() + " Has logged in!");
                            loggedIn = true;
                            output = "LOGIN OK";
                            allUsers.add(usernameToCheck);
                            System.out.println("allusers " +allUsers);
                        }
                        else{
                            output = "LOGIN FAIL";
                        }
                        if (u.isAdmin() == true)
                        {
                            userIsAdmin = true;
                        }
                    }
                    else{
                        output = "LOGIN FAIL";
                    }
                    outPrintln(output);
                
                } 
                else if (command.compareTo("LOGOUT") == 0) {
                    
                    //TODO: user logs out
                } 
                else if (command.compareTo("NEW USER") == 0) 
                {
                    //TODO: add new user to the linked list
                    String name = scanner.nextLine();
                    String password = scanner.nextLine();
                    String confirm = scanner.nextLine();
                    if (password.compareTo(confirm) ==0)
                    {
                        output = "NEW USER OK";
                        //create the user
                        User u = new User(name, password, false);
                        usersList.add(u);
                        allUsers.add(name);
                        userIsAdmin = false;
                        loggedIn = true;
                    }
                    System.out.println(usersList.getAllUsers());
                    outPrintln(output);
                    SaveUsers();

                }
                else if (command.compareTo("ADD_EVENT") == 0) {
                    //Event e = new Event(String name, Date startDate, Date endDate, ArrayList<String> availableTo)
                    //get the information for the event
                    
                    //get all the parts of the event
                    String name = scanner.nextLine();
                    String sdate = scanner.nextLine();
                    String edate = scanner.nextLine();
                    String location = scanner.nextLine();
                    String avail = scanner.nextLine();
                    Date startDate= null;
                    Date endDate = null;
                    System.out.println(client.getInetAddress() + " Attempting to add event: "+ name);
                    
                    try {
                        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                        startDate = format.parse(sdate);
                        endDate = format.parse(edate);
                    } catch (Exception ex) {
                        outPrintln("ERROR");
                        outPrintln("Date Invalid");
                    }
                    //list of users this event is going to belong to
                    String[] availList = avail.split(" ");
                    ArrayList<String> tmp = new ArrayList<>();
                    for (String s : availList)
                    {
                        tmp.add(s);
                    }
                    //add to EventList code here
                    Event e = new Event(name, startDate, endDate, location, tmp);
                    eventsList.add(e);
                    System.out.println("Added " +e.toString() + " to the calendar");
                    outPrintln("ADD OK" +", "+ e.toString() +", "+ avail);
                    
                    UpdatePrint("UPDATE ADD_EVENT, "+ e.toString()+", " +avail);
                    //Now write this event to the file, but how to handle this if we're removing as well?
                    //just write everything in memory to the text file, this way the file has no "holes"
                    SaveEvents();
                    //TODO: write back to all connected users
                    //The output should start off with UPDATE and write an event just like ADD OK
                    //server will send the UPDATE line to everyone that is part of the avail list except the user that sent it, (which is name of user in this thread)
                    //UPDATE thread should grab that
                    //then updateThread will tell the main calendar window to update itself
                    

                } 
                else if (command.compareToIgnoreCase("REMOVE_EVENT") == 0) {
                    //next line is the name of the event
                    String name = scanner.nextLine();
                    
                    if (eventsList.contains(name))
                    {
                        Event tmpEvent = eventsList.getEvent(name);
                        eventsList.remove(tmpEvent);
                        System.out.println(client.getInetAddress().toString() + "Removing event: "+ name);
                        output = "REMOVE OK";
                        outPrintln(output);
                        UpdatePrint("UPDATE REMOVE_EVENT, "+ tmpEvent.getName());
                        SaveEvents();
                    }
                    else{
                        output = "REMOVE FAIL";
                        outPrintln(output);
                    }
                } 
                
                else if(command.compareTo("LOAD EVENTS") == 0)
                {
                    String name = scanner.nextLine(); //this is the user's name
                    //when a client connects and logs in they will request events, and we return them back as a string
                    System.out.println(client.getInetAddress().toString()+ " Client attempting to load events");
                    output = clientRequestEvents(name);
                    outPrintln(output);
                }
                
                else if(command.compareTo("LOAD USERS") == 0)
                {
                    //when a client connects and logs in they will request users, and we return them back as a string
                    System.out.println(client.getInetAddress().toString()+" Client attempting to load users");
                    output = clientRequestUsers();
                    outPrintln(output);
                }
                else {
                    System.out.println(command);
                    outPrintln("ERROR");
                    break;
                }

            }

        } catch (IOException ex) {
            System.out.println(ex);
        }
        
    }
    public static void main(String[] str) throws FileNotFoundException {
        //server begins here
        //load necessary components
        System.out.println("LOADING COMPONENTS");
        usersList = new UserList();
        eventsList = new EventList();
        //load users to memory
        UsersFile = new File("src\\NCACST376\\users.txt");
        loadUsers(UsersFile);
        //load events to memory
        EventsFile = new File("src\\NCACST376\\events.txt");
        loadEvents(EventsFile);
        System.out.println("SERVER HAS STARTED");
        
        //hold all the sockets connected
        allUserSockets = new ArrayList<Socket>();
        //hold all the names of users connected
        allUsers = new ArrayList<String>();
        
        try {
            //create a socket
            ServerSocket socket = new ServerSocket(5555);
            //server loop
            while (true) {
                //wait for incoming....
                System.out.println("waiting for cleints...");
                Socket client = socket.accept();
                //create a thread
                allUserSockets.add(client);
                System.out.println("allUserSockets " + allUserSockets);
                System.out.println(client.getInetAddress().toString() + " Has attempted to login");
                CalendarServer ts = new CalendarServer(client);
                //start the thread!
                ts.start();
            }

        } catch (IOException ex) {
            System.out.println(ex);

        }
        
    }

}
