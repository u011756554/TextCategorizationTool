//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Parameters;

import TCTParameters.SemiSupervisedLearning.Parameters_KAG;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class Interface_Parameters_KAG extends javax.swing.JInternalFrame {

    private Parameters_KAG parameters;
    
    public Interface_Parameters_KAG(Parameters_KAG parameters) {
        this.parameters = parameters;
        initComponents();
        
        DefaultListModel listModel = new DefaultListModel();
        for(int item=0;item<parameters.getNeighbors().size();item++){
            listModel.addElement(parameters.getNeighbor(item));
        }
        lVizinhos.setModel(listModel);
        if(parameters.getKAG() == true){
            cKAG.setSelected(true);
        }else{
            cKAG.setSelected(false);
        }
        if(parameters.getKAOG() == true){
            cKAOG.setSelected(true);
        }else{
            cKAOG.setSelected(false);
        }
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pVizinhos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lVizinhos = new javax.swing.JList();
        bIncluir = new javax.swing.JButton();
        bRemover = new javax.swing.JButton();
        cKAG = new javax.swing.JCheckBox();
        cKAOG = new javax.swing.JCheckBox();
        bOK = new javax.swing.JButton();
        bCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - KAG  Parameters");

        pVizinhos.setBorder(javax.swing.BorderFactory.createTitledBorder("Ks"));

        lVizinhos.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "1", "3", "5", "7", "9", "11", "13", "15", "17", "19", "21", "25", "29", "35", "41", "49", "57", "73", "89" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lVizinhos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(lVizinhos);

        bIncluir.setText("Add");
        bIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIncluirActionPerformed(evt);
            }
        });

        bRemover.setText("Remove");
        bRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pVizinhosLayout = new javax.swing.GroupLayout(pVizinhos);
        pVizinhos.setLayout(pVizinhosLayout);
        pVizinhosLayout.setHorizontalGroup(
            pVizinhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVizinhosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pVizinhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bRemover, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pVizinhosLayout.setVerticalGroup(
            pVizinhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVizinhosLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pVizinhosLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(bIncluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bRemover)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cKAG.setText("k-Associated Graph");

        cKAOG.setText("k-Optimial Associated Graph");

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cKAG)
                            .addComponent(cKAOG))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pVizinhos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cKAG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pVizinhos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cKAOG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bCancelar)
                    .addComponent(bOK))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIncluirActionPerformed
        JOptionPane ent = new JOptionPane();
        try{
            Integer item = Integer.parseInt(ent.showInputDialog(this,"K:","TCT", ent.PLAIN_MESSAGE));
            ListModel model = lVizinhos.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for(int j = 0;j<model.getSize();j++){
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lVizinhos.setModel(model2);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Incorrect value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_bIncluirActionPerformed

    private void bRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoverActionPerformed
        int i = lVizinhos.getSelectedIndex();
        if(i == -1) return;
        ListModel model = lVizinhos.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for(int j = 0;j<model.getSize();j++){
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lVizinhos.setModel(model2);
    }//GEN-LAST:event_bRemoverActionPerformed

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed

        ArrayList<Integer> neighbors = new ArrayList<Integer>();
        ListModel model = lVizinhos.getModel();
        for(int item = 0;item<model.getSize();item++){
            neighbors.add((Integer)model.getElementAt(item));
        }
        parameters.setNeighbors(neighbors);
        if(cKAG.isSelected()){
            parameters.setKAG(true);
        }else{
            parameters.setKAG(false);
        }
        if(cKAOG.isSelected()){
            parameters.setKAOG(true);
        }else{
            parameters.setKAOG(false);
        }
        this.dispose();
    }//GEN-LAST:event_bOKActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancelar;
    private javax.swing.JButton bIncluir;
    private javax.swing.JButton bOK;
    private javax.swing.JButton bRemover;
    private javax.swing.JCheckBox cKAG;
    private javax.swing.JCheckBox cKAOG;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lVizinhos;
    private javax.swing.JPanel pVizinhos;
    // End of variables declaration//GEN-END:variables
}
