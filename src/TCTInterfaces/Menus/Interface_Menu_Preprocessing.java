//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Menus;

import TCTInterfaces.Preprocessing.Interface_Preprocessing_FeatureSelection;
import TCTInterfaces.Preprocessing.Interface_Preprocessing_InsertID;
import TCTInterfaces.Preprocessing.Interface_Preprocessing_TextRepresentation;

public class Interface_Menu_Preprocessing extends javax.swing.JFrame {

    public Interface_Menu_Preprocessing() {
        initComponents();
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bRepresentacao = new javax.swing.JButton();
        bSelecaoAtributos = new javax.swing.JButton();
        bInserirID = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("TCT - Preprocessing");
        setResizable(false);

        bRepresentacao.setText("Generate Document-Term Matrix");
        bRepresentacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRepresentacaoActionPerformed(evt);
            }
        });

        bSelecaoAtributos.setText("Select Features");
        bSelecaoAtributos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSelecaoAtributosActionPerformed(evt);
            }
        });

        bInserirID.setText("Insert ID in Documet-Term Matrix");
        bInserirID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInserirIDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bInserirID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bRepresentacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bSelecaoAtributos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bRepresentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bSelecaoAtributos, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bInserirID, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bRepresentacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRepresentacaoActionPerformed
        new Interface_Preprocessing_TextRepresentation();
    }//GEN-LAST:event_bRepresentacaoActionPerformed

    private void bSelecaoAtributosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSelecaoAtributosActionPerformed
        new Interface_Preprocessing_FeatureSelection();
    }//GEN-LAST:event_bSelecaoAtributosActionPerformed

    private void bInserirIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInserirIDActionPerformed
        new Interface_Preprocessing_InsertID();
    }//GEN-LAST:event_bInserirIDActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bInserirID;
    private javax.swing.JButton bRepresentacao;
    private javax.swing.JButton bSelecaoAtributos;
    // End of variables declaration//GEN-END:variables
}
