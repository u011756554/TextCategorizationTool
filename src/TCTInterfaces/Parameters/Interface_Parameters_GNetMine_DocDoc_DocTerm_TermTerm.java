//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Parameters;

import TCTParameters.Parameters_GNetMine_DocDoc_DocTerm_TermTerm;
import TCTStructures.LambdasGNetMine;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class Interface_Parameters_GNetMine_DocDoc_DocTerm_TermTerm extends javax.swing.JInternalFrame {

    Parameters_GNetMine_DocDoc_DocTerm_TermTerm configuration;
    
    public Interface_Parameters_GNetMine_DocDoc_DocTerm_TermTerm(Parameters_GNetMine_DocDoc_DocTerm_TermTerm configuration) {
        this.configuration = configuration;
        initComponents();
        ArrayList<Double> alphasDocs = configuration.getAlphasDocs();
        ArrayList<LambdasGNetMine> lambdas = configuration.getLambdas();
        
        DefaultListModel listModel = new DefaultListModel();
        for(int item=0;item<alphasDocs.size();item++){
            listModel.addElement(alphasDocs.get(item));
        }
        lAlphas.setModel(listModel);
        
        listModel = new DefaultListModel();
        for(int item=0;item<lambdas.size();item++){
            String itemLista = lambdas.get(item).lambdaDocDoc + ";" + lambdas.get(item).lambdaDocTerm + ";" + lambdas.get(item).lambdaTermTerm;
            listModel.addElement(itemLista);
        }
        lLambdas.setModel(listModel);
        
        tNumMax.setText(configuration.getMaxNumberIterations().toString());
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        bIncluirAlphaDoc = new javax.swing.JButton();
        bExcluirAlphaDoc = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lLambdas = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        bIncluirLambdaDocTermo = new javax.swing.JButton();
        bExcluirLambdaDocTermo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lAlphas = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tNumMax = new javax.swing.JTextField();
        bOK = new javax.swing.JButton();
        bCancelar = new javax.swing.JToggleButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - GNetMine Parameters");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("λ Doc-Doc;Doc-Term;Term-Term"));

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

        lLambdas.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "0.3", "0.5", "0.7", "0.9" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lLambdas);

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("α Docs"));

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

        lAlphas.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.1", "0.3", "0.5", "0.7", "0.9" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lAlphas);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bExcluirLambdaDocTermo, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Iterations"));

        jLabel1.setText("Maximum Number: ");

        tNumMax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tNumMax.setText("1000");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(tNumMax, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCancelar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bOK)
                            .addComponent(bCancelar))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bIncluirAlphaDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirAlphaDocActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "λ (different values of different λs must be separated by \",\"):", "TCT", ent.PLAIN_MESSAGE);
            ListModel model = lLambdas.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for (int j = 0; j < model.getSize(); j++) {
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lLambdas.setModel(model2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bIncluirAlphaDocActionPerformed

    private void bExcluirAlphaDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluirAlphaDocActionPerformed
        int i = lLambdas.getSelectedIndex();
        if (i == -1) {
            return;
        }
        ListModel model = lLambdas.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for (int j = 0; j < model.getSize(); j++) {
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lLambdas.setModel(model2);
    }//GEN-LAST:event_bExcluirAlphaDocActionPerformed

    private void bIncluirLambdaDocTermoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirLambdaDocTermoActionPerformed
        JOptionPane ent = new JOptionPane();
        try {
            String item = ent.showInputDialog(this, "α:", "TCT", ent.PLAIN_MESSAGE);
            ListModel model = lAlphas.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for (int j = 0; j < model.getSize(); j++) {
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lAlphas.setModel(model2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bIncluirLambdaDocTermoActionPerformed

    private void bExcluirLambdaDocTermoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluirLambdaDocTermoActionPerformed
        int i = lAlphas.getSelectedIndex();
        if (i == -1) {
            return;
        }
        ListModel model = lAlphas.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for (int j = 0; j < model.getSize(); j++) {
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lAlphas.setModel(model2);
    }//GEN-LAST:event_bExcluirLambdaDocTermoActionPerformed

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
        ArrayList<Double> alphasDocs = new ArrayList<Double>();
        ListModel listModel = lAlphas.getModel();
        for (int item = 0; item < listModel.getSize(); item++) {
            alphasDocs.add(Double.parseDouble(listModel.getElementAt(item).toString()));
        }
        configuration.setAlphasDocs(alphasDocs);

        ArrayList<LambdasGNetMine> lambdas = new ArrayList<LambdasGNetMine>();
        listModel = lLambdas.getModel();
        for(int item=0;item<listModel.getSize();item++){
            String[] parts = listModel.getElementAt(item).toString().split(";");
            LambdasGNetMine lambdaGM = new LambdasGNetMine();
            lambdaGM.lambdaDocDoc = Double.parseDouble(parts[0]);
            lambdaGM.lambdaDocTerm = Double.parseDouble(parts[1]);
            lambdaGM.lambdaTermTerm = Double.parseDouble(parts[2]);
            lambdas.add(lambdaGM);
        }
        configuration.setLambdas(lambdas);

        configuration.setMaxNumIterations(Integer.parseInt(tNumMax.getText()));

        this.dispose();
    }//GEN-LAST:event_bOKActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCancelarActionPerformed

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
    private javax.swing.JList lAlphas;
    private javax.swing.JList lLambdas;
    private javax.swing.JTextField tNumMax;
    // End of variables declaration//GEN-END:variables
}
