//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Parameters;

import TCTParameters.Parameters_DocumentNetwork_Exp;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class Interface_Parameters_DocDocRelations_Exp extends javax.swing.JInternalFrame {

    Parameters_DocumentNetwork_Exp parameters;
    
    public Interface_Parameters_DocDocRelations_Exp(Parameters_DocumentNetwork_Exp parameters) {
        this.parameters = parameters;
        initComponents();
        
        ArrayList<Double> sigmas = parameters.getSigmas();
        DefaultListModel listModel2 = new DefaultListModel();
        listModel2 = new DefaultListModel();
        for(int item=0;item<sigmas.size();item++){
            listModel2.addElement(sigmas.get(item));
        }
        lThreshold.setModel(listModel2);
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lThreshold = new javax.swing.JList();
        bAddThreshold = new javax.swing.JButton();
        bRemoveThreshold = new javax.swing.JButton();
        bOK = new javax.swing.JButton();
        bCancelar = new javax.swing.JToggleButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Parameters Doc-Doc Relatins (Exp)");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Thresholds"));

        lThreshold.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "1.0", "10.0", "100.0", "1000.0" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lThreshold);

        bAddThreshold.setText("Add");
        bAddThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddThresholdActionPerformed(evt);
            }
        });

        bRemoveThreshold.setText("Remove");
        bRemoveThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveThresholdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bAddThreshold, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bRemoveThreshold, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(bAddThreshold)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRemoveThreshold)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bOK.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bOK.setText("OK");
        bOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOKActionPerformed(evt);
            }
        });

        bCancelar.setText("Cancel");
        bCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCancelar))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bOK)
                    .addComponent(bCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bAddThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddThresholdActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "Threshold:", "TCT", ent.PLAIN_MESSAGE);
            ListModel model = lThreshold.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for (int j = 0; j < model.getSize(); j++) {
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lThreshold.setModel(model2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bAddThresholdActionPerformed

    private void bRemoveThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveThresholdActionPerformed
        int i = lThreshold.getSelectedIndex();
        if (i == -1) {
            return;
        }
        ListModel model = lThreshold.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for (int j = 0; j < model.getSize(); j++) {
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lThreshold.setModel(model2);
    }//GEN-LAST:event_bRemoveThresholdActionPerformed

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed

        ArrayList<Double> sigmas = new ArrayList<Double>();
        ListModel listModel = lThreshold.getModel();
        for (int item = 0; item < listModel.getSize(); item++) {
            sigmas.add(Double.parseDouble(listModel.getElementAt(item).toString()));
        }
        parameters.setSigma(sigmas);

        this.dispose();
    }//GEN-LAST:event_bOKActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddThreshold;
    private javax.swing.JToggleButton bCancelar;
    private javax.swing.JButton bOK;
    private javax.swing.JButton bRemoveThreshold;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList lThreshold;
    // End of variables declaration//GEN-END:variables
}
