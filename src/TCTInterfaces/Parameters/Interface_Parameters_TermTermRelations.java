//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************       

package TCTInterfaces.Parameters;

import TCTParameters.Parameters_TermNetwork;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class Interface_Parameters_TermTermRelations extends javax.swing.JInternalFrame {

    Parameters_TermNetwork parameters;
    
    public Interface_Parameters_TermTermRelations(Parameters_TermNetwork parameters, String title) {
        this.parameters = parameters;
        initComponents();
        
        this.setTitle(title);
        
        if(parameters.getThresholdNetwork()){
            cThreshold.setSelected(true);
        }else{
            cThreshold.setSelected(false);
        }
        ArrayList<Double> thresholdes = parameters.getThresholds();
        DefaultListModel listModel = new DefaultListModel();
        listModel = new DefaultListModel();
        for(int item=0;item<thresholdes.size();item++){
            listModel.addElement(thresholdes.get(item));
        }
        lThreshold.setModel(listModel);
        
        if(parameters.getNetworkTopK()){
            cTopK.setSelected(true);
        }else{
            cTopK.setSelected(false);
        }
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        cThreshold = new javax.swing.JCheckBox();
        cTopK = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lTopK = new javax.swing.JList();
        bIncluirTopK = new javax.swing.JButton();
        bExcluirTopK = new javax.swing.JButton();
        bOK = new javax.swing.JButton();
        bCancelar = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lThreshold = new javax.swing.JList();
        bIncluirThreshold = new javax.swing.JButton();
        bExcluirThreshold = new javax.swing.JButton();
        oRelative = new javax.swing.JRadioButton();
        oAbsolute = new javax.swing.JRadioButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Term-Term Relations (Kappa)");

        cThreshold.setText("Threshold");

        cTopK.setText("TopK");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ks"));

        lTopK.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "1.0", "10.0", "100.0", "1000.0" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(lTopK);

        bIncluirTopK.setText("Add");
        bIncluirTopK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIncluirTopKActionPerformed(evt);
            }
        });

        bExcluirTopK.setText("Remove");
        bExcluirTopK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExcluirTopKActionPerformed(evt);
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
                    .addComponent(bIncluirTopK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bExcluirTopK, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(bIncluirTopK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bExcluirTopK)))
                .addContainerGap(26, Short.MAX_VALUE))
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Thresholds"));

        lThreshold.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "1.0", "10.0", "100.0", "1000.0" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lThreshold);

        bIncluirThreshold.setText("Add");
        bIncluirThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIncluirThresholdActionPerformed(evt);
            }
        });

        bExcluirThreshold.setText("Remove");
        bExcluirThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExcluirThresholdActionPerformed(evt);
            }
        });

        buttonGroup1.add(oRelative);
        oRelative.setSelected(true);
        oRelative.setText("Relative");

        oAbsolute.setText("Absolute");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bIncluirThreshold, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bExcluirThreshold, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(oRelative)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(oAbsolute)
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oAbsolute, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(oRelative, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(bIncluirThreshold)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bExcluirThreshold)
                        .addGap(13, 13, 13)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cTopK)
                        .addComponent(cThreshold)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(17, 17, 17))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cThreshold)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cTopK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bIncluirTopKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirTopKActionPerformed
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
    }//GEN-LAST:event_bIncluirTopKActionPerformed

    private void bExcluirTopKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluirTopKActionPerformed
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
    }//GEN-LAST:event_bExcluirTopKActionPerformed

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed

        ArrayList<Double> thresholdes = new ArrayList<Double>();
        ListModel listModel = lThreshold.getModel();
        for (int item = 0; item < listModel.getSize(); item++) {
            thresholdes.add(Double.parseDouble(listModel.getElementAt(item).toString()));
        }
        parameters.setThresholds(thresholdes);
        parameters.setNetworkThreshold(cThreshold.isSelected());

        ArrayList<Integer> ks = new ArrayList<Integer>();
        listModel = lTopK.getModel();
        for (int item = 0; item < listModel.getSize(); item++) {
            ks.add(Integer.parseInt(listModel.getElementAt(item).toString()));
        }
        parameters.setKs(ks);
        parameters.setNetworkTopK(cTopK.isSelected());

        if(oRelative.isSelected()){
            parameters.setRelative(true);
        }else{
            parameters.setRelative(false);
        }
        
        this.dispose();
    }//GEN-LAST:event_bOKActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bIncluirThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirThresholdActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "Digite o threshold:", "TCT", ent.PLAIN_MESSAGE);
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
    }//GEN-LAST:event_bIncluirThresholdActionPerformed

    private void bExcluirThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluirThresholdActionPerformed
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
    }//GEN-LAST:event_bExcluirThresholdActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton bCancelar;
    private javax.swing.JButton bExcluirThreshold;
    private javax.swing.JButton bExcluirTopK;
    private javax.swing.JButton bIncluirThreshold;
    private javax.swing.JButton bIncluirTopK;
    private javax.swing.JButton bOK;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cThreshold;
    private javax.swing.JCheckBox cTopK;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList lThreshold;
    private javax.swing.JList lTopK;
    private javax.swing.JRadioButton oAbsolute;
    private javax.swing.JRadioButton oRelative;
    // End of variables declaration//GEN-END:variables
}
