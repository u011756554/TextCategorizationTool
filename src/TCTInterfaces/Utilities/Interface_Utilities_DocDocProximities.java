//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************      

package TCTInterfaces.Utilities;

import TCTNetworkGeneration.Proximity;
import TCTStructures.NeighborHash;
import TCTIO.ListFiles;
import TCTIO.SearchIO;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class Interface_Utilities_DocDocProximities extends javax.swing.JInternalFrame {

    public Interface_Utilities_DocDocProximities() {
        initComponents();
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tEntrada = new javax.swing.JTextField();
        bProcurarDirIn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tSaida = new javax.swing.JTextField();
        bProcurarDirIn1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        oCosseno = new javax.swing.JRadioButton();
        oEuclidiana = new javax.swing.JRadioButton();
        bCalcular = new javax.swing.JButton();
        bFechar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Doc-Doc Proximities");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("<html>\nArffs (<b>with ID</b>)\n<html>"));

        jLabel1.setText("Directory:");

        bProcurarDirIn.setText("Search...");
        bProcurarDirIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarDirInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bProcurarDirIn, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        jLabel2.setText("Directoty:");

        bProcurarDirIn1.setText("Search...");
        bProcurarDirIn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProcurarDirIn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bProcurarDirIn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn1))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Proximity Measure"));

        buttonGroup1.add(oCosseno);
        oCosseno.setSelected(true);
        oCosseno.setText("Cosine");

        buttonGroup1.add(oEuclidiana);
        oEuclidiana.setText("Euclidean");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(oCosseno)
                .addGap(93, 93, 93)
                .addComponent(oEuclidiana)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oCosseno)
                    .addComponent(oEuclidiana))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        bCalcular.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bCalcular.setText("Run");
        bCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCalcularActionPerformed(evt);
            }
        });

        bFechar.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        bFechar.setText("Close");
        bFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFecharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bCalcular)
                    .addComponent(bFechar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFecharActionPerformed
        this.dispose();
    }//GEN-LAST:event_bFecharActionPerformed

    private void bCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCalcularActionPerformed
        
        Instances data = null;
        try{
            ArrayList<File> filesIn = new ArrayList<File>();
            System.out.println("Obtendo Lista de Arquivos...");
            ListFiles.List(new File(tEntrada.getText()), filesIn); //Vetor para armazenar os documentos textuais
            System.out.println("Carregando arquivos Arquivos...");
            for(int i =0;i<filesIn.size();i++){ // criando vetores contendo os atributos e suas frquências em cada documento da coleção

                if(!filesIn.get(i).getAbsolutePath().endsWith(".arff")){
                    continue;
                }
                System.out.println(filesIn.get(i).getAbsolutePath());
                System.out.println("Loading ARFF file");
                
                ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(filesIn.get(i).getAbsolutePath()); //Carregando arquivo de Dados
                data = trainSource.getDataSet();
                
                String tipo = "";
                if(oCosseno.isSelected()){
                    tipo = "_cosine";
                }else{
                    tipo = "_euclidean";
                }
                FileWriter outputFile = new FileWriter(tSaida.getText() + "/" + data.relationName() + tipo + ".dist");            
                
                int numTerms = data.numAttributes() - 1;
                int numInsts = data.numInstances();
                
                NeighborHash[] adjancecyListDocTerm = null;
                Long begin = System.currentTimeMillis();
                if(oCosseno.isSelected()){
                    adjancecyListDocTerm = new NeighborHash[numInsts];
                    for(int inst=0;inst<numInsts;inst++){
                        adjancecyListDocTerm[inst] = new NeighborHash();
                    }
                    for(int inst=0;inst<numInsts;inst++){
                        for(int term=1;term<numTerms;term++){
                            if(data.instance(inst).value(term) > 0){
                                adjancecyListDocTerm[inst].AddNeighbor(term,data.instance(inst).value(term));
                            }    
                        }
                    }
                    for(int inst1=0;inst1<numInsts;inst1++){
                        System.out.println(inst1);
                        for(int inst2=inst1+1;inst2<numInsts;inst2++){
                            double prox = Proximity.calcDistCossenoID(adjancecyListDocTerm[inst1],adjancecyListDocTerm[inst2]);
                            if(prox != 0){
                                outputFile.write((int)data.instance(inst1).value(0) + "," + (int)data.instance(inst2).value(0) + "," + prox + "\n");
                            }
                        }
                    }
                }else{
                    for(int inst1=0;inst1<numInsts;inst1++){
                        System.out.println(inst1);
                        for(int inst2=inst1+1;inst2<numInsts;inst2++){
                            double prox = Proximity.calcDistEuclidianaID(data.instance(inst1),data.instance(inst2));
                            if(prox != 0){
                                outputFile.write((int)data.instance(inst1).value(0) + "," + (int)data.instance(inst2).value(0) + "," + prox + "\n");
                            }
                        }
                    }
                }
                Long end = System.currentTimeMillis();
                System.out.println("Time to compute similarities among documents: " + ((end - begin) / 1000) + " seconds.");
                outputFile.close();
            }
            
        }catch(Exception e){
            System.out.println("Error when saving doc-doc proximities.");
            e.printStackTrace();
            System.exit(0);
        }
        
        JOptionPane.showMessageDialog(null, "Matriz de Proximidades Gerada com Sucesso");
        
    }//GEN-LAST:event_bCalcularActionPerformed

    private void bProcurarDirInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirInActionPerformed
        tEntrada.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirInActionPerformed

    private void bProcurarDirIn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirIn1ActionPerformed
        tSaida.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirIn1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCalcular;
    private javax.swing.JButton bFechar;
    private javax.swing.JButton bProcurarDirIn;
    private javax.swing.JButton bProcurarDirIn1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton oCosseno;
    private javax.swing.JRadioButton oEuclidiana;
    private javax.swing.JTextField tEntrada;
    private javax.swing.JTextField tSaida;
    // End of variables declaration//GEN-END:variables
}
