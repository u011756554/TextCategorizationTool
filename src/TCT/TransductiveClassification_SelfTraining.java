//********************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Inctive Transductive Lerning and Evaluation.
//              Self-Training (Zhu & Goldberg, 2009) is used to perform 
//              transductive learning.
// References: - X. Zhu, A. B. Goldberg, Introduction to Semi-Supervised Learning, 
//               Morgan and Claypool Publishers, 2009.
//********************************************************************************

package TCT;

import TCTAlgorithms.InductiveSupervised.KNN_InductiveSupervised;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_SelfTraining;
import TCTIO.ListFiles;
import TCTParameters.SemiSupervisedLearning.Parameters_KNN;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

public class TransductiveClassification_SelfTraining {
    
    /*Function to read a document-term matrix and set parameters and algorithms to perform Self-Training */
    public static  void learning(TransductiveConfiguration_SelfTraining configuration){
        
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
        
        ArrayList<String> filesIn = ListFiles.listFilesStr(new File(configuration.getDirEntrada()));
        
        try {
            for (int i = 0; i < filesIn.size(); i++) {

                if (!filesIn.get(i).endsWith(".arff")) {
                    continue;
                }
                System.out.println(filesIn.get(i));
                System.out.println("Loading ARFF file");

                DataSource trainSource = new DataSource(filesIn.get(i).toString()); //Carregando arquivo de Dados
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
                outputFile.append("_Transductive_SelfTraining_");

                for (int numExInsTrain = 0; numExInsTrain < configuration.getNumInstPerClassAddTraining().size(); numExInsTrain++) {
                    for (int numEx = 0; numEx < configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size(); numEx++) {
                        double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                        if (configuration.isMNB()) {
                            //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                            //System.out.println("Number of documents added to the set of labeled documents: " + (int)configuration.getParametersnumInstPerClassInseridosTreinamento().getnumInstPerClassInseridosTreinamento(numExInsTrain));
                            //System.out.println("Classification Algorithm: Multinomial Naive Bayes");
                            outputFilePar = new StringBuilder();
                            outputFilePar.append(outputFile.toString());
                            outputFilePar.append("MNB_");
                            outputFilePar.append(numLabeledInstances);
                            outputFilePar.append("_");
                            if (configuration.isPorcentage() == true) {
                                outputFilePar.append("percentage");
                                outputFilePar.append("_");
                            } else {
                                outputFilePar.append("real");
                                outputFilePar.append("_");
                            }
                            outputFilePar.append(configuration.getNumInstPerClassAddTraining().get(numExInsTrain));
                            Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                NaiveBayesMultinomial classifMultinomial = new NaiveBayesMultinomial();
                                classifiers[rep] = classifMultinomial;
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numLabeledInstances, numClasses, (int) configuration.getNumInstPerClassAddTraining().get(numExInsTrain));
                        }
                        if (configuration.isKNN()) {
                            Parameters_KNN parametersKNN = configuration.getParametersKNN();
                            outputFilePar = new StringBuilder();
                            outputFilePar.append(outputFile.toString());
                            outputFilePar.append("KNN_");
                            outputFilePar.append(numLabeledInstances);
                            outputFilePar.append("_");
                            for (int viz = 0; viz < parametersKNN.getNeighbors().size(); viz++) {
                                //System.out.println("Classification Algorithm: KNN");
                                //System.out.println("Number of Neighbors: " + parametersKNN.getNeighbor(viz));
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersKNN.getNeighbor(viz));
                                outputFilePar2.append("_");
                                if (parametersKNN.getCosine()) {
                                    StringBuilder outputFilePar3 = new StringBuilder();
                                    outputFilePar3.append(outputFilePar2.toString());
                                    outputFilePar3.append("Cosine_");
                                    if (parametersKNN.isWeighted()) {
                                        StringBuilder outputFilePar4 = new StringBuilder();
                                        outputFilePar4.append(outputFilePar3.toString());
                                        outputFilePar4.append("W");
                                        Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                            KNN_InductiveSupervised classifKnn = new KNN_InductiveSupervised();
                                            classifKnn.setK(parametersKNN.getNeighbor(viz));
                                            classifKnn.setWeightedVote(true);
                                            classifiers[rep] = classifKnn;
                                        }
                                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar4.toString(), numLabeledInstances, numClasses, (int) configuration.getNumInstPerClassAddTraining().get(numExInsTrain));
                                    }
                                    if (parametersKNN.isUnweighted()) {
                                        Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                            KNN_InductiveSupervised classifKnn = new KNN_InductiveSupervised();
                                            classifKnn.setK(parametersKNN.getNeighbor(viz));
                                            classifKnn.setWeightedVote(false);
                                            classifiers[rep] = classifKnn;
                                        }
                                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses, (int) configuration.getNumInstPerClassAddTraining().get(numExInsTrain));
                                    }
                                }
                                if (parametersKNN.getEuclidean()) {
                                    StringBuilder outputFilePar3 = new StringBuilder();
                                    outputFilePar3.append(outputFilePar2.toString());
                                    outputFilePar3.append("Euclidean_");
                                    EuclideanDistance df = new EuclideanDistance();
                                    if (parametersKNN.isWeighted()) {
                                        SelectedTag dist = new SelectedTag(IBk.WEIGHT_INVERSE, IBk.TAGS_WEIGHTING);
                                        StringBuilder outputFilePar4 = new StringBuilder();
                                        outputFilePar4.append(outputFilePar3.toString());
                                        outputFilePar4.append("W");
                                        Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                            IBk classifKnn = new IBk();
                                            classifKnn.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df);
                                            classifKnn.setKNN(parametersKNN.getNeighbor(viz));
                                            classifKnn.setDistanceWeighting(dist);
                                            classifiers[rep] = classifKnn;
                                        }
                                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar4.toString(), numLabeledInstances, numClasses, (int) configuration.getNumInstPerClassAddTraining().get(numExInsTrain));
                                    }
                                    if (parametersKNN.isUnweighted()) {
                                        SelectedTag dist = new SelectedTag(IBk.WEIGHT_NONE, IBk.TAGS_WEIGHTING);
                                        Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                            IBk classifKnn = new IBk();
                                            classifKnn.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df);
                                            classifKnn.setKNN(parametersKNN.getNeighbor(viz));
                                            classifKnn.setDistanceWeighting(dist);
                                            classifiers[rep] = classifKnn;
                                        }
                                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses, (int) configuration.getNumInstPerClassAddTraining().get(numExInsTrain));
                                    }
                                }
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
    
    //Function to run and evaluate Self-Training
    public static void learning(final TransductiveConfiguration_SelfTraining configuration, final ExecutorService threads, final Classifier[] classifiers, final Instances dataOriginal, String outputFile, final double numInstPerClass, final int numClasses, final int numExInstTreino){
        
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

                        Attribute classAttTrain = data.attribute(dataOriginal.numAttributes()-1);
                        Attribute classAttTest = data.attribute(dataOriginal.numAttributes()-1);
                       
                        try{
                            long begin = System.currentTimeMillis();
                            while(dataTest.numInstances()>0){
                                classifiers[numRep].buildClassifier(dataTrain);
                                Evaluations.SelfTrainingEvaluation(classifiers[numRep], dataTrain, dataTest, confusionMatrix, numExInstTreino);
                            }
                            Long end  = System.currentTimeMillis();
                            results.setBuildingTime(numRep, 0, end - begin);
                            results.setClassificationTime(numRep, 0, 0);
                        }catch(OutOfMemoryError e){
                            configuration.getEmail().getContent().append("OutOfMemory!!!!");
                            configuration.getEmail().getContent().append(configuration.toString());
                            configuration.getEmail().send();
                            e.printStackTrace();
                            System.exit(0);
                        }catch(Exception e){
                            System.err.println("Error when generating a classifier.");
                            configuration.getEmail().getContent().append(e.getMessage());
                            configuration.getEmail().getContent().append(configuration.toString());
                            configuration.getEmail().send();
                            e.printStackTrace();
                            System.exit(0);
                        }
                        
                        results.computeEvaluationMeasures(confusionMatrix, numClasses, numRep, 0);
                        results.setComplete(numRep, 0, true);
                        
                    }
                });
                
                threads.execute(thread);
                
            }   
        }catch(OutOfMemoryError e){
            configuration.getEmail().getContent().append("OutOfMemory!!!!");
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().send();
            e.printStackTrace();
            System.exit(0);
        }catch(Exception e){
            System.err.println("Error when generating a classifier.");
            configuration.getEmail().getContent().append(e.getMessage());
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().send();
            e.printStackTrace();
            System.exit(0);
        }
        
    }
    
}
