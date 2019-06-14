//**********************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Transductive Learning and Evaluations. 
//              Transductive Learning is performed trough an Ensemble of Classifiers
//**********************************************************************************


package TCT;

import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_Multiview;
import TCTIO.ListFiles;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TransductiveClassification_MultiView {
    
    /*Function to read a document-term matrix and to set the parameters of transductive learning algorithms */
    public static  void learning(TransductiveConfiguration_Multiview configuration){
        
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());

        ArrayList<String> filesIn = ListFiles.listFilesStr(new File(configuration.getDirEntrada()));

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
                outputFile.append("_Transductive_Multiview_");

                for (int numExInsTrain = 0; numExInsTrain < configuration.getNumInstPerClassAddTraining().size(); numExInsTrain++) {
                    int numLabeledInstancesInseridos = (int) configuration.getNumInstPerClassAddTraining().get(numExInsTrain);
                    for (int numEx = 0; numEx < configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size(); numEx++) {
                        int numLabeledInstances = (int) configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                        for (int numVisoes = 0; numVisoes < configuration.getNumberViews().size(); numVisoes++) {
                            int numberViews = configuration.getNumberView(numVisoes);
                            if (configuration.isMNB()) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: Multinomial Naive Bayes");
                                //System.out.println("Number of Views: " + numberViews);
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
                                outputFilePar.append(numberViews);
                                outputFilePar.append("_");
                                outputFilePar.append(numLabeledInstancesInseridos);
                                outputFilePar.append(".txt");
                                ArrayList<Classifier>[] classifiers = new ArrayList[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    ArrayList<Classifier> classifiersMNB = new ArrayList<Classifier>();
                                    for (int vis = 0; vis < numberViews; vis++) {
                                        classifiersMNB.add(new NaiveBayesMultinomial());
                                    }
                                    classifiers[rep] = classifiersMNB;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numLabeledInstances, numClasses, numberViews, numLabeledInstancesInseridos);
                            }
                        }
                    }
                }

            }

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
    public static void learning(final TransductiveConfiguration_Multiview configuration, final ExecutorService threads, final ArrayList<Classifier>[] classifiers, final Instances dataOriginal, String outputFile, final double numInstPerClass, final int numClasses, final int numberViews, final int numExInstTreino){
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

                        for(int randView=1;randView<=configuration.getNumRandomViews();randView++){
                            ArrayList<Instances> trainViews = new ArrayList<Instances>();
                            ArrayList<Instances> testViews = new ArrayList<Instances>();

                            for(int vis=0;vis<numberViews;vis++){
                                trainViews.add(new Instances(dataOriginal,0));
                                testViews.add(new Instances(dataOriginal,0));
                            }

                            //System.out.println("Repetition "+(numRep+1));

                            Instances data = new Instances(dataOriginal);
                            data.randomize(new Random(numRep));

                            //System.out.println("Building Views...");
                            SplitData.splitViewsTransductive(configuration, data, trainViews, testViews, numInstPerClass, ((numRep+1) * (randView+1)),numberViews);
                            //System.out.println("Visões construídas");

                            boolean exit = false;
                            long begin = System.currentTimeMillis();
                            while(exit == false){
                                for(int vis=0;vis<numberViews;vis++){
                                    final int numVis = vis;
                                    try{
                                        classifiers[numVis].get(vis).buildClassifier(trainViews.get(numVis));    
                                    }catch(OutOfMemoryError e){
                                        configuration.getEmail().getContent().append("OutOfMemory!!!!");
                                        configuration.getEmail().getContent().append(configuration.toString());
                                        configuration.getEmail().send();
                                        e.printStackTrace();
                                        System.exit(0);
                                    }catch(Exception e){
                                        System.err.println("Error when generating a classifier.");
                                        configuration.getEmail().getContent().append(e.getMessage());
                                        configuration.getEmail().send();
                                        e.printStackTrace();
                                        System.exit(0);
                                    }
                                }
                                Evaluations.TransductiveMultiviewEvaluation(classifiers[numRep], trainViews, testViews, confusionMatrix, numExInstTreino);
                                if(testViews.get(0).numInstances() == 0){
                                    exit = true;
                                }
                            }
                            long end  = System.currentTimeMillis();
                            results.setBuildingTime(numRep, 0, end - begin);
                            results.setClassificationTime(numRep, 0, 0);
                        
                            results.computeEvaluationMeasures(confusionMatrix, numClasses, numRep, 0);
                            results.setComplete(numRep, 0, true);
                        }
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
