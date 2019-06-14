/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCT;

import TCTAlgorithms.InductiveSupervisedOneClass.BisectingKMeans_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.IMBHNR_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.KMeans_Parametric_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.KNNDensity_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.KNNRelativeDensity_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.KSNN_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.OneClassSupervisedClassifier;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples;
import TCTIO.ListFiles;
import TCTParameters.SupervisedOneClass.ParametersIMHN;
import TCTParameters.SupervisedOneClass.ParametersKNNDensity;
import TCTParameters.SupervisedOneClass.ParametersNaiveBayes;
import TCTParameters.SupervisedOneClass.ParametersOneClass;
import TCTParameters.SupervisedOneClass.ParametersPrototypeBasedClustering;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author rafael
 */
public class SupervisedInductiveClassification_OneClass_FewLabeledExamples {
   
    public static void learning(SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples configuration){
        
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
        
        ArrayList<String> filesIn = ListFiles.listFilesStr(new File(configuration.getDirInput()));
        
        for(int i =0;i<filesIn.size();i++){ 
            
            if(!filesIn.get(i).endsWith(".arff")){
                continue;
            }
            System.out.println(filesIn.get(i));
            System.out.println("Loading ARFF file");
            try{
                
                ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(filesIn.get(i)); //Carregando arquivo de Dados
                Instances dataOriginal = trainSource.getDataSet();
                
                Attribute classAtt = null;

                classAtt = dataOriginal.attribute(dataOriginal.numAttributes()-1); //Setting the last feature as class
                dataOriginal.setClass(classAtt);
                final int numClasses = classAtt.numValues();
                
                for(int j=0;j<numClasses;j++){
                    System.out.println(j + ": " + classAtt.value(j));
                }
                
                StringBuilder outputFile = new StringBuilder();
                outputFile.append(configuration.getDirOutput());
                outputFile.append("/");
                outputFile.append(dataOriginal.relationName());
                outputFile.append("_InductiveSupervised_OneClass_FewLabeledExamples_");
                
                
                for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
                    double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                    
                    StringBuilder outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append(numLabeledInstances);
                    outputFilePar.append("_");
                    if (configuration.isPorcentage() == true) {
                        outputFilePar.append("percentage");
                    } else {
                        outputFilePar.append("real");
                    }
                    outputFilePar.append("_");
                    
                    if(configuration.isMNB()){
                        StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                        
                        ParametersNaiveBayes parameters = configuration.getParametersMNB();
                        OneClassSupervisedClassifier[] classifiers = new OneClassSupervisedClassifier[configuration.getNumReps()];
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            
                                //MultinomialNaiveBayes_SupervisedOneClass classifier = new MultinomialNaiveBayes_SupervisedOneClass();
                                //MultiNomialNaiveBayesWeka_SupervisedOneClass classifier = new MultiNomialNaiveBayesWeka_SupervisedOneClass();
                                //classifiers[rep][fold] = classifier;
                        }    
                        learning(configuration, threads, classifiers, parameters, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                    }
                    if(configuration.isKME()){
                        ParametersPrototypeBasedClustering parameters = configuration.getParametersKME();
                        for(int idK=0;idK<parameters.getKs().size();idK++){
                            StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                            outputFilePar2.append("KME_");
                            outputFilePar2.append(parameters.getK(idK) + "_");
                            outputFilePar2.append(parameters.getNumMaxIterations() + "_");
                            outputFilePar2.append(parameters.getMinChangeRate() + "_");
                            outputFilePar2.append(parameters.getMinDiffObjective() + "_");
                            outputFilePar2.append(parameters.getNumTrials() + "_");
                            OneClassSupervisedClassifier[]classifiers = new OneClassSupervisedClassifier[configuration.getNumReps()];
                            for(int rep=0;rep<configuration.getNumReps();rep++){
                                KMeans_Parametric_SupervisedOneClass classifier = new KMeans_Parametric_SupervisedOneClass();
                                classifier.setK(parameters.getK(idK));
                                classifier.setNumMaxIterations(parameters.getNumMaxIterations());
                                classifier.setChangeRate(parameters.getMinChangeRate());
                                classifier.setConvergence(parameters.getMinDiffObjective());
                                classifier.setNumTrials(parameters.getNumTrials());
                                classifier.setEuclidean(parameters.isEuclidean());
                                classifier.setCosine(parameters.isCosine());
                                classifier.setPearson(parameters.isPearson());
                                classifiers[rep] = classifier;
                            }
                            learning(configuration, threads, classifiers, parameters, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                        }
                    }
                    if(configuration.isKNNDensity()){
                        ParametersKNNDensity parameters = configuration.getParametersKNNDensity();
                        for(int idK=0;idK<parameters.getKs().size();idK++){
                            StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                            outputFilePar2.append("KNNDensity_");
                            outputFilePar2.append(parameters.getK(idK) + "_");
                            OneClassSupervisedClassifier[] classifiers = new OneClassSupervisedClassifier[configuration.getNumReps()];
                            for(int rep=0;rep<configuration.getNumReps();rep++){
                                KNNDensity_SupervisedOneClass classifier = new KNNDensity_SupervisedOneClass();
                                classifier.setK(parameters.getK(idK));
                                classifiers[rep] = classifier;
                            }
                            learning(configuration, threads, classifiers, parameters, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                        }
                    }
                    if(configuration.isIMBHNR()){
                        ParametersIMHN parameters = configuration.getParametersIMBHNR();
                        for(int error=0;error<parameters.getErrors().size();error++){
                            for(int apr=0;apr<parameters.getErrorCorrectionRates().size();apr++){
                                //System.out.println("Classification Algorithm: IMBHN2");
                                //System.out.println("Minimum Mean Squared Error: " + parametersIMBHN2.getError(error));
                                //System.out.println("Error Correction Rate: " + parametersIMBHN2.getErrorCorrectionRate(apr));
                                StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                                outputFilePar2.append("IMBHNR_");
                                outputFilePar2.append(parameters.getErrorCorrectionRate(apr) + "_");
                                outputFilePar2.append(parameters.getError(error) + "_");
                                OneClassSupervisedClassifier[]classifiers = new OneClassSupervisedClassifier[configuration.getNumReps()];
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    IMBHNR_SupervisedOneClass classifier = new IMBHNR_SupervisedOneClass();
                                    classifier.setErrorCorrectionRate(parameters.getErrorCorrectionRate(apr));
                                    classifier.setMinError(parameters.getError(error));
                                    classifier.setMaxNumIterations(parameters.getMaxNumberIterations());
                                    classifiers[rep] = classifier;
                                }    
                                learning(configuration, threads, classifiers, parameters, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
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
        
        threads.shutdown();
        boolean exit = false;
        while(exit == false){
            if(threads.isTerminated()){
                System.out.println("Process concluded successfully");
                configuration.getEmail().getContent().append(configuration.toString());
                configuration.getEmail().send();
                exit = true;
            }else{
                try {
                    Thread.sleep(1000);
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
        
    }
    
    public static void learning(final SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples configuration, final ExecutorService threads, final OneClassSupervisedClassifier[] classifiers, final ParametersOneClass parameters, final Instances dataOriginal, final String outputFile, final double numInstPerClass, final int numClasses){
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
        
                for (int classe = 0; classe < numClasses; classe++) {

                    final int idClasse = classe;

                    for(int rep = 0; rep < configuration.getNumReps(); rep++){

                        final int numRep = rep;

                        boolean runThreshold = false;
                        if(parameters.isManual()){
                            for(int t=0;t<parameters.getThresholds().size();t++){
                                double threshold = parameters.getThreshold(t);
                                final File output = new File(outputFile + configuration.getNumReps() + "_" +  "_" + threshold);
                                final File outputResult = new File(output.getAbsolutePath() + ".txt");
                                if(!outputResult.exists()){
                                    runThreshold = true;
                                    break;
                                }
                            }    
                        }

                        boolean runAutomatic = false;
                        if(parameters.isAutomatic()){
                            final File output = new File(outputFile + configuration.getNumReps() + "_" + "_automatic_");
                            final File outputResultAutomaticMinimun = new File(output.getAbsolutePath() + "minimum_" + ".txt");
                            if(!outputResultAutomaticMinimun.exists()){
                                runAutomatic = true;
                            }
                            final File outputResultAutomaticAverage = new File(output.getAbsolutePath() + "average_" + ".txt");
                            if(!outputResultAutomaticAverage.exists()){
                                runAutomatic = true;
                            }
                            final File outputResultAutomaticAverageStdDev = new File(output.getAbsolutePath() + "average_stddev_" + ".txt");
                            if(!outputResultAutomaticAverageStdDev.exists()){
                                runAutomatic = true;
                            }

                        }

                        if(runAutomatic == false && runThreshold == false){
                            return;
                        }

                        Instances dataTrain = new Instances(dataOriginal, 0);
                        Instances dataTest = new Instances(dataOriginal, 0);

                        Instances data = new Instances(dataOriginal);
                        data.randomize(new Random(numRep));

                        SplitData.splitTrainTestTransductiveOneClass(configuration.isPorcentage(), data, dataTrain, dataTest, numInstPerClass, idClasse);

                        long begin = System.currentTimeMillis();
                        try{
                            classifiers[numRep].buildClassifier(dataTrain);
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
                        long end = System.currentTimeMillis();

                        long buildingTime = end - begin;

                        boolean enoughtData = true;

                        if(parameters.isManual() && runThreshold){
                            for(int t=0;t<parameters.getThresholds().size();t++){

                                double threshold = parameters.getThreshold(t);
                                ResultsOneClass results = null;

                                final File output = new File(outputFile + configuration.getNumReps() + "_" + "_" + threshold);
                                learnAndEvaluate(configuration, classifiers[numRep], threshold, output, idClasse, numRep, numClasses, dataTest, buildingTime, enoughtData);

                            }

                        }

                        if(parameters.isAutomatic() && runAutomatic){

                            ArrayList<Double> thresholds = getBestThresholds(parameters,classifiers[numRep],dataTrain,idClasse);

                            File output = new File(outputFile + configuration.getNumReps() +"_automatic_minimum_");
                            learnAndEvaluate(configuration, classifiers[numRep], thresholds.get(0), output, idClasse, numRep, numClasses, dataTest, buildingTime, enoughtData);

                            output = new File(outputFile + configuration.getNumReps() + "_automatic_average-3simga_");
                            learnAndEvaluate(configuration, classifiers[numRep], thresholds.get(1), output, idClasse, numRep, numClasses, dataTest, buildingTime, enoughtData);

                            output = new File(outputFile + configuration.getNumReps() +  "_automatic_average-2simga_");
                            learnAndEvaluate(configuration, classifiers[numRep], thresholds.get(2), output, idClasse, numRep, numClasses, dataTest, buildingTime, enoughtData);

                            output = new File(outputFile + configuration.getNumReps() +  "_automatic_average-1simga_");
                            learnAndEvaluate(configuration, classifiers[numRep], thresholds.get(3), output, idClasse, numRep, numClasses, dataTest, buildingTime, enoughtData);

                            output = new File(outputFile + configuration.getNumReps() +  "_automatic_average_");
                            learnAndEvaluate(configuration, classifiers[numRep], thresholds.get(4), output, idClasse, numRep, numClasses, dataTest, buildingTime, enoughtData);

                            output = new File(outputFile + configuration.getNumReps() +  "_automatic_average+1simga_");
                            learnAndEvaluate(configuration, classifiers[numRep], thresholds.get(5), output, idClasse, numRep, numClasses, dataTest, buildingTime, enoughtData);

                            output = new File(outputFile + configuration.getNumReps() + "_automatic_average+2simga_");
                            learnAndEvaluate(configuration, classifiers[numRep], thresholds.get(6), output, idClasse, numRep, numClasses, dataTest, buildingTime, enoughtData);

                            output = new File(outputFile + configuration.getNumReps() + "_automatic_average+3simga_");
                            learnAndEvaluate(configuration, classifiers[numRep], thresholds.get(7), output, idClasse, numRep, numClasses, dataTest, buildingTime, enoughtData);

                        }
                                
                        }
                    }    
                }
            
            });
            threads.execute(thread);   
    }
    
    public static void learnAndEvaluate(SupervisedInductiveConfiguraton_OneClass configuration, OneClassSupervisedClassifier classifier, double threshold, File output, int idClasse, int numRep, int numClasses, Instances dataTest, long buildingTime, boolean enoughtData){
        
        ResultsOneClass results = null;

        final File outputResult = new File(output.getAbsolutePath() + ".txt");
        if(outputResult.exists()){
            return;
        }

        final File outputTemp = new File(output.getAbsolutePath() + ".tmp");

        if(outputTemp.exists()){
            results = readResultFile(configuration, outputTemp);
        }else{
            results = new ResultsOneClass(output, numClasses, configuration.getNumReps(), 1, "Transductive");
        }   
        
        if(results.getComplete(idClasse, numRep, 0) == true){
            return;
        }else if(enoughtData == false){
            results.setComplete(idClasse, numRep, 0, true);
            return;
        }

        results.setBuildingTime(idClasse, numRep, 0, buildingTime);

        Integer[][] confusionMatrix = new Integer[2][2];
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){
                confusionMatrix[i][j] = new Integer(0);
            }
        }

        classifier.setThreshold(threshold);
        long begin = System.currentTimeMillis();
        Evaluations.SupervisedInductiveEvaluationOneClass(classifier, dataTest, confusionMatrix, idClasse);
        long end = System.currentTimeMillis();

        results.setClassificationTime(idClasse, numRep, 0, end - begin);
        results.computeEvaluationMeasures(confusionMatrix, idClasse, numRep, 0);
        
    }
    
    public static ArrayList<Double> getBestThresholds(ParametersOneClass parameters, OneClassSupervisedClassifier classifier, Instances dataTrain, int classTarget){
        int numInstances = dataTrain.numInstances();
        
        double[] scores = new double[numInstances];
        
        double sumScores = 0;
        double minimun = Double.MAX_VALUE;
        
        for(int inst=0; inst<numInstances; inst++){
            scores[inst] = classifier.getScore(dataTrain.instance(inst));
            sumScores += scores[inst];
            if(scores[inst] < minimun){
                minimun = scores[inst];
            }
        }
        
        double average = sumScores / (double)numInstances;
        double stdDev = 0;
        
        for(int inst=0; inst<numInstances; inst++){
            stdDev = Math.pow(scores[inst] - average, 2);
        }
        
        stdDev = stdDev / (double)numInstances;
        stdDev = Math.sqrt(stdDev);
        
        ArrayList<Double> thresholds = new ArrayList<Double>();
        thresholds.add(minimun);
        thresholds.add(average - (stdDev * 3));
        thresholds.add(average - (stdDev * 2));
        thresholds.add(average - (stdDev));
        thresholds.add(average);
        thresholds.add(average + (stdDev));
        thresholds.add(average + (stdDev * 2));
        thresholds.add(average + (stdDev * 3));
        
        return thresholds;
         
    }
    
    public static synchronized ResultsOneClass readResultFile(SupervisedInductiveConfiguraton_OneClass configuration, File outputTemp){
        
        ResultsOneClass results = null;
        
        try{
                                                
            ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(outputTemp));
            results = (ResultsOneClass)objInput.readObject();
            objInput.close();
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
        
        return results;
        
    } 
}
