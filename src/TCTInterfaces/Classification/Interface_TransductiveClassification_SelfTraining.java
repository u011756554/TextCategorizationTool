//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTInterfaces.Classification;

import TCTInterfaces.Parameters.Interface_Parameters_NumberLabeledExamples;
import TCTInterfaces.Parameters.Interface_Parameters_KNN;
import TCTInterfaces.Menus.Interface_Menu2;
import TCT.TransductiveClassification_SelfTraining;
import TCTConfigurations.Email;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_SelfTraining;
import TCTIO.SearchIO;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Interface_TransductiveClassification_SelfTraining extends javax.swing.JInternalFrame {
    
    TransductiveConfiguration_SelfTraining configurationSelfTraining;
    public Interface_TransductiveClassification_SelfTraining(TransductiveConfiguration_SelfTraining configurationSelfTraining) {
        this.configurationSelfTraining = configurationSelfTraining;
        initComponents();
        
        DefaultListModel model = new DefaultListModel();
        for(int item=0;item<configurationSelfTraining.getNumInstPerClassAddTraining().size();item++){
            model.addElement(configurationSelfTraining.getNumInstPerClassAddTraining().get(item));
        }
        lnumInstPerClassTreinamento.setModel(model);
        
        this.setVisible(true);
    }
    
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tDirIn = new javax.swing.JTextField();
        tDirOut = new javax.swing.JTextField();
        bProcurarDirIn = new javax.swing.JButton();
        bProcurarDirOut = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        cMNB = new javax.swing.JCheckBox();
        cKnn = new javax.swing.JCheckBox();
        bKnn = new javax.swing.JButton();
        lLegend1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tRep = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        oPercentual = new javax.swing.JRadioButton();
        oReal = new javax.swing.JRadioButton();
        bNumInstPerClass = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        bExecutar = new javax.swing.JButton();
        bSalvar = new javax.swing.JButton();
        bFechar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lnumInstPerClassTreinamento = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Transductive Classification (Self-Training)");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Directories"));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("<html>\nArffs (<b>without ID</b>):\n<html>");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Results:");

        bProcurarDirIn.setText("Search...");
        bProcurarDirIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarDirInActionPerformed(evt);
            }
        });

        bProcurarDirOut.setText("Search...");
        bProcurarDirOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarDirOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tDirOut)
                    .addComponent(tDirIn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bProcurarDirOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bProcurarDirIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tDirIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tDirOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirOut)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Classification Algorithms"));

        cMNB.setText("MNB");

        cKnn.setText("KNN");

        bKnn.setText("...");
        bKnn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bKnnActionPerformed(evt);
            }
        });

        lLegend1.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        lLegend1.setText("<html>\n<u>Legend</u>\n<html>");
        lLegend1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lLegend1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lLegend1MouseClicked(evt);
            }
        });
        lLegend1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lLegend1AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cMNB)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lLegend1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cKnn))
                        .addGap(18, 18, 18)
                        .addComponent(bKnn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cMNB)
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cKnn)
                    .addComponent(bKnn))
                .addGap(18, 18, 18)
                .addComponent(lLegend1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Evaluation"));

        jLabel3.setText("Nº of Repetitions:");

        tRep.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tRep.setText("10");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Exemplos Rotulados"));

        buttonGroup1.add(oPercentual);
        oPercentual.setText("Relative");

        buttonGroup1.add(oReal);
        oReal.setSelected(true);
        oReal.setText("Absolute");
        oReal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oRealActionPerformed(evt);
            }
        });

        bNumInstPerClass.setText("...");
        bNumInstPerClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNumInstPerClassActionPerformed(evt);
            }
        });

        jLabel6.setText("<html>Nº of labeled <br>documents per class</html>");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bNumInstPerClass, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(oReal)
                    .addComponent(oPercentual)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(oPercentual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(oReal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bNumInstPerClass)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tRep, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tRep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );

        bExecutar.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bExecutar.setText("Run");
        bExecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExecutarActionPerformed(evt);
            }
        });

        bSalvar.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        bSalvar.setText("Save Configurations");
        bSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSalvarActionPerformed(evt);
            }
        });

        bFechar.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        bFechar.setText("Close");
        bFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFecharActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Nº of Examples Added in the Training Set"));

        lnumInstPerClassTreinamento.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "5", "10", "15", "20" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lnumInstPerClassTreinamento);

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 193, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(bExecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bFechar)
                    .addComponent(bExecutar)
                    .addComponent(bSalvar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bProcurarDirInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirInActionPerformed
        tDirIn.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirInActionPerformed

    private void bProcurarDirOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirOutActionPerformed
        tDirOut.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirOutActionPerformed

    private void oRealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oRealActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oRealActionPerformed

    private void bNumInstPerClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNumInstPerClassActionPerformed
        JInternalFrame frame = new Interface_Parameters_NumberLabeledExamples(configurationSelfTraining.getParametersNumLabeledInstancesPerClass());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bNumInstPerClassActionPerformed

    private void bFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFecharActionPerformed
        this.dispose();
    }//GEN-LAST:event_bFecharActionPerformed
    
    private void defineConfiguration(){

        Email.emailInterface(configurationSelfTraining);
        
        ArrayList<Integer> numExTreinamento = new ArrayList<Integer>();
        ListModel listModel = lnumInstPerClassTreinamento.getModel();
        for (int item=0;item<listModel.getSize(); item++) {
            numExTreinamento.add(Integer.parseInt(listModel.getElementAt(item).toString()));
        }
        configurationSelfTraining.setNumInstPerClassAddTraining(numExTreinamento);
        
        configurationSelfTraining.setNumReps(Integer.parseInt(tRep.getText()));
        
        configurationSelfTraining.setDirEntrada(tDirIn.getText());
        configurationSelfTraining.setDirSaida (tDirOut.getText());
        
        if(cMNB.isSelected()){
            configurationSelfTraining.setMNB(true);
        }else{
            configurationSelfTraining.setMNB(false);
        }
        if(cKnn.isSelected()){
            configurationSelfTraining.setKNN(true);
        }else{
            configurationSelfTraining.setKNN(false);
        }
        
        if(oPercentual.isSelected()){
            configurationSelfTraining.setPorcentage(true);
        }else{
            configurationSelfTraining.setPorcentage(false);
        }
        
    }
    
    private void bExecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExecutarActionPerformed
        defineConfiguration();
        TransductiveClassification_SelfTraining.learning(configurationSelfTraining);
    }//GEN-LAST:event_bExecutarActionPerformed

    private void bSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSalvarActionPerformed
        defineConfiguration();
        JFileChooser save = new JFileChooser();
        save.setFileSelectionMode(save.FILES_ONLY);
        save.setDialogTitle("Save");
        save.setDialogType(save.SAVE_DIALOG);
        save.setFileFilter(new FileNameExtensionFilter("Text Categorization Tool (*.tct)", "tct"));
        save.showSaveDialog(null);

        File config = save.getSelectedFile();
        if(config == null){
            return;
        }
        String fileName = config.toString();
        
        FileOutputStream file;
        ObjectOutputStream obj;
        try{
            if(!fileName.endsWith(".tct")){
                fileName = fileName + ".tct";
            }
            file = new FileOutputStream(fileName);
            obj = new ObjectOutputStream(file);
            obj.writeObject(configurationSelfTraining);
            obj.close();
        }catch(Exception e){
            System.err.println("Error when saving configuration object.");
            e.printStackTrace();
            System.exit(0);
        }
    }//GEN-LAST:event_bSalvarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JOptionPane ent = new JOptionPane();
        try{
            Integer item = Integer.parseInt(ent.showInputDialog(this,"Taxa de errorCorrectionRate:","TCT", ent.PLAIN_MESSAGE));
            ListModel model = lnumInstPerClassTreinamento.getModel();
            DefaultListModel model2 = new DefaultListModel();
            for(int j = 0;j<model.getSize();j++){
                model2.addElement(model.getElementAt(j));
            }
            model2.addElement(item);
            lnumInstPerClassTreinamento.setModel(model2);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Invalid value");
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int i = lnumInstPerClassTreinamento.getSelectedIndex();
        if(i == -1) return;
        ListModel model = lnumInstPerClassTreinamento.getModel();
        DefaultListModel model2 = new DefaultListModel();
        for(int j = 0;j<model.getSize();j++){
            model2.addElement(model.getElementAt(j));
        }
        model2.removeElementAt(i);
        lnumInstPerClassTreinamento.setModel(model2);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void bKnnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKnnActionPerformed
        JInternalFrame frame = new Interface_Parameters_KNN(configurationSelfTraining.getParametersKNN());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bKnnActionPerformed

    private void lLegend1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lLegend1MouseClicked
        String text = "<html>\n" +
        "<b>MNB</b> - Multinomial Naïve Bayes <br>\n" +
        "<b>KNN</b> - <i>K</i> Nearest Neighbors <br>\n" +
        "</html>";
        JInternalFrame frame = new Interface_Legend(text);
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lLegend1MouseClicked

    private void lLegend1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lLegend1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lLegend1AncestorAdded

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bExecutar;
    private javax.swing.JButton bFechar;
    private javax.swing.JButton bKnn;
    private javax.swing.JButton bNumInstPerClass;
    private javax.swing.JButton bProcurarDirIn;
    private javax.swing.JButton bProcurarDirOut;
    private javax.swing.JButton bSalvar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cKnn;
    private javax.swing.JCheckBox cMNB;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lLegend1;
    private javax.swing.JList lnumInstPerClassTreinamento;
    private javax.swing.JRadioButton oPercentual;
    private javax.swing.JRadioButton oReal;
    private javax.swing.JTextField tDirIn;
    private javax.swing.JTextField tDirOut;
    private javax.swing.JTextField tRep;
    // End of variables declaration//GEN-END:variables
}
