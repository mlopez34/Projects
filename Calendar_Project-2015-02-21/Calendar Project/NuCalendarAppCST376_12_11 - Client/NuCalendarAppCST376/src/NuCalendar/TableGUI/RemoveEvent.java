package NuCalendar.TableGUI;

import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author Alec Freeman
 */
public class RemoveEvent extends javax.swing.JFrame {
    private Socket client;
    private OutputStreamWriter outToServer;
    public RemoveEvent(Socket Client, OutputStreamWriter Out) {
        super();
        this.client=Client;
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
        eventDate = new javax.swing.JLabel();
        eventTitleField = new javax.swing.JTextField();
        startMonthField = new javax.swing.JTextField();
        startDayField = new javax.swing.JTextField();
        startYearField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();

        addPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Remove Event"));

        eventTitle.setText("Event Title");

        eventDate.setText("Event Date");

        eventTitleField.setText("Title");
        eventTitleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventTitleFieldActionPerformed(evt);
            }
        });

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

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
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
                        .addComponent(eventDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(startMonthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startDayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startYearField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 90, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(sendButton))
        );
        addPanelLayout.setVerticalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eventTitle)
                    .addComponent(eventTitleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eventDate)
                    .addComponent(startMonthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startDayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startYearField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sendButton))
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

    private void startMonthFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMonthFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startMonthFieldActionPerformed

    private void startDayFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDayFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startDayFieldActionPerformed

    private void startYearFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startYearFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startYearFieldActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        MessageToServer = "";
        MessageToServer = "REMOVE_EVENT, " + eventTitleField.getText();
        System.out.println(MessageToServer);
        String[] msg = MessageToServer.split(", ");
        try{
            for (String s : msg)
            {
                System.out.println(s);
                outPrintln(s);
            }
        }
        catch (Exception exx)
        {
            System.out.println(exx);
        }
        this.setVisible(false);
        
        
    }//GEN-LAST:event_sendButtonActionPerformed
    private String MessageToServer;
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
            java.util.logging.Logger.getLogger(RemoveEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RemoveEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RemoveEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RemoveEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new RemoveEvent().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel eventDate;
    private javax.swing.JLabel eventTitle;
    private javax.swing.JTextField eventTitleField;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField startDayField;
    private javax.swing.JTextField startMonthField;
    private javax.swing.JTextField startYearField;
    // End of variables declaration//GEN-END:variables
}
