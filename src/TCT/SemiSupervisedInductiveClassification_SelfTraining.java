//********************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Inctive Semi-Supervised Lerning and Evaluation.
//              Self-Training (Zhu & Goldberg, 2009) is used to perform semi-
//              supervised learning.
// References: - X. Zhu, A. B. Goldberg, Introduction to Semi-Supervised Learning, 
//               Morgan and Claypool Publishers, 2009.
//********************************************************************************

package TCT;

import TCTAlgorithms.InductiveSupervised.KNN_InductiveSupervised;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_SelfTraining;
import TCTIO.ListFiles;
import TCTParameters.SemiSupervisedLearning.Parameters_KNN;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

public class SemiSupervisedInductiveClassification_SelfTraining {
    
    /*Function to read a document-term matrix and set parameters and algorithms to induce a classification model through Self-Training */
    public static  void learning(TransductiveConfiguration_SelfTraining configuration){
        
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
        
        ArrayList<String> filesIn = ListFiles.listFilesStr(new File(configuration.getDirEntrada()));
        for(int i =0;i<filesIn.size();i++){ 

            if(!filesIn.get(i).endsWith(".arff")){
                continue;
            }
            System.out.println(filesIn.get(i));
            System.out.println("Loading ARFF file");
            try{
                DataSource trainSource = new DataSource(filesIn.get(i)); //Carregando arquivo de Dados
                Instances dataOriginal = trainSource.getDataSet();
                
                int numClasses = 0;
                Attribute classAtt = null;

                classAtt = dataOriginal.attribute(dataOriginal.numAttributes()-1); //Setting the last feature as class
                dataOriginal.setClass(classAtt);
                numClasses = classAtt.numValues();
                
                for(int j=0;j<numClasses;j++){
                    System.out.println(j + ": " + classAtt.value(j));
                }
                
                StringBuilder outputFile = new StringBuilder();
                StringBuilder outputFilePar = new StringBuilder();
                outputFile.append(configuration.getDirSaida());
                outputFile.append("/");
                outputFile.append(dataOriginal.relationName());
                outputFile.append("_SemiSupervisedInductive_SelfTraining_");
                
                
                
                for(int numExInsTrain=0;numExInsTrain<configuration.getNumInstPerClassAddTraining().size();numExInsTrain++){
                    int numExemplosInseridos = (int)configuration.getNumInstPerClassAddTraining().get(numExInsTrain);
                    for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
                        double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                        if(configuration.isMNB()){
                            NaiveBayesMultinomial classifMultinomial = new NaiveBayesMultinomial();
                            System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                            System.out.println("Number of documents added to the set of labeled documents: " + numExemplosInseridos);
                            System.out.println("Classification Algorithm: Multinomial Naive Bayes");
                            outputFilePar = new StringBuilder();
                            outputFilePar.append(outputFile.toString());
                            outputFilePar.append("_" + numExemplosInseridos + "_");
                            outputFilePar.append("MNB_");
                            outputFilePar.append(numLabeledInstances);
                            outputFilePar.append("_");
                            if(configuration.isPorcentage() == true){
                                outputFilePar.append("percentage");
                                outputFilePar.append("_");
                            }else{
                                outputFilePar.append("real");
                                outputFilePar.append("_");
                            }
                            outputFilePar.append(configuration.getNumInstPerClassAddTraining().get(numExInsTrain));
                            learning(configuration, classifMultinomial, dataOriginal, outputFilePar.toString(), (int)numLabeledInstances, numClasses, numExemplosInseridos); 
                        }
                        if(configuration.isKNN()){
                            Parameters_KNN parametersKNN = configuration.getParametersKNN();
                            outputFilePar = new StringBuilder();
                            outputFilePar.append(outputFile.toString());
                            outputFilePar.append("_" + numExemplosInseridos + "_");
                            outputFilePar.append("KNN_");
                            for(int viz=0;viz<parametersKNN.getNeighbors().size();viz++){
                                System.out.println("Classification Algorithm: KNN");
                                System.out.println("Number of Neighbors: " + parametersKNN.getNeighbor(viz));

                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");        
                                outputFilePar2.append(parametersKNN.getNeighbor(viz));
                                outputFilePar2.append("_");        
                                if(parametersKNN.getCosine()){
                                    StringBuilder outputFilePar3 = new StringBuilder();
                                    outputFilePar3.append(outputFilePar2.toString());
                                    outputFilePar3.append("Cosine_");
                                    KNN_InductiveSupervised classifKnn = new KNN_InductiveSupervised();
                                    classifKnn.setK(parametersKNN.getNeighbor(viz));
                                    if(parametersKNN.isWeighted()){
                                        classifKnn.setWeightedVote(true);
                                        StringBuilder outputFilePar4 = new StringBuilder();
                                        outputFilePar4.append(outputFilePar3.toString());
                                        outputFilePar4.append("W_");
                                        learning(configuration, classifKnn, dataOriginal, outputFilePar4.toString(), (int)numLabeledInstances, numClasses, numExemplosInseridos);
                                    }
                                    if(parametersKNN.isUnweighted()){
                                        classifKnn.setWeightedVote(false);
                                        learning(configuration, classifKnn, dataOriginal, outputFilePar3.toString(), (int)numLabeledInstances, numClasses, numExemplosInseridos);
                                    }
                                }
                                if(parametersKNN.getEuclidean()){
                                    StringBuilder outputFilePar3 = new StringBuilder();
                                    outputFilePar3.append(outputFilePar2.toString());
                                    outputFilePar3.append("Euclidean_");
                                    IBk classifKnn = new IBk();
                                    EuclideanDistance df = new EuclideanDistance();
                                    classifKnn.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df);
                                    classifKnn.setKNN(parametersKNN.getNeighbor(viz));
                                    if(parametersKNN.isWeighted()){
                                        SelectedTag dist = new SelectedTag(IBk.WEIGHT_INVERSE, IBk.TAGS_WEIGHTING);
                                        classifKnn.setDistanceWeighting(dist);
                                        StringBuilder outputFilePar4 = new StringBuilder();
                                        outputFilePar4.append(outputFilePar3.toString());
                                        outputFilePar4.append("W_");
                                        learning(configuration, classifKnn, dataOriginal, outputFilePar4.toString(), (int)numLabeledInstances, numClasses, numExemplosInseridos);
                                    }else{
                                        SelectedTag dist = new SelectedTag(IBk.WEIGHT_NONE, IBk.TAGS_WEIGHTING);
                                        classifKnn.setDistanceWeighting(dist);
                                        learning(configuration, classifKnn, dataOriginal, outputFilePar3.toString(), (int)numLabeledInstances, numClasses, numExemplosInseridos);
                                    }
                                }
                            }
                        }
                    }          
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
        System.out.println("Process concluded successfully");
        configuration.getEmail().getContent().append(configuration.toString());
        configuration.getEmail().send();
    }
    
    //Function to run and evaluate Self-Training
    public static void learning(TransductiveConfiguration_SelfTraining configuration, Classifier classifier, Instances dataOriginal, String outputFile, int numInstPerClass, int numClasses,int numExInstTreino){
        
        try{
            Integer[][] confusionMatrix = new Integer[numClasses][numClasses]; //matriz de confusão
            
            ArrayList<Double> accuracies = new ArrayList<Double>();
            ArrayList<Double> microPrecisions = new ArrayList<Double>();
            ArrayList<Double> microRecalls = new ArrayList<Double>();
            ArrayList<Double> macroPrecisions = new ArrayList<Double>();
            ArrayList<Double> macroRecalls = new ArrayList<Double>();
            ArrayList<Long> buildingTimes = new ArrayList<Long>(); 
            ArrayList<Integer> iterations = new ArrayList<Integer>();
            ArrayList<Long> classificationTimes = new ArrayList<Long>();
            
            int[] folds = new int[1];
            
            File output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + ".txt");
            if(output.exists()){
                CheckExperiment(configuration, output, accuracies, microPrecisions, microRecalls, macroPrecisions, macroRecalls, buildingTimes, classificationTimes, folds);
                if(accuracies.size() == (configuration.getNumReps() * configuration.getNumFolds())){
                    return;
                }
            }
            System.out.println("Output: " + output.getAbsolutePath());
            
            folds[0] = folds[0]-1;
            if(folds[0] < 0){
                folds[0] = 0;
            }
            
            Instances data = new Instances(dataOriginal);
            data.setClass(dataOriginal.attribute(dataOriginal.numAttributes()-1));
            data.randomize(new Random(0));
            for(int fold=folds[0]; fold<configuration.getNumFolds();fold++){
                System.out.println("Fold "+(fold+1));
                
                FileWriter resultFile = new FileWriter(output, true);
                resultFile.write("Fold: " + (fold + 1) + " --------------------------------------------------------------------\n");
                resultFile.close();
                
                
                
                int beginRep = 0;
                if(fold == 0){
                    beginRep = accuracies.size();
                }else{
                    beginRep = accuracies.size() % (fold * configuration.getNumReps());
                }
                
                for(int rep=beginRep;rep<configuration.getNumReps();rep++){
                    
                    for(int class1=0;class1<numClasses;class1++){//inicializando a matriz
                        for(int class2=0;class2<numClasses;class2++){
                            confusionMatrix[class1][class2] = 0;
                        }
                    }
                    
                    System.out.println("Repetition: " + (rep + 1));
                    resultFile = new FileWriter(output, true);
                    resultFile.write("Repetition: " + (rep + 1) + " --------------------------------------------------------------------\n");
                    resultFile.close();

                    Instances dataTrain = data.trainCV(configuration.getNumFolds(), fold);
                    Instances dataTest = data.testCV(configuration.getNumFolds(),fold);

                    dataTrain.randomize(new Random(rep));
                    
                    Instances dataTrainLabeled = new Instances(dataTrain,0);
                    Instances dataTrainUnlabeled = new Instances(dataTrain,0);

                    SplitTrainTest(configuration, dataTrain, dataTrainLabeled, dataTrainUnlabeled, numInstPerClass);

                    long begin = System.currentTimeMillis();
                    while(dataTrainUnlabeled.numInstances()>0){
                        classifier.buildClassifier(dataTrainLabeled);
                        Evaluations.SelfTrainingEvaluation(classifier, dataTrainLabeled, dataTrainUnlabeled, confusionMatrix, numExInstTreino);
                        System.out.print(".");
                    }
                    System.out.println();
                    long end  = System.currentTimeMillis();
                    buildingTimes.add(end - begin);
                    for(int class1=0;class1<numClasses;class1++){//inicializando a matriz
                        for(int class2=0;class2<numClasses;class2++){
                            confusionMatrix[class1][class2] = 0;
                        }
                    }
                    
                    begin = System.currentTimeMillis();
                    Evaluations.SemiSupervisedInductiveEvaluation(classifier, dataTest, confusionMatrix);
                    //Avaliacoes.SupervisedInductiveEvaluation(classifier, dataTrain, confusionMatrix);
                    end = System.currentTimeMillis();
                    classificationTimes.add(end - begin);

                    //Printing Confusion Matrix
                    for(int i1=0;i1<numClasses;i1++){
                        for(int i2=0;i2<numClasses;i2++){
                            System.out.print(confusionMatrix[i1][i2] + "\t");
                        }
                        System.out.println();
                    }
                    ComputeEvaluationMeasures(confusionMatrix, numClasses, accuracies, microPrecisions, microRecalls, macroPrecisions, macroRecalls);
                    SaveRepetition(configuration, output, accuracies.get((fold * configuration.getNumReps()) + rep), microPrecisions.get((fold * configuration.getNumReps()) + rep), microRecalls.get((fold * configuration.getNumReps()) + rep), macroPrecisions.get((fold * configuration.getNumReps()) + rep), macroRecalls.get((fold * configuration.getNumReps()) + rep), buildingTimes.get((fold * configuration.getNumReps()) + rep), classificationTimes.get((fold * configuration.getNumReps()) + rep), rep + 1);
                }
                    
            }    
            SaveAverage(configuration, output, accuracies, microPrecisions, microRecalls, macroPrecisions, macroRecalls, buildingTimes, classificationTimes);
            //GravarEstatisticas(configuration, outputFile, accuracies, microPrecisions, microRecalls, macroPrecisions, macroRecalls, buildingTimes, iterations, classificationTimes);
            
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
    
    /*Function to split text collection into labeled and unlabled sets. numInstPerClass instances for each class are selected as labeled examples.*/
    private static void SplitTrainTest(TransductiveConfiguration_SelfTraining configuration, Instances data, Instances dataTrain, Instances dataTest, double numInstPerClass){
        int numClasses = data.numClasses();
        int[] totalInstClass = new int[numClasses];
        int[] instPerClass = new int[numClasses];
        int[] instChosenByClass = new int[numClasses];
        for(int classe=0;classe<numClasses;classe++){
            totalInstClass[classe] = 0;
            instPerClass[classe] = 0;
            instChosenByClass[classe] = 0;
        }
        
        if(configuration.isPorcentage() == true){
            for(int inst=0;inst<data.numInstances();inst++){
                Instance instance = data.instance(inst);
                int classe = (int)instance.classValue();
                int value = totalInstClass[classe];
                value++;
                totalInstClass[classe] = value;
            }    
        }
        
        if(configuration.isPorcentage() == false){
            for(int classe=0;classe<numClasses;classe++){
                instPerClass[classe] = (int)numInstPerClass;
            }    
        }else{
            for(int classe=0;classe<numClasses;classe++){
                double value = totalInstClass[classe] * ((double)numInstPerClass/(double)100);
                if(value < 1){
                    value = 1;
                }
                instPerClass[classe] = (int)value;
            }
        }
        
        for(int inst=0;inst<data.numInstances();inst++){
            Instance instance = data.instance(inst);
            int classe = (int)instance.classValue();
            int value = instChosenByClass[classe];
            value++;
            if(value > instPerClass[classe]){
                dataTest.add(instance);
            }else{
                dataTrain.add(instance);
                instChosenByClass[classe] = value;
            }
        }
    }
    
    //Save the classification performance results from a repetition
    public static void SaveRepetition(TransductiveConfiguration_SelfTraining configuration, File file, double accuracy, double microPrecision, double microRecall, double macroPrecision, double macroRecall, long buildingTime, long classificationTime, int numRepetition){
        try{
            FileWriter outputResults = new FileWriter(file,true);
            outputResults.write("Accuracy (%): " + accuracy + "\n");
            outputResults.write("Error (%): " + (100 - accuracy) + "\n");
            outputResults.write("Micro-Precision: " + microPrecision + "\n");
            outputResults.write("Micro-Recall: " + microRecall + "\n");
            outputResults.write("Macro-Precision: " + macroPrecision + "\n");
            outputResults.write("Macro-Recall: " + macroRecall + "\n");
            outputResults.write("Model Building Time (s): " + ((double)buildingTime / (double)1000) + "\n");
            outputResults.write("Classification Time (s): " + ((double)classificationTime / (double)1000) + "\n");
            outputResults.write("Number of Iterations: " + 0 + "\n");
            outputResults.close();
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
    
    //Function to save the average of classification performance measures obtained from different repetitions
    public static void SaveAverage(TransductiveConfiguration_SelfTraining configuration, File file, ArrayList<Double> accuracies, ArrayList<Double> microPrecisions, ArrayList<Double> microRecalls, ArrayList<Double> macroPrecisions, ArrayList<Double> macroRecalls, ArrayList<Long> buildingTimes, ArrayList<Long> classificationTimes){
        System.out.println("Saving Results...");
        
        try{
            double acmAccuracy = 0;
            double acmMicroPrecision = 0;
            double acmMicroRecall = 0;
            double acmMacroPrecision = 0;
            double acmMacroRecall = 0;
            long acmBuildingTime = 0;
            long acmClassificationTime = 0;
            int acmiterations = 0;
            
            double acmSD = 0;
            
            FileWriter outputResults = new FileWriter(file,true);
            for(int fold=0; fold<configuration.getNumFolds();fold++){
                for(int rep=0;rep<configuration.getNumReps();rep++){
                    acmAccuracy += accuracies.get((fold * configuration.getNumReps()) + rep);
                    acmMicroPrecision += microPrecisions.get((fold * configuration.getNumReps()) + rep);
                    acmMicroRecall += microRecalls.get((fold * configuration.getNumReps()) + rep);
                    acmMacroPrecision += macroPrecisions.get((fold * configuration.getNumReps()) + rep);
                    acmMacroRecall += macroRecalls.get((fold * configuration.getNumReps()) + rep);
                    acmBuildingTime += buildingTimes.get((fold * configuration.getNumReps()) + rep);
                    acmClassificationTime += classificationTimes.get((fold * configuration.getNumReps()) + rep);
                }
            }

            outputResults.write("\n-------------------------------------\n");
                        
            double averageAccuracy = ((double)acmAccuracy / (double)(configuration.getNumReps() * configuration.getNumFolds()));
            double averageMicroPrecision = ((double)acmMicroPrecision / (double)(configuration.getNumReps() * configuration.getNumFolds()));
            double averageMicroRecall = ((double)acmMicroRecall / (double)(configuration.getNumReps() * configuration.getNumFolds()));
            double averageMacroPrecision = ((double)acmMacroPrecision / (double)(configuration.getNumReps() * configuration.getNumFolds()));
            double averageMacroRecall = ((double)acmMacroRecall / (double)(configuration.getNumReps() * configuration.getNumFolds()));
            
            //Standard Deviation of the Accuracy
            
            for(int rep=0;rep<configuration.getNumReps();rep++){
                for(int fold=0; fold<configuration.getNumFolds();fold++){
                    acmSD += Math.pow((accuracies.get((rep * configuration.getNumFolds()) + fold) - averageAccuracy), 2);
                }
            }
            acmSD = (double)acmSD / (double)(configuration.getNumReps() * configuration.getNumFolds());
            double desvio = Math.sqrt(acmSD);
            
            outputResults.write("Average Accuracy (%): " + averageAccuracy + "\n");
            outputResults.write("Average Micro-Precision: " + averageMicroPrecision + "\n");
            outputResults.write("Average Micro-Recall: " + averageMicroRecall + "\n");
            outputResults.write("Average Macro-Precision: " + averageMacroPrecision + "\n");
            outputResults.write("Average Macro-Recall: " + averageMacroRecall + "\n");
            outputResults.write("Standard Deviation Accuracy: " + desvio +"\n");
            outputResults.write("Average Model Building Time (s): " + (((double)acmBuildingTime / (double)1000) / (double)configuration.getNumReps())+"\n");
            outputResults.write("Average Classification Time (s): " + (((double)acmClassificationTime / (double)1000) / (double)configuration.getNumReps())+"\n");
            outputResults.write("Average Number of Iterations (s): " + ((double)acmiterations / (double)configuration.getNumReps()) + "\n");

            outputResults.close();
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
    
    //Function to recover experiments
    public static void CheckExperiment(TransductiveConfiguration_SelfTraining configuration, File file, ArrayList<Double> accuracies, ArrayList<Double> microPrecisions, ArrayList<Double> microRecalls, ArrayList<Double> macroPrecisions, ArrayList<Double> macroRecalls, ArrayList<Long> buildingTimes, ArrayList<Long> classificationTimes, int[] iniFold){
        try{
            BufferedReader arqResult = new BufferedReader(new FileReader(file));
            String line = "";
            int indRepetition = -1;
            double accuracy = -1;
            double microPrecision = -1;
            double microRecall = -1;
            double macroPrecision = -1;
            double macroRecall = -1;
            long buildingTime = -1;
            long classificationTime = -1;
            //int numIterations = -1;
            
            while((line = arqResult.readLine()) != null){
                if(line.contains("Average") || line.contains("Media")){
                    accuracies.add(accuracy);
                    microPrecisions.add(microPrecision);
                    microRecalls.add(microRecall);
                    macroPrecisions.add(macroPrecision);
                    macroRecalls.add(macroRecall);
                    buildingTimes.add(buildingTime);
                    classificationTimes.add(classificationTime);
                    //iterations.add(numIterations);
                    //beginRep[0] = indRepetition;
                    return;
                }
                if(line.contains("Fold")){
                    String[] parts = line.split(" ");
                    iniFold[0] = Integer.parseInt(parts[1]);
                }
                if (line.contains("Repetition") || line.contains("Repetic")){
                    String[] parts = line.split(" ");
                    indRepetition = Integer.parseInt(parts[1]);
                    if((accuracy >= 0) && (microPrecision >= 0) && (microRecall >= 0) && (macroPrecision >= 0) && (macroRecall >= 0)){
                        accuracies.add(accuracy);
                        microPrecisions.add(microPrecision);
                        microRecalls.add(microRecall);
                        macroPrecisions.add(macroPrecision);
                        macroRecalls.add(macroRecall);
                        buildingTimes.add(buildingTime);
                        classificationTimes.add(classificationTime);
                    }
                    accuracy = -1;
                    microPrecision = -1;
                    microRecall = -1;
                    macroPrecision = -1;
                    macroRecall = -1;    
                    buildingTime = -1;
                    classificationTime = -1;
                }
                if((line.contains("Accuracy") || line.contains("Taxa de Acerto")) && (!line.contains("Average"))){
                    String[] parts = line.split(":");
                    if(parts.length == 2){
                        accuracy = Double.parseDouble(parts[1].trim());
                    }else{
                        accuracy = -1;
                    }
                }
                if((line.contains("Micro-Precision") || line.contains("Micro Precisao")) && (!line.contains("Average"))){
                    String[] parts = line.split(":");
                    if(parts.length == 2){
                        microPrecision = Double.parseDouble(parts[1].trim());
                    }else{
                        microPrecision = -1;
                    }
                }
                if((line.contains("Micro-Recall") || line.contains("Micro Revocacao")) && (!line.contains("Average"))){
                    String[] parts = line.split(":");
                    if(parts.length == 2){
                        microRecall = Double.parseDouble(parts[1].trim());
                    }else{
                        microRecall = -1;
                    }
                }
                if((line.contains("Macro-Precision") || line.contains("Macro Precisao")) && (!line.contains("Average"))){
                    String[] parts = line.split(":");
                    if(parts.length == 2){
                        macroPrecision = Double.parseDouble(parts[1].trim());
                    }else{
                        macroPrecision = -1;
                    }
                }
                if((line.contains("Macro-Recall") || line.contains("Macro Revocacao"))&& (!line.contains("Average"))){
                    String[] parts = line.split(":");
                    if(parts.length == 2){
                        macroRecall = Double.parseDouble(parts[1].trim());
                    }else{
                        macroRecall = -1;
                    }
                }
                if((line.contains("Model Building Time") || line.contains("Tempo Constru")) && (!line.contains("Average"))){
                    String[] parts = line.split(":");
                    if(parts.length == 2){
                        Double value = Double.parseDouble(parts[1].trim()) * 1000;
                        buildingTime = value.longValue();
                    }else{
                        buildingTime = -1;
                    }
                }
                if((line.contains("Classification Time") || line.contains("Tempo Classifi")) && (!line.contains("Average"))){
                    String[] parts = line.split(":");
                    if(parts.length == 2){
                        Double value = Double.parseDouble(parts[1].trim()) * 1000;
                        classificationTime = value.longValue();
                    }else{
                        classificationTime = -1;
                    }
                }
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
    
    /*Function to compute Accuracy, Micro-Precision, Micro-Recall, Macro-Precision, and Macro-Recal */
    private static void ComputeEvaluationMeasures(Integer[][] confusionMatrix, int numClasses, ArrayList<Double> accuracies, ArrayList<Double> microPrecisions, ArrayList<Double> microRecalls, ArrayList<Double> macroPrecisions, ArrayList<Double> macroRecalls){
        
        int tp=0;
        int total=0;
        for(int i=0;i<numClasses;i++){
            for(int j=0;j<numClasses;j++){
                if(i==j){
                    tp += confusionMatrix[i][j];
                }
                total += confusionMatrix[i][j];
            }
        }
        double accuracy = ((double)tp/(double)total) * 100;
        accuracies.add(accuracy);
        
        
        double microPrecTotNum = 0, microPrecTotDen = 0, microRevTotNum = 0, microRevTotDen = 0, macroPrecTot = 0, macroRevTot = 0;
        for(int j=0;j<numClasses;j++){
            
            int TPi = confusionMatrix[j][j];
            int FPi = 0;
            for(int k=0;k<numClasses;k++){
                if(k!=j){
                    FPi += confusionMatrix[j][k];
                }
            }
            microPrecTotNum += TPi;
            microPrecTotDen += (TPi + FPi);
            if((TPi + FPi)==0){
                macroPrecTot += 0;
            }else{
                macroPrecTot += (double)TPi/(double)(TPi + FPi);
            }

            
            int FNi=0;
            for(int k=0;k<numClasses;k++){
                if(k!=j){
                    FNi += confusionMatrix[k][j];
                }
            }
            microRevTotNum += TPi;
            microRevTotDen += TPi + FNi;
            if((TPi + FNi)==0){
                macroRevTot += 0;
            }else{
                macroRevTot += (double)TPi/(double)(TPi + FNi);
            }
        }
        double microPrecision = (double)microPrecTotNum/(double)microPrecTotDen;
        double microRecall = (double)microRevTotNum/(double)microRevTotDen;
        double macroPrecision = (double) macroPrecTot / (double) numClasses;
        double macroRecall = (double)macroRevTot/(double)numClasses;
        microPrecisions.add(microPrecision);
        microRecalls.add(microRecall);
        macroPrecisions.add(macroPrecision);
        macroRecalls.add(macroRecall);
    }
}
