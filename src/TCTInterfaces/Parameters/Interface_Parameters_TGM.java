//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************    

package TCTInterfaces.Parameters;

import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfigurationBase;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class Interface_Parameters_TGM extends javax.swing.JInternalFrame {

    private SupervisedInductiveConfigurationBase configuration;
    
    public Interface_Parameters_TGM(SupervisedInductiveConfigurationBase configuration) {
        this.configuration = configuration;
        initComponents();
        
        DefaultListModel listModel = new DefaultListModel();
        for(int item=0;item<configuration.getParametersTGM().getMinSups().size();item++){
            listModel.addElement(configuration.getParametersTGM().getMinSup(item));
        }
        lSupMin.setModel(listModel);
        
        cUnion.setSelected(configuration.getParametersTGM().getUnionSet());
        cBigger.setSelected(configuration.getParametersTGM().getBiggerSet());
        cSmaller.setSelected(configuration.getParametersTGM().getSmallerSet());
        cIntersection.setSelected(configuration.getParametersTGM().getIntersection());
        
        tDifMediaMinima.setText(configuration.getParametersTGM().getAvgMinDifference().toString());
        tNumMaxIterations.setText(configuration.getParametersTGM().getMaxNumberIterations().toString());
        tDampingFactor.setText(configuration.getParametersTGM().getDampingFactor().toString());
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lSupMin = new javax.swing.JList();
        bAddSupport = new javax.swing.JButton();
        bRemoveSupport = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tNumMaxIterations = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tDifMediaMinima = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tDampingFactor = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        cUnion = new javax.swing.JCheckBox();
        cBigger = new javax.swing.JCheckBox();
        cSmaller = new javax.swing.JCheckBox();
        cIntersection = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        Fechar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - TGM Parameters");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Minimum Support"));

        lSupMin.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0.00", "0.15", "0.30", "0.45" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lSupMin);

        bAddSupport.setText("Add");
        bAddSupport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddSupportActionPerformed(evt);
            }
        });

        bRemoveSupport.setText("Remove");
        bRemoveSupport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveSupportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(bAddSupport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bRemoveSupport))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(bAddSupport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bRemoveSupport)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pagerank Parameters"));

        jLabel1.setText("Maximum Nº of Iterations:");

        tNumMaxIterations.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tNumMaxIterations.setText("1000");

        jLabel2.setText("Minimum Difference");

        tDifMediaMinima.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tDifMediaMinima.setText("0.01");

        jLabel3.setText("Damping Factor:");

        tDampingFactor.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tDampingFactor.setText("0.85");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tDampingFactor)
                    .addComponent(tDifMediaMinima, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                    .addComponent(tNumMaxIterations, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tNumMaxIterations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tDifMediaMinima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tDampingFactor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Correlation"));

        cUnion.setText("Union Set");
        cUnion.setEnabled(false);

        cBigger.setText("Bigger Set");
        cBigger.setEnabled(false);

        cSmaller.setText("Smaller Set");
        cSmaller.setEnabled(false);

        cIntersection.setSelected(true);
        cIntersection.setText("Intersection");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cUnion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cBigger)
                .addGap(41, 41, 41)
                .addComponent(cSmaller)
                .addGap(35, 35, 35)
                .addComponent(cIntersection)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cUnion)
                    .addComponent(cBigger)
                    .addComponent(cSmaller)
                    .addComponent(cIntersection))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Fechar.setText("Cancel");
        Fechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FecharActionPerformed(evt);
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
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(Fechar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bAddSupportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddSupportActionPerformed
        JOptionPane ent = new JOptionPane();
        try{
            Double item = Double.parseDouble(ent.showInputDialog(this,"Minimum support:","TCT", ent.PLAIN_MESSAGE));
            ListModel model = lSupMin.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for(int j = 0;j<model.getSize();j++){
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lSupMin.setModel(model2);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bAddSupportActionPerformed

    private void bRemoveSupportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveSupportActionPerformed
        int i = lSupMin.getSelectedIndex();
        if(i == -1) return;
        ListModel model = lSupMin.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for(int j = 0;j<model.getSize();j++){
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lSupMin.setModel(model2);
    }//GEN-LAST:event_bRemoveSupportActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ArrayList<Double> minSups = new ArrayList<Double>();
        ListModel model = lSupMin.getModel();
        for(int item = 0;item<model.getSize();item++){
            minSups.add((Double)model.getElementAt(item));
        }
        configuration.getParametersTGM().setMinSups(minSups);
        
        configuration.getParametersTGM().setUnionSet(cUnion.isSelected());
        configuration.getParametersTGM().setBiggerSet(cBigger.isSelected());
        configuration.getParametersTGM().setSmallerSet(cSmaller.isSelected());
        configuration.getParametersTGM().setIntersection(cIntersection.isSelected());
        
        configuration.getParametersTGM().setMaxNumIterations(Integer.parseInt(tNumMaxIterations.getText()));
        configuration.getParametersTGM().setDifMediaMinima(Double.parseDouble(tDifMediaMinima.getText()));
        configuration.getParametersTGM().setDampingFactor(Double.parseDouble(tDampingFactor.getText()));
        
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void FecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FecharActionPerformed
        this.dispose();
    }//GEN-LAST:event_FecharActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Fechar;
    private javax.swing.JButton bAddSupport;
    private javax.swing.JButton bRemoveSupport;
    private javax.swing.JCheckBox cBigger;
    private javax.swing.JCheckBox cIntersection;
    private javax.swing.JCheckBox cSmaller;
    private javax.swing.JCheckBox cUnion;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lSupMin;
    private javax.swing.JTextField tDampingFactor;
    private javax.swing.JTextField tDifMediaMinima;
    private javax.swing.JTextField tNumMaxIterations;
    // End of variables declaration//GEN-END:variables
}
