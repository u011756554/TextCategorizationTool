//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTInterfaces.Preprocessing;

import TCTIO.SearchIO;
import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class Interface_Preprocessing_FeatureSelection extends javax.swing.JInternalFrame {

    public Interface_Preprocessing_FeatureSelection() {
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
        tEntrada = new javax.swing.JTextField();
        tSaida = new javax.swing.JTextField();
        bProcurarDirIn = new javax.swing.JButton();
        bProcurarDirIn1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        oRank = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        tRank = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        oTF = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        bClose = new javax.swing.JButton();
        bRun = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Feature Selection");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Files"));

        jLabel1.setText("<html>\nOriginal Arff (<b>without ID</b>):\n</html>");

        jLabel2.setText("<html>\nArff with selected features (<b>output</b>):\n</html>");

        bProcurarDirIn.setText("Search...");
        bProcurarDirIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarDirInActionPerformed(evt);
            }
        });

        bProcurarDirIn1.setText("Search...");
        bProcurarDirIn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarDirIn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                    .addComponent(tSaida))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bProcurarDirIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bProcurarDirIn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("TF-IDF"));

        buttonGroup2.add(oRank);
        oRank.setSelected(true);
        oRank.setText("Rank");

        buttonGroup2.add(jRadioButton2);
        jRadioButton2.setText("Threshold");

        tRank.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tRank.setText("1000000000");

        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.setText("1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(oRank)
                        .addGap(36, 36, 36))
                    .addComponent(tRank, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jRadioButton2))
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(102, 102, 102))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oRank)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tRank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));
        jPanel3.setEnabled(false);

        buttonGroup1.add(oTF);
        oTF.setText("TF");
        oTF.setEnabled(false);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("TF-IDF");
        jRadioButton1.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jRadioButton1)
                .addGap(46, 46, 46)
                .addComponent(oTF)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oTF)
                    .addComponent(jRadioButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bClose.setText("Close");
        bClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseActionPerformed(evt);
            }
        });

        bRun.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bRun.setText("Run");
        bRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRunActionPerformed(evt);
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
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(bRun, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bClose, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bClose)
                    .addComponent(bRun))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_bCloseActionPerformed

    private void bRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRunActionPerformed
        try{
            ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(tEntrada.getText()); //Carregando arquivo de Dados
                Instances instances = trainSource.getDataSet();
                
                int numClasses = 0;
                Attribute classAtt = null;

                classAtt = instances.attribute(instances.numAttributes()-1); //Setting the last feature as class
                instances.setClass(classAtt);
                numClasses = classAtt.numValues();
                
                for(int j=0;j<numClasses;j++){
                    System.out.println(j + ": " + classAtt.value(j));
                }
                
                int numDocs = instances.numInstances();
                int numTerms = instances.numAttributes() - 1;
                
                IndexValue[] atrTFIDF = new IndexValue[numTerms];
                for(int term=0;term<numTerms;term++){
                    atrTFIDF[term] = new IndexValue();
                }
                //double[][] matSaida = new double[numDocs][numTerms];
                HashMap<Integer,Double>[] adjancecyListDocTerm = new HashMap[numDocs];
                for(int inst=0;inst<numDocs;inst++){
                    adjancecyListDocTerm[inst] = new HashMap<Integer,Double>();
                }
                
                for(int term=0;term<numTerms;term++){
                    System.out.println(term);
                    int df = 0;
                    for(int inst=0;inst<numDocs;inst++){
                        if(instances.instance(inst).value(term) > 0){
                            df++;
                        }
                    }
                    double acmTFIDF = 0;
                    for(int inst=0;inst<numDocs;inst++){
                        if(instances.instance(inst).value(term) > 0){
                            double tfidf = instances.instance(inst).value(term) * Math.log10((double)numDocs / (double)df);
                            adjancecyListDocTerm[inst].put(term,tfidf);
                            //matSaida[inst][term] = tfidf;
                            acmTFIDF += tfidf;    
                        }
                    }
                    atrTFIDF[term].setIndex(term);
                    atrTFIDF[term].setValue(acmTFIDF);
                }
                
                Arrays.sort( atrTFIDF, new Comparator() {
                public int compare( Object obj1, Object obj2 ) {
                        IndexValue indVal1 = (IndexValue)obj1;
                        IndexValue indVal2 = (IndexValue)obj2;
                        if(indVal1.getValue() > indVal2.getValue()){
                            return -1;
                        }else{
                            if(indVal1.getValue() < indVal2.getValue()){
                                return 1;
                            }else{
                                return 0;
                            }         
                        }
                }});
                
                //Gravando arquivo de saída
                int max=0;
                int numRank = Integer.parseInt(tRank.getText());
                if(numRank > numTerms){
                    max = numTerms;
                }else{
                    max = numRank;
                }
                
                FileWriter arffSaida = new FileWriter(tSaida.getText());
                //arffSaida.write("@RELATION " + instances.relationName() + "_TFIDF_" + numRank + "\n\n");
                arffSaida.write("@RELATION " + instances.relationName() + "_TFIDF\n\n");
                
                for(int term=0;term<max;term++){
                    int index = atrTFIDF[term].getIndex();
                    arffSaida.write("@ATTRIBUTE " + instances.attribute(index).name() + " REAL\n");
                }
                
                
                StringBuffer listaClasses = new StringBuffer();
                for(int classe=0;classe<numClasses;classe++){
                    listaClasses.append(classAtt.value(classe) + ",");
                }
                arffSaida.write("@ATTRIBUTE class_atr {" + listaClasses.substring(0, listaClasses.length()-1) + "}\n\n@DATA\n");
                
                for(int inst=0;inst<numDocs;inst++){
                    StringBuffer line = new StringBuffer();
                    
                    StringBuffer lineData = new StringBuffer();
                    for(int term=0;term<max;term++){
                        int indexAtr = atrTFIDF[term].getIndex();
                        if(adjancecyListDocTerm[inst].containsKey(indexAtr)){
                            lineData.append(term + " " + adjancecyListDocTerm[inst].get(indexAtr) + ",");
                        }
                        
                    }
                    if(max < numTerms){
                        lineData.append(max);
                    }else{
                        lineData.append(numTerms);
                    }
                    lineData.append(", " + classAtt.value((int)instances.instance(inst).classValue()));
                    arffSaida.write("{" + lineData.toString() + "}\n");
                    /*for(int term=0;term<max;term++){
                        int indexAtr = atrTFIDF[term].getIndex();
                        line.append(matSaida[inst][indexAtr] + ",");
                    }
                    line.append(classAtt.value((int)instances.instance(inst).classValue()));
                    arffSaida.write(line.toString() + "\n");*/
                }
                
                arffSaida.close();
                System.out.println("Done!");
        }catch(Exception e){
            System.out.println("Error when preprocessing document-term matrix.");
            e.printStackTrace();
            System.exit(0);
        }
        
    }//GEN-LAST:event_bRunActionPerformed

    private void bProcurarDirInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirInActionPerformed
        tEntrada.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirInActionPerformed

    private void bProcurarDirIn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirIn1ActionPerformed
        tSaida.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirIn1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClose;
    private javax.swing.JButton bProcurarDirIn;
    private javax.swing.JButton bProcurarDirIn1;
    private javax.swing.JButton bRun;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JRadioButton oRank;
    private javax.swing.JRadioButton oTF;
    private javax.swing.JTextField tEntrada;
    private javax.swing.JTextField tRank;
    private javax.swing.JTextField tSaida;
    // End of variables declaration//GEN-END:variables
}
