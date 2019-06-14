//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Menus;

import TCTInterfaces.Menus.Interface_Menu_SemiSupervisedInductiveClassification;
import TCTInterfaces.Menus.Interface_Menu_SupervisedInductiveClassification;
import TCTInterfaces.Menus.Interface_Menu_TransductiveClassification;
import TCTInterfaces.Menus.Interface_Menu_Preprocessing;
import TCTInterfaces.Menus.Interface_Menu_Utilities;
import javax.swing.SwingUtilities;

public class Interface_Menu extends javax.swing.JFrame {

    public Interface_Menu() {
        initComponents();
        
        this.setVisible(true);
        
        try {
            System.out.println("Operating System: " + System.getProperties().get("os.name"));
            try{
                javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e){
                if(System.getProperties().get("os.name").toString().contains("Windows")){
                    javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                }else{
                    javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                }
            }

            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
        } catch (Exception e) {
            System.out.println("Error when loading Look and Feel.");
            e.printStackTrace();
        }
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        bPreprocessamento = new javax.swing.JButton();
        bUtilitarios = new javax.swing.JButton();
        bClassificacaoIndutivaSupervisionada = new javax.swing.JButton();
        bClassificacaoIndutivaSemissupervisionada = new javax.swing.JButton();
        bClassificacaoTransdutiva = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GERAL - Menu");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        bPreprocessamento.setText("Preprocessing");
        bPreprocessamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPreprocessamentoActionPerformed(evt);
            }
        });

        bUtilitarios.setText("Utilities");
        bUtilitarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUtilitariosActionPerformed(evt);
            }
        });

        bClassificacaoIndutivaSupervisionada.setText("Supervised Inductive Classification");
        bClassificacaoIndutivaSupervisionada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bClassificacaoIndutivaSupervisionadaActionPerformed(evt);
            }
        });

        bClassificacaoIndutivaSemissupervisionada.setText("Semi-Supervised Inductive Classification");
        bClassificacaoIndutivaSemissupervisionada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bClassificacaoIndutivaSemissupervisionadaActionPerformed(evt);
            }
        });

        bClassificacaoTransdutiva.setText("Transductive Classification");
        bClassificacaoTransdutiva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bClassificacaoTransdutivaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bPreprocessamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bUtilitarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bClassificacaoIndutivaSupervisionada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bClassificacaoIndutivaSemissupervisionada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bClassificacaoTransdutiva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bPreprocessamento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bUtilitarios, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bClassificacaoIndutivaSupervisionada, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bClassificacaoIndutivaSemissupervisionada, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bClassificacaoTransdutiva, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TCTImages/logo.png"))); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bPreprocessamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPreprocessamentoActionPerformed
        new Interface_Menu_Preprocessing();
    }//GEN-LAST:event_bPreprocessamentoActionPerformed

    private void bUtilitariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUtilitariosActionPerformed
        new Interface_Menu_Utilities();
    }//GEN-LAST:event_bUtilitariosActionPerformed

    private void bClassificacaoIndutivaSupervisionadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClassificacaoIndutivaSupervisionadaActionPerformed
        new Interface_Menu_SupervisedInductiveClassification();
    }//GEN-LAST:event_bClassificacaoIndutivaSupervisionadaActionPerformed

    private void bClassificacaoIndutivaSemissupervisionadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClassificacaoIndutivaSemissupervisionadaActionPerformed
        new Interface_Menu_SemiSupervisedInductiveClassification();
    }//GEN-LAST:event_bClassificacaoIndutivaSemissupervisionadaActionPerformed

    private void bClassificacaoTransdutivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClassificacaoTransdutivaActionPerformed
        new Interface_Menu_TransductiveClassification();
    }//GEN-LAST:event_bClassificacaoTransdutivaActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClassificacaoIndutivaSemissupervisionada;
    private javax.swing.JButton bClassificacaoIndutivaSupervisionada;
    private javax.swing.JButton bClassificacaoTransdutiva;
    private javax.swing.JButton bPreprocessamento;
    private javax.swing.JButton bUtilitarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
