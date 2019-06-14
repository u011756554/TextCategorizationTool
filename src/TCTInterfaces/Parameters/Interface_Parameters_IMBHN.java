//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Parameters;

import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class Interface_Parameters_IMBHN extends javax.swing.JInternalFrame {

    Parameters_IMHN parameters;
    
    public Interface_Parameters_IMBHN(Parameters_IMHN parameters) {
        this.parameters = parameters;
        initComponents();
        
        DefaultListModel listModel = new DefaultListModel();
        for(int item=0;item<parameters.getErrorCorrectionRates().size();item++){
            listModel.addElement(parameters.getErrorCorrectionRate(item));
        }
        lErrorCorrectionRate.setModel(listModel);
        
        listModel = new DefaultListModel();
        for(int item=0;item<parameters.getErrors().size();item++){
            listModel.addElement(parameters.getError(item));
        }
        lError.setModel(listModel);

        tMaxEpocas.setText(parameters.getMaxNumberIterations().toString());
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tMaxEpocas = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lError = new javax.swing.JList();
        bIncluir1 = new javax.swing.JButton();
        bExcluir1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lErrorCorrectionRate = new javax.swing.JList();
        bIncluir = new javax.swing.JButton();
        bExcluir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - IMHN Parameters");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Iterations"));

        jLabel1.setText("Maximum Number:");

        tMaxEpocas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tMaxEpocas.setText("100");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(tMaxEpocas, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tMaxEpocas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Minimum Mean Squared Error"));

        lError.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.01", "0.005" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lError);

        bIncluir1.setText("Add");
        bIncluir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIncluir1ActionPerformed(evt);
            }
        });

        bExcluir1.setText("Remove");
        bExcluir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExcluir1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bIncluir1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bExcluir1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(bIncluir1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bExcluir1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jButton3.setText("OK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Error Correction Rate"));

        lErrorCorrectionRate.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lErrorCorrectionRate);

        bIncluir.setText("Add");
        bIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIncluirActionPerformed(evt);
            }
        });

        bExcluir.setText("Remove");
        bExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(bIncluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bExcluir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bIncluir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluir1ActionPerformed
        JOptionPane ent = new JOptionPane();
        try{
            Double item = Double.parseDouble(ent.showInputDialog(this,"Error correction rate:","TCT", ent.PLAIN_MESSAGE));
            ListModel model = lError.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for(int j = 0;j<model.getSize();j++){
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lError.setModel(model2);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bIncluir1ActionPerformed

    private void bExcluir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluir1ActionPerformed
        int i = lError.getSelectedIndex();
        if(i == -1) return;
        ListModel model = lError.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for(int j = 0;j<model.getSize();j++){
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lError.setModel(model2);
    }//GEN-LAST:event_bExcluir1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ArrayList<Double> tas = new ArrayList<Double>();
        ListModel model = lErrorCorrectionRate.getModel();
        for(int item = 0;item<model.getSize();item++){
            tas.add((Double)model.getElementAt(item));
        }
        model = lError.getModel();
        ArrayList<Double> errors = new ArrayList<Double>();
        for(int item = 0;item<model.getSize();item++){
            errors.add((Double)model.getElementAt(item));
        }

        parameters.setTaxaserrorCorrectionRate(tas);
        parameters.setErrors(errors);
        parameters.setMaxNumIterations(Integer.parseInt(tMaxEpocas.getText()));

        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void bIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirActionPerformed
        JOptionPane ent = new JOptionPane();
        try{
            Double item = Double.parseDouble(ent.showInputDialog(this,"Error correction rate:","TCT", ent.PLAIN_MESSAGE));
            ListModel model = lErrorCorrectionRate.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for(int j = 0;j<model.getSize();j++){
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lErrorCorrectionRate.setModel(model2);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bIncluirActionPerformed

    private void bExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluirActionPerformed
        int i = lErrorCorrectionRate.getSelectedIndex();
        if(i == -1) return;
        ListModel model = lErrorCorrectionRate.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for(int j = 0;j<model.getSize();j++){
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lErrorCorrectionRate.setModel(model2);
    }//GEN-LAST:event_bExcluirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bExcluir;
    private javax.swing.JButton bExcluir1;
    private javax.swing.JButton bIncluir;
    private javax.swing.JButton bIncluir1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList lError;
    private javax.swing.JList lErrorCorrectionRate;
    private javax.swing.JTextField tMaxEpocas;
    // End of variables declaration//GEN-END:variables
}
