//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: February 26, 2015
// Description: Main class. A graphical interface is displayed if there is no args. A corresponding learning is performance accorting to the parametes listed above.
//*****************************************************************************

package TCT;

import TCTConfigurations.ConfigurationBase;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_CoTraining;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_DocTermAndTermTermRelations;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_EnsembleMultiview;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_FewLabeledExamples;
import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration;
import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration_DocTermAndTermTermRelations;
import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration_OnlyLabeledExamples;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_TrainTest;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_SelfTraining;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocRelations_ID;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndDocDocRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndTermTermRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_VSM;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_Multiview;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_OneClass;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_TermTermRelations;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_TrainTest;
import TCTInterfaces.Menus.Interface_Menu2;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Main2 {
    
    public static void main(String args[]) {
        
        /* First arg: classificatoin type (listed above)
           Second arg: configuration file created in graphical interface */
        
        
        if(args.length > 0){
            try{
                
                FileInputStream file;
                ObjectInputStream obj;
                File arquivo = new File(args[0]);
                if(!arquivo.isFile()){
                    System.out.println("Invalid configuration file.");
                    System.exit(0);
                }
                
                file = new FileInputStream(arquivo);
                obj = new ObjectInputStream(file);
                
                ConfigurationBase config = (ConfigurationBase)obj.readObject();
                System.out.println(config.getClass().getName());
                
                if(args.length >= 2){
                    if(args[1].matches("\\d*")){
                        config.setNumThreads(Integer.parseInt(args[1]));
                    }
                }
                
                if(args.length >= 3){
                    if(args[2].matches("\\d*")){
                        config.setNumReps(Integer.parseInt(args[2]));
                    }
                }
                
                if(args.length >= 4){
                    if(args[3].matches("\\d*")){
                        config.setNumFolds(Integer.parseInt(args[3]));
                    }
                }
                
                
                if(config.getClass().getName().equals("TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration")){
                    SupervisedInductiveConfiguration configuration = (SupervisedInductiveConfiguration)config;
                    SupervisedInductiveClassification2.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_SelfTraining")){
                    TransductiveConfiguration_SelfTraining configuration = (TransductiveConfiguration_SelfTraining)config;
                    TransductiveClassification_SelfTraining.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_TermTermRelations")){
                    TransductiveConfiguration_TermTermRelations configuration = (TransductiveConfiguration_TermTermRelations)config;
                    TransductiveClassification_TermTerm.learning(configuration);    
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocRelations_ID")){
                    TransductiveConfiguration_DocDocRelations_ID configuration = (TransductiveConfiguration_DocDocRelations_ID)config;
                    TransductiveClassification_DocDoc_ID.learning(configuration);    
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration")){
                    TransductiveConfiguration configuration = (TransductiveConfiguration)config;
                    TransductiveClassification.learning(configuration);   
                }else if(config.getClass().getName().equals("TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_DocTermAndTermTermRelations")){
                    SupervisedInductiveConfiguration_DocTermAndTermTermRelations configuration = (SupervisedInductiveConfiguration_DocTermAndTermTermRelations)config;
                    SupervisedInductiveClassification_DocTermAndTermTermRelations.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndTermTermRelations")){
                    TransductiveConfiguration_DocTermAndTermTermRelations configuration = (TransductiveConfiguration_DocTermAndTermTermRelations)config;
                    TransductiveClassification_DocTerm_TermTerm.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_FewLabeledExamples")){
                    SupervisedInductiveConfiguration_FewLabeledExamples configuration = (SupervisedInductiveConfiguration_FewLabeledExamples)config;
                    SupervisedInductiveClassification_FewLabeledExamples.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndDocDocRelations")){
                    TransductiveConfiguration_DocTermAndDocDocRelations configuration = (TransductiveConfiguration_DocTermAndDocDocRelations)config;
                    TransductiveClassification_DocDoc_DocTerm.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations")){
                    TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations configuration = (TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations)config;
                    TransductiveClassification_DocDoc_DocTerm_TermTerm.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_EnsembleMultiview")){
                    SupervisedInductiveConfiguration_EnsembleMultiview configuration = (SupervisedInductiveConfiguration_EnsembleMultiview)config;
                    SupervisedInductiveClassification_MultiviewEnsemble.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_VSM")){
                    TransductiveConfiguration_VSM configuration = (TransductiveConfiguration_VSM)config;
                    TransductiveClassification_VSM.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_CoTraining")){
                    TransductiveConfiguration_CoTraining configuration = (TransductiveConfiguration_CoTraining)config;
                    TransductiveClassification_CoTraining.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_Multiview")){
                    TransductiveConfiguration_Multiview configuration = (TransductiveConfiguration_Multiview)config;
                    TransductiveClassification_MultiView.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration configuration")){
                    SemiSupervisedInductiveConfiguration configuration = (SemiSupervisedInductiveConfiguration)config;
                    SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration_OnlyLabeledExamples")){
                    SemiSupervisedInductiveConfiguration_OnlyLabeledExamples configuration = (SemiSupervisedInductiveConfiguration_OnlyLabeledExamples)config;
                    SemiSupervisedInductiveClassification_OnlyLabeledExamples.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration_DocTermAndTermTermRelations")){
                    SemiSupervisedInductiveConfiguration_DocTermAndTermTermRelations configuration = (SemiSupervisedInductiveConfiguration_DocTermAndTermTermRelations)config;
                    SemiSupervisedInductiveClassification_DocTerm_TermTerm.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration_SelfTraining")){
                    TransductiveConfiguration_SelfTraining configuration = (TransductiveConfiguration_SelfTraining)config;
                    SemiSupervisedInductiveClassification_SelfTraining.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.SupervisedInductiveConfiguration_TrainTest")){
                    SupervisedInductiveConfiguration_TrainTest configuration = (SupervisedInductiveConfiguration_TrainTest)config;
                    SupervisedInductiveClassification_TrainTest.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_TrainTest")){
                    TransductiveConfiguration_TrainTest configuration = (TransductiveConfiguration_TrainTest)config;
                    TransductiveClassification_TrainTest.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass")){
                    SupervisedInductiveConfiguraton_OneClass configuration = (SupervisedInductiveConfiguraton_OneClass)config;
                    if(configuration.isSVM()){
                        SupervisedInductiveClassification_SVM_OneClass.learning(configuration);
                    }else{
                        SupervisedInductiveClassification_OneClass.learning(configuration);
                    }
                }else if(config.getClass().getName().equals("TCTConfigurations.TransductiveLearning.TransductiveConfiguration_OneClass")){
                    TransductiveConfiguration_OneClass configuration = (TransductiveConfiguration_OneClass)config;
                    TransductiveClassification_OneClass.learning(configuration);
                }else if(config.getClass().getName().equals("TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples")){
                    SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples configuration = (SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples)config;
                    SupervisedInductiveClassification_OneClass_FewLabeledExamples.learning(configuration);
                }else{
                    System.out.println("Invalid Configuration");
                    System.exit(0);
                }    
                
                obj.close();
                
            }catch(Exception e){
                System.out.println("Configuration object was not created.");
                e.printStackTrace();
                System.exit(0);
            }
            
        }else{
            try {
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
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Interface_Menu2().setVisible(true);
                }
            });   
        }
        
    }
}
