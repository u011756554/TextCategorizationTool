//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Parameters;

import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndTermTermRelations;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;


public class Interface_Parameters_GNetMine_DocTerm_TermTerm extends javax.swing.JInternalFrame {

    TransductiveConfiguration_DocTermAndTermTermRelations configurationTransdutiva;
    
    public Interface_Parameters_GNetMine_DocTerm_TermTerm(TransductiveConfiguration_DocTermAndTermTermRelations configurationTransdutiva) {
        this.configurationTransdutiva = configurationTransdutiva;
        initComponents();
        ArrayList<Double> alphasDocs = configurationTransdutiva.getParameters_GNetMine().getAlphasDocs();
        ArrayList<Double> lambdasDocTermo = configurationTransdutiva.getParameters_GNetMine().getLambdasDocTermo();
        
        DefaultListModel listModel = new DefaultListModel();
        for(int item=0;item<alphasDocs.size();item++){
            listModel.addElement(alphasDocs.get(item));
        }
        lAlphasDocs.setModel(listModel);
        
        listModel = new DefaultListModel();
        for(int item=0;item<lambdasDocTermo.size();item++){
            listModel.addElement(lambdasDocTermo.get(item));
        }
        lLambdasDocTermo.setModel(listModel);
        
        tNumMax.setText(configurationTransdutiva.getParameters_GNetMine().getMaxNumberIterations().toString());
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bOK = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tNumMax = new javax.swing.JTextField();
        bCancelar = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        bIncluirAlphaDoc = new javax.swing.JButton();
        bExcluirAlphaDoc = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lAlphasDocs = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        bIncluirLambdaDocTermo = new javax.swing.JButton();
        bExcluirLambdaDocTermo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lLambdasDocTermo = new javax.swing.JList();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - GNetMine Parameters");

        bOK.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bOK.setText("OK");
        bOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOKActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Iterations"));

        jLabel1.setText("Maximum Number:");

        tNumMax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tNumMax.setText("1000");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tNumMax, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tNumMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bCancelar.setText("Cancel");
        bCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelarActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("α Labeled Documents"));

        bIncluirAlphaDoc.setText("Add");
        bIncluirAlphaDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIncluirAlphaDocActionPerformed(evt);
            }
        });

        bExcluirAlphaDoc.setText("Remove");
        bExcluirAlphaDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExcluirAlphaDocActionPerformed(evt);
            }
        });

        lAlphasDocs.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "0.3", "0.5", "0.7", "0.9" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lAlphasDocs);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bExcluirAlphaDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bIncluirAlphaDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(bIncluirAlphaDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bExcluirAlphaDoc)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("λ Doc-Term Relations"));

        bIncluirLambdaDocTermo.setText("Add");
        bIncluirLambdaDocTermo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIncluirLambdaDocTermoActionPerformed(evt);
            }
        });

        bExcluirLambdaDocTermo.setText("Remove");
        bExcluirLambdaDocTermo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExcluirLambdaDocTermoActionPerformed(evt);
            }
        });

        lLambdasDocTermo.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "0.3", "0.5", "0.7", "0.9" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lLambdasDocTermo);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bExcluirLambdaDocTermo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bIncluirLambdaDocTermo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(bIncluirLambdaDocTermo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bExcluirLambdaDocTermo)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bOK)
                            .addComponent(bCancelar))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
        ArrayList<Double> alphasDocs = new ArrayList<Double>();
        ListModel listModel = lAlphasDocs.getModel();
        for (int item = 0; item < listModel.getSize(); item++) {
            alphasDocs.add(Double.parseDouble(listModel.getElementAt(item).toString()));
        }
        configurationTransdutiva.getParameters_GNetMine().setAlphasDocs(alphasDocs);
        
        ArrayList<Double> lambdasDocTermo = new ArrayList<Double>();
        listModel = lLambdasDocTermo.getModel();
        for(int item=0;item<listModel.getSize();item++){
            lambdasDocTermo.add(Double.parseDouble(listModel.getElementAt(item).toString()));
        }
        configurationTransdutiva.getParameters_GNetMine().setLambdasDocTermo(lambdasDocTermo);
        
        configurationTransdutiva.getParameters_GNetMine().setMaxNumIterations(Integer.parseInt(tNumMax.getText()));

        this.dispose();
    }//GEN-LAST:event_bOKActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bIncluirAlphaDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirAlphaDocActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "α:", "TCT", ent.PLAIN_MESSAGE);
            ListModel model = lAlphasDocs.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for (int j = 0; j < model.getSize(); j++) {
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lAlphasDocs.setModel(model2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bIncluirAlphaDocActionPerformed

    private void bExcluirAlphaDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluirAlphaDocActionPerformed
        int i = lAlphasDocs.getSelectedIndex();
        if (i == -1) {
            return;
        }
        ListModel model = lAlphasDocs.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for (int j = 0; j < model.getSize(); j++) {
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lAlphasDocs.setModel(model2);
    }//GEN-LAST:event_bExcluirAlphaDocActionPerformed

    private void bIncluirLambdaDocTermoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirLambdaDocTermoActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "λ:", "TCT", ent.PLAIN_MESSAGE);
            ListModel model = lLambdasDocTermo.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for (int j = 0; j < model.getSize(); j++) {
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lLambdasDocTermo.setModel(model2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bIncluirLambdaDocTermoActionPerformed

    private void bExcluirLambdaDocTermoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluirLambdaDocTermoActionPerformed
        int i = lLambdasDocTermo.getSelectedIndex();
        if (i == -1) {
            return;
        }
        ListModel model = lLambdasDocTermo.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for (int j = 0; j < model.getSize(); j++) {
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lLambdasDocTermo.setModel(model2);
    }//GEN-LAST:event_bExcluirLambdaDocTermoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton bCancelar;
    private javax.swing.JButton bExcluirAlphaDoc;
    private javax.swing.JButton bExcluirLambdaDocTermo;
    private javax.swing.JButton bIncluirAlphaDoc;
    private javax.swing.JButton bIncluirLambdaDocTermo;
    private javax.swing.JButton bOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList lAlphasDocs;
    private javax.swing.JList lLambdasDocTermo;
    private javax.swing.JTextField tNumMax;
    // End of variables declaration//GEN-END:variables
}
