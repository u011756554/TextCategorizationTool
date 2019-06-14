//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTInterfaces.Preprocessing;

import TCTIO.ListFiles;
import TCTIO.SearchIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Interface_Preprocessing_InsertID extends javax.swing.JInternalFrame {


    public Interface_Preprocessing_InsertID() {
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
        tDestino = new javax.swing.JTextField();
        bProcurarDirIn1 = new javax.swing.JButton();
        bGerar = new javax.swing.JButton();
        bFechar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Insert ID");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("<html>\nOriginal Arffs (<b>Only Spase Matrix</b>)\n</html>"));

        jLabel1.setText("Diretory:");

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
                .addComponent(tEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bProcurarDirIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("<html>\nArffs with ID (<b>output</b>)\n</html>"));

        jLabel2.setText("Directory:");

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
                .addComponent(tDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bProcurarDirIn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bProcurarDirIn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        bGerar.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bGerar.setText("Run");
        bGerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGerarActionPerformed(evt);
            }
        });

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bGerar, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bGerar)
                    .addComponent(bFechar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFecharActionPerformed
        this.dispose();
    }//GEN-LAST:event_bFecharActionPerformed

    private void bGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGerarActionPerformed
        
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

                BufferedReader arqEnt = new BufferedReader(new FileReader(filesIn.get(i).getAbsolutePath()));
                FileWriter outputFile = new FileWriter(tDestino.getText() + "/" + filesIn.get(i).getName());            
                String line = "";
                boolean flagData = false;
                int contador = 0;
                
                while((line=arqEnt.readLine())!=null){
                    if(line.toLowerCase().contains("@relation")){
                       outputFile.write(line + "_ID\n\n");
                       outputFile.write("@attribute ID_DOC integer\n");
                    }else if(line.toLowerCase().contains("@data")){
                        flagData = true;
                        outputFile.write(line + "\n");
                    }else if(flagData == true){
                        if(line.startsWith("{")){
                            line = line.substring(1,line.length()-1);
                            outputFile.write("{0 " + contador);
                            String[] parts1 = line.split(",");
                            for(int p=0;p<parts1.length;p++){
                                String[] parts2 = parts1[p].split(" ");
                                int index = Integer.parseInt(parts2[0]);
                                index++;
                                outputFile.write("," + index + " " + parts2[1]);
                            }
                            outputFile.write("}\n");
                            contador++;
                        }else{
                            outputFile.write(contador + ",");
                            outputFile.write(line + "\n");
                            contador++;
                        }
                            
                    }else{
                        outputFile.write(line + "\n");
                    }
                }

                arqEnt.close();
                outputFile.close();
            }
            
        }catch(Exception e){
            System.out.println("Error when preprocessing document-term matrix.");
            e.printStackTrace();
            System.exit(0);
        }
        
        JOptionPane.showMessageDialog(null, "Matriz com ID gerada com sucesso");
        
    }//GEN-LAST:event_bGerarActionPerformed

    private void bProcurarDirInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirInActionPerformed
        tEntrada.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirInActionPerformed

    private void bProcurarDirIn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProcurarDirIn1ActionPerformed
        tDestino.setText(SearchIO.AbreDir());
    }//GEN-LAST:event_bProcurarDirIn1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bFechar;
    private javax.swing.JButton bGerar;
    private javax.swing.JButton bProcurarDirIn;
    private javax.swing.JButton bProcurarDirIn1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField tDestino;
    private javax.swing.JTextField tEntrada;
    // End of variables declaration//GEN-END:variables
}
