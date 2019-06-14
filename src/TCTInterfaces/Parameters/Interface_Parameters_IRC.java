//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Parameters;

import TCTConfigurations.TransductiveLearning.TransductiveConfiguration;
import TCTStructures.AlphaBeta;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class Interface_Parameters_IRC extends javax.swing.JInternalFrame {
    
    TransductiveConfiguration configurationTransdutiva;

    public Interface_Parameters_IRC(TransductiveConfiguration configurationTransdutiva) {
        this.configurationTransdutiva = configurationTransdutiva;
        initComponents();
        ArrayList<AlphaBeta> pondDocs = configurationTransdutiva.getParametersIRC().getPondDocs();
        ArrayList<AlphaBeta> pondTermos = configurationTransdutiva.getParametersIRC().getPondTermos();

        DefaultListModel listModel = new DefaultListModel();
        for(int item=0;item<pondTermos.size();item++){
            listModel.addElement(pondTermos.get(item).getAlpha() + "-" +  pondTermos.get(item).getBeta());
        }
        listaPondTermos.setModel(listModel);
        
        listModel = new DefaultListModel();
        for(int item=0;item<pondDocs.size();item++){
            listModel.addElement(pondDocs.get(item).getAlpha() + "-" +  pondDocs.get(item).getBeta());
        }
        listaPondDocs.setModel(listModel);
        
        tNumMax.setText(configurationTransdutiva.getParametersIRC().getMaxNumberIterations().toString());
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        bIncluirPondTermos = new javax.swing.JButton();
        bExcluirPondTermos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaPondTermos = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaPondDocs = new javax.swing.JList();
        bIncluirPondDocs = new javax.swing.JButton();
        bExcluirPondDocs = new javax.swing.JButton();
        bCancelar = new javax.swing.JToggleButton();
        bOK = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tNumMax = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - IRC Parameters");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pairs [α - β]"));

        bIncluirPondTermos.setText("Add");
        bIncluirPondTermos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIncluirPondTermosActionPerformed(evt);
            }
        });

        bExcluirPondTermos.setText("Remove");
        bExcluirPondTermos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExcluirPondTermosActionPerformed(evt);
            }
        });

        listaPondTermos.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "1 - 9", "2 - 8", "3 - 7", "4 - 6", "5 - 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listaPondTermos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bExcluirPondTermos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bIncluirPondTermos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(bIncluirPondTermos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bExcluirPondTermos)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Pairs [α' - β']"));

        listaPondDocs.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "1 - 9", "2 - 8", "3 - 7", "4 - 6", "5 - 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listaPondDocs);

        bIncluirPondDocs.setText("Add");
        bIncluirPondDocs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIncluirPondDocsActionPerformed(evt);
            }
        });

        bExcluirPondDocs.setText("Remove");
        bExcluirPondDocs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExcluirPondDocsActionPerformed(evt);
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
                    .addComponent(bIncluirPondDocs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bExcluirPondDocs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(bIncluirPondDocs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bExcluirPondDocs)))
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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Iterations"));

        jLabel1.setText("Maximum Number:");

        tNumMax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tNumMax.setText("100");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tNumMax, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCancelarActionPerformed

    private void bIncluirPondTermosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirPondTermosActionPerformed
        JOptionPane ent = new JOptionPane();
        try{
            String item = ent.showInputDialog(this,"α-β:","TCT", ent.PLAIN_MESSAGE);
            ListModel model = listaPondTermos.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for(int j = 0;j<model.getSize();j++){
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            listaPondTermos.setModel(model2);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bIncluirPondTermosActionPerformed

    private void bIncluirPondDocsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirPondDocsActionPerformed
        JOptionPane ent = new JOptionPane();
        try{
            String item = ent.showInputDialog(this,"α'-β':","TCT", ent.PLAIN_MESSAGE);
            ListModel model = listaPondDocs.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for(int j = 0;j<model.getSize();j++){
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            listaPondDocs.setModel(model2);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bIncluirPondDocsActionPerformed

    private void bExcluirPondTermosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluirPondTermosActionPerformed
        int i = listaPondTermos.getSelectedIndex();
        if(i == -1) return;
        ListModel model = listaPondTermos.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for(int j = 0;j<model.getSize();j++){
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        listaPondTermos.setModel(model2);
    }//GEN-LAST:event_bExcluirPondTermosActionPerformed

    private void bExcluirPondDocsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcluirPondDocsActionPerformed
        int i = listaPondDocs.getSelectedIndex();
        if(i == -1) return;
        ListModel model = listaPondDocs.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for(int j = 0;j<model.getSize();j++){
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        listaPondDocs.setModel(model2);
    }//GEN-LAST:event_bExcluirPondDocsActionPerformed

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
        ArrayList<AlphaBeta> pondTermos = new ArrayList<AlphaBeta>();
        ListModel listModel = listaPondTermos.getModel();
        for(int item=0;item<listModel.getSize();item++){
            String[] parameters = listModel.getElementAt(item).toString().split("-");
            pondTermos.add(new AlphaBeta(Double.parseDouble(parameters[0]),Double.parseDouble(parameters[1])));
        }
        configurationTransdutiva.getParametersIRC().setPondTermos(pondTermos);
        
        ArrayList<AlphaBeta> pondDocs = new ArrayList<AlphaBeta>();
        listModel = listaPondDocs.getModel();
        for(int item=0;item<listModel.getSize();item++){
            String[] parameters = listModel.getElementAt(item).toString().split("-");
            pondDocs.add(new AlphaBeta(Double.parseDouble(parameters[0]),Double.parseDouble(parameters[1])));
        }
        configurationTransdutiva.getParametersIRC().setPondDocs(pondDocs);
        
        configurationTransdutiva.getParametersIRC().setMaxNumIterations(Integer.parseInt(tNumMax.getText()));
        
        this.dispose();
    }//GEN-LAST:event_bOKActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton bCancelar;
    private javax.swing.JButton bExcluirPondDocs;
    private javax.swing.JButton bExcluirPondTermos;
    private javax.swing.JButton bIncluirPondDocs;
    private javax.swing.JButton bIncluirPondTermos;
    private javax.swing.JButton bOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList listaPondDocs;
    private javax.swing.JList listaPondTermos;
    private javax.swing.JTextField tNumMax;
    // End of variables declaration//GEN-END:variables
}
