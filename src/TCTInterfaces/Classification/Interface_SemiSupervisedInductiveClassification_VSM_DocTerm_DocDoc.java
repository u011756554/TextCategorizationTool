//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTInterfaces.Classification;

import TCTInterfaces.Parameters.Interface_Parameters_DocDocRelations_Exp;
import TCTInterfaces.Parameters.Interface_Parameters_GNetMine_DocTerm;
import TCTInterfaces.Parameters.Interface_Parameters_DocDocRelations_KNN;
import TCTInterfaces.Parameters.Interface_Parameters_GFHF;
import TCTInterfaces.Parameters.Interface_Parameters_EM;
import TCTInterfaces.Parameters.Interface_Parameters_NumberLabeledExamples;
import TCTInterfaces.Parameters.Interface_Parameters_LPHN;
import TCTInterfaces.Parameters.Interface_Parameters_TagBased;
import TCTInterfaces.Parameters.Interface_Parameters_LLGC;
import TCTInterfaces.Parameters.Interface_Parameters_NMFE;
import TCTInterfaces.Parameters.Interface_Parameters_IMHN;
import TCTInterfaces.Parameters.Interface_Parameters_TSVM;
import TCTInterfaces.Menus.Interface_Menu2;
import TCT.SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc;
import TCTConfigurations.Email;
import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration;
import TCTIO.SearchIO;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Interface_SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc extends javax.swing.JInternalFrame {

    SemiSupervisedInductiveConfiguration configuration;
    
    public Interface_SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc(SemiSupervisedInductiveConfiguration configuration) {
        this.configuration = configuration;
        initComponents();
        this.setVisible(true);
    }

 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tDirIn = new javax.swing.JTextField();
        tDirOut = new javax.swing.JTextField();
        bProcurarDirIn = new javax.swing.JButton();
        bProcurarDirOut = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        lLegend = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cLPBHN = new javax.swing.JCheckBox();
        bGFHFBipartite = new javax.swing.JButton();
        cRHLMS = new javax.swing.JCheckBox();
        bIMBHN = new javax.swing.JButton();
        cTagBased = new javax.swing.JCheckBox();
        bTagBased = new javax.swing.JButton();
        cGNetMine = new javax.swing.JCheckBox();
        bGNetMine = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        cEM = new javax.swing.JCheckBox();
        bEM = new javax.swing.JButton();
        cTSVM = new javax.swing.JCheckBox();
        bTSVM = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        cLLGC = new javax.swing.JCheckBox();
        bLLGC = new javax.swing.JButton();
        bGFHF = new javax.swing.JButton();
        cGFHF = new javax.swing.JCheckBox();
        cNMFE = new javax.swing.JCheckBox();
        bNMFE = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        cKnn = new javax.swing.JCheckBox();
        cExp = new javax.swing.JCheckBox();
        oCosine = new javax.swing.JRadioButton();
        oEuclidean = new javax.swing.JRadioButton();
        bKnn = new javax.swing.JButton();
        bExp = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tRep = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        oPercentual = new javax.swing.JRadioButton();
        oReal = new javax.swing.JRadioButton();
        bnumInstPerClass = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tNumFolds = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        bFechar = new javax.swing.JButton();
        bSalvar = new javax.swing.JButton();
        bExecutar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Semi-Supervised Inductive Classification");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Directories"));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("<html>\nArffs (<b>without ID</b>):\n</html>");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tDirOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirOut))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Classification Algorithms"));

        lLegend.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        lLegend.setText("<html>\n<u>Legend</u>\n<html>");
        lLegend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lLegend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lLegendMouseClicked(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Bipartite Network"));

        cLPBHN.setText("LPBHN");

        bGFHFBipartite.setText("...");
        bGFHFBipartite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGFHFBipartiteActionPerformed(evt);
            }
        });

        cRHLMS.setText("TCBHN");

        bIMBHN.setText("...");
        bIMBHN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIMBHNActionPerformed(evt);
            }
        });

        cTagBased.setText("TagBased");

        bTagBased.setText("...");
        bTagBased.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTagBasedActionPerformed(evt);
            }
        });

        cGNetMine.setText("GNetMine");

        bGNetMine.setText("...");
        bGNetMine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGNetMineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cGNetMine)
                        .addGap(24, 24, 24)
                        .addComponent(bGNetMine, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cLPBHN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(38, 38, 38)
                                .addComponent(bGFHFBipartite, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cRHLMS)
                                    .addComponent(cTagBased))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(bTagBased, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(bIMBHN, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))))
                        .addGap(49, 49, 49))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLPBHN)
                    .addComponent(bGFHFBipartite))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bIMBHN)
                    .addComponent(cRHLMS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cTagBased)
                    .addComponent(bTagBased))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cGNetMine)
                    .addComponent(bGNetMine))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("VSM"));

        cEM.setText("EM");

        bEM.setText("...");
        bEM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEMActionPerformed(evt);
            }
        });

        cTSVM.setText("TSVM");

        bTSVM.setText("...");
        bTSVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTSVMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cEM)
                    .addComponent(cTSVM))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bEM, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(bTSVM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cEM)
                    .addComponent(bEM))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cTSVM)
                    .addComponent(bTSVM))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Document Network"));

        cLLGC.setText("LLGC");

        bLLGC.setText("...");
        bLLGC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bLLGCActionPerformed(evt);
            }
        });

        bGFHF.setText("...");
        bGFHF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGFHFActionPerformed(evt);
            }
        });

        cGFHF.setText("GFHF");

        cNMFE.setText("NMFE");

        bNMFE.setText("...");
        bNMFE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNMFEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cLLGC)
                            .addComponent(cGFHF))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bGFHF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bLLGC, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(cNMFE)
                        .addGap(23, 23, 23)
                        .addComponent(bNMFE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLLGC)
                    .addComponent(bLLGC))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cGFHF)
                    .addComponent(bGFHF))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cNMFE)
                    .addComponent(bNMFE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Doc-Doc Parameters"));

        cKnn.setText("Knn");
        cKnn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cKnnActionPerformed(evt);
            }
        });

        cExp.setText("Exp");
        cExp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cExpActionPerformed(evt);
            }
        });

        buttonGroup1.add(oCosine);
        oCosine.setSelected(true);
        oCosine.setText("Cosseno");

        buttonGroup1.add(oEuclidean);
        oEuclidean.setText("Euclidiana");

        bKnn.setText("...");
        bKnn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bKnnActionPerformed(evt);
            }
        });

        bExp.setText("...");
        bExp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cKnn)
                            .addComponent(cExp))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bKnn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bExp, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(oEuclidean)
                            .addComponent(oCosine))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bKnn)
                    .addComponent(cKnn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cExp)
                    .addComponent(bExp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(oCosine)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oEuclidean)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Evaluation"));

        jLabel3.setText("Nº of Repetitions:");

        tRep.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tRep.setText("10");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Labeled Documents"));

        buttonGroup2.add(oPercentual);
        oPercentual.setText("Relative");

        buttonGroup2.add(oReal);
        oReal.setSelected(true);
        oReal.setText("Absolute");
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

        jLabel5.setText("<html>Nº of labeled <br>documents per class</html>");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oPercentual)
                    .addComponent(oReal)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bnumInstPerClass, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(oPercentual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oReal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bnumInstPerClass))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel4.setText("Nº of Folds:");

        tNumFolds.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tNumFolds.setText("10");

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Multithreading"));

        jLabel6.setText("# of Threads:");

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setText("10");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tRep, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tNumFolds, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tRep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tNumFolds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        bFechar.setText("Close");
        bFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFecharActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bExecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bExecutar)
                    .addComponent(bSalvar)
                    .addComponent(bFechar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bProcurarDirInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirInActionPerformed
        tDirIn.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirInActionPerformed

    private void bProcurarDirOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirOutActionPerformed
        tDirOut.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirOutActionPerformed

    private void bGFHFBipartiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGFHFBipartiteActionPerformed
        JInternalFrame frame = new Interface_Parameters_LPHN(configuration.getParametersLPHN());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bGFHFBipartiteActionPerformed

    private void bIMBHNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIMBHNActionPerformed
        JInternalFrame frame = new Interface_Parameters_IMHN(configuration.getParametersTCHN());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bIMBHNActionPerformed

    private void bGNetMineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGNetMineActionPerformed
        JInternalFrame frame = new Interface_Parameters_GNetMine_DocTerm(configuration.getParametersGNetMine());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bGNetMineActionPerformed

    private void bTagBasedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTagBasedActionPerformed
        JInternalFrame frame = new Interface_Parameters_TagBased(configuration.getParametersTB());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bTagBasedActionPerformed

    private void oRealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oRealActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oRealActionPerformed

    private void bnumInstPerClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnumInstPerClassActionPerformed
        JInternalFrame frame = new Interface_Parameters_NumberLabeledExamples(configuration.getParametersNumLabeledInstancesPerClass());
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
            obj.writeObject(configuration);
            obj.close();
        } catch (Exception e) {
            System.err.println("Error when saving configuration object.");
            e.printStackTrace();
            System.exit(0);
        }
    }//GEN-LAST:event_bSalvarActionPerformed
    
    private void defineConfiguration(){
        Email.emailInterface(configuration);
        
        configuration.setNumReps(Integer.parseInt(tRep.getText()));
        configuration.setNumFolds(Integer.parseInt(tNumFolds.getText()));
        
        configuration.setDirEntrada(tDirIn.getText());
        configuration.setDirSaida (tDirOut.getText());
        
        if(oCosine.isSelected()){
            configuration.getParametersNetworkExp().setCosine(true);
            configuration.getParametersNetworkKnn().setCosine(true);
        }else{
            configuration.getParametersNetworkExp().setCosine(false);
            configuration.getParametersNetworkKnn().setCosine(false);
        }
        
        if(cKnn.isSelected()){
            configuration.setKnnNetwork(true);
        }else{
            configuration.setKnnNetwork(false);
        }
        if(cExp.isSelected()){
            configuration.setExpNetwork(true);
        }else{
            configuration.setExpNetwork(false);
        }
        
        if(cNMFE.isSelected()){
            configuration.setNMFE(true);
        }else{
            configuration.setNMFE(false);
        }    
        
        if(cLLGC.isSelected()){
            configuration.setLLGC(true);
        }else{
            configuration.setLLGC(false);
        }
        
        if(cGFHF.isSelected()){
            configuration.setGFHF(true);
        }else{
            configuration.setGFHF(false);
        }
        
        if(cEM.isSelected()){
            configuration.setExpectationMaximization(true);
        }else{
            configuration.setExpectationMaximization(false);
        }
        
        if(cTSVM.isSelected()){
            configuration.setTSVM(true);
        }else{
            configuration.setTSVM(false);
        }
        
        if(cTagBased.isSelected()){
            configuration.setTB(true);
        }else{
            configuration.setTB(false);
        }
        
        if(cLPBHN.isSelected()){
            configuration.setLPHN(true);
        }else{
            configuration.setLPHN(false);
        }
        
        if(cRHLMS.isSelected()){
            configuration.setTCHN(true);
        }else{
            configuration.setTCHN(false);
        }
        
        if(cGNetMine.isSelected()){
            configuration.setGNetMine(true);
        }else{
            configuration.setGNetMine(false);
        }
        
        if(oPercentual.isSelected()){
            configuration.setPorcentage(true);
        }else{
            configuration.setPorcentage(false);
        }
        
    }
    
    private void bExecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExecutarActionPerformed
        defineConfiguration();
        SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc.learning(configuration);
    }//GEN-LAST:event_bExecutarActionPerformed

    private void bEMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEMActionPerformed
        JInternalFrame frame = new Interface_Parameters_EM(configuration.getParametersEM());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bEMActionPerformed

    private void bTSVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTSVMActionPerformed
        JInternalFrame frame = new Interface_Parameters_TSVM(configuration.getParametersTSVM());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bTSVMActionPerformed

    private void lLegendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lLegendMouseClicked
        String text = "<html>\n" +
        "<b>LPBHN</b> - Label Propagation based on Bipartite Heterogeneous Network <br>\n" +
        "<b>TCBHN</b> - Transductive Categorization based on Bipartite Heterogeneous Network <br>\n" +
        "<b>EM</b> - Expectation Maximization<br>\n" +
        "<b>TSVM</b> - Transductive Support Vector Machines<br>\n" +
        "</html>";
        JInternalFrame frame = new Interface_Legend(text);
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lLegendMouseClicked

    private void bGFHFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGFHFActionPerformed
        JInternalFrame frame = new Interface_Parameters_GFHF(configuration.getParametersGFHF());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bGFHFActionPerformed

    private void bLLGCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLLGCActionPerformed
        JInternalFrame frame = new Interface_Parameters_LLGC(configuration.getParametersLLGC());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bLLGCActionPerformed

    private void cKnnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cKnnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cKnnActionPerformed

    private void cExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cExpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cExpActionPerformed

    private void bKnnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKnnActionPerformed
        JInternalFrame frame = new Interface_Parameters_DocDocRelations_KNN(configuration.getParametersNetworkKnn());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bKnnActionPerformed

    private void bExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExpActionPerformed
        JInternalFrame frame = new Interface_Parameters_DocDocRelations_Exp(configuration.getParametersNetworkExp());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bExpActionPerformed

    private void bNMFEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNMFEActionPerformed
        JInternalFrame frame = new Interface_Parameters_NMFE(configuration.getParametersNMFE());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bNMFEActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEM;
    private javax.swing.JButton bExecutar;
    private javax.swing.JButton bExp;
    private javax.swing.JButton bFechar;
    private javax.swing.JButton bGFHF;
    private javax.swing.JButton bGFHFBipartite;
    private javax.swing.JButton bGNetMine;
    private javax.swing.JButton bIMBHN;
    private javax.swing.JButton bKnn;
    private javax.swing.JButton bLLGC;
    private javax.swing.JButton bNMFE;
    private javax.swing.JButton bProcurarDirIn;
    private javax.swing.JButton bProcurarDirOut;
    private javax.swing.JButton bSalvar;
    private javax.swing.JButton bTSVM;
    private javax.swing.JButton bTagBased;
    private javax.swing.JButton bnumInstPerClass;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox cEM;
    private javax.swing.JCheckBox cExp;
    private javax.swing.JCheckBox cGFHF;
    private javax.swing.JCheckBox cGNetMine;
    private javax.swing.JCheckBox cKnn;
    private javax.swing.JCheckBox cLLGC;
    private javax.swing.JCheckBox cLPBHN;
    private javax.swing.JCheckBox cNMFE;
    private javax.swing.JCheckBox cRHLMS;
    private javax.swing.JCheckBox cTSVM;
    private javax.swing.JCheckBox cTagBased;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lLegend;
    private javax.swing.JRadioButton oCosine;
    private javax.swing.JRadioButton oEuclidean;
    private javax.swing.JRadioButton oPercentual;
    private javax.swing.JRadioButton oReal;
    private javax.swing.JTextField tDirIn;
    private javax.swing.JTextField tDirOut;
    private javax.swing.JTextField tNumFolds;
    private javax.swing.JTextField tRep;
    // End of variables declaration//GEN-END:variables
}
