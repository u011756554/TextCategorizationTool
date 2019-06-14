//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************      

package TCTInterfaces.Utilities;

import TCTInterfaces.Menus.Interface_Menu2;
import TCTIO.SearchIO;
import TCTStructures.FileMeasure;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Interface_Utilities_ResultStructuring extends javax.swing.JInternalFrame {

    public Interface_Utilities_ResultStructuring() {
        initComponents();
        this.setVisible(true);
        Interface_Menu2.getDesktop().setSelectedFrame(this);
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
        bProcurarDirIn1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        oMultiClass = new javax.swing.JRadioButton();
        oOneClass = new javax.swing.JRadioButton();
        oMultiLabel = new javax.swing.JRadioButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Structuring the Results");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Directories"));

        jLabel1.setText("Results:");

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
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tDirIn, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                    .addComponent(tDirOut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bProcurarDirIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bProcurarDirIn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tDirIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tDirOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn1))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jButton1.setText("Run");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Learning Type"));

        buttonGroup1.add(oMultiClass);
        oMultiClass.setSelected(true);
        oMultiClass.setText("Multi-Class");

        buttonGroup1.add(oOneClass);
        oOneClass.setText("One-Class");

        buttonGroup1.add(oMultiLabel);
        oMultiLabel.setText("Multi-Label");
        oMultiLabel.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(oMultiClass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addComponent(oMultiLabel)
                .addGap(142, 142, 142)
                .addComponent(oOneClass)
                .addGap(60, 60, 60))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oMultiClass)
                    .addComponent(oOneClass)
                    .addComponent(oMultiLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 13, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        File dir = new File(tDirIn.getText());
        File[] dirFiles = dir.listFiles();
        Arrays.sort(dirFiles);
        String dataset;
        String line;
        ArrayList<FileMeasure> estruturas = new ArrayList<FileMeasure>();
        ArrayList<String> datasets = new ArrayList<String>();
        for(int i=0;i<dirFiles.length;i++){
            if(!dirFiles[i].getAbsolutePath().endsWith(".txt")){
                continue;
                
            }
            try{
                RandomAccessFile file = new RandomAccessFile(dirFiles[i], "r");
                FileMeasure est = new FileMeasure();
                est.setFileName(dirFiles[i].getName());
                dataset = dirFiles[i].getName();
                dataset = dataset.substring(0,dataset.indexOf("_"));
                if(!datasets.contains(dataset)){
                    datasets.add(dataset);
                }
                while((line = file.readLine())!=null){
                    if(line.contains("Taxa de Acerto Media") | line.contains("Average Accuracy")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setAccuracy(Double.parseDouble(infs[1]));
                    }else if(line.contains("Desvio Padr") || line.contains("Standard Deviation")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setStdDev(Double.parseDouble(infs[1]));
                    }else if(line.contains("Tempo m") || line.contains("Time")){
                        if(line.contains("constr") || line.contains("Model")){
                            String infs[] = line.split(": ");
                            est.getMeasures().setBuildingTime(Double.parseDouble(infs[1]));
                        }else{
                            String infs[] = line.split(": ");
                            est.getMeasures().setClassificationTime(Double.parseDouble(infs[1]));
                        }
                    }else if((line.contains("mero") && line.contains("dio de itera")) || line.contains("Average Number of")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setNumIterations(Double.parseDouble(infs[1]));
                    }else if(line.contains("MicroPreci") || line.contains("Average Micro-Precision")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setMicroPrecision(Double.parseDouble(infs[1]));
                    }else if(line.contains("MicroRevoca") || line.contains("Average Micro-Recall")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setMicroRecall(Double.parseDouble(infs[1]));
                    }else if(line.contains("MacroPreci") || line.contains("Average Macro-Precision")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setMacroPrecision(Double.parseDouble(infs[1]));
                    }else if(line.contains("MacroRevoca") || line.contains("Average Macro-Recall")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setMacroRecall(Double.parseDouble(infs[1]));
                    }else if(line.contains("Average Precision")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setPrecision(Double.parseDouble(infs[1]));
                    }else if(line.contains("Average Recall")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setRecall(Double.parseDouble(infs[1]));
                    }else if(line.contains("Average F1")){
                        String infs[] = line.split(": ");
                        est.getMeasures().setF1(Double.parseDouble(infs[1]));
                    }
                }
                file.close();
                est.getMeasures().setError(100 - est.getMeasures().getAccuracy());
                if(oMultiClass.isSelected()){
                    est.getMeasures().setMicroF1((2*est.getMeasures().getMicroPrecision()*est.getMeasures().getMicroRecall()) / (est.getMeasures().getMicroPrecision() + est.getMeasures().getMicroRecall()));
                    est.getMeasures().setMacroF1((2*est.getMeasures().getMacroPrecision()*est.getMeasures().getMacroRecall()) / (est.getMeasures().getMacroPrecision() + est.getMeasures().getMacroRecall()));
                }
                estruturas.add(est);
            }catch(Exception e){
                System.err.println("Houve uma Exceção ao Processar o Arquivo: " + dirFiles[i].getAbsolutePath());
                e.printStackTrace();
                //System.exit(0);
            }
            
        }
        System.out.println("Saving Result Compilation");
        //Vendo os tipos de arquivos diferentes
        for(int i=0;i<datasets.size();i++){
            dataset = datasets.get(i);
            File file = new File(tDirOut.getText() + "/" + dataset + ".txt");
            int contador = 1;
            boolean exit = false;
            while(exit == false){
                if(!file.exists()){
                    exit = true;
                }else{
                    file = new File(tDirOut.getText() + "/" + dataset + "-" + contador + ".txt");
                    contador++;
                }
            }
            try{
                FileWriter output = new FileWriter(file);
                if(oMultiClass.isSelected()){
                    output.write("Algorithm/Parameters;Accuracy(%); Error(%); Std. Deviation (Accuracy); Micro-Precision; Micro-Recall; Macro-Precision; Macro-Recall; Micro-F1; Macro-F1; Building Time; Classification Time; Nº of Iterations\n");
                    for(int j=0;j<estruturas.size();j++){
                        FileMeasure est = estruturas.get(j);
                        if(est.getFileName().substring(0,est.getFileName().indexOf("_")).equals(dataset)){
                            output.write(est.getFileName() + ";" + est.getMeasures().getAccuracy() + ";");
                            output.write(est.getMeasures().getError() + ";" + est.getMeasures().getStdDev() + ";");
                            output.write(est.getMeasures().getMicroPrecision() + ";" + est.getMeasures().getMicroRecall() + ";");
                            output.write(est.getMeasures().getMacroPrecision() + ";" + est.getMeasures().getMacroRecall() + ";");
                            output.write(est.getMeasures().getMicroF1() + ";" + est.getMeasures().getMacroF1() + ";");
                            output.write(est.getMeasures().getBuildingTime() + ";" + est.getMeasures().getClassificationTime() + ";" + est.getMeasures().getNumIterations() + "\n");
                        }        
                    }    
                }else if(oOneClass.isSelected()){
                    output.write("Algorithm/Parameters;Accuracy(%); Error(%); Std. Deviation (Accuracy); Precision; Recall; F1; Building Time; Classification Time; Nº of Iterations\n");
                    for(int j=0;j<estruturas.size();j++){
                        FileMeasure est = estruturas.get(j);
                        if(est.getFileName().substring(0,est.getFileName().indexOf("_")).equals(dataset)){
                            output.write(est.getFileName() + ";" + est.getMeasures().getAccuracy() + ";");
                            output.write(est.getMeasures().getError() + ";" + est.getMeasures().getStdDev() + ";");
                            output.write(est.getMeasures().getPrecision() + ";" + est.getMeasures().getRecall() + ";");
                            output.write(est.getMeasures().getF1() + ";" + est.getMeasures().getBuildingTime() + ";");
                            output.write(est.getMeasures().getClassificationTime() + ";" + est.getMeasures().getNumIterations() + "\n");
                        }        
                    }
                }
                
                output.close();
            }catch(Exception e){
                System.out.println("Error when saving result file.");
                e.printStackTrace();
                System.exit(0);
            }
            
        }
        System.out.println("Process concluded successfully");
        JOptionPane.showMessageDialog(null, "The results were structured!","Text Categorization Tool", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void bProcurarDirInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirInActionPerformed
        tDirIn.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirInActionPerformed

    private void bProcurarDirIn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirIn1ActionPerformed
        tDirOut.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirIn1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bProcurarDirIn;
    private javax.swing.JButton bProcurarDirIn1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton oMultiClass;
    private javax.swing.JRadioButton oMultiLabel;
    private javax.swing.JRadioButton oOneClass;
    private javax.swing.JTextField tDirIn;
    private javax.swing.JTextField tDirOut;
    // End of variables declaration//GEN-END:variables
}
