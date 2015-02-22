package NuCalendar.TableGUI;

/**
 * @author Alec Freeman
 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public final class MainCalendarWindow extends javax.swing.JFrame {

    /**
     * Window attributes.
     */
    private static DefaultTableModel modelCalendar;
    private String[] monthNames;
    private int[] monthLength;
    private static int realYear, realMonth, realDay, currentYear, currentMonth;
    private Socket client;
    private OutputStream out;
    private OutputStreamWriter outToServer;
    private Scanner fromServer;
    private boolean quit;
    private ArrayList<String> usersAvailable;
    private EventList eventsList;
    private AddEvent add;
    private RemoveEvent remove;
    private CopyEvent copy;
    private SwitchEvent switchEvt;
    private EventDisplay evtDisplay;
    private colorChooser cc;

//    EventDisplay evtDisplay = new EventDisplay();
    public MainCalendarWindow(Socket Client, OutputStreamWriter Out, Scanner In,
            String userName, String Password, boolean newUserRequest) {
        /**
         * Initialization.
         */
        super();

        eventsList = new EventList();
        initComponents();

        try {
            this.client = Client;
            this.out = client.getOutputStream();
            //InputStream in = client.getInputStream();
            outToServer = Out;
            fromServer = In;
            quit = false;

            outPrintln("LOAD EVENTS");
            outPrintln(userName);
            eventsFromServer();
            
            usersAvailable = new ArrayList<String>();
            outPrintln("LOAD USERS");
            //outPrintln(userName);
            UsersFromServer();
            

        } catch (Exception e) {
            System.out.println(e);
        }

        add = new AddEvent(client, outToServer, usersAvailable);
        remove = new RemoveEvent(client, outToServer);
        copy = new CopyEvent();
        switchEvt = new SwitchEvent();
        evtDisplay = new EventDisplay();
        cc = new colorChooser();

        add.setVisible(false);
        remove.setVisible(false);
        copy.setVisible(false);
        switchEvt.setVisible(false);
//        evtDisplay.setVisible(false);
        cc.setVisible(false);
//        evtDisplay.setVisible(false);

//        GregorianCalendar cal = new GregorianCalendar();
//        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH);
//        realMonth = cal.get(GregorianCalendar.MONTH);
//        realYear = cal.get(GregorianCalendar.YEAR);
//        currentMonth = realMonth;
//        currentYear = realYear;
        
        startupScreen();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        tableCalendar.setGridColor(Color.WHITE);

        /**
         * Expands the cell height to fill the screen.
         */
        tableCalendar.setRowHeight(74);

        /**
         * Key bindings.
         */
        InputMap inputMap = panelInput.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panelInput.getActionMap();
        /**
         * Input map activating the left and right arrow keys.
         */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left Arrow");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right Arrow");

        actionMap.put("Left Arrow", new ArrowAction("Left Arrow"));
        actionMap.put("Right Arrow", new ArrowAction("Right Arrow"));
    }

    public boolean loginResponseFromServer() {
        if (fromServer.hasNextLine()) {
            String line = fromServer.nextLine();
            if (line.compareTo("LOGIN OK") == 0) {
                //user successfully logged in
                return true;
            } else {
                //user successfully logged out
                return false;
            }
        }
        return false;
    }

    public void SuccessfulRemoveEvent(String name)
    {
        //an event has been successfully removed from the calendar
        if (eventsList.contains(name))
        {
            Event toRemove = eventsList.getEvent(name);
            eventsList.remove(toRemove);
        }
        int firstDayofMonth = getFirstDayOfMonth(currentYear, currentMonth);
        try {
            populateMonth(firstDayofMonth, monthLength[currentMonth - 1]);
        } catch (ParseException ex) {
            System.out.println(ex);
        }     
               
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("C:\\notify.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }
    public void SuccessfulEventAdd(Event e) {
        //an event has been successfully added to the calendar
        //play sound
        try{
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("C:\\bell.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        }
        catch (Exception exc)
        {
            System.out.println(exc);
        }
        if (!eventsList.contains(e.getName()))
        {
            eventsList.add(e);
        }
        
        int firstDayofMonth = getFirstDayOfMonth(currentYear, currentMonth);
        try {
            /**
             * Manual repaint of jTable.
             */
            populateMonth(firstDayofMonth, monthLength[currentMonth - 1]);
        } catch (ParseException ex) {
            //Logger.getLogger(MainCalendarWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eventsFromServer() {
        if (fromServer.hasNextLine()) {
            //server sends all the events at once
            String line = fromServer.nextLine();
            //colon is delimiter
            String[] events = line.split(";");
            //create an event for each event sent
            for (String s : events) {
                String[] eventInfo = s.split(", ");
                String eventName = eventInfo[0];
                Date eventDate = new Date(eventInfo[1]);
                Date eventEndDate = new Date(eventInfo[2]);

                String eventDescription = eventInfo[3];

                ArrayList<String> availableTo = new ArrayList<>();
                String[] tempNames = eventInfo[4].split(" ");

                for (String z : tempNames) {
                    availableTo.add(z);
                }
                //add each event to the list
                Event e = new Event(eventName, eventDate, eventEndDate, eventDescription, availableTo);
                eventsList.add(e);
            }
            System.out.println(eventsList.getAllEvents());
        }
    }
    public void UsersFromServer()
    {
        if (fromServer.hasNextLine()) {
            String line = fromServer.nextLine();
            String[] usrs = line.split(";");
            for (String s : usrs) {
                usersAvailable.add(s);
                //System.out.println(s);
            }
            
        }
    }
    public void outPrintln(String str) {
        //write out to the client
        try {
            outToServer.write(str + "\r\n");
            outToServer.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    
    public final void startupScreen() {
        /**
         * Determines which month to display once application is started.
         */
        /**
         * Storage for month terminations and jLabel alteration.
         */
        monthNames = new String[]{"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};
        monthLength = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        /**
         * Queries GregorianCalendar for the current month and the current year.
         */
        int myYear = thisYear();
        int myMonth = thisMonth();
        /**
         * Stores both results within attributes; extends lifetime, used in
         * button methods.
         */
        currentMonth = myMonth;
        currentYear = myYear;
        /**
         * Alters the jLabels in the main window in accordance with the above.
         */
        labelMonth.setText(monthNames[myMonth - 1]);
        labelYear.setText(myYear + "");
        /**
         * Parsed into a SimpleDateFormat.
         */
        int firstDayofMonth = getFirstDayOfMonth(myYear, myMonth);
        try {
            /**
             * Handles potential offByOne error.
             */
            populateMonth(firstDayofMonth, monthLength[myMonth - 1]);
            
//         DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
//         rightRenderer.setHorizontalAlignment(SwingConstants.NORTH_WEST);
//         tableCalendar.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        } catch (ParseException ex) {
            //Logger.getLogger(MainCalendarWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void previousMonth() {
        /**
         * Method for previousMonthButton; moves back one month || one year.
         */
        if (currentMonth == 1) {
            currentMonth = 12;
            currentYear = currentYear - 1;
            // tableCalendar.repaint();
        } else {
            currentMonth = currentMonth - 1;
            // tableCalendar.repaint();
        }
        int firstDayofMonth = getFirstDayOfMonth(currentYear, currentMonth);
        labelMonth.setText(monthNames[currentMonth - 1]);
        labelYear.setText(currentYear + "");
        try {
            /**
             * Manual repaint of jTable.
             */
            populateMonth(firstDayofMonth, monthLength[currentMonth - 1]);
        } catch (ParseException ex) {
            //Logger.getLogger(MainCalendarWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nextMonth() throws ParseException {
        /**
         * Method for nextMonthButton; moves forward one month || one year.
         */
        if (currentMonth == 12) {
            currentMonth = 1;
            currentYear = currentYear + 1;
            // tableCalendar.repaint();
        } else {
            currentMonth = currentMonth + 1;
            // tableCalendar.repaint();
        }
        int firstDayofMonth = getFirstDayOfMonth(currentYear, currentMonth);
        labelMonth.setText(monthNames[currentMonth - 1]);
        labelYear.setText(currentYear + "");
        /**
         * Manual repaint of jTable.
         */
        populateMonth(firstDayofMonth, monthLength[currentMonth - 1]);
    }

    public int getDay(String str) {
        /**
         * Obtains and counts days.
         */
        /**
         * 'Title' storage; not directly used in jTable.
         */
        String[] days = new String[]{"Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday"};
        int count = 0;
        /**
         * Counter for days @ length attribute.
         */
        for (int i = 0; i < days.length; i++) {
            if (str.equalsIgnoreCase(days[i])) {
                break;
            }
            count = count + 1;
        }
        return count;
    }

    public int getFirstDayOfMonth(int year, int month) {
        /**
         * WYSIWYG method; finds the first of the month.
         */
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, 1);
        Date monthStartDate = new Date(calendar.getTime().getTime());
        /**
         * Formats into daily.
         */
        SimpleDateFormat format = new SimpleDateFormat("EEEE");

        return getDay(format.format(monthStartDate));
    }

    public int thisMonth() {
        /**
         * WYSIWYG method; finds this month based on system time.
         */
        GregorianCalendar calendar = new GregorianCalendar();
        Date monthStartDate = new Date(calendar.getTime().getTime());
        /**
         * Formats into monthly.
         */
        SimpleDateFormat format = new SimpleDateFormat("MM");

        String s = format.format(monthStartDate);
        int month = Integer.valueOf(s);
        return month;
    }

    public int thisYear() {
        /**
         * WYSIWYG method; finds this year based on system time.
         */
        GregorianCalendar calendar = new GregorianCalendar();
        Date monthStartDate = new Date(calendar.getTime().getTime());
        /**
         * Formats into annually.
         */
        SimpleDateFormat format = new SimpleDateFormat("YYYY");

        String s = format.format(monthStartDate);
        int year = Integer.valueOf(s);
        return year;
    }

    public void populateMonth(int startDay, int endDay) throws ParseException {
        /**
         * Repaint supplementary method.
         */
        tableCalendar.repaint();
        int numberOfDays, startOfMonth;
        int endOfMonth = endDay;
        /**
         * Calculates for the event of a leap year; adds one day to February.
         */
        GregorianCalendar calendar = new GregorianCalendar(currentYear, currentMonth - 1, 1);
        if (calendar.isLeapYear(currentYear) && currentMonth == 2) {
            endOfMonth = endOfMonth + 1;
        }
        /**
         * Cycles through supplied cells, clears past data on button press.
         */
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                tableCalendar.setValueAt(null, i, j);
            }
        }
        numberOfDays = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        startOfMonth = calendar.get(GregorianCalendar.DAY_OF_WEEK);
        /**
         * Adds in dates.
         */
        for (int i = 1; i <= numberOfDays; i++) {
            int row = new Integer((i + startOfMonth - 2) / 7);
            int column = ((i + startOfMonth - 2) % 7);
            //this will add a notice to the calendar cell if there is an event there
            try{
                String sdate = currentMonth +"/"+i+"/"+currentYear;
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                Date eventOnDay = format.parse(sdate);
                //System.out.println(eventOnDay);
                //the day has an event
                if (eventsList.DayHasEvent(eventOnDay)){
                    tableCalendar.setValueAt(i+ "   *", row, column);
                }
                //the day doesn't have an event
                else{
                    tableCalendar.setValueAt(i+"", row, column);
                }
            }
            catch (Exception e)
            {
                
            }
            
        }

        /**
         * Cell rendered invocation; determines the week and weekend coloration
         * and current day highlight.
         */
        tableCalendar.setDefaultRenderer(tableCalendar.getColumnClass(0), new tableCalendarRenderer());
        
    }

    class colorChooser extends JFrame {

        private JColorChooser jcc = null;

        public colorChooser() {
            initComponents();
        }

        private void initComponents() {
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLocationRelativeTo(null);
            
            jcc = new JColorChooser();
            jcc.getSelectionModel().addChangeListener(new ColorSelection());
            getContentPane().add(jcc, BorderLayout.PAGE_START);

            this.pack();
        }

        class ColorSelection implements ChangeListener {

            @Override
            public void stateChanged(ChangeEvent e) {
                /** Color editing block. */
                Color color = jcc.getColor();
                labelMonth.setForeground(color);
                labelYear.setForeground(color);
                buttonPrevious.setForeground(color);
                buttonNext.setForeground(color);
                holidayToggle.setForeground(color);
                groupEventsToggle.setForeground(color);
                colorSelectionMenu.setForeground(color);
                addEventMenuItem.setForeground(color);
                removeEventMenuItem.setForeground(color);
                copyEventMenuItem.setForeground(color);
                switchEventMenuItem.setForeground(color);
                seeAllEventsToggle.setForeground(color);
            }
        }
    }

   static class tableCalendarRenderer extends DefaultTableCellRenderer {

        /**
         * Style determining method.
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);

            if (column == 0 || column == 6) {
                /**
                 * Weekend colors.
                 */
                setBackground(new Color(224, 235, 255));
            } else {
                /**
                 * Weekday colors.
                 */
                setBackground(new Color(255, 252, 224));
            }
            
            
//            if (value != null) {
//                if (Integer.parseInt(value.toString()) == realDay && currentMonth
//                        == realMonth && currentYear == realYear) {
//                    setBackground(new Color(220, 220, 225));
//                }
//            }
            setBorder(null);
            setForeground(Color.black);

            return this;
        }
    }

//    CalendarSort modelCalendar = new CalendarSort();
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        panelCalendar = new javax.swing.JPanel();
        scrollCalendar = new javax.swing.JScrollPane();
        tableCalendar = new javax.swing.JTable(modelCalendar);
        panelInput = new javax.swing.JPanel();
        buttonPrevious = new javax.swing.JButton();
        buttonNext = new javax.swing.JButton();
        labelMonth = new javax.swing.JLabel();
        labelYear = new javax.swing.JLabel();
        nuCalendarMenuBar = new javax.swing.JMenuBar();
        calendarMenu = new javax.swing.JMenu();
        holidayToggle = new javax.swing.JCheckBoxMenuItem();
        groupEventsToggle = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        colorSelectionMenu = new javax.swing.JMenuItem();
        eventsMenu = new javax.swing.JMenu();
        addEventMenuItem = new javax.swing.JMenuItem();
        removeEventMenuItem = new javax.swing.JMenuItem();
        copyEventMenuItem = new javax.swing.JMenuItem();
        switchEventMenuItem = new javax.swing.JMenuItem();
        adminMenu = new javax.swing.JMenu();
        seeAllEventsToggle = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        panelCalendar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tableCalendar.setBackground(new java.awt.Color(255, 249, 237));
        tableCalendar.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        tableCalendar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCalendar.setCellSelectionEnabled(true);
        tableCalendar.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tableCalendar.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableCalendar.setShowGrid(true);
        tableCalendar.setSurrendersFocusOnKeystroke(true);
        tableCalendar.getTableHeader().setReorderingAllowed(false);
        tableCalendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableCalendarMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCalendarMouseClicked(evt);
            }
        });
        scrollCalendar.setViewportView(tableCalendar);

        javax.swing.GroupLayout panelCalendarLayout = new javax.swing.GroupLayout(panelCalendar);
        panelCalendar.setLayout(panelCalendarLayout);
        panelCalendarLayout.setHorizontalGroup(
            panelCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCalendarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCalendarLayout.setVerticalGroup(
            panelCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCalendarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelInput.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonPrevious.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        buttonPrevious.setText("<< Prev");
        buttonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPreviousActionPerformed(evt);
            }
        });

        buttonNext.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        buttonNext.setText("Next >>");
        buttonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNextActionPerformed(evt);
            }
        });

        labelMonth.setFont(new java.awt.Font("Braggadocio", 0, 48)); // NOI18N
        labelMonth.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMonth.setText("jLabel1");

        labelYear.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        labelYear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelYear.setText("jLabel2");

        javax.swing.GroupLayout panelInputLayout = new javax.swing.GroupLayout(panelInput);
        panelInput.setLayout(panelInputLayout);
        panelInputLayout.setHorizontalGroup(
            panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInputLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonPrevious)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelMonth, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addComponent(labelYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonNext)
                .addContainerGap())
        );
        panelInputLayout.setVerticalGroup(
            panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonPrevious, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelInputLayout.createSequentialGroup()
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(buttonNext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelInputLayout.createSequentialGroup()
                                .addComponent(labelYear)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelMonth)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        calendarMenu.setText("Calendar");

        holidayToggle.setSelected(true);
        holidayToggle.setText("National Holidays");
        holidayToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                holidayToggleActionPerformed(evt);
            }
        });
        calendarMenu.add(holidayToggle);

        groupEventsToggle.setSelected(true);
        groupEventsToggle.setText("Group Events");
        groupEventsToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupEventsToggleActionPerformed(evt);
            }
        });
        calendarMenu.add(groupEventsToggle);
        calendarMenu.add(jSeparator1);

        colorSelectionMenu.setText("Title Color Selection");
        colorSelectionMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorSelectionMenuActionPerformed(evt);
            }
        });
        calendarMenu.add(colorSelectionMenu);

        nuCalendarMenuBar.add(calendarMenu);

        eventsMenu.setText("Events");

        addEventMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        addEventMenuItem.setText("Add Event");
        addEventMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEventMenuItemActionPerformed(evt);
            }
        });
        eventsMenu.add(addEventMenuItem);

        removeEventMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_MASK));
        removeEventMenuItem.setText("Remove Event");
        removeEventMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeEventMenuItemActionPerformed(evt);
            }
        });
        eventsMenu.add(removeEventMenuItem);

        copyEventMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, java.awt.event.InputEvent.CTRL_MASK));
        copyEventMenuItem.setText("Copy Event");
        copyEventMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyEventMenuItemActionPerformed(evt);
            }
        });
        eventsMenu.add(copyEventMenuItem);

        switchEventMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SLASH, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        switchEventMenuItem.setText("Switch Event");
        switchEventMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchEventMenuItemActionPerformed(evt);
            }
        });
        eventsMenu.add(switchEventMenuItem);

        nuCalendarMenuBar.add(eventsMenu);

        adminMenu.setText("Administrator");

        seeAllEventsToggle.setText("See All Events");
        seeAllEventsToggle.setEnabled(false);
        seeAllEventsToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seeAllEventsToggleActionPerformed(evt);
            }
        });
        adminMenu.add(seeAllEventsToggle);

        nuCalendarMenuBar.add(adminMenu);

        setJMenuBar(nuCalendarMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelInput, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCalendar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Menu activation.
     */
    public void addEvent() {
//        AddEvent add = new AddEvent();
//        add.setVisible(true);
    }

    public void removeEvent() {
//        RemoveEvent remove = new RemoveEvent();
//        remove.setVisible(true);
    }

    public void copyEvent() {
//        CopyEvent copy = new CopyEvent();
//        copy.setVisible(true);
    }

    public void switchEvent() {
//        SwitchEvent switchevt = new SwitchEvent();
//        switchevt.setVisible(true);
    }

    public void eventDisplay() {
//        EventDisplay evtDisplay = new EventDisplay();
//        evtDisplay.setVisible(true);
    }

    private void tableCalendarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCalendarMousePressed
        //Placeholder for future method.
    }//GEN-LAST:event_tableCalendarMousePressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void tableCalendarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCalendarMouseClicked
        /**
         * Placeholder for cell listeners.
         */
        EventDisplay evtDisplay = new EventDisplay();
        int rowIndex = tableCalendar.getSelectedRow();
        int colIndex = tableCalendar.getSelectedColumn();
        int valueInCell = 0;
//        System.out.println(rowIndex + " , " + colIndex);
        
        ArrayList<String> labelsMonths = new ArrayList<>();
        labelsMonths.add(labelMonth.getText());

        ArrayList<String> labelsYears = new ArrayList<>();
        labelsYears.add(labelYear.getText());

        if (tableCalendar.getValueAt(rowIndex, colIndex) != null) {
            String temp = (String)tableCalendar.getValueAt(rowIndex, colIndex);
            if (temp.contains(" ")){
                String[] split = temp.split(" ");
                valueInCell = Integer.parseInt(split[0]);
            }
            else{
                valueInCell = Integer.parseInt(temp);
            }
            //valueInCell = (Integer) tableCalendar.getValueAt(rowIndex, colIndex);
        }

        if (valueInCell != 0) {
            //System.out.println(valueInCell);
//            Date d = new Date(currentYear, currentMonth, valueInCell);
//            Calendar cal = new Calendar(currentYear + 1998, currentMonth, valueInCell);

            //System.out.println(valueInCell);
            //System.out.println(currentYear);
            String sdate = currentMonth + "/" + valueInCell + "/" + currentYear + " " + "00:00";
            Date startDate = null;
            try {
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                startDate = format.parse(sdate);
            } catch (Exception e) {
                System.out.println(e);
            }
            //System.out.println(startDate.toString());
            ArrayList<Event> listEvents = eventsList.FilterForDay(startDate);

            ArrayList<Integer> labelsDays = new ArrayList<>();
            labelsDays.add(valueInCell);

            ArrayList<Event> temp = eventsList.getAllEvents();

            /**
             * Filtering; to be added.
             */
//            EventDisplay evtDisplay = new EventDisplay();
            evtDisplay.rePopulate(listEvents);

            evtDisplay.dateReadoutForMonths(labelsMonths);
            evtDisplay.dateReadoutForYears(labelsYears);
            evtDisplay.dateReadoutForDays(labelsDays);

            evtDisplay.setVisible(true);
        }

        ArrayList<Event> listEvents = new ArrayList<>();
        listEvents = eventsList.getAllEvents();

    }//GEN-LAST:event_tableCalendarMouseClicked
    /**
     * Menu Items.
     */
    private void holidayToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_holidayToggleActionPerformed

    }//GEN-LAST:event_holidayToggleActionPerformed

    private void groupEventsToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupEventsToggleActionPerformed

    }//GEN-LAST:event_groupEventsToggleActionPerformed

    private void addEventMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEventMenuItemActionPerformed
        add.setVisible(true);
    }//GEN-LAST:event_addEventMenuItemActionPerformed

    private void removeEventMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeEventMenuItemActionPerformed
        remove.setVisible(true);
    }//GEN-LAST:event_removeEventMenuItemActionPerformed

    private void copyEventMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyEventMenuItemActionPerformed
        copy.setVisible(true);
    }//GEN-LAST:event_copyEventMenuItemActionPerformed

    private void switchEventMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_switchEventMenuItemActionPerformed
        switchEvt.setVisible(true);
    }//GEN-LAST:event_switchEventMenuItemActionPerformed
    /**
     * Button Items.
     */
    private void buttonPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPreviousActionPerformed
        previousMonth();
    }//GEN-LAST:event_buttonPreviousActionPerformed

    private void buttonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNextActionPerformed
        try {
            nextMonth();
        } catch (ParseException ex) {
            Logger.getLogger(MainCalendarWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonNextActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped

    private void colorSelectionMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorSelectionMenuActionPerformed
        cc.setVisible(true);
    }//GEN-LAST:event_colorSelectionMenuActionPerformed

    private void seeAllEventsToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seeAllEventsToggleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_seeAllEventsToggleActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainCalendarWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainCalendarWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainCalendarWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainCalendarWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

//        MainCalendarWindow app = new MainCalendarWindow();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new MainCalendarWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addEventMenuItem;
    private javax.swing.JMenu adminMenu;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton buttonNext;
    private javax.swing.JButton buttonPrevious;
    private javax.swing.JMenu calendarMenu;
    private javax.swing.JMenuItem colorSelectionMenu;
    private javax.swing.JMenuItem copyEventMenuItem;
    private javax.swing.JMenu eventsMenu;
    private javax.swing.JCheckBoxMenuItem groupEventsToggle;
    private javax.swing.JCheckBoxMenuItem holidayToggle;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel labelMonth;
    private javax.swing.JLabel labelYear;
    private javax.swing.JMenuBar nuCalendarMenuBar;
    private javax.swing.JPanel panelCalendar;
    private javax.swing.JPanel panelInput;
    private javax.swing.JMenuItem removeEventMenuItem;
    private javax.swing.JScrollPane scrollCalendar;
    private javax.swing.JCheckBoxMenuItem seeAllEventsToggle;
    private javax.swing.JMenuItem switchEventMenuItem;
    private javax.swing.JTable tableCalendar;
    // End of variables declaration//GEN-END:variables

    private class ArrowAction extends AbstractAction {

        /**
         * Key bind listener.
         */
        private String command;

        public ArrowAction(String command) {
            this.command = command;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (command.equalsIgnoreCase("Left Arrow")) {
                previousMonth();
            } else {
                if (command.equalsIgnoreCase("Right Arrow")) {
                    try {
                        nextMonth();
                    } catch (ParseException ex) {
                        //Logger.getLogger(MainCalendarWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
