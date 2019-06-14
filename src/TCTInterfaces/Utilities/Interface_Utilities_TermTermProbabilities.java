//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************      

package TCTInterfaces.Utilities;

import TCTStructures.NeighborHash;
import TCTIO.ListFiles;
import TCTIO.SearchIO;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JOptionPane;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class Interface_Utilities_TermTermProbabilities extends javax.swing.JInternalFrame {

    public Interface_Utilities_TermTermProbabilities() {
        initComponents();
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bExecutar = new javax.swing.JButton();
        bFechar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tColecao = new javax.swing.JTextField();
        tProb = new javax.swing.JTextField();
        bProcurarDirIn = new javax.swing.JButton();
        bProcurarDirIn1 = new javax.swing.JButton();
        cID = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Term-Term Probabilities");

        bExecutar.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bExecutar.setText("Run");
        bExecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExecutarActionPerformed(evt);
            }
        });

        bFechar.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        bFechar.setText("Close");
        bFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFecharActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Directories"));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Arffs:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Output:");

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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tColecao, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addComponent(tProb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bProcurarDirIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bProcurarDirIn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tColecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cID.setText("Document-Term matrix has ID as the first attribute");

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
                        .addComponent(cID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bExecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bFechar)
                    .addComponent(bExecutar)
                    .addComponent(cID))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bExecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExecutarActionPerformed
        ArrayList<File> filesIn = new ArrayList<File>();
        System.out.println("Obtendo Lista de Arquivos...");
        ListFiles.List(new File(tColecao.getText()), filesIn); //Vetor para armazenar os documentos textuais
        System.out.println("Carregando arquivos Arquivos...");
        for(int i =0;i<filesIn.size();i++){ // criando vetores contendo os atributos e suas frquências em cada documento da coleção

            if(!filesIn.get(i).getAbsolutePath().endsWith(".arff")){
                continue;
            }
            System.out.println(filesIn.get(i).getAbsolutePath());
            System.out.println("Loading ARFF file");

            //HashMap<String,Double> hashProb = new HashMap<String,Double>();
            try{
                //Obtendo nome do arquivo pra definir o arquivo de saída
                String fileName = "";
                fileName = filesIn.get(i).getName();
                fileName = fileName.substring(0, fileName.indexOf("."));
                File output = new File(tProb.getText() + "/" + fileName + ".prb");
                boolean append = false;

                FileWriter outputFile = new FileWriter(output, append);

                ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(filesIn.get(i).getAbsolutePath()); //Carregando arquivo de Dados
                Instances data = trainSource.getDataSet();

                int numClasses = 0;
                Attribute classAtt = null;

                classAtt = data.attribute(data.numAttributes()-1); //Setting the last feature as class
                data.setClass(classAtt);

                int numTerms = data.numAttributes() - 1;
                int numInst = data.numInstances();
                
                NeighborHash[] listaAtrInst = new NeighborHash[numTerms];
                for(int atr1=0;atr1<numTerms;atr1++){
                    listaAtrInst[atr1] = new NeighborHash();
                }
                
                int iniAtr = 0;
                int decAtr = 0;
                if(cID.isSelected()){
                    iniAtr = 1;
                    decAtr = 1;
                }
                for(int term=iniAtr;term<numTerms;term++){
                    for(int inst=0;inst<numInst;inst++){
                        if(data.instance(inst).value(term) > 0){
                            listaAtrInst[term].AddNeighbor(inst, data.instance(inst).value(term));
                        }    
                    }
                }
                
                for(int atr1=iniAtr;atr1<numTerms;atr1++){
                    Set<Integer> listaAtr1 = listaAtrInst[atr1].getNeighbors().keySet();
                    double frequencia = listaAtr1.size();
                    double probabilidade = (double)frequencia / (double)numInst;
                    String index = "" + (atr1 - decAtr);
                    if(probabilidade > 0){
                        outputFile.write(index + " " + probabilidade + "\n");
                    }
                }
                
                for(int atr1=iniAtr;atr1<numTerms;atr1++){
                    System.out.print(atr1 + " ");
                    Set<Integer> listaAtr1 = listaAtrInst[atr1].getNeighbors().keySet();
                    for(int atr2=atr1+1;atr2<numTerms;atr2++){
                        String index = (atr1 - decAtr) + "," + (atr2 - decAtr);
                        
                        double frequencia = 0;
                        for(Integer indAtr1 : listaAtr1){
                            if(listaAtrInst[atr2].getNeighbors().containsKey(indAtr1)){
                                frequencia++;
                            }
                        }
                        
                        double probabilidade = (double)frequencia / (double)numInst;
                        if(probabilidade > 0){
                            //hashProb.put(index, probabilidade);
                            outputFile.write(index + " " + probabilidade + "\n");
                        }
                    }
                    System.out.println();
                }

                outputFile.close();

            }catch(Exception e){
                System.err.println("Error when saving term-term probabilities.");
                e.printStackTrace();
                System.exit(0);
            }
        }
        JOptionPane.showMessageDialog(null, "Process concluded successfully");
    }//GEN-LAST:event_bExecutarActionPerformed

    private void bFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFecharActionPerformed
        this.dispose();
    }//GEN-LAST:event_bFecharActionPerformed

    private void bProcurarDirInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirInActionPerformed
        tColecao.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirInActionPerformed

    private void bProcurarDirIn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirIn1ActionPerformed
        tProb.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirIn1ActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bExecutar;
    private javax.swing.JButton bFechar;
    private javax.swing.JButton bProcurarDirIn;
    private javax.swing.JButton bProcurarDirIn1;
    private javax.swing.JCheckBox cID;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField tColecao;
    private javax.swing.JTextField tProb;
    // End of variables declaration//GEN-END:variables
}
