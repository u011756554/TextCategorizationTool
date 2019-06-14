//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTInterfaces.Classification;

import TCTInterfaces.Parameters.Interface_Parameters_GFHF;
import TCTInterfaces.Parameters.Interface_Parameters_NumberLabeledExamples;
import TCTInterfaces.Parameters.Interface_Parameters_TermTermRelations;
import TCTInterfaces.Parameters.Interface_Parameters_LLGC;
import TCTInterfaces.Menus.Interface_Menu2;
import TCT.TransductiveClassification_TermTerm;
import TCTConfigurations.Email;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_TermTermRelations;
import TCTIO.SearchIO;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Interface_TransductiveClassification_TermTerm extends javax.swing.JInternalFrame {
    
    TransductiveConfiguration_TermTermRelations configurationTransductive;

    public Interface_TransductiveClassification_TermTerm(TransductiveConfiguration_TermTermRelations configurationTransductive) {
        this.configurationTransductive = configurationTransductive;
        
        initComponents();
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tArff = new javax.swing.JTextField();
        tDirOut = new javax.swing.JTextField();
        bProcurarArff = new javax.swing.JButton();
        bProcurarDirOut = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tProb = new javax.swing.JTextField();
        bProcurarProb = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tRep = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        oPercentual = new javax.swing.JRadioButton();
        oReal = new javax.swing.JRadioButton();
        bnumInstPerClass = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cLLGC = new javax.swing.JCheckBox();
        bLGLC = new javax.swing.JButton();
        cGFHF = new javax.swing.JCheckBox();
        bGFHF = new javax.swing.JButton();
        lLegend = new javax.swing.JLabel();
        bFechar = new javax.swing.JButton();
        bSalvar = new javax.swing.JButton();
        bExecutar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        cSupport = new javax.swing.JCheckBox();
        cInfMutua = new javax.swing.JCheckBox();
        cKappa = new javax.swing.JCheckBox();
        cShapiro = new javax.swing.JCheckBox();
        bSupport = new javax.swing.JButton();
        bInfMutua = new javax.swing.JButton();
        bKappa = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        cQYule = new javax.swing.JCheckBox();
        jButton5 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        tNumThreads4 = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Transductive Classification (Term-Term Relations)");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Paths"));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("<html>\nDocument-term matrix (<b>*.arff without ID</b>): \n</html>");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Results directory:");

        tArff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tArffActionPerformed(evt);
            }
        });

        bProcurarArff.setText("Search...");
        bProcurarArff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarArffActionPerformed(evt);
            }
        });

        bProcurarDirOut.setText("Search...");
        bProcurarDirOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarDirOutActionPerformed(evt);
            }
        });

        jLabel4.setText("Probability file:");

        bProcurarProb.setText("Search...");
        bProcurarProb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarProbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tDirOut)
                    .addComponent(tProb)
                    .addComponent(tArff))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bProcurarProb, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bProcurarDirOut, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bProcurarArff, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tArff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarArff))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarProb))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tDirOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirOut)
                    .addComponent(jLabel2))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Evaluation"));

        jLabel3.setText("Nº of Repetitions:");

        tRep.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tRep.setText("10");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Labeled Examples"));

        buttonGroup1.add(oPercentual);
        oPercentual.setText("Percentual");

        buttonGroup1.add(oReal);
        oReal.setSelected(true);
        oReal.setText("Real");
        oReal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oRealActionPerformed(evt);
            }
        });

        bnumInstPerClass.setText("...");
        bnumInstPerClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnumInstPerClassActionPerformed(evt);
            }
        });

        jLabel6.setText("<html>Nº of labeled <br>documents per class</html>");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bnumInstPerClass, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(oReal)
                        .addGap(18, 18, 18)
                        .addComponent(oPercentual)
                        .addGap(26, 26, 26))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oReal)
                    .addComponent(oPercentual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bnumInstPerClass)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tRep, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tRep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Classification Algorithms"));

        cLLGC.setText("LLGC");

        bLGLC.setText("...");
        bLGLC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bLGLCActionPerformed(evt);
            }
        });

        cGFHF.setText("GFHF");

        bGFHF.setText("...");
        bGFHF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGFHFActionPerformed(evt);
            }
        });

        lLegend.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        lLegend.setText("<html>\n<u>Legend</u>\n<html>");
        lLegend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lLegend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lLegendMouseClicked(evt);
            }
        });
        lLegend.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lLegendAncestorAdded(evt);
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cLLGC)
                .addGap(18, 18, 18)
                .addComponent(bLGLC, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(cGFHF, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bGFHF, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(lLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cLLGC)
                        .addComponent(bLGLC))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cGFHF)
                        .addComponent(bGFHF)
                        .addComponent(lLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        bFechar.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        bFechar.setText("Close");
        bFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFecharActionPerformed(evt);
            }
        });

        bSalvar.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        bSalvar.setText("Save Configurations");
        bSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSalvarActionPerformed(evt);
            }
        });

        bExecutar.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bExecutar.setText("Run");
        bExecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExecutarActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Term-Term Relations"));

        cSupport.setText("Support");

        cInfMutua.setText("Mutual Information");

        cKappa.setText("Kappa");

        cShapiro.setText("Piatetsky-Shapiro's");

        bSupport.setText("...");
        bSupport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSupportActionPerformed(evt);
            }
        });

        bInfMutua.setText("...");
        bInfMutua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInfMutuaActionPerformed(evt);
            }
        });

        bKappa.setText("...");
        bKappa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bKappaActionPerformed(evt);
            }
        });

        jButton4.setText("...");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        cQYule.setText("Yule's Q");

        jButton5.setText("...");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cSupport)
                    .addComponent(cKappa)
                    .addComponent(cQYule))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bKappa, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSupport, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cInfMutua)
                        .addGap(18, 18, 18)
                        .addComponent(bInfMutua, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cShapiro)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bSupport)
                    .addComponent(cSupport)
                    .addComponent(bInfMutua)
                    .addComponent(cInfMutua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bKappa)
                    .addComponent(cKappa)
                    .addComponent(cShapiro)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cQYule)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Multithreading"));

        jLabel10.setText("# of threads:");

        tNumThreads4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tNumThreads4.setText("10");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tNumThreads4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tNumThreads4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(bExecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bExecutar)
                    .addComponent(bSalvar)
                    .addComponent(bFechar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void bProcurarArffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarArffActionPerformed
        tArff.setText(SearchIO.AbreArq());
    }//GEN-LAST:event_bProcurarArffActionPerformed

    private void bProcurarDirOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirOutActionPerformed
        tDirOut.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirOutActionPerformed

    private void oRealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oRealActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oRealActionPerformed

    private void bnumInstPerClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnumInstPerClassActionPerformed
        JInternalFrame frame = new Interface_Parameters_NumberLabeledExamples(configurationTransductive.getParametersNumLabeledInstancesPerClass());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bnumInstPerClassActionPerformed

    private void bFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFecharActionPerformed
        this.dispose();
    }//GEN-LAST:event_bFecharActionPerformed

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
        try {
            if(!fileName.endsWith(".tct")){
                fileName = fileName + ".tct";
            }
            file = new FileOutputStream(fileName);
            obj = new ObjectOutputStream(file);
            obj.writeObject(configurationTransductive);
            obj.close();
        } catch (Exception e) {
            System.err.println("Error when saving configuration object.");
            e.printStackTrace();
            System.exit(0);
        }
    }//GEN-LAST:event_bSalvarActionPerformed
    
    private void defineConfiguration(){
        Email.emailInterface(configurationTransductive);
        
        configurationTransductive.setNumReps(Integer.parseInt(tRep.getText()));
        configurationTransductive.setNumThreads(Integer.parseInt(tNumThreads4.getText()));
        
        configurationTransductive.setArff(tArff.getText());
        configurationTransductive.setArqProb(tProb.getText());
        configurationTransductive.setDirSaida (tDirOut.getText());
        
        if(cLLGC.isSelected()){
            configurationTransductive.setLLGC(true);
        }else{
            configurationTransductive.setLLGC(false);
        }
        
        if(cGFHF.isSelected()){
            configurationTransductive.setGFHF(true);
        }else{
            configurationTransductive.setGFHF(false);
        }
        
        if(oPercentual.isSelected()){
            configurationTransductive.setPorcentage(true);
        }else{
            configurationTransductive.setPorcentage(false);
        }
        
        if(cSupport.isSelected()){
            configurationTransductive.setSupportNetwork(true);
        }else{
            configurationTransductive.setSupportNetwork(false);
        }
        
        if(cInfMutua.isSelected()){
            configurationTransductive.setMutualInformationNetwork(true);
        }else{
            configurationTransductive.setMutualInformationNetwork(false);
        }
        
        if(cKappa.isSelected()){
            configurationTransductive.setKappaNetwork(true);
        }else{
            configurationTransductive.setKappaNetwork(false);
        }
        
        if(cShapiro.isSelected()){
            configurationTransductive.setShapiroNetwork(true);
        }else{
            configurationTransductive.setShapiroNetwork(false);
        }
        
        if(cQYule.isSelected()){
            configurationTransductive.setYulesQNetwork(true);
        }else{
            configurationTransductive.setYulesQNetwork(false);
        }
        
    }
    
    private void bExecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExecutarActionPerformed
        defineConfiguration();
        TransductiveClassification_TermTerm.learning(configurationTransductive);
    }//GEN-LAST:event_bExecutarActionPerformed

    private void bProcurarProbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarProbActionPerformed
        tProb.setText(SearchIO.AbreArq());
    }//GEN-LAST:event_bProcurarProbActionPerformed

    private void bLGLCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLGLCActionPerformed
        JInternalFrame frame = new Interface_Parameters_LLGC(configurationTransductive.getParametersLLGC());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bLGLCActionPerformed

    private void bSupportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSupportActionPerformed
        JInternalFrame frame = new Interface_Parameters_TermTermRelations(configurationTransductive.getParametersSupportNetwork(), "Support");
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bSupportActionPerformed

    private void bInfMutuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInfMutuaActionPerformed
        JInternalFrame frame = new Interface_Parameters_TermTermRelations(configurationTransductive.getParametersMutualInformationNetwork(), "Mutual Information");
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bInfMutuaActionPerformed

    private void bKappaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKappaActionPerformed
        JInternalFrame frame = new Interface_Parameters_TermTermRelations(configurationTransductive.getParametersKappaNetwork(), "Kappa");
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bKappaActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        JInternalFrame frame = new Interface_Parameters_TermTermRelations(configurationTransductive.getParametersShapiroNetwork(), "Shapiro");
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JInternalFrame frame = new Interface_Parameters_TermTermRelations(configurationTransductive.getParametersYulesQNetwork(), "Yule\'s Q");
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void bGFHFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGFHFActionPerformed
        JInternalFrame frame = new Interface_Parameters_GFHF(configurationTransductive.getParametersGFHF());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bGFHFActionPerformed

    private void tArffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tArffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tArffActionPerformed

    private void lLegendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lLegendMouseClicked
        String text = "<html>\n" +
        "<b>LLGC</b> - Learning with Local and Global Consistency <br>\n" +
        "<b>GFHF</b> - Gaussian Fields and Harmonic Functions <br>\n" +
        "</html>";
        JInternalFrame frame = new Interface_Legend(text);
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lLegendMouseClicked

    private void lLegendAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lLegendAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lLegendAncestorAdded

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bExecutar;
    private javax.swing.JButton bFechar;
    private javax.swing.JButton bGFHF;
    private javax.swing.JButton bInfMutua;
    private javax.swing.JButton bKappa;
    private javax.swing.JButton bLGLC;
    private javax.swing.JButton bProcurarArff;
    private javax.swing.JButton bProcurarDirOut;
    private javax.swing.JButton bProcurarProb;
    private javax.swing.JButton bSalvar;
    private javax.swing.JButton bSupport;
    private javax.swing.JButton bnumInstPerClass;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cGFHF;
    private javax.swing.JCheckBox cInfMutua;
    private javax.swing.JCheckBox cKappa;
    private javax.swing.JCheckBox cLLGC;
    private javax.swing.JCheckBox cQYule;
    private javax.swing.JCheckBox cShapiro;
    private javax.swing.JCheckBox cSupport;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lLegend;
    private javax.swing.JRadioButton oPercentual;
    private javax.swing.JRadioButton oReal;
    private javax.swing.JTextField tArff;
    private javax.swing.JTextField tDirOut;
    private javax.swing.JTextField tNumThreads4;
    private javax.swing.JTextField tProb;
    private javax.swing.JTextField tRep;
    // End of variables declaration//GEN-END:variables
}
