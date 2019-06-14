/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTInterfaces.Parameters;

import TCTParameters.SupervisedOneClass.ParametersOneClass;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

/**
 *
 * @author rafael
 */
public class Interface_Parameters_OneClass_Threshold extends javax.swing.JInternalFrame {

    ParametersOneClass parameters;
    
    public Interface_Parameters_OneClass_Threshold(ParametersOneClass parameters, String algorithm) {
        initComponents();
        
        this.parameters = parameters;
        
        cManual.setSelected(parameters.isManual());
        DefaultListModel model = new DefaultListModel();
        ArrayList<Double> thresholds = parameters.getThresholds();
        for(Double t : thresholds){
            model.addElement(t);
        }
        lThresholds.setModel(model);
        
        cAutomatic.setSelected(parameters.isAutomatic());
        
        this.setTitle(this.getTitle() + " " + algorithm);
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        cManual = new javax.swing.JCheckBox();
        cAutomatic = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lThresholds = new javax.swing.JList();
        bAdd = new javax.swing.JButton();
        bRemove = new javax.swing.JButton();
        bOK = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Thresholds - One Class -");

        cManual.setSelected(true);
        cManual.setText("Manual");

        cAutomatic.setSelected(true);
        cAutomatic.setText("Automatic (Six σ approach)");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("List of Thresholds"));

        lThresholds.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "1.0", "10.0", "100.0", "1000.0" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lThresholds);

        bAdd.setText("Add");
        bAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddActionPerformed(evt);
            }
        });

        bRemove.setText("Remove");
        bRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bRemove, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addComponent(bAdd)
                .addGap(18, 18, 18)
                .addComponent(bRemove)
                .addGap(55, 55, 55))
        );

        bOK.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        bOK.setText("OK");
        bOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOKActionPerformed(evt);
            }
        });

        bCancel.setText("Cancel");
        bCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cManual)
                            .addComponent(cAutomatic))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cManual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cAutomatic)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bOK)
                    .addComponent(bCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "α:", "TCT", ent.PLAIN_MESSAGE);
            ListModel model = lThresholds.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for (int j = 0; j < model.getSize(); j++) {
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lThresholds.setModel(model2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bAddActionPerformed

    private void bRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveActionPerformed
        int i = lThresholds.getSelectedIndex();
        if (i == -1) {
            return;
        }
        ListModel model = lThresholds.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for (int j = 0; j < model.getSize(); j++) {
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lThresholds.setModel(model2);
    }//GEN-LAST:event_bRemoveActionPerformed

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCancelActionPerformed

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
        
        parameters.setManual(cManual.isSelected());
        ArrayList<Double> thresholds = new ArrayList<Double>();
        ListModel model = lThresholds.getModel();
        for(int i=0;i<model.getSize();i++){
            thresholds.add(Double.parseDouble(model.getElementAt(i).toString()));
        }
        parameters.setThresholds(thresholds);
        
        parameters.setAutomatic(cAutomatic.isSelected());
        
        this.dispose();
    }//GEN-LAST:event_bOKActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAdd;
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bOK;
    private javax.swing.JButton bRemove;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cAutomatic;
    private javax.swing.JCheckBox cManual;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lThresholds;
    // End of variables declaration//GEN-END:variables
}