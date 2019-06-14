/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCT;

import TCTAlgorithms.InductiveSupervisedOneClass.OneClassSupervisedClassifier;
import TCTAlgorithms.InductiveSupervisedOneClass.SVM_SupervisedOneClass;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass;
import TCTIO.ListFiles;
import TCTParameters.SupervisedOneClass.ParametersOneClass;
import TCTParameters.SupervisedOneClass.ParametersOneClassSVM;
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
public class SupervisedInductiveClassification_SVM_OneClass {
    
    public static void learning(final SupervisedInductiveConfiguraton_OneClass configuration){
        
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
                final Instances dataOriginal = trainSource.getDataSet();
                
                Attribute classAtt = null;

                classAtt = dataOriginal.attribute(dataOriginal.numAttributes()-1); //Setting the last feature as class
                dataOriginal.setClass(classAtt);
                final int numClasses = classAtt.numValues();
                
                for(int j=0;j<numClasses;j++){
                    System.out.println(j + ": " + classAtt.value(j));
                }
                
                final StringBuilder outputFile = new StringBuilder();
                outputFile.append(configuration.getDirOutput());
                outputFile.append("/");
                outputFile.append(dataOriginal.relationName());
                outputFile.append("_InductiveSupervised_OneClass_");
                
                if(configuration.isSVM()){
                    final ParametersOneClassSVM parametersSVM = configuration.getParametersSVM();
                    if(parametersSVM.isLinearKernel()){
                        for(int g=0;g < parametersSVM.getGammas().size(); g++){
                            final int gammaId = g;
                            for(int n=0;n<parametersSVM.getNus().size();n++){
                                final int nuId = n;
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        StringBuilder outputFilePar = new StringBuilder();
                                        outputFilePar.append(outputFile.toString());
                                        outputFilePar.append("OSVM_LinearKernel_" + parametersSVM.getGamma(gammaId) + "_" + parametersSVM.getNu(nuId) + "_");
                                        SVM_SupervisedOneClass classifier = new SVM_SupervisedOneClass();
                                        classifier.setNu(parametersSVM.getNu(nuId));
                                        classifier.setGamma(parametersSVM.getGamma(gammaId));
                                        classifier.setKernelType(0);
                                        learning(configuration, classifier, dataOriginal, outputFilePar.toString(), numClasses);
                                    }
                                });
                                threads.execute(thread);    
                            }
                        }        
                    }
                    if(parametersSVM.isPolynomialKernel()){
                        for(int g=0;g < parametersSVM.getGammas().size(); g++){
                            final int gammaId = g;
                            for(int n=0;n<parametersSVM.getNus().size();n++){
                                final int nuId = n;
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        StringBuilder outputFilePar = new StringBuilder();
                                        outputFilePar.append(outputFile.toString());
                                        outputFilePar.append("OSVM_PolyKernel_" + parametersSVM.getGamma(gammaId) + "_" + parametersSVM.getNu(nuId) + "_");
                                        SVM_SupervisedOneClass classifier = new SVM_SupervisedOneClass();
                                        classifier.setNu(parametersSVM.getNu(nuId));
                                        classifier.setGamma(parametersSVM.getGamma(gammaId));
                                        classifier.setKernelType(1);
                                        learning(configuration, classifier, dataOriginal, outputFilePar.toString(), numClasses);
                                    }
                                });
                                threads.execute(thread);    
                            }
                        }        
                    }
                    if(parametersSVM.isRbfKernel()){
                        for(int g=0;g < parametersSVM.getGammas().size(); g++){
                            final int gammaId = g;
                            for(int n=0;n<parametersSVM.getNus().size();n++){
                                final int nuId = n;
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        StringBuilder outputFilePar = new StringBuilder();
                                        outputFilePar.append(outputFile.toString());
                                        outputFilePar.append("OSVM_RBFKernel_" + parametersSVM.getGamma(gammaId) + "_" + parametersSVM.getNu(nuId) + "_");
                                        SVM_SupervisedOneClass classifier = new SVM_SupervisedOneClass();
                                        classifier.setNu(parametersSVM.getNu(nuId));
                                        classifier.setGamma(parametersSVM.getGamma(gammaId));
                                        classifier.setKernelType(2);
                                        learning(configuration, classifier, dataOriginal, outputFilePar.toString(), numClasses);
                                    }
                                });
                                threads.execute(thread);    
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
    
    public static void learning(final SupervisedInductiveConfiguraton_OneClass configuration, OneClassSupervisedClassifier classifier, final Instances dataOriginal, final String outputFile, final int numClasses){
        
        for (int classe = 0; classe < numClasses; classe++) {

            final int idClasse = classe;

            for(int rep = 0; rep < configuration.getNumReps(); rep++){

                final int numRep = rep;

                final Instances data = new Instances(dataOriginal);
                data.setClass(dataOriginal.attribute(dataOriginal.numAttributes()-1));
                data.randomize(new Random(rep));

                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {

                    final int numFold = fold;

                    boolean run = false;

                    final File output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds());
                    final File outputResult = new File(output.getAbsolutePath() + ".txt");
                    if(!outputResult.exists()){
                        run = true;
                    }


                    if(run == false){
                        return;
                    }

                    Instances dataTrain = new Instances(data, 0);
                    Instances dataTest = new Instances(data, 0);

                    boolean enoughtData = SplitData.splitTrainTestSupervisedOneClass(data, dataTrain, dataTest, idClasse, numFold);

                    long begin = System.currentTimeMillis();
                    if(enoughtData == true){
                        try{
                            classifier.buildClassifier(dataTrain);
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
                    long end = System.currentTimeMillis();

                    long buildingTime = end - begin;

                    ResultsOneClass results = null;

                    learnAndEvaluate(configuration, classifier, 0.5, output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

                }
            }    
        }
            
        
    }
    
    public static void learnAndEvaluate(SupervisedInductiveConfiguraton_OneClass configuration, OneClassSupervisedClassifier classifier, double threshold, File output, int idClasse, int numRep, int numFold, int numClasses, Instances dataTest, long buildingTime, boolean enoughtData){
        
        ResultsOneClass results = null;

        final File outputResult = new File(output.getAbsolutePath() + ".txt");
        if(outputResult.exists()){
            return;
        }

        final File outputTemp = new File(output.getAbsolutePath() + ".tmp");

        if(outputTemp.exists()){
            results = readResultFile(configuration, outputTemp);
        }else{
            results = new ResultsOneClass(output, numClasses, configuration.getNumReps(), configuration.getNumFolds(), "InductiveSupervised");
        }   
        
        if(results.getComplete(idClasse, numRep, numFold) == true){
            return;
        }else if(enoughtData == false){
            results.setComplete(idClasse, numRep, numFold, true);
            return;
        }

        results.setBuildingTime(idClasse, numRep, numFold, buildingTime);

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

        //printConfusionMatrix(idClasse, numFold, confusionMatrix);
        //printStatistics(confusionMatrix);

        results.setClassificationTime(idClasse, numRep, numFold, end - begin);
        results.computeEvaluationMeasures(confusionMatrix, idClasse, numRep, numFold);
        
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
    
    // Print the confusion matrix
    public static void printConfusionMatrix(int classNumber, int foldNumber, Integer[][] confusionMatrix) {

        System.out.println("\nClass: " + classNumber);
        System.out.println("Fold: " + foldNumber);
        System.out.println("Matriz:");
        for (int i = 0; i < 2; i++) {
            for (int y = 0; y < 2; y++) {
                System.out.print("\t" + confusionMatrix[i][y] + "    ");
            }
            System.out.println();
        }
    }
    
    // Apresenta as estatisticas da matriz de confusao
    private static void printStatistics(Integer[][] confusionMatrix) {

        double vp = confusionMatrix[0][0];
        double fp = confusionMatrix[0][1];
        double fn = confusionMatrix[1][0];
        double vn = confusionMatrix[1][1];

        double accuracy = (vp + vn) / (vp + vn + fp + fn);
        double precision = (vp) / (vp + fp);
        double recall = (vp) / (vp + fn);

        if (Double.isNaN(precision)) {
            precision = 0;
        }

        if (Double.isNaN(recall)) {
            recall = 0;
        }

        System.out.println("\nAccuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("");
    }
}
