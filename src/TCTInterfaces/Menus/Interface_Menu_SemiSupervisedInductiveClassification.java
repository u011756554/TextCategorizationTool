//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Menus;

import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration;
import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration_DocTermAndTermTermRelations;
import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration_OnlyLabeledExamples;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_SelfTraining;
import TCTInterfaces.Classification.Interface_SemiSupervisedInductiveClassification_DocTerm_TermTerm;
import TCTInterfaces.Classification.Interface_SemiSupervisedInductiveClassification_OnlyLabeledExamples;
import TCTInterfaces.Classification.Interface_SemiSupervisedInductiveClassification_SelfTraining;
import TCTInterfaces.Classification.Interface_SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc;

public class Interface_Menu_SemiSupervisedInductiveClassification extends javax.swing.JFrame {

    SemiSupervisedInductiveConfiguration configurationIndutivoSemissupervisionado;
    SemiSupervisedInductiveConfiguration_OnlyLabeledExamples configurationIndutivoSemissupervisionadoOnlyLabeledExamples;
    SemiSupervisedInductiveConfiguration_DocTermAndTermTermRelations configurationIndutivo_DocTerm_TermTerm;
    TransductiveConfiguration_SelfTraining configurationIndutivo_SelfTraining;
    
    
    public Interface_Menu_SemiSupervisedInductiveClassification() {
        
        initComponents();
        
        this.setVisible(true);
    }

     @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bMEVouNetworkDT = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("TCT - Semi-Supervised Inductive Classification");
        setResizable(false);

        bMEVouNetworkDT.setText("<html><center>Vector Space Model, Document<br>or  Bipartite Networks</center></html>");
        bMEVouNetworkDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMEVouNetworkDTActionPerformed(evt);
            }
        });

        jButton1.setText("Supervised Inductive Classification");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("<html><center>Term-Term or <br>Doc-Term + Term-Term Relations</center></html> ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Self-Training");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bMEVouNetworkDT)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(jButton2)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bMEVouNetworkDT, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bMEVouNetworkDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMEVouNetworkDTActionPerformed
        new Interface_SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc(configurationIndutivoSemissupervisionado);
    }//GEN-LAST:event_bMEVouNetworkDTActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new Interface_SemiSupervisedInductiveClassification_OnlyLabeledExamples(configurationIndutivoSemissupervisionadoOnlyLabeledExamples);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new Interface_SemiSupervisedInductiveClassification_DocTerm_TermTerm(configurationIndutivo_DocTerm_TermTerm);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new Interface_SemiSupervisedInductiveClassification_SelfTraining(configurationIndutivo_SelfTraining);
    }//GEN-LAST:event_jButton3ActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bMEVouNetworkDT;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    // End of variables declaration//GEN-END:variables
}
