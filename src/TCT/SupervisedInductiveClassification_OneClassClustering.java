package TCT;

import TCTAlgorithms.InductiveSupervisedOneClass.Clustering;
import TCTAlgorithms.InductiveSupervisedOneClass.IMBHNR_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.KMeans_Parametric_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.MultinomialNaiveBayes_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.NaiveBayes_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.OneClassSupervisedClassifier;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguraton_OneClass_Clustering;
import TCTIO.ListFiles;
import TCTParameters.SupervisedOneClass.ParametersIMHN;
import TCTParameters.SupervisedOneClass.ParametersNaiveBayes;
import TCTParameters.SupervisedOneClass.ParametersOneClass;
import TCTParameters.SupervisedOneClass.ParametersPrototypeBasedClustering;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class SupervisedInductiveClassification_OneClassClustering {
    
    public static void learning(SupervisedInductiveConfiguraton_OneClass_Clustering configuration){
        
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
                outputFile.append("_InductiveSupervised_OneClass_Clustering_");
                
                if (configuration.iskMeans()) {
                    ParametersPrototypeBasedClustering parameters = configuration.getParametersClusteringKME();
                    for (int idK = 0; idK < parameters.getKs().size(); idK++) {
                        StringBuilder outputFilePar = new StringBuilder();
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("K-Means_");
                        outputFilePar.append(parameters.getK(idK) + "_");
                        outputFilePar.append(parameters.getNumMaxIterations() + "_");
                        outputFilePar.append(parameters.getMinChangeRate() + "_");
                        outputFilePar.append(parameters.getMinDiffObjective() + "_");
                        outputFilePar.append(parameters.getNumTrials() + "_");
                        //OneClassSupervisedClassifier[][] classifiers = new OneClassSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                        KMeans_Parametric_SupervisedOneClass clusterer = new KMeans_Parametric_SupervisedOneClass();
                        clusterer.setK(parameters.getK(idK));
                        clusterer.setNumMaxIterations(parameters.getNumMaxIterations());
                        clusterer.setChangeRate(parameters.getMinChangeRate());
                        clusterer.setConvergence(parameters.getMinDiffObjective());
                        clusterer.setNumTrials(parameters.getNumTrials());
                        clusterer.setEuclidean(parameters.isEuclidean());
                        clusterer.setCosine(parameters.isCosine());
                        clusterer.setPearson(parameters.isPearson());

                        //Learning goes here
                        System.out.println("Here 1");
                        learning(configuration, dataOriginal, threads, clusterer, outputFilePar, numClasses, parameters.getK(idK));
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
        
        /*threads.shutdown();
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
                } catch (InterruptedException ex) {
                    System.err.println("An error occurred when the control thread tryed to sleep.");
                    ex.printStackTrace();
                }
            }
        }*/
        
    }
    
    public static void learning(SupervisedInductiveConfiguraton_OneClass_Clustering configuration, Instances dataOriginal, ExecutorService threads, Clustering clusterer, StringBuilder entrada, int numClasses, int numClusters){
        
        StringBuilder outputFile = new StringBuilder(entrada.toString());

        if(configuration.isNB()){
            StringBuilder outputFilePar = new StringBuilder();
            outputFilePar = new StringBuilder();
            outputFilePar.append(outputFile.toString());
            outputFilePar.append("NB_");
            OneClassSupervisedClassifier[][][] classifiers = new OneClassSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()][numClusters];
            for(int rep=0;rep<configuration.getNumReps();rep++){
                for(int fold=0;fold<configuration.getNumFolds();fold++){
                    for(int c=0;c<numClusters;c++){
                        NaiveBayes_SupervisedOneClass classifier = new NaiveBayes_SupervisedOneClass();
                        //NaiveBayesWeka_OneClass classifier = new NaiveBayesWeka_OneClass();
                        classifiers[rep][fold][c] = classifier;    
                    }
                    
                }
            }    
            ParametersNaiveBayes parameters = configuration.getParametersNB();
            learning(configuration, threads, classifiers, clusterer, parameters, dataOriginal, outputFilePar.toString(), numClasses);
        }
        if(configuration.isMNB()){
            StringBuilder outputFilePar = new StringBuilder();
            outputFilePar = new StringBuilder();
            outputFilePar.append(outputFile.toString());
            outputFilePar.append("MNB_");
            ParametersNaiveBayes parameters = configuration.getParametersMNB();
            OneClassSupervisedClassifier[][][] classifiers = new OneClassSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()][numClusters];
            for(int rep=0;rep<configuration.getNumReps();rep++){
                for(int fold=0;fold<configuration.getNumFolds();fold++){
                    for(int c=0;c<numClusters;c++){
                        MultinomialNaiveBayes_SupervisedOneClass classifier = new MultinomialNaiveBayes_SupervisedOneClass();
                        //MultiNomialNaiveBayesWeka_SupervisedOneClass classifier = new MultiNomialNaiveBayesWeka_SupervisedOneClass();
                        classifiers[rep][fold][c] = classifier;    
                    }
                    
                }
            }    
            learning(configuration, threads, classifiers, clusterer, parameters, dataOriginal, outputFilePar.toString(), numClasses);
        }
        if(configuration.isIMBHNR()){
            ParametersIMHN parameters = configuration.getParametersIMBHNR();
            for(int error=0;error<parameters.getErrors().size();error++){
                for(int apr=0;apr<parameters.getErrorCorrectionRates().size();apr++){
                    //System.out.println("Classification Algorithm: IMBHN2");
                    //System.out.println("Minimum Mean Squared Error: " + parametersIMBHN2.getError(error));
                    //System.out.println("Error Correction Rate: " + parametersIMBHN2.getErrorCorrectionRate(apr));
                    StringBuilder outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("IMBHNR_");
                    outputFilePar.append(parameters.getErrorCorrectionRate(apr) + "_");
                    outputFilePar.append(parameters.getError(error) + "_");
                    OneClassSupervisedClassifier[][][] classifiers = new OneClassSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()][numClusters];
                    for(int rep=0;rep<configuration.getNumReps();rep++){
                        for(int fold=0;fold<configuration.getNumFolds();fold++){
                            for(int c=0;c<numClusters;c++){
                                IMBHNR_SupervisedOneClass classifier = new IMBHNR_SupervisedOneClass();
                                classifier.setErrorCorrectionRate(parameters.getErrorCorrectionRate(apr));
                                classifier.setMinError(parameters.getError(error));
                                classifier.setMaxNumIterations(parameters.getMaxNumberIterations());
                                classifiers[rep][fold][c] = classifier;
                            }
                        }
                    }    
                    System.out.println("Here 2");
                    learning(configuration, threads, classifiers, clusterer, parameters, dataOriginal, outputFilePar.toString(), numClasses);
                }    
            }
        }
        
        
    }
    
    public static void learning(final SupervisedInductiveConfiguraton_OneClass_Clustering configuration, final ExecutorService threads, final OneClassSupervisedClassifier[][][] classifiers, final Clustering clusterer, final ParametersOneClass parameters, final Instances dataOriginal, final String outputFile, final int numClasses){
        
        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {*/
        
                for (int classe = 0; classe < numClasses; classe++) {
                    System.out.println("Here 3");
                    final int idClasse = classe;

                    for(int rep = 0; rep < configuration.getNumReps(); rep++){
                        System.out.println("Here 4");
                        final int numRep = rep;

                        final Instances data = new Instances(dataOriginal);
                        data.setClass(dataOriginal.attribute(dataOriginal.numAttributes()-1));
                        data.randomize(new Random(rep));

                        for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                            System.out.println("Here 5");
                            final int numFold = fold;

                                boolean runThreshold = false;
                                if(parameters.isManual()){
                                    for(int t=0;t<parameters.getThresholds().size();t++){
                                        double threshold = parameters.getThreshold(t);
                                        final File output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_" + threshold);
                                        final File outputResult = new File(output.getAbsolutePath() + ".txt");
                                        if(!outputResult.exists()){
                                            runThreshold = true;
                                            break;
                                        }
                                    }    
                                }

                                boolean runAutomatic = false;
                                if(parameters.isAutomatic()){
                                    final File output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_automatic_");
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

                                Instances dataTrain = new Instances(data, 0);
                                Instances dataTest = new Instances(data, 0);

                                boolean enoughtData = SplitData.splitTrainTestSupervisedOneClass(data, dataTrain, dataTest, idClasse, numFold);
                                
                                //Grouping instance in order to later call the leaning algorithms
                                try{
                                    clusterer.buildClassifier(dataTrain);
                                    HashSet<Integer>[] solution = clusterer.getSolution();
                                    ArrayList<Instances> clusteredInstances = buildClusteredDataset(dataTrain, solution);

                                    //Learning goes here
                                    System.out.println("Here 6");
                                    learning(configuration, enoughtData, numRep, numFold, classifiers, parameters, clusteredInstances, dataTest, runThreshold, runAutomatic, idClasse, outputFile, numClasses);    
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
                    }    
            // }

         /*});
         threads.execute(thread);*/   
    }
    
    public static void learning(final SupervisedInductiveConfiguraton_OneClass_Clustering configuration, final boolean enoughtData, final int numRep, final int numFold,  final OneClassSupervisedClassifier[][][] classifiers, final ParametersOneClass parameters, final ArrayList<Instances> clusteredData, final Instances dataTest, final boolean runThreshold, final boolean runAutomatic, final int idClasse, final String outputFile, final int numClasses){
        System.out.println("Here 7");
        int numClusters = clusteredData.size();

        long buildingTime = 0;
        
        for (int cluster = 0; cluster < numClusters; cluster++) {
            long begin = System.currentTimeMillis();
            if(enoughtData == true){
                try{
                    classifiers[numRep][numFold][cluster].buildClassifier(clusteredData.get(cluster));
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
            buildingTime += end - begin;
        }
        buildingTime = buildingTime / numClusters;
        //Until here -> one classifier for each clustered data
        
        //Fix the building times
        
        if(parameters.isManual() && runThreshold){
            for(int t=0;t<parameters.getThresholds().size();t++){

                double threshold = parameters.getThreshold(t);
                double[] thresholds = new double[numClusters];
                for(int c=0;c<numClusters;c++){
                    thresholds[c] = threshold; 
                }
                
                ResultsOneClass results = null;

                final File output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_" + threshold);
                learnAndEvaluate(configuration, classifiers[numRep][numFold], thresholds, output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

            }

        }
        System.out.println("Here 8");
            
        if(parameters.isAutomatic() && runAutomatic){

            double[][] thresholdsClusters = getBestThresholds(parameters,classifiers[numRep][numFold],clusteredData,idClasse);

            File output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_automatic_minimum_");
            learnAndEvaluate(configuration, classifiers[numRep][numFold], thresholdsClusters[0], output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

            output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_automatic_average-3simga_");
            learnAndEvaluate(configuration, classifiers[numRep][numFold], thresholdsClusters[1], output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

            output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_automatic_average-2simga_");
            learnAndEvaluate(configuration, classifiers[numRep][numFold], thresholdsClusters[2], output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

            output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_automatic_average-1simga_");
            learnAndEvaluate(configuration, classifiers[numRep][numFold], thresholdsClusters[3], output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

            output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_automatic_average_");
            learnAndEvaluate(configuration, classifiers[numRep][numFold], thresholdsClusters[4], output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

            output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_automatic_average+1simga_");
            learnAndEvaluate(configuration, classifiers[numRep][numFold], thresholdsClusters[5], output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

            output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_automatic_average+2simga_");
            learnAndEvaluate(configuration, classifiers[numRep][numFold], thresholdsClusters[6], output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

            output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds() + "_automatic_average+3simga_");
            learnAndEvaluate(configuration, classifiers[numRep][numFold], thresholdsClusters[7], output, idClasse, numRep, numFold, numClasses, dataTest, buildingTime, enoughtData);

        }
        
    }
    
    public static ArrayList<Instances> buildClusteredDataset(Instances dataTrain, HashSet<Integer>[] solution){
        System.out.println("Here 9");
        ArrayList<Instances> clusteredData = new ArrayList<Instances>();
        int k=solution.length;
        for (int cluster = 0; cluster < k; cluster++) {
            Instances clusterData = new Instances(dataTrain,0);
            
            HashSet<Integer> documentsCluster = solution[cluster];
            for(int doc : documentsCluster){
                if(dataTrain.instance(doc) == null){
                    System.out.println("Aqui!!");
                }else if(clusterData == null){
                    System.out.println("Aqui!!");
                }
            
                clusterData.add(dataTrain.instance(doc));
            }
            
            clusteredData.add(clusterData);
        }
        System.out.println("Here 10");
        return clusteredData;
    }
    
    public static void learnAndEvaluate(SupervisedInductiveConfiguraton_OneClass configuration, OneClassSupervisedClassifier[] classifiers, double[] thresholds, File output, int idClasse, int numRep, int numFold, int numClasses, Instances dataTest, long buildingTime, boolean enoughtData){
        System.out.println("Here 11");
        int numClassifiers = classifiers.length;
        
        ResultsOneClass results = null;

        final File outputResult = new File(output.getAbsolutePath() + ".txt");
        if(outputResult.exists()){
            return;
        }

        final File outputTemp = new File(output.getAbsolutePath() + ".tmp");

        if(outputTemp.exists()){
            results = readResultFile(configuration, outputTemp);
        }else{
            results = new ResultsOneClass(output, numClasses, configuration.getNumReps(), configuration.getNumFolds(), "InductiveSupervised_OneClass_Clustering");
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
        
        for (int c = 0; c < numClassifiers; c++) {
            classifiers[c].setThreshold(thresholds[c]);
        }
        
        
        long begin = System.currentTimeMillis();
        Evaluations.SupervisedInductiveEvaluationOneClass_Clustering(classifiers, dataTest, confusionMatrix, idClasse);
        long end = System.currentTimeMillis();

        //printConfusionMatrix(idClasse, numFold, confusionMatrix);
        //printStatistics(confusionMatrix);

        results.setClassificationTime(idClasse, numRep, numFold, end - begin);
        results.computeEvaluationMeasures(confusionMatrix, idClasse, numRep, numFold);
        System.out.println("Here 2");
    }
    
    public static double[][] getBestThresholds(ParametersOneClass parameters, OneClassSupervisedClassifier[] classifier, ArrayList<Instances> clusteredDataTrain, int classTarget){
        
        int numClusters = clusteredDataTrain.size();
        
        double[][] thresholdsClusters = new double[8][numClusters];
        
        for(int c=0;c<numClusters;c++){
            int numInstances = clusteredDataTrain.get(c).numInstances();

            double[] scores = new double[numInstances];

            double sumScores = 0;
            double minimun = Double.MAX_VALUE;

            for(int inst=0; inst<numInstances; inst++){
                scores[inst] = classifier[c].getScore(clusteredDataTrain.get(c).instance(inst));
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
            thresholdsClusters[0][c] = minimun;
            thresholdsClusters[1][c] = average - (stdDev * 3);
            thresholdsClusters[2][c] = average - (stdDev * 2);
            thresholdsClusters[3][c] = average - (stdDev);
            thresholdsClusters[4][c] = average;
            thresholdsClusters[5][c] = average + (stdDev);
            thresholdsClusters[6][c] = average + (stdDev * 2);
            thresholdsClusters[7][c] = average + (stdDev * 3);
            
        }
        
        return thresholdsClusters;
         
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