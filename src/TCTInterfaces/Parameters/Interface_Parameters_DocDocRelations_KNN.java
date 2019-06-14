//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Parameters;

import TCTParameters.Parameters_DocumentNetwork_Knn;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class Interface_Parameters_DocDocRelations_KNN extends javax.swing.JInternalFrame {

    Parameters_DocumentNetwork_Knn parameters;
    
    public Interface_Parameters_DocDocRelations_KNN(Parameters_DocumentNetwork_Knn parameters) {
        this.parameters = parameters;
        initComponents();
        
        ArrayList<Integer> ks = parameters.getKs();
        DefaultListModel listModel2 = new DefaultListModel();
        listModel2 = new DefaultListModel();
        for(int item=0;item<ks.size();item++){
            listModel2.addElement(ks.get(item));
        }
        lTopK.setModel(listModel2);
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lTopK = new javax.swing.JList();
        bAddTopK = new javax.swing.JButton();
        bRemoveTopK = new javax.swing.JButton();
        bOK = new javax.swing.JButton();
        bCancelar = new javax.swing.JToggleButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Doc-Doc Relatinos Parameters (Exp)");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ks"));

        lTopK.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "1.0", "10.0", "100.0", "1000.0" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(lTopK);

        bAddTopK.setText("Add");
        bAddTopK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddTopKActionPerformed(evt);
            }
        });

        bRemoveTopK.setText("Remove");
        bRemoveTopK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveTopKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bAddTopK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bRemoveTopK, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(bAddTopK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRemoveTopK)))
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
                        .addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bOK)
                    .addComponent(bCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bAddTopKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddTopKActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "K:", "TCT", ent.PLAIN_MESSAGE);
            ListModel model = lTopK.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for (int j = 0; j < model.getSize(); j++) {
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lTopK.setModel(model2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bAddTopKActionPerformed

    private void bRemoveTopKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveTopKActionPerformed
        int i = lTopK.getSelectedIndex();
        if (i == -1) {
            return;
        }
        ListModel model = lTopK.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for (int j = 0; j < model.getSize(); j++) {
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lTopK.setModel(model2);
    }//GEN-LAST:event_bRemoveTopKActionPerformed

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed

        ArrayList<Integer> ks = new ArrayList<Integer>();
        ListModel listModel = lTopK.getModel();
        for (int item = 0; item < listModel.getSize(); item++) {
            ks.add(Integer.parseInt(listModel.getElementAt(item).toString()));
        }
        parameters.setK(ks);

        this.dispose();
    }//GEN-LAST:event_bOKActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddTopK;
    private javax.swing.JToggleButton bCancelar;
    private javax.swing.JButton bOK;
    private javax.swing.JButton bRemoveTopK;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList lTopK;
    // End of variables declaration//GEN-END:variables
}
