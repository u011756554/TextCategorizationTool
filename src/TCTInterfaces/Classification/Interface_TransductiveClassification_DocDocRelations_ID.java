//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTInterfaces.Classification;

import TCTInterfaces.Parameters.Interface_Parameters_DocDocRelations_Exp;
import TCTInterfaces.Parameters.Interface_Parameters_DocDocRelations_KNN;
import TCTInterfaces.Parameters.Interface_Parameters_NumberLabeledExamples;
import TCTInterfaces.Parameters.Interface_Parameters_GFHF;
import TCTInterfaces.Parameters.Interface_Parameters_LLGC;
import TCTInterfaces.Parameters.Interface_Parameters_IMHN;
import TCTInterfaces.Menus.Interface_Menu2;
import TCT.TransductiveClassification_DocDoc_ID;
import TCTConfigurations.Email;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocRelations_ID;
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

public class Interface_TransductiveClassification_DocDocRelations_ID extends javax.swing.JInternalFrame {

    TransductiveConfiguration_DocDocRelations_ID configurationTransdutivo;
    
    
    public Interface_TransductiveClassification_DocDocRelations_ID(TransductiveConfiguration_DocDocRelations_ID configurationTransdutivo) {
        this.configurationTransdutivo = configurationTransdutivo;
        
        
        initComponents();
        
        if(configurationTransdutivo.getAdjList() == true){
            oList.setSelected(true);
        }else{
            oMatrix.setSelected(true);
        }
        
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tArff = new javax.swing.JTextField();
        tResultados = new javax.swing.JTextField();
        bProcurarMatrizDados = new javax.swing.JButton();
        bProcurarDirResultados = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tProx = new javax.swing.JTextField();
        bProcurarMatrizProx = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        cLLGC = new javax.swing.JCheckBox();
        bLGLC = new javax.swing.JButton();
        cGFHF = new javax.swing.JCheckBox();
        bGFHF = new javax.swing.JButton();
        cTCHN = new javax.swing.JCheckBox();
        bTCHN = new javax.swing.JButton();
        lLegend = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tRep = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        oPercentual = new javax.swing.JRadioButton();
        oReal = new javax.swing.JRadioButton();
        bnumInstPerClass = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        bFechar = new javax.swing.JButton();
        bSalvar = new javax.swing.JButton();
        bExecutar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        cKnn = new javax.swing.JCheckBox();
        cExp = new javax.swing.JCheckBox();
        oCosseno = new javax.swing.JRadioButton();
        oEuclidiana = new javax.swing.JRadioButton();
        bKnn = new javax.swing.JButton();
        bExp = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        oMatrix = new javax.swing.JRadioButton();
        oList = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        tNumThreads = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Transductive Classification (Doc-Doc Relations with ID)");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Paths"));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("<html> Document-Term Matrix (<b>arff with ID</b>): <html>");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Results Directory:");

        bProcurarMatrizDados.setText("Search...");
        bProcurarMatrizDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarMatrizDadosActionPerformed(evt);
            }
        });

        bProcurarDirResultados.setText("Search...");
        bProcurarDirResultados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarDirResultadosActionPerformed(evt);
            }
        });

        jLabel4.setText("Proximity File:");

        bProcurarMatrizProx.setText("Search...");
        bProcurarMatrizProx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarMatrizProxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tProx, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .addComponent(tArff, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tResultados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bProcurarDirResultados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bProcurarMatrizProx, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bProcurarMatrizDados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tArff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bProcurarMatrizDados)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tProx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarMatrizProx))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tResultados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirResultados)))
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

        cTCHN.setText("TCHN");

        bTCHN.setText("...");
        bTCHN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTCHNActionPerformed(evt);
            }
        });

        lLegend.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        lLegend.setText("<html>\n<u>Legend</u>\n<html>");
        lLegend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lLegend.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lLegendAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        lLegend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lLegendMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cGFHF, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLLGC)
                    .addComponent(cTCHN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(bLGLC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bGFHF, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bTCHN, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLLGC)
                    .addComponent(bLGLC))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cGFHF)
                    .addComponent(bGFHF))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cTCHN)
                    .addComponent(bTCHN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(lLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oPercentual)
                    .addComponent(oReal)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bnumInstPerClass, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(oPercentual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oReal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bnumInstPerClass, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tRep, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tRep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters to Generate Doc-Doc Relations"));

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

        buttonGroup2.add(oCosseno);
        oCosseno.setSelected(true);
        oCosseno.setText("Cosseno");

        buttonGroup2.add(oEuclidiana);
        oEuclidiana.setText("Euclidiana");

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cKnn)
                            .addComponent(cExp))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bKnn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bExp, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(oEuclidiana)
                        .addGap(18, 18, 18)
                        .addComponent(oCosseno)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bKnn)
                    .addComponent(cKnn))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cExp)
                    .addComponent(bExp))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oEuclidiana)
                    .addComponent(oCosseno))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Structure"));

        buttonGroup3.add(oMatrix);
        oMatrix.setText("Matrix");

        buttonGroup3.add(oList);
        oList.setSelected(true);
        oList.setText("List");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(oMatrix)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(oList)
                .addGap(50, 50, 50))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oMatrix)
                    .addComponent(oList))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Multithreading"));

        jLabel6.setText("# of threads:");

        tNumThreads.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tNumThreads.setText("10");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tNumThreads, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(tNumThreads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bExecutar)
                            .addComponent(bSalvar)
                            .addComponent(bFechar)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bProcurarMatrizDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarMatrizDadosActionPerformed
        tArff.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarMatrizDadosActionPerformed

    private void bProcurarDirResultadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirResultadosActionPerformed
        tResultados.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirResultadosActionPerformed

    private void bLGLCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLGLCActionPerformed
        JInternalFrame frame = new Interface_Parameters_LLGC(configurationTransdutivo.getParametersLLGC());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bLGLCActionPerformed

    private void bGFHFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGFHFActionPerformed
        JInternalFrame frame = new Interface_Parameters_GFHF(configurationTransdutivo.getParametersGFHF());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bGFHFActionPerformed

    private void oRealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oRealActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oRealActionPerformed

    private void bnumInstPerClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnumInstPerClassActionPerformed
        JInternalFrame frame = new Interface_Parameters_NumberLabeledExamples(configurationTransdutivo.getParametersNumLabeledInstancesPerClass());
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
            obj.writeObject(configurationTransdutivo);
            obj.close();
        } catch (Exception e) {
            System.err.println("Error when saving configuration object.");
            e.printStackTrace();
            System.exit(0);
        }
    }//GEN-LAST:event_bSalvarActionPerformed
    
    private void defineConfiguration(){
        Email.emailInterface(configurationTransdutivo);
        
        configurationTransdutivo.setNumReps(Integer.parseInt(tRep.getText()));
        configurationTransdutivo.setNumThreads(Integer.parseInt(tNumThreads.getText()));
        
        configurationTransdutivo.setMatrizDados(tArff.getText());
        configurationTransdutivo.setMatrizProx(tProx.getText());
        configurationTransdutivo.setDirResultados(tResultados.getText());
        
        if(cLLGC.isSelected()){
            configurationTransdutivo.setLLGC(true);
        }else{
            configurationTransdutivo.setLLGC(false);
        }
        
        if(cGFHF.isSelected()){
            configurationTransdutivo.setGFHF(true);
        }else{
            configurationTransdutivo.setGFHF(false);
        }
        
        if(cTCHN.isSelected()){
            configurationTransdutivo.setTCHN(true);
        }else{
            configurationTransdutivo.setTCHN(false);
        }
        
        if(oPercentual.isSelected()){
            configurationTransdutivo.setPorcentage(true);
        }else{
            configurationTransdutivo.setPorcentage(false);
        }
        
        if(cKnn.isSelected()){
            configurationTransdutivo.setNetworkKnn(true);
        }else{
            configurationTransdutivo.setNetworkKnn(false);
        }
        if(cExp.isSelected()){
            configurationTransdutivo.setNetworkExp(true);
        }else{
            configurationTransdutivo.setNetworkExp(false);
        }
        if(oMatrix.isSelected()){
            configurationTransdutivo.setAdjList(false);
        }else{
            configurationTransdutivo.setAdjList(true);
        }
        
        if(oCosseno.isSelected()){
            configurationTransdutivo.getParametersExpNetwork().setCosine(true);
            configurationTransdutivo.getParametersKnnNetwork().setCosine(true);
        }else{
            configurationTransdutivo.getParametersExpNetwork().setCosine(false);
            configurationTransdutivo.getParametersKnnNetwork().setCosine(false);
        }
        
    }
    
    private void bExecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExecutarActionPerformed
        defineConfiguration();
        TransductiveClassification_DocDoc_ID.learning(configurationTransdutivo);
    }//GEN-LAST:event_bExecutarActionPerformed

    private void bProcurarMatrizProxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarMatrizProxActionPerformed
        tProx.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarMatrizProxActionPerformed

    private void cKnnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cKnnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cKnnActionPerformed

    private void cExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cExpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cExpActionPerformed

    private void bKnnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKnnActionPerformed
        JInternalFrame frame = new Interface_Parameters_DocDocRelations_KNN(configurationTransdutivo.getParametersKnnNetwork());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bKnnActionPerformed

    private void bExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExpActionPerformed
        JInternalFrame frame = new Interface_Parameters_DocDocRelations_Exp(configurationTransdutivo.getParametersExpNetwork());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bExpActionPerformed

    private void bTCHNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTCHNActionPerformed
        JInternalFrame frame = new Interface_Parameters_IMHN(configurationTransdutivo.getParametersTCHN());
        Interface_Menu2.getDesktop().add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bTCHNActionPerformed

    private void lLegendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lLegendMouseClicked
        String text = "<html>\n" +
        "<b>LLGC</b> - Learning with Local and Global Consistency <br>\n" +
        "<b>GFHF</b> - Gaussian Fields and Harmonic Functions <br>\n" +
        "<b>TCHN</b> - Transductive Categorization based on Heterogeneous Network <br>\n" +
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bExecutar;
    private javax.swing.JButton bExp;
    private javax.swing.JButton bFechar;
    private javax.swing.JButton bGFHF;
    private javax.swing.JButton bKnn;
    private javax.swing.JButton bLGLC;
    private javax.swing.JButton bProcurarDirResultados;
    private javax.swing.JButton bProcurarMatrizDados;
    private javax.swing.JButton bProcurarMatrizProx;
    private javax.swing.JButton bSalvar;
    private javax.swing.JButton bTCHN;
    private javax.swing.JButton bnumInstPerClass;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JCheckBox cExp;
    private javax.swing.JCheckBox cGFHF;
    private javax.swing.JCheckBox cKnn;
    private javax.swing.JCheckBox cLLGC;
    private javax.swing.JCheckBox cTCHN;
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
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lLegend;
    private javax.swing.JRadioButton oCosseno;
    private javax.swing.JRadioButton oEuclidiana;
    private javax.swing.JRadioButton oList;
    private javax.swing.JRadioButton oMatrix;
    private javax.swing.JRadioButton oPercentual;
    private javax.swing.JRadioButton oReal;
    private javax.swing.JTextField tArff;
    private javax.swing.JTextField tNumThreads;
    private javax.swing.JTextField tProx;
    private javax.swing.JTextField tRep;
    private javax.swing.JTextField tResultados;
    // End of variables declaration//GEN-END:variables
}
