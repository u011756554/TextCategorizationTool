/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTInterfaces.Menus;

import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration;
import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration_DocTermAndTermTermRelations;
import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration_OnlyLabeledExamples;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_DocTermAndTermTermRelations;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_EnsembleMultiview;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_FewLabeledExamples;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_TrainTest;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass_Clustering;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_CoTraining;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocRelations_ID;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndDocDocRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndTermTermRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_Multiview;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_OneClass;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_SelfTraining;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_TermTermRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_TrainTest;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_VSM;
import TCTConfigurations.UtilitiesFeatureExtractionConfiguration;
import TCTInterfaces.Preprocessing.Interface_Preprocessing_FeatureSelection;
import TCTInterfaces.Preprocessing.Interface_Preprocessing_InsertID;
import TCTInterfaces.Preprocessing.Interface_Preprocessing_TextRepresentation;
import TCTInterfaces.Classification.Interface_SemiSupervisedInductiveClassification_DocTerm_TermTerm;
import TCTInterfaces.Classification.Interface_SemiSupervisedInductiveClassification_OnlyLabeledExamples;
import TCTInterfaces.Classification.Interface_SemiSupervisedInductiveClassification_SelfTraining;
import TCTInterfaces.Classification.Interface_SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc;
import TCTInterfaces.Classification.Interface_SupervisedInductiveClassification2;
import TCTInterfaces.Classification.Interface_SupervisedInductiveClassification_DocTerm_TermTerm;
import TCTInterfaces.Classification.Interface_SupervisedInductiveClassification_FewLabeledExamples;
import TCTInterfaces.Classification.Interface_SupervisedInductiveClassification_MultiviewEnsemble;
import TCTInterfaces.Classification.Interface_SupervisedInductiveClassification_OneClass;
import TCTInterfaces.Classification.Interface_SupervisedInductiveClassification_OneClass_Clustering;
import TCTInterfaces.Classification.Interface_SupervisedInductiveClassification_OneClass_FewLabeledExamples;
import TCTInterfaces.Classification.Interface_SupervisedInductiveClassification_OneClass_SVM;
import TCTInterfaces.Classification.Interface_SupervisedInductiveClassification_TrainTest;
import TCTInterfaces.Classification.Interface_TransductiveClassification;
import TCTInterfaces.Classification.Interface_TransductiveClassification_CoTraining;
import TCTInterfaces.Classification.Interface_TransductiveClassification_DocDocRelations_ID;
import TCTInterfaces.Classification.Interface_TransductiveClassification_DocDoc_DocTerm_TermTerm;
import TCTInterfaces.Classification.Interface_TransductiveClassification_DocTerm_DocDoc;
import TCTInterfaces.Classification.Interface_TransductiveClassification_DocTerm_TermTerm;
import TCTInterfaces.Classification.Interface_TransductiveClassification_Multiview;
import TCTInterfaces.Classification.Interface_TransductiveClassification_OneClass;
import TCTInterfaces.Classification.Interface_TransductiveClassification_SelfTraining;
import TCTInterfaces.Classification.Interface_TransductiveClassification_TermTerm;
import TCTInterfaces.Classification.Interface_TransductiveClassification_TrainTest;
import TCTInterfaces.Classification.Interface_TransductiveClassification_VSM;
import TCTInterfaces.Utilities.Interface_Utilities_DocDocProximities;
import TCTInterfaces.Utilities.Interface_Utilities_FeatureExtraction;
import TCTInterfaces.Utilities.Interface_Utilities_NetworkProperties;
import TCTInterfaces.Utilities.Interface_Utilities_ResultStructuring;
import TCTInterfaces.Utilities.Interface_Utilities_TermTermProbabilities;
import TCTParameters.SupervisedOneClass.ParametersKNNDensity;
import TCTParameters.SupervisedOneClass.ParametersPrototypeBasedClustering;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author rafael
 */
public class Interface_Menu2 extends javax.swing.JFrame {

    //Inductive Supervised Configurations - OneClass
    SupervisedInductiveConfiguraton_OneClass configuration_InductiveSupervised_OneClass;
    SupervisedInductiveConfiguraton_OneClass_Clustering configuration_InductiveSupervised_OneClass_Clustering;
    SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples configuration_InductiveSupervised_OneClass_FewLabeledExamples;
    
    //Transductive Learning Configuration - One Class
    TransductiveConfiguration_OneClass configurationTransductiveLearningOneClass;
    
    //Inductive Supervised Configurations - MultiClass
    SupervisedInductiveConfiguration configurationIndutivo;
    SupervisedInductiveConfiguration_TrainTest configurationIndutivo_TrainTest;
    SupervisedInductiveConfiguration_FewLabeledExamples configurationIndutivoPoucosExemplos;
    SupervisedInductiveConfiguration_DocTermAndTermTermRelations configurationIndutivoDTeTT;
    SupervisedInductiveConfiguration_EnsembleMultiview configurationIndutivoEsembleMultiview;
    
    //Indutive Semi-Supervised Configurations - MultiClass
    SemiSupervisedInductiveConfiguration configurationIndutivoSemissupervisionado;
    SemiSupervisedInductiveConfiguration_OnlyLabeledExamples configurationIndutivoSemissupervisionadoOnlyLabeledExamples;
    SemiSupervisedInductiveConfiguration_DocTermAndTermTermRelations configurationIndutivo_DocTerm_TermTerm;
    TransductiveConfiguration_SelfTraining configurationIndutivo_SelfTraining;
    
    // Transductive Semi-supervised Configurations - Multiclass
    TransductiveConfiguration_SelfTraining configurationSelfTraining;
    TransductiveConfiguration_CoTraining configurationCoTraining;
    TransductiveConfiguration configurationTransdutivo;
    TransductiveConfiguration_TrainTest configurationTransdutivo_TrainTest;
    TransductiveConfiguration_DocTermAndTermTermRelations configurationTransdutivoDTeTT;
    TransductiveConfiguration_TermTermRelations configurationTransdutivoTT;
    TransductiveConfiguration_DocTermAndDocDocRelations configurationTransdutivoDTeDD;
    TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations configurationTransdutivoDDeDTeTT;
    TransductiveConfiguration_DocDocRelations_ID configurationTransdutivoDDcomID;
    TransductiveConfiguration_VSM configurationTransdutivoMEV;
    TransductiveConfiguration_Multiview configurationTransdutivoMultiview;
    
    //Utilities
    UtilitiesFeatureExtractionConfiguration configurationFeatureExtraction;
    
    public Interface_Menu2() {
        
        configuration_InductiveSupervised_OneClass = new SupervisedInductiveConfiguraton_OneClass();
        configuration_InductiveSupervised_OneClass_Clustering = new SupervisedInductiveConfiguraton_OneClass_Clustering();
        configuration_InductiveSupervised_OneClass_FewLabeledExamples = new SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples();
        
        // ===========================================================
        ArrayList<Integer> numNeighbors = new ArrayList<Integer>();
        numNeighbors.add(1);
        numNeighbors.add(3);
        numNeighbors.add(5);
        numNeighbors.add(7);
        numNeighbors.add(9);
        numNeighbors.add(11);
        numNeighbors.add(13);
        numNeighbors.add(15);
        numNeighbors.add(17);
        numNeighbors.add(19);
        configuration_InductiveSupervised_OneClass_FewLabeledExamples.getParametersKNNDensity().setKs(numNeighbors);

        ArrayList<Integer> numClusters = new ArrayList<Integer>();
        numClusters.add(1);
        numClusters.add(3);
        numClusters.add(5);
        numClusters.add(7);
        numClusters.add(9);
        numClusters.add(11);
        numClusters.add(13);
        numClusters.add(15);
        numClusters.add(17);
        numClusters.add(19);
        configuration_InductiveSupervised_OneClass_FewLabeledExamples.getParametersKME().setKs(numClusters);
        // ===========================================================
        
        configurationTransductiveLearningOneClass = new TransductiveConfiguration_OneClass();
        
        configurationIndutivo = new SupervisedInductiveConfiguration();
        configurationIndutivo_TrainTest = new SupervisedInductiveConfiguration_TrainTest();
        configurationIndutivoPoucosExemplos = new SupervisedInductiveConfiguration_FewLabeledExamples();
        configurationIndutivoDTeTT = new SupervisedInductiveConfiguration_DocTermAndTermTermRelations();
        configurationIndutivoEsembleMultiview = new SupervisedInductiveConfiguration_EnsembleMultiview();
        
        configurationIndutivoSemissupervisionado = new SemiSupervisedInductiveConfiguration();
        configurationIndutivoSemissupervisionadoOnlyLabeledExamples = new SemiSupervisedInductiveConfiguration_OnlyLabeledExamples();
        configurationIndutivo_DocTerm_TermTerm = new SemiSupervisedInductiveConfiguration_DocTermAndTermTermRelations();
        configurationIndutivo_SelfTraining = new TransductiveConfiguration_SelfTraining();
        
        configurationSelfTraining = new TransductiveConfiguration_SelfTraining();
        configurationCoTraining = new TransductiveConfiguration_CoTraining(); 
        configurationTransdutivo = new TransductiveConfiguration();
        configurationTransdutivo_TrainTest= new TransductiveConfiguration_TrainTest();
        configurationTransdutivoDTeTT = new TransductiveConfiguration_DocTermAndTermTermRelations();
        configurationTransdutivoTT = new TransductiveConfiguration_TermTermRelations();
        configurationTransdutivoDTeDD = new TransductiveConfiguration_DocTermAndDocDocRelations();
        configurationTransdutivoDDeDTeTT = new TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations();
        configurationTransdutivoDDcomID = new TransductiveConfiguration_DocDocRelations_ID();
        configurationTransdutivoMEV = new TransductiveConfiguration_VSM();
        configurationTransdutivoMultiview = new TransductiveConfiguration_Multiview();
        
        configurationFeatureExtraction = new UtilitiesFeatureExtractionConfiguration();
        
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static JDesktopPane getDesktop(){
        return dDesktop;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem23 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jLabel1 = new javax.swing.JLabel();
        dDesktop = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        mPreProcessing = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        mMultiClasse = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        mOneClass = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        mOCSingleAlgorithmsFewLabeledExamples = new javax.swing.JMenu();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        mTCOC = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        mFeatureExtraction = new javax.swing.JMenuItem();

        jMenuItem23.setText("jMenuItem23");

        jMenu6.setText("jMenu6");

        jMenuItem25.setText("jMenuItem25");

        jMenuItem30.setText("jMenuItem30");

        jMenuItem31.setText("jMenuItem31");

        jMenuItem32.setText("jMenuItem32");

        jMenuItem33.setText("jMenuItem33");

        jMenu11.setText("jMenu11");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Text Categorization Tool");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Developed by Rafael Geraldeli Rossi - Last Update: 2018-01-04");

        mPreProcessing.setText("Pre-Processing       ");
        mPreProcessing.setFont(new java.awt.Font("DejaVu Sans", 0, 24)); // NOI18N

        jMenuItem3.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N
        jMenuItem3.setText("Insert ID in Documet-Term Matrix");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        mPreProcessing.add(jMenuItem3);

        jMenuItem1.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N
        jMenuItem1.setText("Generate Document-Term Matrix");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mPreProcessing.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N
        jMenuItem2.setText("Select Features");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mPreProcessing.add(jMenuItem2);

        jMenuBar1.add(mPreProcessing);

        mMultiClasse.setText("Multi-Class Learning       ");
        mMultiClasse.setFont(new java.awt.Font("DejaVu Sans", 0, 24)); // NOI18N

        jMenu3.setText("Supervised Inductive Classification");
        jMenu3.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N

        jMenuItem4.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jMenuItem4.setText("<html> <center> X-Fold Cross Validation <br> <font size=\"2\">(Vector Space Model, Doc-Doc Networks,<br> Term-Term Networks, and Doc-Term Networks)</font> </center> <html> ");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jMenuItem5.setText("<html> <center> Train-Test Validation <br> <font size=\"2\">(Vector Space Model, Doc-Doc Networks,<br> Term-Term Networks, and Doc-Term Networks)</font> </center> <html> ");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jMenuItem6.setText("<html> <center> X Fold Cross Validation <br> <font size=\"2\">(Networks with Doc-Term and <br>Term-Term Relations)</font> </center> </html>");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem7.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jMenuItem7.setText("<html> <center> X-Fold Cross Validation <br> Multiview Ensemble </center> </html>");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem8.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jMenuItem8.setText("<html> <center> Few Labeled Examples <br> <font size = \"2\">(Vector Space Model and Doc-Term Networks)</font> </center> </html>");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        mMultiClasse.add(jMenu3);

        jMenu2.setText("Semi-Supervised Inductive Classification");
        jMenu2.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N

        jMenuItem11.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jMenuItem11.setText("<html><center>Vector Space Model, Document<br>or  Bipartite Networks</center></html>");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuItem12.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jMenuItem12.setText("<html><center>Term-Term or <br>Doc-Term + Term-Term Relations</center></html> ");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);

        jMenuItem10.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jMenuItem10.setText("Self-Training");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem9.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        jMenuItem9.setText("Supervised Inductive Classification");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        mMultiClasse.add(jMenu2);

        jMenu1.setText("Transductive Classification");
        jMenu1.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N

        jMenu4.setText("Networks");
        jMenu4.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N

        jMenuItem13.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem13.setText("Doc-Doc or Doc-Term Relations");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem13);

        jMenuItem14.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem14.setText("<html><center>Doc-Doc or Doc-Term Relations<br> <font size = \"2\">(Train-Test Validation)</font></center><html>");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem14);

        jMenuItem15.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem15.setText("Term-Term Relations");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem15);

        jMenuItem17.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem17.setText("<html> <center> Doc-Term and Term-Term Relations </center> </html>");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem17);

        jMenuItem18.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem18.setText("<html> Doc-Doc and Doc-Term Relations </html>");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem18);

        jMenuItem19.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem19.setText("<html> <center> Doc-Doc, Doc-Term and <br>Term-Term Relations </center> </html>");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem19);

        jMenuItem16.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem16.setText("<html> Doc-Doc Relations <br> <font size = \"2\">(Representations with ID)</font> </html>");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem16);

        jMenu1.add(jMenu4);

        jMenu5.setText("Vector-Space Model");
        jMenu5.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N

        jMenuItem20.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem20.setText("Self-Training");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem20);

        jMenuItem21.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem21.setText("Co-Training");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem21);

        jMenuItem22.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem22.setText("MultiView Learning");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem22);

        jMenuItem24.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jMenuItem24.setText("<html> <center> Expectation Maximization and <br>  Transductive Support Vector Machines </center> </html>");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem24);

        jMenu1.add(jMenu5);

        mMultiClasse.add(jMenu1);

        jMenuBar1.add(mMultiClasse);

        mOneClass.setText("One-Class Learning       ");
        mOneClass.setFont(new java.awt.Font("DejaVu Sans", 0, 24)); // NOI18N

        jMenu9.setText("Supervised Inductive Learning");
        jMenu9.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N

        mOCSingleAlgorithmsFewLabeledExamples.setText("Single Algorithms");
        mOCSingleAlgorithmsFewLabeledExamples.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N

        jMenuItem35.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jMenuItem35.setText("General Algorithms");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        mOCSingleAlgorithmsFewLabeledExamples.add(jMenuItem35);

        jMenuItem36.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jMenuItem36.setText("One-Class SVM");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        mOCSingleAlgorithmsFewLabeledExamples.add(jMenuItem36);

        jMenuItem38.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jMenuItem38.setText("Few Labeled Examples");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        mOCSingleAlgorithmsFewLabeledExamples.add(jMenuItem38);

        jMenu9.add(mOCSingleAlgorithmsFewLabeledExamples);

        jMenu12.setText("Clustering + Single Algorithms");
        jMenu12.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N

        jMenuItem34.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jMenuItem34.setText("General Algorithms");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem34);

        jMenuItem37.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jMenuItem37.setText("One-Class SVM");
        jMenu12.add(jMenuItem37);

        jMenu9.add(jMenu12);

        mOneClass.add(jMenu9);

        jMenu10.setText("Transductive Learning");
        jMenu10.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N

        mTCOC.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        mTCOC.setText("Vector Space Model");
        mTCOC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mTCOCActionPerformed(evt);
            }
        });
        jMenu10.add(mTCOC);

        mOneClass.add(jMenu10);

        jMenuBar1.add(mOneClass);

        jMenu8.setText("Unsupervised Learning      ");
        jMenu8.setFont(new java.awt.Font("DejaVu Sans", 0, 24)); // NOI18N
        jMenuBar1.add(jMenu8);

        jMenu7.setText("Utilities          ");
        jMenu7.setFont(new java.awt.Font("DejaVu Sans", 0, 24)); // NOI18N

        jMenuItem26.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N
        jMenuItem26.setText("Term-Term Probabilities");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem26);

        jMenuItem27.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N
        jMenuItem27.setText("Doc-Doc Proximities");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem27);

        jMenuItem29.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N
        jMenuItem29.setText("Network Properties");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem29);

        jMenuItem28.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N
        jMenuItem28.setText("Structuring the Results");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem28);

        mFeatureExtraction.setFont(new java.awt.Font("DejaVu Sans", 0, 21)); // NOI18N
        mFeatureExtraction.setText("Feature Extraction");
        mFeatureExtraction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mFeatureExtractionActionPerformed(evt);
            }
        });
        jMenu7.add(mFeatureExtraction);

        jMenuBar1.add(jMenu7);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1469, Short.MAX_VALUE)
                    .addComponent(dDesktop))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dDesktop, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        JInternalFrame frame = new Interface_Preprocessing_InsertID();
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        JInternalFrame frame = new Interface_Preprocessing_TextRepresentation();
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JInternalFrame frame = new Interface_Preprocessing_FeatureSelection();
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JInternalFrame frame = new Interface_SupervisedInductiveClassification2(configurationIndutivo);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        JInternalFrame frame = new Interface_SupervisedInductiveClassification_TrainTest(configurationIndutivo_TrainTest);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        JInternalFrame frame = new Interface_SupervisedInductiveClassification_DocTerm_TermTerm(configurationIndutivoDTeTT);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        JInternalFrame frame = new Interface_SupervisedInductiveClassification_MultiviewEnsemble(configurationIndutivoEsembleMultiview);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        JInternalFrame frame = new Interface_SupervisedInductiveClassification_FewLabeledExamples(configurationIndutivoPoucosExemplos);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        JInternalFrame frame = new Interface_SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc(configurationIndutivoSemissupervisionado);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        JInternalFrame frame = new Interface_SemiSupervisedInductiveClassification_DocTerm_TermTerm(configurationIndutivo_DocTerm_TermTerm);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        JInternalFrame frame = new Interface_SemiSupervisedInductiveClassification_SelfTraining(configurationIndutivo_SelfTraining);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        JInternalFrame frame = new Interface_SemiSupervisedInductiveClassification_OnlyLabeledExamples(configurationIndutivoSemissupervisionadoOnlyLabeledExamples);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification(configurationTransdutivo);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_TrainTest(configurationTransdutivo_TrainTest);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_TermTerm(configurationTransdutivoTT);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_DocTerm_TermTerm(configurationTransdutivoDTeTT);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_DocTerm_DocDoc(configurationTransdutivoDTeDD);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_DocDoc_DocTerm_TermTerm(configurationTransdutivoDDeDTeTT);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_DocDocRelations_ID(configurationTransdutivoDDcomID);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_SelfTraining(configurationSelfTraining);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_CoTraining(configurationCoTraining);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_Multiview(configurationTransdutivoMultiview);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_VSM(configurationTransdutivoMEV);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void mFeatureExtractionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mFeatureExtractionActionPerformed
        JInternalFrame frame = new Interface_Utilities_FeatureExtraction(configurationFeatureExtraction);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mFeatureExtractionActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        JInternalFrame frame = new Interface_Utilities_ResultStructuring();
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        JInternalFrame frame = new Interface_Utilities_NetworkProperties();
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        JInternalFrame frame = new Interface_Utilities_DocDocProximities();
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        JInternalFrame frame = new Interface_Utilities_TermTermProbabilities();
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        JInternalFrame frame = new Interface_SupervisedInductiveClassification_OneClass(configuration_InductiveSupervised_OneClass);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        JInternalFrame frame = new Interface_SupervisedInductiveClassification_OneClass_SVM(configuration_InductiveSupervised_OneClass);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        JInternalFrame frame = new Interface_SupervisedInductiveClassification_OneClass_Clustering(configuration_InductiveSupervised_OneClass_Clustering);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void mTCOCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mTCOCActionPerformed
        JInternalFrame frame = new Interface_TransductiveClassification_OneClass(configurationTransductiveLearningOneClass);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mTCOCActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
        JInternalFrame frame = new Interface_SupervisedInductiveClassification_OneClass_FewLabeledExamples(configuration_InductiveSupervised_OneClass_FewLabeledExamples);
        dDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Interface_Menu2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /*try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface_Menu2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface_Menu2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface_Menu2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface_Menu2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface_Menu2().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JDesktopPane dDesktop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem mFeatureExtraction;
    private javax.swing.JMenu mMultiClasse;
    private javax.swing.JMenu mOCSingleAlgorithmsFewLabeledExamples;
    private javax.swing.JMenu mOneClass;
    private javax.swing.JMenu mPreProcessing;
    private javax.swing.JMenuItem mTCOC;
    // End of variables declaration//GEN-END:variables
}
