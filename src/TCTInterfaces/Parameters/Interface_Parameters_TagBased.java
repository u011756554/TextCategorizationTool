//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************       

package TCTInterfaces.Parameters;

import TCTParameters.SemiSupervisedLearning.Parameters_TagBased;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class Interface_Parameters_TagBased extends javax.swing.JInternalFrame {

    Parameters_TagBased parameters;
    
    public Interface_Parameters_TagBased(Parameters_TagBased parameters) {
        this.parameters = parameters;
        initComponents();
        ArrayList<Double> betas = parameters.getBetas();
        ArrayList<Double> gammas = parameters.getGammas();

        DefaultListModel listModel = new DefaultListModel();
        for(int item=0;item<betas.size();item++){
            listModel.addElement(betas.get(item));
        }
        lBetas.setModel(listModel);
        
        listModel = new DefaultListModel();
        for(int item=0;item<gammas.size();item++){
            listModel.addElement(gammas.get(item));
        }
        lGammas.setModel(listModel);
        
        tNumMax.setText(parameters.getMaxNumberIterations().toString());
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        bAddBeta = new javax.swing.JButton();
        bRemoveBeta = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lBetas = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tNumMax = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lGammas = new javax.swing.JList();
        bAddGamma = new javax.swing.JButton();
        bRemoveGamma = new javax.swing.JButton();
        bCancelar = new javax.swing.JToggleButton();
        bOK = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TagBased Parameters");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("βs"));

        bAddBeta.setText("Add");
        bAddBeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddBetaActionPerformed(evt);
            }
        });

        bRemoveBeta.setText("Remove");
        bRemoveBeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveBetaActionPerformed(evt);
            }
        });

        lBetas.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "1.0", "10.0", "100.0", "1000.0" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lBetas);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bRemoveBeta, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                    .addComponent(bAddBeta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(bAddBeta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRemoveBeta)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Iterations"));

        jLabel1.setText("Maximum nº:");

        tNumMax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tNumMax.setText("100");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(tNumMax)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tNumMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("γs"));

        lGammas.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "1.0", "10.0", "100.0", "1000.0" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lGammas);

        bAddGamma.setText("Add");
        bAddGamma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddGammaActionPerformed(evt);
            }
        });

        bRemoveGamma.setText("Remove");
        bRemoveGamma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveGammaActionPerformed(evt);
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
                    .addComponent(bAddGamma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bRemoveGamma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(bAddGamma)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRemoveGamma)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bCancelar.setText("Cancel");
        bCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelarActionPerformed(evt);
            }
        });

        bOK.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bOK.setText("OK");
        bOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bOK)
                    .addComponent(bCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bAddBetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddBetaActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "Digite α-β:", "TCT", ent.PLAIN_MESSAGE);
            ListModel model = lBetas.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for (int j = 0; j < model.getSize(); j++) {
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lBetas.setModel(model2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bAddBetaActionPerformed

    private void bRemoveBetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveBetaActionPerformed
        int i = lBetas.getSelectedIndex();
        if (i == -1) {
            return;
        }
        ListModel model = lBetas.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for (int j = 0; j < model.getSize(); j++) {
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lBetas.setModel(model2);
    }//GEN-LAST:event_bRemoveBetaActionPerformed

    private void bAddGammaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddGammaActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "Digite α'-β':", "TCT", ent.PLAIN_MESSAGE);
            ListModel model = lGammas.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for (int j = 0; j < model.getSize(); j++) {
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lGammas.setModel(model2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bAddGammaActionPerformed

    private void bRemoveGammaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveGammaActionPerformed
        int i = lGammas.getSelectedIndex();
        if (i == -1) {
            return;
        }
        ListModel model = lGammas.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for (int j = 0; j < model.getSize(); j++) {
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lGammas.setModel(model2);
    }//GEN-LAST:event_bRemoveGammaActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
        ArrayList<Double> betas = new ArrayList<Double>();
        ListModel listModel = lBetas.getModel();
        for (int item = 0; item < listModel.getSize(); item++) {
            betas.add(Double.parseDouble(listModel.getElementAt(item).toString()));
        }
        parameters.setBetas(betas);

        ArrayList<Double> gammas = new ArrayList<Double>();
        listModel = lGammas.getModel();
        for (int item = 0; item < listModel.getSize(); item++) {
            gammas.add(Double.parseDouble(listModel.getElementAt(item).toString()));
        }
        parameters.setGammas(gammas);
        
        parameters.setMaxNumIterations(Integer.parseInt(tNumMax.getText()));

        this.dispose();
    }//GEN-LAST:event_bOKActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddBeta;
    private javax.swing.JButton bAddGamma;
    private javax.swing.JToggleButton bCancelar;
    private javax.swing.JButton bOK;
    private javax.swing.JButton bRemoveBeta;
    private javax.swing.JButton bRemoveGamma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList lBetas;
    private javax.swing.JList lGammas;
    private javax.swing.JTextField tNumMax;
    // End of variables declaration//GEN-END:variables
}
