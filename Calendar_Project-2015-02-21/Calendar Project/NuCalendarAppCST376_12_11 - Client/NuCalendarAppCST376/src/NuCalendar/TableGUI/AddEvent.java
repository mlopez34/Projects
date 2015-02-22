package NuCalendar.TableGUI;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Alec Freeman
 */

public class AddEvent extends javax.swing.JFrame {
        
    
    private Socket client;
    private OutputStreamWriter outToServer;
    private ArrayList<String> Users;
    public AddEvent(Socket Client, OutputStreamWriter Out, ArrayList<String> users) {
        super();
        this.Users = users;
        this.client = Client;
        this.outToServer = Out;
        initComponents();
    }
    public final void outPrintln(String str) {
        //write out to the client
        try{
            outToServer.write(str + "\r\n");
            outToServer.flush();
            //System.out.println("wrote " + str);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        addPanel = new javax.swing.JPanel();
        eventTitle = new javax.swing.JLabel();
        eventDescription = new javax.swing.JLabel();
        eventDate = new javax.swing.JLabel();
        eventTime = new javax.swing.JLabel();
        eventTitleField = new javax.swing.JTextField();
        eventDescriptionField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        startMonthField = new javax.swing.JTextField();
        startDayField = new javax.swing.JTextField();
        startYearField = new javax.swing.JTextField();
        endMonthField = new javax.swing.JTextField();
        endDayField = new javax.swing.JTextField();
        endYearField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        startHourField = new javax.swing.JTextField();
        startMinuteField = new javax.swing.JTextField();
        endHourField = new javax.swing.JTextField();
        endMinuteField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        clearInputButton = new javax.swing.JButton();

        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        addPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Event"));

        eventTitle.setText("Event Title");

        eventDescription.setText("Event Description");

        eventDate.setText("Event Date");

        eventTime.setText("Event Time");

        eventTitleField.setText("Title");
        eventTitleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventTitleFieldActionPerformed(evt);
            }
        });

        eventDescriptionField.setText("Description");
        eventDescriptionField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventDescriptionFieldActionPerformed(evt);
            }
        });

        jLabel7.setText("to");

        startMonthField.setText("Month");
        startMonthField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMonthFieldActionPerformed(evt);
            }
        });

        startDayField.setText("Day");
        startDayField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDayFieldActionPerformed(evt);
            }
        });

        startYearField.setText("Year");
        startYearField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startYearFieldActionPerformed(evt);
            }
        });

        endMonthField.setText("Month");
        endMonthField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endMonthFieldActionPerformed(evt);
            }
        });

        endDayField.setText("Day");
        endDayField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        endDayField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endDayFieldActionPerformed(evt);
            }
        });

        endYearField.setText("Year");
        endYearField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endYearFieldActionPerformed(evt);
            }
        });

        jLabel6.setText("to");

        startHourField.setText("Hour");
        startHourField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startHourFieldActionPerformed(evt);
            }
        });

        startMinuteField.setText("Minute");
        startMinuteField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMinuteFieldActionPerformed(evt);
            }
        });

        endHourField.setText("Hour");
        endHourField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endHourFieldActionPerformed(evt);
            }
        });

        endMinuteField.setText("Minute");
        endMinuteField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endMinuteFieldActionPerformed(evt);
            }
        });

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        clearInputButton.setText("Clear Input");
        clearInputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearInputButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addPanelLayout = new javax.swing.GroupLayout(addPanel);
        addPanel.setLayout(addPanelLayout);
        addPanelLayout.setHorizontalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addComponent(eventTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eventTitleField))
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addComponent(eventDescription)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eventDescriptionField))
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addComponent(eventTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(startHourField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startMinuteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endHourField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endMinuteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addComponent(eventDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(startMonthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startDayField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startYearField, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endMonthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endDayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endYearField, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanelLayout.createSequentialGroup()
                .addComponent(clearInputButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sendButton))
        );
        addPanelLayout.setVerticalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eventTitle)
                    .addComponent(eventTitleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eventDescription)
                    .addComponent(eventDescriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eventDate)
                    .addComponent(startMonthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startDayField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startYearField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endMonthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endDayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endYearField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(endHourField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(endMinuteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(eventTime)
                        .addComponent(startHourField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(startMinuteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendButton)
                    .addComponent(clearInputButton)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void eventTitleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventTitleFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eventTitleFieldActionPerformed

    private void eventDescriptionFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventDescriptionFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eventDescriptionFieldActionPerformed

    private void startMonthFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMonthFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startMonthFieldActionPerformed

    private void startDayFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDayFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startDayFieldActionPerformed

    private void startYearFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startYearFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startYearFieldActionPerformed

    private void endMonthFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endMonthFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endMonthFieldActionPerformed

    private void endDayFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endDayFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endDayFieldActionPerformed

    private void endYearFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endYearFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endYearFieldActionPerformed

    private void startHourFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startHourFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startHourFieldActionPerformed

    private void startMinuteFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMinuteFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startMinuteFieldActionPerformed

    private void endHourFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endHourFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endHourFieldActionPerformed

    private void endMinuteFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endMinuteFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endMinuteFieldActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        // TODO add your handling code here:
        //get all the users (public event)
        String allusrs = "";
        for (String s : Users)
        {
            allusrs = allusrs + " "+ s;
        }
        MessageToServer = "";
        String startD = startMonthField.getText()+"/"+startDayField.getText()+"/"+startYearField.getText()+" "+startHourField.getText()+":"+startMinuteField.getText();
        String endD = endMonthField.getText()+"/"+endDayField.getText()+"/"+endYearField.getText()+" "+endHourField.getText()+":"+endMinuteField.getText();
        
        MessageToServer = "ADD_EVENT, " +eventTitleField.getText()+", "+startD+", "+endD+", "+eventDescriptionField.getText()+", "+allusrs;
        System.out.println(MessageToServer);
        String[] msg = MessageToServer.split(", ");
        try{
            for (String s : msg)
            {
                System.out.println(s);
                outPrintln(s);
            }
            //out.write((message + "\r\n").getBytes());
            
        }
        
        catch(Exception e)
        {
            System.out.println(e);
        }
        this.setVisible(false);
    }//GEN-LAST:event_sendButtonActionPerformed

    private void clearInputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearInputButtonActionPerformed
        endDayField.setText(null);
        endHourField.setText(null);
        endMinuteField.setText(null);
        endYearField.setText(null);
        endMonthField.setText(null);
        eventDescriptionField.setText(null);
        eventTitleField.setText(null);
        startDayField.setText(null);
        startHourField.setText(null);
        startMinuteField.setText(null);
        startYearField.setText(null);
        startMonthField.setText(null);
    }//GEN-LAST:event_clearInputButtonActionPerformed
    private String MessageToServer = "";
    
    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(AddEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new AddEvent().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton clearInputButton;
    private javax.swing.JTextField endDayField;
    private javax.swing.JTextField endHourField;
    private javax.swing.JTextField endMinuteField;
    private javax.swing.JTextField endMonthField;
    private javax.swing.JTextField endYearField;
    private javax.swing.JLabel eventDate;
    private javax.swing.JLabel eventDescription;
    private javax.swing.JTextField eventDescriptionField;
    private javax.swing.JLabel eventTime;
    private javax.swing.JLabel eventTitle;
    private javax.swing.JTextField eventTitleField;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField startDayField;
    private javax.swing.JTextField startHourField;
    private javax.swing.JTextField startMinuteField;
    private javax.swing.JTextField startMonthField;
    private javax.swing.JTextField startYearField;
    // End of variables declaration//GEN-END:variables
}
