//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Inctive Transductive Lerning and Evaluation.
//              The transductive learning algorithms are based on vector-
//              space model.
//*****************************************************************************

package TCT;

import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTAlgorithms.Transductive.ExpectationMaximization_Transductive;
import TCTAlgorithms.Transductive.TSVM_Balanced_Transductive;
import TCTAlgorithms.Transductive.TSVM_Unbalanced_Transductive;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_VSM;
import TCTIO.ListFiles;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TransductiveClassification_VSM {
    
    /*Function to read a document-term matrix and set parameters and algorithms to perform transductive learning */
    public static  void learning(TransductiveConfiguration_VSM configuration){
        
        ArrayList<String> filesIn = ListFiles.listFilesStr(new File(configuration.getDirEntrada()));
        
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
        
        try {
            for (int i = 0; i < filesIn.size(); i++) {

                if (!filesIn.get(i).endsWith(".arff")) {
                    continue;
                }
                System.out.println(filesIn.get(i));
                System.out.println("Loading ARFF file");

                DataSource trainSource = new DataSource(filesIn.get(i));
                Instances dataOriginal = trainSource.getDataSet();

                int numClasses = 0;
                Attribute classAtt = null;

                classAtt = dataOriginal.attribute(dataOriginal.numAttributes() - 1); //Setting the last feature as class
                dataOriginal.setClass(classAtt);
                numClasses = classAtt.numValues();

                for (int j = 0; j < numClasses; j++) {
                    System.out.println(j + ": " + classAtt.value(j));
                }

                StringBuilder outputFile = new StringBuilder();
                StringBuilder outputFilePar = new StringBuilder();
                outputFile.append(configuration.getDirSaida());
                outputFile.append("/");
                outputFile.append(dataOriginal.relationName());
                outputFile.append("_Transductive_");

                for (int numEx = 0; numEx < configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size(); numEx++) {
                    double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                    if (configuration.isEM()) {

                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("_");
                        outputFilePar.append("EM_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        for (int weight = 0; weight < configuration.getParametersEM().getWeightsUnlabeled().size(); weight++) {
                            for (int numComp = 0; numComp < configuration.getParametersEM().getNumCompClasses().size(); numComp++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: Expectation Maximization");
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append(configuration.getParametersEM().getWeightUnlabeledInstances(weight));
                                outputFilePar2.append("_");
                                //System.out.println("Weight Unlabeled Instances: " + configuration.getParametersEM().getWeightUnlabeledInstances(weight));
                                outputFilePar2.append(configuration.getParametersEM().getNumCompClasse(numComp));
                                //System.out.println("Number of Components per Class: " + configuration.getParametersEM().getNumCompClasse(numComp));
                                //outputFilePar2.append(".txt");
                                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    ExpectationMaximization_Transductive classifEM = new ExpectationMaximization_Transductive();
                                    classifEM.setMaxNumIterations(configuration.getParametersEM().getMaxNumberIterations());
                                    classifEM.setMinLogLikelihood(configuration.getParametersEM().getMinLogLikelihood());
                                    classifEM.setWeightUnlabeled(configuration.getParametersEM().getWeightUnlabeledInstances(weight));
                                    classifEM.setNumCompClass(configuration.getParametersEM().getNumCompClasse(numComp));
                                    classifiers[rep] = classifEM;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                            }

                        }
                    }
                    if (configuration.isTSVM()) {
                        if (configuration.getParametersTSVM().getBalanced()) {
                            for (int c = 0; c < configuration.getParametersTSVM().getCs().size(); c++) {
                                double valueC = configuration.getParametersTSVM().getC(c);
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: TSVM - Same Proportion of Labeled Examples");
                                //System.out.println("C: " + valueC);
                                outputFilePar = new StringBuilder();
                                outputFilePar.append(outputFile.toString());
                                outputFilePar.append("_");
                                outputFilePar.append(numLabeledInstances);
                                outputFilePar.append("_");
                                outputFilePar.append("TSVM_SameProportinoLabeledExamples_");
                                if (configuration.isPorcentage() == true) {
                                    outputFilePar.append("percentage");
                                    outputFilePar.append("_");
                                } else {
                                    outputFilePar.append("real");
                                    outputFilePar.append("_");
                                }
                                outputFilePar.append(valueC);
                                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    TSVM_Balanced_Transductive classifTSVM = new TSVM_Balanced_Transductive();
                                    classifTSVM.setC(valueC);
                                    classifTSVM.setMaxNumIterations(configuration.getParametersTSVM().getMaxNumberIterations());
                                    classifiers[rep] = classifTSVM;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numLabeledInstances, numClasses);
                            }
                        }
                        if (configuration.getParametersTSVM().getUnbalanced()) {
                            for (int c = 0; c < configuration.getParametersTSVM().getCs().size(); c++) {
                                double valueC = configuration.getParametersTSVM().getC(c);
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: TSVM");
                                //System.out.println("C: " + valueC);
                                outputFilePar = new StringBuilder();
                                outputFilePar.append(outputFile.toString());
                                outputFilePar.append("_");
                                outputFilePar.append(numLabeledInstances);
                                outputFilePar.append("_");
                                outputFilePar.append("TSVM_");
                                if (configuration.isPorcentage() == true) {
                                    outputFilePar.append("percentage");
                                    outputFilePar.append("_");
                                } else {
                                    outputFilePar.append("real");
                                    outputFilePar.append("_");
                                }
                                outputFilePar.append(valueC);
                                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    TSVM_Unbalanced_Transductive classifTSVM = new TSVM_Unbalanced_Transductive();
                                    classifTSVM.setC(valueC);
                                    classifTSVM.setMaxNumIterations(configuration.getParametersTSVM().getMaxNumberIterations());
                                    classifiers[rep] = classifTSVM;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numLabeledInstances, numClasses);
                            }
                        }

                    }
                }

            }

            threads.shutdown();

            boolean exit = false;
            while (exit == false) {
                if (threads.isTerminated()) {
                    System.out.println("Process concluded successfully");
                    configuration.getEmail().getContent().append(configuration.toString());
                    configuration.getEmail().getContent().append(configuration.toString());
                    configuration.getEmail().send();
                    exit = true;
                } else {
                    Thread.sleep(1000);
                }
            }

        } catch (OutOfMemoryError e) {
            configuration.getEmail().getContent().append("OutOfMemory!!!!");
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().send();
            e.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Error when generating a classifier.");
            configuration.getEmail().getContent().append(e.getMessage());
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().send();
            e.printStackTrace();
            System.exit(0);
        }
        
      
    }

    //Function to run and evaluate transductive learning
    public static void learning(final TransductiveConfiguration_VSM configuration, final ExecutorService threads, final TransductiveLearner[] classifiers, final Instances dataOriginal, String outputFile, final double numInstPerClass, final int numClasses){
        
        try{
            
            final Results results;
            
            final File output = new File(outputFile);
            final File outputResult = new File(output.getAbsolutePath() + ".txt");
            if(outputResult.exists()){
                return;
            }
            final File outputTemp = new File(output.getAbsolutePath() + ".tmp");
            
            if(outputTemp.exists()){
                ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(output.getAbsolutePath() + ".tmp"));
                results = (Results)objInput.readObject();
                objInput.close();
            }else{
                results = new Results(output, configuration.getNumReps(), 1, "Transductive");
            }
            
            //System.out.println("Output: " + output.getAbsolutePath());
            
            for(int rep=0;rep<configuration.getNumReps();rep++){
                
                if(results.getComplete(rep, 0) == true){
                    continue;
                }
                
                final int numRep = rep;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        Integer[][] confusionMatrix = new Integer[numClasses][numClasses]; 
                        for(int class1=0;class1<numClasses;class1++){
                            for(int class2=0;class2<numClasses;class2++){
                                confusionMatrix[class1][class2] = 0;
                            }
                        }

                        Instances dataTrain = new Instances(dataOriginal,0);
                        Instances dataTest = new Instances(dataOriginal,0);

                        Instances data = new Instances(dataOriginal);
                        data.randomize(new Random(numRep));

                        SplitData.splitTrainTestTransductive(configuration, data, dataTrain, dataTest, numInstPerClass);

                        classifiers[numRep].setNumIterations(0);
                        long begin = System.currentTimeMillis();
                        try{
                            classifiers[numRep].buildClassifier(dataTrain, dataTest);
                        }catch(OutOfMemoryError e){
                            e.printStackTrace();
                            System.exit(0);
                        }catch(Exception e){
                            e.printStackTrace();
                            System.exit(0);
                        }
                        
                        long end  = System.currentTimeMillis();
                        results.setBuildingTime(numRep, 0, end - begin);
                        results.setNumIterations(numRep, 0, classifiers[numRep].getNumiterations());
                        begin = System.currentTimeMillis();
                        Evaluations.TransductiveEvaluation(classifiers[numRep], dataTest, confusionMatrix);
                        end = System.currentTimeMillis();
                        results.setClassificationTime(numRep, 0, end - begin);

                        results.computeEvaluationMeasures(confusionMatrix, numClasses, numRep, 0);
                        results.setComplete(numRep, 0, true);
                        
                    }
                });
                
                threads.execute(thread);
                
            }   
            
        }catch(Exception e){
            System.err.println("Error when generating a classifier.");
            e.printStackTrace();
            System.exit(0);
        }
        
    }
    
    /*Function to split text collection into train (labeled) and test (unlabeled) sets. Test data are used as unlabeled data in transductive learning and for evaluation. 
      numInstPerClass instances for each class are selected as labeled examples.*/
    
    
}
