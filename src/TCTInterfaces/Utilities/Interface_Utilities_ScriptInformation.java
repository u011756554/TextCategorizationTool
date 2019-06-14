//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************      

package TCTInterfaces.Utilities;

import TCT.TransductiveClassification_DocDoc_DocTerm;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_DocTermAndTermTermRelations;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_FewLabeledExamples;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_SelfTraining;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocRelations_ID;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndDocDocRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndTermTermRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_TermTermRelations;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Interface_Utilities_ScriptInformation extends javax.swing.JInternalFrame {

    public Interface_Utilities_ScriptInformation() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tLocal = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        oIndutivo = new javax.swing.JRadioButton();
        oIndPoucosEx = new javax.swing.JRadioButton();
        oIndutivoDTeTT = new javax.swing.JRadioButton();
        oTransdutivoDDcomID = new javax.swing.JRadioButton();
        oTransdutivoDDouDT = new javax.swing.JRadioButton();
        oTransdutivoDTeTT = new javax.swing.JRadioButton();
        oSelfTraining = new javax.swing.JRadioButton();
        oTransdutivoDTeDD = new javax.swing.JRadioButton();
        oTransdutivoTT = new javax.swing.JRadioButton();
        bInformacoes = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tInformacoes = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setTitle("TCT - Script Information");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Path"));

        jLabel1.setText("Script:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tLocal)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Classification Type"));

        oIndutivo.setSelected(true);
        oIndutivo.setText("Inductive");

        oIndPoucosEx.setText("Inductive Few Labeled Examples");

        oIndutivoDTeTT.setText("Inductive (Doc-Term and Term-Term Relations");

        oTransdutivoDDcomID.setText("Transductive (Doc-Doc Relations with ID)");

        oTransdutivoDDouDT.setText("Transductive (Doc-Doc or Doc-Term Relations)");

        oTransdutivoDTeTT.setText("Transductive (Doc-Term and Term-Terms Relations)");

        oSelfTraining.setText("Self-Training");

        oTransdutivoDTeDD.setText("Transdutive (Doc-Term and Doc-Doc Relation)");

        oTransdutivoTT.setText("Transductive (Term-Term Relations)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oIndPoucosEx)
                    .addComponent(oIndutivoDTeTT)
                    .addComponent(oIndutivo))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oSelfTraining)
                    .addComponent(oTransdutivoDDouDT)
                    .addComponent(oTransdutivoDDcomID))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oTransdutivoDTeTT)
                    .addComponent(oTransdutivoDTeDD)
                    .addComponent(oTransdutivoTT))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(oIndutivo)
                    .addComponent(oSelfTraining)
                    .addComponent(oTransdutivoTT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(oIndPoucosEx)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(oIndutivoDTeTT))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(oTransdutivoDDouDT)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(oTransdutivoDDcomID))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(oTransdutivoDTeTT)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(oTransdutivoDTeDD))))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        bInformacoes.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        bInformacoes.setText("Get Informations");
        bInformacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInformacoesActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Informations"));

        tInformacoes.setColumns(20);
        tInformacoes.setRows(5);
        jScrollPane1.setViewportView(tInformacoes);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(bInformacoes)
                .addGap(470, 470, 470))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bInformacoes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bInformacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInformacoesActionPerformed
        
        try{
            FileInputStream file;
            ObjectInputStream obj;
            File arquivo = new File(tLocal.getText());
            if(!arquivo.isFile()){
                tInformacoes.setText("Arquivo de configuração inválido.");
            }

            file = new FileInputStream(arquivo);
            obj = new ObjectInputStream(file);
            
            if(oIndutivo.isSelected()){
                SupervisedInductiveConfiguration configurationIndutivo = (SupervisedInductiveConfiguration)obj.readObject();
                tInformacoes.setText("configuration Indutivo ======================= \n");
                
                if(configurationIndutivo.isIMBHN()){
                    tInformacoes.append("Número máximo de épocas local: " + configurationIndutivo.getParametersIMBHN2().getMaxNumberIterationsLocal() + "\n");
                    tInformacoes.append("Número máximo de épocas global: " + configurationIndutivo.getParametersIMBHN2().getMaxNumberIterationsGlobal() + "\n");
                    tInformacoes.append("Número máximo de épocas: " + configurationIndutivo.getParametersIMBHN2().getMaxNumberIterations() + "\n");
                    tInformacoes.append("Learning Rate: ");
                    for(int apr=0;apr<configurationIndutivo.getParametersIMBHN2().getErrorCorrectionRates().size();apr++){
                        tInformacoes.append(configurationIndutivo.getParametersIMBHN2().getErrorCorrectionRate(apr) + ",");
                        tInformacoes.append("\n");
                    }
                }
                tInformacoes.append("TGM: " + configurationIndutivo.isTGM() + "\n");
                if(configurationIndutivo.isTGM()){
                    tInformacoes.append("Numero máximo de iterações: " + configurationIndutivo.getParametersTGM().getMaxNumberIterations());
                    tInformacoes.append("Diferença média mínima: " + configurationIndutivo.getParametersTGM().getAvgMinDifference());
                    tInformacoes.append("Damping Factor: " + configurationIndutivo.getParametersTGM().getDampingFactor());
                    tInformacoes.append("UnionSet: " + configurationIndutivo.getParametersTGM().getUnionSet() + "\n");
                    tInformacoes.append("BiggerSet: " + configurationIndutivo.getParametersTGM().getBiggerSet() + "\n");
                    tInformacoes.append("SmallerSet: " + configurationIndutivo.getParametersTGM().getSmallerSet() + "\n");
                    tInformacoes.append("Intersection: " + configurationIndutivo.getParametersTGM().getIntersection() + "\n");
                    tInformacoes.append("Suportes mínimos: ");
                    for(int sup=0;sup<configurationIndutivo.getParametersTGM().getMinSups().size();sup++){
                        tInformacoes.append(configurationIndutivo.getParametersTGM().getMinSup(sup) + ",");
                    }
                    tInformacoes.append("\n");
                }
            }else if(oIndPoucosEx.isSelected()){
                SupervisedInductiveConfiguration_FewLabeledExamples configurationIndutivoPoucosExemplos = (SupervisedInductiveConfiguration_FewLabeledExamples)obj.readObject();
                tInformacoes.setText("configuration Indutivo Poucos Exemplos Rotulados ======================= \n");
                tInformacoes.append("Diretório de Entrada: " + configurationIndutivoPoucosExemplos.getDirEntrada() + "\n");
                tInformacoes.append("Diretório de Saida: " + configurationIndutivoPoucosExemplos.getDirSaida() + "\n");
                tInformacoes.append("Número de Repetições: " + configurationIndutivoPoucosExemplos.getNumReps() + "\n");
                tInformacoes.append("Naive Bayes: " + configurationIndutivoPoucosExemplos.isNB() + "\n");
                tInformacoes.append("Multinomial Naive Bayes: " + configurationIndutivoPoucosExemplos.isMNB() + "\n");
                tInformacoes.append("J48: " + configurationIndutivoPoucosExemplos.isJ48() + "\n");
                tInformacoes.append("SMO: " + configurationIndutivoPoucosExemplos.isSMO() + "\n");
                if(configurationIndutivoPoucosExemplos.isSMO()){
                    tInformacoes.append("- Kernel Linear: " + configurationIndutivoPoucosExemplos.getParametersSMO().isLinearKernel() + "\n");
                    tInformacoes.append("- Kernel Polinomial: " + configurationIndutivoPoucosExemplos.getParametersSMO().isPolyKernel() + "\n");
                    tInformacoes.append("- Kernel RBF: " + configurationIndutivoPoucosExemplos.getParametersSMO().isRbfKernel() + "\n");
                    tInformacoes.append("- Kernel Min: " + configurationIndutivoPoucosExemplos.getParametersSMO().isMinKernel() + "\n");
                    tInformacoes.append("- C: ");
                    for(int c=0;c<configurationIndutivoPoucosExemplos.getParametersSMO().getvalueesC().size();c++){
                        tInformacoes.append(configurationIndutivoPoucosExemplos.getParametersSMO().getvalueC(c) + ",");
                    }
                    tInformacoes.append("\n");
                } 
                tInformacoes.append("MLP: " + configurationIndutivoPoucosExemplos.isMLP() + "\n");
                tInformacoes.append("IMBHN: " + configurationIndutivoPoucosExemplos.isIMBHN() + "\n");
                if(configurationIndutivoPoucosExemplos.isIMBHN()){
                    tInformacoes.append("Número máximo de épocas local: " + configurationIndutivoPoucosExemplos.getParametersIMBHN().getMaxNumberIterationsLocal() + "\n");
                    tInformacoes.append("Número máximo de épocas global: " + configurationIndutivoPoucosExemplos.getParametersIMBHN().getMaxNumberIterationsGlobal() + "\n");
                    tInformacoes.append("Número máximo de épocas: " + configurationIndutivoPoucosExemplos.getParametersIMBHN().getMaxNumberIterations() + "\n");
                    tInformacoes.append("Learning Rate: ");
                    for(int apr=0;apr<configurationIndutivoPoucosExemplos.getParametersIMBHN().getErrorCorrectionRates().size();apr++){
                        tInformacoes.append(configurationIndutivoPoucosExemplos.getParametersIMBHN().getErrorCorrectionRate(apr) + ",");
                        tInformacoes.append("\n");
                    }
                }
                tInformacoes.append("IMBHN2: " + configurationIndutivoPoucosExemplos.isIMBHN2() + "\n");
                if(configurationIndutivoPoucosExemplos.isIMBHN()){
                    tInformacoes.append("Número máximo de épocas local: " + configurationIndutivoPoucosExemplos.getParametersIMBHN2().getMaxNumberIterationsLocal() + "\n");
                    tInformacoes.append("Número máximo de épocas global: " + configurationIndutivoPoucosExemplos.getParametersIMBHN2().getMaxNumberIterationsGlobal() + "\n");
                    tInformacoes.append("Número máximo de épocas: " + configurationIndutivoPoucosExemplos.getParametersIMBHN2().getMaxNumberIterations() + "\n");
                    tInformacoes.append("Learning Rate: ");
                    for(int apr=0;apr<configurationIndutivoPoucosExemplos.getParametersIMBHN2().getErrorCorrectionRates().size();apr++){
                        tInformacoes.append(configurationIndutivoPoucosExemplos.getParametersIMBHN2().getErrorCorrectionRate(apr) + ",");
                        tInformacoes.append("\n");
                    }
                }
            }else if(oIndutivoDTeTT.isSelected()){
                SupervisedInductiveConfiguration_DocTermAndTermTermRelations configurationIndutivoDTeTT = (SupervisedInductiveConfiguration_DocTermAndTermTermRelations)obj.readObject();
                tInformacoes.setText("configuration Indutivo DT e TT ======================= \n");
                tInformacoes.append("Arff entrada: " + configurationIndutivoDTeTT.getArff() + "\n");
                tInformacoes.append("Arquivo de probabilidades: " + configurationIndutivoDTeTT.getArqProb() + "\n");
                tInformacoes.append("Diretório de saída: " + configurationIndutivoDTeTT.getDirSaida() + "\n");
                tInformacoes.append("Number of Folds: " + configurationIndutivoDTeTT.getNumFolds() + "\n");
                tInformacoes.append("Número de Repetições: " + configurationIndutivoDTeTT.getNumReps() + "\n");
                tInformacoes.append("IMHN: " + configurationIndutivoDTeTT.isIMHN() + "\n");
                if(configurationIndutivoDTeTT.isIMHN()){
                    tInformacoes.append("- errorCorrectionRates: ");
                    for(int ap=0;ap<configurationIndutivoDTeTT.getParameters_IMHN().getErrorCorrectionRates().size();ap++){
                        tInformacoes.append(configurationIndutivoDTeTT.getParameters_IMHN().getErrorCorrectionRate(ap) + "\n");
                    }
                    tInformacoes.append("\n");
                    tInformacoes.append("- Errors: ");
                    for(int err=0;err<configurationIndutivoDTeTT.getParameters_IMHN().getErrors().size();err++){
                        tInformacoes.append(configurationIndutivoDTeTT.getParameters_IMHN().getError(err) + "\n");
                    }
                    tInformacoes.append("\n");
                }
                tInformacoes.append("Network Coocorrência: " + configurationIndutivoDTeTT.isSupportNetwork() + "\n");
                if(configurationIndutivoDTeTT.isSupportNetwork()){
                    tInformacoes.append("- Network Threshold: " + configurationIndutivoDTeTT.getParametersSupportNetwork().getThresholdNetwork() + "\n");
                    if(configurationIndutivoDTeTT.getParametersSupportNetwork().getThresholdNetwork()){
                        tInformacoes.append("Thresholdes: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersSupportNetwork().getThresholds().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersSupportNetwork().getThreshold(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                    tInformacoes.append("- Network TopK: " + configurationIndutivoDTeTT.getParametersSupportNetwork().getNetworkTopK() + "\n");
                    if(configurationIndutivoDTeTT.getParametersSupportNetwork().getNetworkTopK()){
                        tInformacoes.append("Ks: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersSupportNetwork().getKs().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersSupportNetwork().getK(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                }
                tInformacoes.append("Network Informação Mútua: " + configurationIndutivoDTeTT.isMutualInformationNetwork() + "\n");
                if(configurationIndutivoDTeTT.isMutualInformationNetwork()){
                    tInformacoes.append("- Network Threshold: " + configurationIndutivoDTeTT.getParametersMutualInformationNetwork().getThresholdNetwork() + "\n");
                    if(configurationIndutivoDTeTT.getParametersMutualInformationNetwork().getThresholdNetwork()){
                        tInformacoes.append("Thresholdes: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersMutualInformationNetwork().getThresholds().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersMutualInformationNetwork().getThreshold(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                    tInformacoes.append("- Network TopK: " + configurationIndutivoDTeTT.getParametersMutualInformationNetwork().getNetworkTopK() + "\n");
                    if(configurationIndutivoDTeTT.getParametersMutualInformationNetwork().getNetworkTopK()){
                        tInformacoes.append("Ks: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersMutualInformationNetwork().getKs().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersMutualInformationNetwork().getK(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                }
                tInformacoes.append("Network Kappa: " + configurationIndutivoDTeTT.isKappaNetwork() + "\n");
                if(configurationIndutivoDTeTT.isKappaNetwork()){
                    tInformacoes.append("- Network Threshold: " + configurationIndutivoDTeTT.getParametersKappaNetwork().getThresholdNetwork() + "\n");
                    if(configurationIndutivoDTeTT.getParametersKappaNetwork().getThresholdNetwork()){
                        tInformacoes.append("Thresholdes: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersKappaNetwork().getThresholds().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersKappaNetwork().getThreshold(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                    tInformacoes.append("- Network TopK: " + configurationIndutivoDTeTT.getParametersKappaNetwork().getNetworkTopK() + "\n");
                    if(configurationIndutivoDTeTT.getParametersKappaNetwork().getNetworkTopK()){
                        tInformacoes.append("Ks: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersKappaNetwork().getKs().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersKappaNetwork().getK(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                }
                tInformacoes.append("Network Shapiro: " + configurationIndutivoDTeTT.isShapiroNetwork() + "\n");
                if(configurationIndutivoDTeTT.isShapiroNetwork()){
                    tInformacoes.append("- Network Threshold: " + configurationIndutivoDTeTT.getParametersShapiroNetwork().getThresholdNetwork() + "\n");
                    if(configurationIndutivoDTeTT.getParametersShapiroNetwork().getThresholdNetwork()){
                        tInformacoes.append("Thresholdes: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersShapiroNetwork().getThresholds().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersShapiroNetwork().getThreshold(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                    tInformacoes.append("- Network TopK: " + configurationIndutivoDTeTT.getParametersShapiroNetwork().getNetworkTopK() + "\n");
                    if(configurationIndutivoDTeTT.getParametersShapiroNetwork().getNetworkTopK()){
                        tInformacoes.append("Ks: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersShapiroNetwork().getKs().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersShapiroNetwork().getK(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                }
                tInformacoes.append("Network QYule: " + configurationIndutivoDTeTT.isYulesQNetwork() + "\n");
                if(configurationIndutivoDTeTT.isYulesQNetwork()){
                    tInformacoes.append("- Network Threshold: " + configurationIndutivoDTeTT.getParametersYulesQNetwork().getThresholdNetwork() + "\n");
                    if(configurationIndutivoDTeTT.getParametersYulesQNetwork().getThresholdNetwork()){
                        tInformacoes.append("Thresholdes: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersYulesQNetwork().getThresholds().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersYulesQNetwork().getThreshold(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                    tInformacoes.append("- Network TopK: " + configurationIndutivoDTeTT.getParametersYulesQNetwork().getNetworkTopK() + "\n");
                    if(configurationIndutivoDTeTT.getParametersYulesQNetwork().getNetworkTopK()){
                        tInformacoes.append("Ks: ");
                        for(int threshold=0;threshold<configurationIndutivoDTeTT.getParametersYulesQNetwork().getKs().size();threshold++){
                            tInformacoes.append(configurationIndutivoDTeTT.getParametersYulesQNetwork().getK(threshold) + ",");
                        }
                        tInformacoes.append("\n");
                    }
                }
            }else if(oSelfTraining.isSelected()){
                TransductiveConfiguration_SelfTraining configurationSelfTraining = (TransductiveConfiguration_SelfTraining)obj.readObject();
                tInformacoes.setText("configuration Self-Training ======================= \n");
                tInformacoes.append("Diretório de Entrada: " + configurationSelfTraining.getDirEntrada() + "\n");
                tInformacoes.append("Diretório de Saida: " + configurationSelfTraining.getDirSaida() + "\n");
                tInformacoes.append("Porcentage: " + configurationSelfTraining.isPorcentage() + "\n");
                tInformacoes.append("Número de Repetições: " + configurationSelfTraining.getNumReps() + "\n");
                tInformacoes.append("Number of labeled intances per class: ");
                for(int ex=0;ex<configurationSelfTraining.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();ex++){
                    tInformacoes.append(configurationSelfTraining.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(ex) + ",");
                }
                tInformacoes.append("\n");
                tInformacoes.append("Multinomial Naive Bayes: " + configurationSelfTraining.isMNB() + "\n");
            }else if(oTransdutivoDDouDT.isSelected()){
                TransductiveConfiguration configurationTransdutivo = (TransductiveConfiguration)obj.readObject();
                tInformacoes.setText("configuration Transdutivo DD ou DT ======================= \n");
                
                
            }else if(oTransdutivoTT.isSelected()){
                TransductiveConfiguration_TermTermRelations configurationTransdutivoTT = (TransductiveConfiguration_TermTermRelations)obj.readObject();
                tInformacoes.setText("configuration Transdutivo TT ======================= \n");
                
            }else if(oTransdutivoDDcomID.isSelected()){
                TransductiveConfiguration_DocDocRelations_ID configurationTransdutivoDDcomID = (TransductiveConfiguration_DocDocRelations_ID)obj.readObject();
                tInformacoes.setText("configuration Transdutivo DD com ID ======================= \n");
                
            }else if(oTransdutivoDTeTT.isSelected()){
                TransductiveConfiguration_DocTermAndTermTermRelations configurationTransdutivoDTeTT = (TransductiveConfiguration_DocTermAndTermTermRelations)obj.readObject();
                tInformacoes.setText("configuration Transdutivo DT e TT ======================= \n");
                
            }else if(oTransdutivoDTeDD.isSelected()){
                TransductiveConfiguration_DocTermAndDocDocRelations configurationTransdutivoDTeDD = (TransductiveConfiguration_DocTermAndDocDocRelations)obj.readObject();
                tInformacoes.setText("configuration Transdutivo DT e DD ======================= \n");
                
            }
            
            obj.close();
        }catch(Exception e){
            tInformacoes.setText(e.getMessage());
        }
        
    }//GEN-LAST:event_bInformacoesActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bInformacoes;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton oIndPoucosEx;
    private javax.swing.JRadioButton oIndutivo;
    private javax.swing.JRadioButton oIndutivoDTeTT;
    private javax.swing.JRadioButton oSelfTraining;
    private javax.swing.JRadioButton oTransdutivoDDcomID;
    private javax.swing.JRadioButton oTransdutivoDDouDT;
    private javax.swing.JRadioButton oTransdutivoDTeDD;
    private javax.swing.JRadioButton oTransdutivoDTeTT;
    private javax.swing.JRadioButton oTransdutivoTT;
    private javax.swing.JTextArea tInformacoes;
    private javax.swing.JTextField tLocal;
    // End of variables declaration//GEN-END:variables
}
