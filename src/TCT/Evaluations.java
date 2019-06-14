//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: February 26, 2015
// Description: Functions to evaluate supervised inductive learning, semi-
//              supervised inductive learning and transductive learning.                  
//*****************************************************************************

package TCT;

import TCTAlgorithms.InductiveSemiSupervised.InductiveSemiSupervisedClassifier;
import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTAlgorithms.TransductiveOneClass.TransductiveLearnerOneClass;
import java.util.ArrayList;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;


public class Evaluations {
    
    
    
    public static void SupervisedInductiveEvaluationOneClass_Clustering(Classifier[] classifiers, Instances dataTest, Integer[][] confusionMatrix, int classeTarget){
        int numClassifiers = classifiers.length;
        for (int inst = 0; inst < dataTest.numInstances(); inst++) {
            //System.out.println("ClassTarget: " + classeTarget);
            try {

                int classeReal = 0;

                // Verifica se a instancia eh da mesma classe de teste
                if (dataTest.instance(inst).classValue() != classeTarget) {
                    classeReal = 1;
                }

                // Captura resultado
                int resultado = 0;
                for(int c=0;c<numClassifiers;c++){
                    resultado = (int) classifiers[c].classifyInstance(dataTest.instance(inst));
                    if (resultado == 0) {
                        break;
                    }
                }
                

                // Adiciona na matriz de confusao
                confusionMatrix[resultado][classeReal]++;
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /* Function to evaluate Supervised Inductive Learning
       classifier - inductive classification model
       dataTest - instances to evaluate the classification model
       confusionMatrix - matrix to store the classification results 
    */
    
    
    
    public static void SupervisedInductiveEvaluationOneClass(Classifier classifier, Instances dataTest, Integer[][] confusionMatrix, int classeTarget){
        for (int inst = 0; inst < dataTest.numInstances(); inst++) {
            //System.out.println("ClassTarget: " + classeTarget);
            try {

                int classeReal = 0;

                // Verifica se a instancia eh da mesma classe de teste
                if (dataTest.instance(inst).classValue() != classeTarget) {
                    classeReal = 1;
                }

                // Captura resultado
                int resultado = (int) classifier.classifyInstance(dataTest.instance(inst));

                // Adiciona na matriz de confusao
                confusionMatrix[resultado][classeReal]++;
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public static void TransductiveEvaluationOneClass(TransductiveLearnerOneClass classifier, Instances dataTest, Integer[][] confusionMatrix, int classeTarget){
        int numClasses = dataTest.numClasses();
        
        Attribute classAttTest = dataTest.attribute(dataTest.numAttributes()-1);
        try{
            
            for(int inst=0;inst<dataTest.numInstances();inst++){
                Instance instance = dataTest.instance(inst);

                int classeReal = 0;

                // Verifica se a instancia eh da mesma classe de teste
                if (instance.classValue() != classeTarget) {
                    classeReal = 1;
                }

                int classValue = -1;
                for(int classe=0;classe<2;classe++){
                    if(classifier.fUnlabeledDocs[inst][classe] == 1){
                        classValue = classe;
                        break;
                    }
                }
                if(classValue == -1){
                    classValue = 0;
                }
                
                confusionMatrix[classValue][classeReal]++;
            }
        }catch(Exception e){
            System.err.println("Classificatin error");
            e.printStackTrace();
            System.exit(0);
        }
    
    }
    
    public static void SupervisedInductiveEvaluation(Classifier classifier, Instances dataTest, Integer[][] confusionMatrix){
        int correct = 0;
        int error = 0;

        for(int i=0;i<dataTest.numInstances();i++){
            Instance inst = dataTest.instance(i);
            double classe = inst.classValue(); 
            try{
                double classPred = -1;
                try{
                    classPred = classifier.classifyInstance(inst);
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(classPred == -1){
                    classPred = 0;
                }
                
                if(classPred == classe){
                    correct++;
                    Integer value = confusionMatrix[(int)classe][(int)classe];
                    confusionMatrix[(int)classe][(int)classe] = value + 1;
                }else{
                    error++;
                    Integer value = confusionMatrix[(int)classPred][(int)inst.classValue()];
                    confusionMatrix[(int)classPred][(int)inst.classValue()] = value + 1;
                }    
            }catch(Exception e){
                System.out.println("Classification Error");
                e.printStackTrace();
                
                System.exit(0);
            }
            
        }
    }
    
    /* Function to evaluate Semi-Supervised Inductive Learning
       classifier - inductive classification model generated considering labeled and unlabeled data
       dataTest - instances to evaluate the classification model
       confusionMatrix - matrix to store the classification results 
    */
    public static void SemiSupervisedInductiveEvaluation(InductiveSemiSupervisedClassifier classifier, Instances dataTest, Integer[][] confusionMatrix){
        int correct = 0;
        int error = 0;

        for(int i=0;i<dataTest.numInstances();i++){
            Instance inst = dataTest.instance(i);
            double classe = inst.classValue();
            try{
                double classPred = classifier.classifyInstance(inst);
                if(classPred == -1){
                    classPred = 0;
                }
                if(classPred == classe){
                    correct++;
                    Integer value = confusionMatrix[(int)classe][(int)classe];
                    confusionMatrix[(int)classe][(int)classe] = value + 1;
                }else{
                    error++;
                    Integer value = confusionMatrix[(int)classPred][(int)inst.classValue()];
                    confusionMatrix[(int)classPred][(int)inst.classValue()] = value + 1;
                }    
            }catch(Exception e){
                System.out.println("Classification Error");
                e.printStackTrace();
                System.exit(0);
            }
            
        }
    }
    
    /* Function to evaluate Semi-Supervised Inductive Learning
       classifier - inductive classification model generated considering only labeled data
       dataTest - instances to evaluate the classification model
       confusionMatrix - matrix to store the classification results 
    */
    public static void SemiSupervisedInductiveEvaluation(Classifier classifier, Instances dataTest, Integer[][] confusionMatrix){
        int correct = 0;
        int error = 0;

        for(int i=0;i<dataTest.numInstances();i++){
            Instance inst = dataTest.instance(i);
            double classe = inst.classValue();
            try{
                double classPred = classifier.classifyInstance(inst);
                if(classPred == -1){
                    classPred = 0;
                }
                if(classPred == classe){
                    correct++;
                    Integer value = confusionMatrix[(int)classe][(int)classe];
                    confusionMatrix[(int)classe][(int)classe] = value + 1;
                }else{
                    error++;
                    Integer value = confusionMatrix[(int)classPred][(int)inst.classValue()];
                    confusionMatrix[(int)classPred][(int)inst.classValue()] = value + 1;
                }
            }catch(Exception e){
                System.out.println("Classification Error");
                e.printStackTrace();
                System.exit(0);
            }
            
        }
    }
    
    /* Function to evaluate an ensemble of inductive learning classification algorithms considering the sum of classification confidences in each view
       classifier1 - inductive classification model from View #1
       classifier2 - inductive classification model from View #2
       dataTestView1 - test instances from View #1
       dataTestView2 - test instances from View #2
       confusionMatrix - matrix to store the classification results 
       weightView1 - weight of the View #1
    */
    public synchronized static void SupervisedInductiveEvaluationEnsembleMultiview(Classifier classifier1, Classifier classifier2, Instances dataTestView1, Instances dataTestView2, Integer[][] confusionMatrix, double weightView1){
        int correct = 0;
        int error = 0;

        int numClasses = dataTestView1.numClasses();
        
        for(int inst=0;inst<dataTestView1.numInstances();inst++){
            if(dataTestView1.instance(inst) == null || dataTestView2.instance(inst) == null){
                System.out.println("Aqui!!");
            }
            
            Instance instView1 = dataTestView1.instance(inst);
            Instance instView2 = dataTestView2.instance(inst);
            int classReal = (int)instView1.classValue();
            try{
                
                if(classifier1 == null || classifier2 == null){
                    System.out.println("Aqui!");
                }
                
                double[] classPredView1 = classifier1.distributionForInstance(instView1);
                double[] classPredView2 = classifier2.distributionForInstance(instView2);
                
                double[] classPred = new double[numClasses];
                for(int c=0;c<numClasses;c++){
                    classPred[c] = (weightView1 * classPredView1[c]) + ((1 - weightView1) * classPredView2[c]);
                }
                    
                double largest = Double.MIN_VALUE + -1;
                int ind = -1;
                for(int c=0;c<numClasses;c++){
                    if(classPred[c] > largest){
                        largest = classPred[c];
                        ind = c;
                    }
                }
                
                int classPredicted = ind;    
                
                if(classPredicted == classReal){
                    correct++;
                    Integer value = confusionMatrix[classReal][classReal];
                    confusionMatrix[classReal][classReal] = value + 1;
                }else{
                    error++;
                    Integer value = confusionMatrix[classPredicted][classReal];
                    confusionMatrix[classPredicted][classReal] = value + 1;
                }    
            }catch(Exception e){
                //System.out.println("Classification Error");
                e.printStackTrace();
                //System.exit(0);
                System.out.println("Aqui!!!!");
                
                double[] classPredView1 = null;
                double[] classPredView2 = null;
                
                try{
                    classPredView1 = classifier1.distributionForInstance(instView1);
                    classPredView2 = classifier2.distributionForInstance(instView2);
                }catch(Exception ex){
                    System.err.println("Pau2");
                    continue;
                }
                
                
                double[] classPred = new double[numClasses];
                for(int c=0;c<numClasses;c++){
                    classPred[c] = (weightView1 * classPredView1[c]) + ((1 - weightView1) * classPredView2[c]);
                }
                    
                double largest = Double.MIN_VALUE + -1;
                int ind = -1;
                for(int c=0;c<numClasses;c++){
                    if(classPred[c] > largest){
                        largest = classPred[c];
                        ind = c;
                    }
                }
                
                int classPredicted = ind;    
                
                if(classPredicted == classReal){
                    correct++;
                    Integer value = confusionMatrix[classReal][classReal];
                    confusionMatrix[classReal][classReal] = value + 1;
                }else{
                    error++;
                    Integer value = confusionMatrix[classPredicted][classReal];
                    confusionMatrix[classPredicted][classReal] = value + 1;
                } 
                
            }
            
        }
    }
    
    /* Function to evaluate an ensemble of inductive learning classification algorithms. The vote of each classifier is weighted by its classification accuracy.
       classifier1 - inductive classification model from View #1
       classifier2 - inductive classification model from View #2
       dataTestView1 - test instances from View #1
       dataTestView2 - test instances from View #2
       confusionMatrix - matrix to store the classification results 
       weightView1 - weight of the View #1
    */
    public static void SupervisedInductiveEvaluationEnsembleMultiview2(Classifier classifier1, Classifier classifier2, Instances dataTrainView1, Instances dataTestView1, Instances dataTrainView2, Instances dataTestView2, Integer[][] confusionMatrix, double weightView1){
        int correct = 0;
        int error = 0;

        int numClasses = dataTrainView1.numClasses();
        
        for(int inst=0;inst<dataTrainView1.numInstances();inst++){
            Instance instView1 = dataTrainView1.instance(inst);
            int classReal = (int)instView1.classValue();
            try{
                
                int classPredView1 = (int)classifier1.classifyInstance(instView1);
                
                if(classPredView1 == classReal){
                    correct++;
                }else{
                    error++;
                }    
            }catch(Exception e){
                System.out.println("Classification Error");
                e.printStackTrace();
                System.exit(0);        
            }
            
        }
        double accuracyView1 = ((double)correct / (double)dataTrainView1.numInstances())*100;
        
        correct = 0;
        error = 0;
        
        for(int inst=0;inst<dataTrainView2.numInstances();inst++){
            Instance instView2 = dataTrainView2.instance(inst);
            int classReal = (int)instView2.classValue();
            try{
                int classPredView2 = (int)classifier2.classifyInstance(instView2);
                
                if(classPredView2 == classReal){
                    correct++;
                }else{
                    error++;
                }    
            }catch(Exception e){
                System.out.println("Classification Error");
                e.printStackTrace();
                System.exit(0);
            }
            
        }
        double accuracyView2 = ((double)correct / (double)dataTrainView2.numInstances())*100;
        
        correct = 0;
        error = 0;
        
        for(int inst=0;inst<dataTestView1.numInstances();inst++){
            Instance instView1 = dataTestView1.instance(inst);
            Instance instView2 = dataTestView2.instance(inst);
            int classReal = (int)instView1.classValue();
            try{
                double[] vectorClassPredView1 = classifier1.distributionForInstance(instView1);
                double[] vectorClassPredView2 = classifier2.distributionForInstance(instView2);
                
                int classPredView1 = (int)classifier1.classifyInstance(instView1);
                int classPredView2 = (int)classifier2.classifyInstance(instView2);
                
                double[] classPred = new double[numClasses];
                for(int c=0;c<numClasses;c++){
                    classPred[c] = (weightView1 * vectorClassPredView1[c] * accuracyView1) + ((1 - weightView1) * vectorClassPredView2[c] * accuracyView2);
                }
                    
                double largest = Double.MIN_VALUE + -1;
                int ind = -1;
                for(int c=0;c<numClasses;c++){
                    if(classPred[c] > largest){
                        largest = classPred[c];
                        ind = c;
                    }
                }
                
                int classPredicted = ind;    
                
                if(classPredicted == classReal){
                    correct++;
                    Integer value = confusionMatrix[classReal][classReal];
                    confusionMatrix[classReal][classReal] = value + 1;
                }else{
                    error++;
                    Integer value = confusionMatrix[classPredicted][classReal];
                    confusionMatrix[classPredicted][classReal] = value + 1;
                }    
            }catch(Exception e){
                System.out.println("Classification Error");
                e.printStackTrace();
                System.exit(0);
            }
            
        }
    }
    
    /* Function to evaluate an ensemble of inductive learning classification algorithms. Test instances are classified according to the highest classification confidence of a classifier.
       classifier1 - inductive classification model from View #1
       classifier2 - inductive classification model from View #2
       dataTestView1 - test instances from View #1
       dataTestView2 - test instances from View #2
       confusionMatrix - matrix to store the classification results 
       weightView1 - weight of the View #1
    */
    public static void SupervisedInductiveEvaluationEnsembleMultiview3(Classifier classifier1, Classifier classifier2, Instances dataTestView1, Instances dataTestView2, Integer[][] confusionMatrix, double weightView1){
        int correct = 0;
        int error = 0;

        int numClasses = dataTestView1.numClasses();
        
        for(int inst=0;inst<dataTestView1.numInstances();inst++){
            Instance instView1 = dataTestView1.instance(inst);
            Instance instView2 = dataTestView2.instance(inst);
            int classReal = (int)instView1.classValue();
            try{
                double[] classPredView1 = classifier1.distributionForInstance(instView1);
                double[] classPredView2 = classifier2.distributionForInstance(instView2);
                
                int classPredictedView1 = (int)classifier1.classifyInstance(instView1);
                int classPredictedView2 = (int)classifier2.classifyInstance(instView2);
                
                int classPredicted = 0;
                if(classPredictedView1 == classPredictedView2){
                    classPredicted = classPredictedView1;
                }else{
                    if((weightView1 * classPredView1[classPredictedView1]) > ((1 - weightView1) * classPredView2[classPredictedView2])){
                        classPredicted = classPredictedView1;
                    }else{
                        classPredicted = classPredictedView2;
                    }
                }
                
                if(classPredicted == classReal){
                    correct++;
                    Integer value = confusionMatrix[classReal][classReal];
                    confusionMatrix[classReal][classReal] = value + 1;
                }else{
                    error++;
                    Integer value = confusionMatrix[classPredicted][classReal];
                    confusionMatrix[classPredicted][classReal] = value + 1;
                }    
            }catch(Exception e){
                System.out.println("Classification Error");
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
    
    /* Function to evaluate an ensemble of inductive learning classification algorithms. The highest confidence classifications are inserted as labed instances in labeled insance set.
       classifier1 - inductive classification model from View #1
       classifier2 - inductive classification model from View #2
       dataTestView1 - test instances from View #1
       dataTestView2 - test instances from View #2
       confusionMatrix - matrix to store the classification results 
       weightView1 - weight of the View #1
    */
    public static void TransductiveMultiviewEvaluation(ArrayList<Classifier> classifiers, ArrayList<Instances> trainViews, ArrayList<Instances> testViews, Integer [][] confusionMatrix, int numIsndAddTrain){
        
        int numClasses = testViews.get(0).numClasses();
        
        boolean exit = false;
        int numInst = 1;
        while(exit == false){
            int numInstTest = testViews.get(0).numInstances();
            double largestGeneralVote = Double.MIN_VALUE * -1;
            int largestClassGeneralVote = -1;
            int indLargestGeneralVote = -1;
            for(int inst=0;inst<numInstTest;inst++){
                int[] votes = new int[numClasses];
                double[] confidences = new double[numClasses];
                for(int classif=0;classif<classifiers.size();classif++){
                    Classifier classifier = classifiers.get(classif);
                    Instance instance = testViews.get(classif).instance(inst);
                    try{
                        int classPredicted = (int)classifier.classifyInstance(instance);
                        votes[classPredicted] = votes[classPredicted] + 1;
                        confidences[classPredicted] = confidences[classPredicted] + classifier.distributionForInstance(instance)[classPredicted];
                    }catch(Exception e){
                        System.out.println("Classification error");
                        e.printStackTrace();
                        System.exit(0);
                    }
                }
                //Obtendo o largest voto
                int currentLargestVote = -1;
                int classCurrentLargestVote = -1;
                double conficenceCurrentLargestVote = -1;
                for(int classe=0;classe<numClasses;classe++){
                    if(votes[classe] > currentLargestVote){
                        currentLargestVote = votes[classe];
                        classCurrentLargestVote = classe;
                        conficenceCurrentLargestVote = confidences[classe];
                    }else if(votes[classe] == currentLargestVote){
                        if(confidences[classe] > conficenceCurrentLargestVote){
                            conficenceCurrentLargestVote = confidences[classe];
                            classCurrentLargestVote = classe;
                        }
                    }
                }
                if(currentLargestVote > largestGeneralVote){
                    largestGeneralVote = currentLargestVote;
                    largestClassGeneralVote = classCurrentLargestVote;
                    indLargestGeneralVote = inst;
                }
            }
            
            if(indLargestGeneralVote == -1){
                return;
            }
            
            //Preenchendo a matrix de confusão
            int classPred = largestClassGeneralVote;
            int classReal = (int)testViews.get(0).instance(indLargestGeneralVote).classValue();
            if(classPred == classReal){
                Integer value = confusionMatrix[classReal][classReal];
                confusionMatrix[classReal][classReal] = value + 1;
            }else{
                Integer value = confusionMatrix[classPred][classReal];
                confusionMatrix[classPred][classReal] = value + 1;
            }
            
            for(int vis=0;vis<testViews.size();vis++){
                Instance instance = testViews.get(vis).instance(indLargestGeneralVote);
                instance.setClassValue(largestClassGeneralVote);
                trainViews.get(vis).add(instance);
                testViews.get(vis).delete(indLargestGeneralVote);
            }
            
            numInst++;
            if((numInst == numIsndAddTrain)){
                exit = true;
            }
        }
    }
    
    /* Function to evaluate Transductive SelfTrainig The highest confidence classifications are inserted as labed instances in labeled insance set.
       classifier - inductive classification model generated considering only labeled data. 
       dataTrain - set of labeled instances
       dataTest - set of unlabeled instances which are also used to evaluate transductive learning
       confusionMatrix - matrix to store the classification results 
       numIsndAddTrain - number of instances with the highest classification confidences which will be inserted into dataTrain
    */
    public static void SelfTrainingEvaluation(Classifier classifier, Instances dataTrain, Instances dataTest, Integer[][] confusionMatrix, int numIsndAddTrain){
        
        int numClasses = dataTrain.numClasses();
        try{
            boolean exit = false;
            int numInst = 1;
            while(exit == false){
                double largestConf = Double.MIN_VALUE * -1;
                int indLargestConf = -1;        
                for(int inst=0;inst<dataTest.numInstances();inst++){
                    Instance instance = dataTest.instance(inst);
                    double[] confClassificacao = classifier.distributionForInstance(instance);
                    int indClasse = (int)classifier.classifyInstance(instance);
                    double conf = confClassificacao[indClasse];
                    if(conf > largestConf){
                        largestConf = conf;
                        indLargestConf = inst;
                    }
                }
                
                Instance instance = dataTest.instance(indLargestConf);
                int classPred = (int)classifier.classifyInstance(instance);
                int classReal = (int)instance.classValue();

                if(classPred == classReal){
                    Integer value = confusionMatrix[classReal][classReal];
                    confusionMatrix[classReal][classReal] = value + 1;
                }else{
                    Integer value = confusionMatrix[classPred][classReal];
                    confusionMatrix[classPred][classReal] = value + 1;
                }
                instance.setClassValue(classPred);
                dataTrain.add(instance);
                dataTest.delete(indLargestConf);
                
                numInst++;
                if((numInst == numIsndAddTrain)||(dataTest.numInstances()==0)){
                    exit = true;
                }
            }
        }catch(Exception e){
            System.out.println("Classification Error");
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    /* Function to evaluate Transductive CoTraining. The highest confidence classifications in View #2 are inserted as labed instances in View #1.
       classifierView1 - inductive classification model from View #1
       classifier2 - inductive classification model from View #2
       dataTrainView1 - labeled intances from View #1
       dataTrainView2 - labeled intances from View #2
       dataTestView1 - test instances from View #1
       dataTestView2 - test instances from View #2
       confusionMatrix - matrix to store the classification results 
       numIsndAddTrain - number of instances with the highest classification confidences which will be inserted into dataTrain
    */
    public static void CoTrainingEvaluation1(Classifier classifierView1, Classifier classifierView2, Instances dataTrainView1, Instances dataTestView1, Instances dataTrainView2, Instances dataTestView2, Integer[][] confusionMatrix, int numIsndAddTrain){
        
        int numClasses = dataTrainView1.numClasses();
        try{
            boolean exit = false;
            int numInst = 1;
            while(exit == false){
                double largestConf = Double.MIN_VALUE * -1;
                int indLargestConf = -1;        
                for(int inst=0;inst<dataTestView1.numInstances();inst++){
                    Instance instance = dataTestView1.instance(inst);
                    double[] confClassificacao = classifierView1.distributionForInstance(instance);
                    int indClasse = (int)classifierView1.classifyInstance(instance);
                    double conf = confClassificacao[indClasse];
                    if(conf > largestConf){
                        largestConf = conf;
                        indLargestConf = inst;
                    }
                }
                
                if(indLargestConf == -1){
                    break;
                }
                
                Instance instanceView1 = dataTestView1.instance(indLargestConf);
                int classPred = (int)classifierView1.classifyInstance(instanceView1);
                int classReal = (int)instanceView1.classValue();

                if(classPred == classReal){
                    Integer value = confusionMatrix[classReal][classReal];
                    confusionMatrix[classReal][classReal] = value + 1;
                }else{
                    Integer value = confusionMatrix[classPred][classReal];
                    confusionMatrix[classPred][classReal] = value + 1;
                }
                Instance instanceView2 = dataTestView2.instance(indLargestConf);
                instanceView2.setClassValue(classPred);
                dataTrainView2.add(instanceView2);
                dataTestView1.delete(indLargestConf);
                dataTestView2.delete(indLargestConf);
                
                numInst++;
                if((numInst == numIsndAddTrain)||(dataTestView1.numInstances()==0)){
                    exit = true;
                }
            }
        }catch(Exception e){
            System.out.println("Classification error");
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    /* Function to evaluate Transductive CoTraining. The highest confidence classifications in View #2 are inserted as labed instances in View #1. 
       classifierView1 - inductive classification model from View #1
       classifier2 - inductive classification model from View #2
       dataTrainView1 - labeled intances from View #1
       dataTrainView2 - labeled intances from View #2
       dataTestView1 - test instances from View #1
       dataTestView2 - test instances from View #2
       confusionMatrix - matrix to store the classification results 
       numIsndAddTrain - number of instances with the highest classification confidences which will be inserted into dataTrain
    */
    public static void CoTrainingEvaluation2(Classifier classifierView1, Classifier classifierView2, Instances dataTrainView1, Instances dataTestView1, Instances dataTrainView2, Instances dataTestView2, Integer[][] confusionMatrix, int numIsndAddTrain){
        
        int numClasses = dataTrainView2.numClasses();
        try{
            boolean exit = false;
            int numInst = 1;
            while(exit == false){
                double largestConf = Double.MIN_VALUE * -1;
                int indLargestConf = -1;        
                for(int inst=0;inst<dataTestView2.numInstances();inst++){
                    Instance instance = dataTestView2.instance(inst);
                    double[] confClassificacao = classifierView2.distributionForInstance(instance);
                    int indClasse = (int)classifierView2.classifyInstance(instance);
                    double conf = confClassificacao[indClasse];
                    if(conf > largestConf){
                        largestConf = conf;
                        indLargestConf = inst;
                    }
                }
                
                if(indLargestConf == -1){
                    break;
                }
                
                Instance instanceView2 = dataTestView2.instance(indLargestConf);
                int classPred = (int)classifierView2.classifyInstance(instanceView2);
                int classReal = (int)instanceView2.classValue();

                Instance instanceView1 = dataTestView1.instance(indLargestConf);
                instanceView1.setClassValue(classPred);
                dataTrainView1.add(instanceView1);
                dataTestView1.delete(indLargestConf);
                dataTestView2.delete(indLargestConf);
                
                numInst++;
                if((numInst == numIsndAddTrain)||(dataTestView2.numInstances()==0)){
                    exit = true;
                }
            }
        }catch(Exception e){
            System.out.println("Houve um error ao classificar exemplos");
            e.printStackTrace();
        }
    }
    
    /* Function to evaluate Transductive Learning
       classifier - transductive classifier which stores the classification results in fUnlabeledDocs[][]
       dataTest - instances to evaluate the classification model
       confusionMatrix - matrix to store the classification results 
    */
    public static void TransductiveEvaluation(TransductiveLearner classifier, Instances dataTest, Integer[][] confusionMatrix){
        int numClasses = dataTest.numClasses();
        
        Attribute classAttTest = dataTest.attribute(dataTest.numAttributes()-1);
        try{
            
            for(int inst=0;inst<dataTest.numInstances();inst++){
                Instance instance = dataTest.instance(inst);

                int classValue = -1;
                for(int classe=0;classe<numClasses;classe++){
                    if(classifier.fUnlabeledDocs[inst][classe] == 1){
                        classValue = classe;
                        break;
                    }
                }
                if(classValue == -1){
                    classValue = 0;
                }
                
                if(instance.classValue() == classValue){
                    Integer value = confusionMatrix[classValue][classValue];
                    confusionMatrix[classValue][classValue] = value + 1;
                }else{
                    Integer value = confusionMatrix[(int)classValue][(int)instance.classValue()];
                    confusionMatrix[(int)classValue][(int)instance.classValue()] = value + 1;
                }
            }
        }catch(Exception e){
            System.err.println("Classificatin error");
            e.printStackTrace();
            System.exit(0);
        }
    
    }
}
