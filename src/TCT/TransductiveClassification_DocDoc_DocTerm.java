//******************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Transductive Learning and Evaluations.
//              The algoritms are based on networks composed by document-document
//              and document-term relations.
//*****************************************************************************

package TCT;

import TCTAlgorithms.Transductive.GNetMine_DocDoc_DocTerm_Transductive;
import TCTParameters.Parameters_GNetMine_DocTerm_TermTerm;
import TCTParameters.Parameters_DocumentNetwork_Knn;
import TCTParameters.Parameters_DocumentNetwork_Exp;
import TCTParameters.SemiSupervisedLearning.Parameters_LPHN;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTAlgorithms.Transductive.LPHN_DocDoc_DocTerm_Transductive;
import TCTAlgorithms.Transductive.TCHN_DocDoc_DocTerm_Transductive;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndDocDocRelations;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateGaussianNetworkMatrix;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateKnnNetworkCosineMatrix;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateKnnNetworkEuclideanMatrix;
import TCTStructures.Neighbor;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class TransductiveClassification_DocDoc_DocTerm {
    
    /*Function to read the data (document-term matrix and document proximities) and build relations among documents. */
    public static  void learning(TransductiveConfiguration_DocTermAndDocDocRelations configuration){
        
        File fileArff = new File(configuration.getArff());
        File fileDist = new File(configuration.getArqDist());
        File dirSaida = new File(configuration.getDirSaida());
        
        int numTerms = 0;
        
        if(!fileArff.getAbsolutePath().endsWith(".arff")){
            System.out.println("Invalid ARF file");
            return;
        }
        if(!fileDist.getAbsolutePath().endsWith(".dist")){
            System.out.println("Invalid proximity file");
            return;
        }
        if(!dirSaida.exists()){
            System.out.println("Output directory does not exist");
            return;
        }
        
        System.out.println(fileArff.getAbsolutePath());
        System.out.println("Loading ARFF file");
        Instances dataOriginal = null;
        int numClasses = 0;
        int numInsts = 0;
        try{
            long begin = System.currentTimeMillis();
            ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(fileArff.getAbsolutePath().toString()); //Carregando arquivo de Dados
            dataOriginal = trainSource.getDataSet();

            long end = System.currentTimeMillis();
            System.out.println("Tempo de carregamento: " + ((end - begin) / (double)1000));

            Attribute classAtt = null;
            numTerms = dataOriginal.numAttributes()-1;
            classAtt = dataOriginal.attribute(numTerms); //Setting the last feature as class
            dataOriginal.setClass(classAtt);
            numClasses = classAtt.numValues();
            numInsts = dataOriginal.numInstances();
            
            for(int j=0;j<numClasses;j++){
                System.out.println(j + ": " + classAtt.value(j));
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
        
        StringBuilder outputFile = new StringBuilder();
        outputFile.append(configuration.getDirSaida());
        outputFile.append("/");
        outputFile.append(dataOriginal.relationName());
        outputFile.append("_Transductive_DTandDD_");
        
        try{
            //Building document-document relations
            if(configuration.isNetworkKnn()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("NetworkKnn");
                outputFilePar.append("_");
                Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                for(int k=0;k<parametersNetworkKnn.getKs().size();k++){
                    int valueK = parametersNetworkKnn.getK(k);
                    System.out.println("Doc-Doc Relations: Mutual Knn");
                    System.out.println("K: " + valueK);
                    double[][] matSim = null; //Similarity Matrix
                    if(configuration.getCosine()){
                        matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                    }else{
                        matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                    }
                    ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                    learning(configuration,threads,matSim,outputFile.toString(),outputFilePar.toString() + "_" + valueK + "_",dataOriginal,numClasses);
                    threads.shutdown();
                    boolean exit = false;
                    while(exit == false){
                        if(threads.isTerminated()){
                            exit = true;
                        }else{
                            Thread.sleep(1000);
                        }
                    }
                }
            }
            if(configuration.isNetworkExp()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("NetworkExp");
                outputFilePar.append("_");
                Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                    double valueSigma = parametersNetworkExp.getSigma(sigma);
                    System.out.println("Doc-Doc Relations: Exp");
                    System.out.println("Sigma: " + valueSigma);
                    double[][] matSim;
                    Neighbor[] adjListDocs = null;
                    matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                    ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                    learning(configuration,threads,matSim,outputFile.toString(),outputFilePar.toString() + "_" + valueSigma + "_",dataOriginal,numClasses);
                    threads.shutdown();
                    boolean exit = false;
                    while(exit == false){
                        if(threads.isTerminated()){
                            exit = true;
                        }else{
                            Thread.sleep(1000);
                        }
                    }
                }
            }
        
            System.out.println("Process concluded successfully");
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().send();
        
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

    //Function to set the parameters of transductive learning algorithms
    public static void learning(TransductiveConfiguration_DocTermAndDocDocRelations configuration, ExecutorService threads, double[][] matSim, String output, String output2, Instances dataOriginal,int numClasses){
        for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
            double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
            StringBuilder outputFilePar = new StringBuilder();
            outputFilePar.append(numLabeledInstances);
            outputFilePar.append("_");
            if(configuration.isPorcentage() == true){
                outputFilePar.append("percentage");
                outputFilePar.append("_");
            }else{
                outputFilePar.append("real");
                outputFilePar.append("_");
            }
            if(configuration.isIMHN()){
                StringBuilder outputFilePar2 = new StringBuilder();
                outputFilePar2.append(outputFilePar);
                outputFilePar2.append("TCHN_");
                Parameters_IMHN parametersIMHN = configuration.getParameters_IMHN();
                for(int error=0;error<parametersIMHN.getErrors().size();error++){
                    for(int apr=0;apr<parametersIMHN.getErrorCorrectionRates().size();apr++){
                        //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                        //System.out.println("Classification Algorithm: TCHN DTeDD");
                        //System.err.println("Error Mínimo: " + parametersIMHN.getError(error));
                        //System.out.println("Learning Rate: " + parametersIMHN.getErrorCorrectionRate(apr));
                        StringBuilder outputFilePar3 = new StringBuilder();
                        outputFilePar3.append(parametersIMHN.getErrorCorrectionRate(apr));
                        outputFilePar3.append("_");
                        outputFilePar3.append(parametersIMHN.getError(error));
                        TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            TCHN_DocDoc_DocTerm_Transductive classifIMHN = new TCHN_DocDoc_DocTerm_Transductive();
                            classifIMHN.setMatSim(matSim);
                            classifIMHN.setUse(0);
                            classifIMHN.setErrorCorrectionRate(parametersIMHN.getErrorCorrectionRate(apr));
                            classifIMHN.setMinError(parametersIMHN.getError(error));
                            classifIMHN.setmaxNumberLocalIterations(parametersIMHN.getMaxNumberIterationsLocal());
                            classifIMHN.setmaxNumberGlobalIterations(parametersIMHN.getMaxNumberIterationsGlobal());
                            classifiers[rep] = classifIMHN;
                        }
                        learning(configuration, threads, classifiers, dataOriginal, output + outputFilePar2.toString() + output2 + outputFilePar3.toString(), numLabeledInstances, numClasses);    
                    }
                }    
            }
            if(configuration.isLPHN()){
                //LPHN_DocDoc_DocTerm_Transductive classifLPHN = new LPHN_DocDoc_DocTerm_Transductive();
                StringBuilder outputFilePar2 = new StringBuilder();
                outputFilePar2.append(outputFilePar);
                outputFilePar2.append("LPHN_");
                Parameters_LPHN parametersLPHN = configuration.getParameters_LPHN();
                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                //System.out.println("Classification Algorithm: LPHN DTeDD");
                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                for(int rep=0;rep<configuration.getNumReps();rep++){
                    LPHN_DocDoc_DocTerm_Transductive classifLPHN = new LPHN_DocDoc_DocTerm_Transductive();
                    classifLPHN.setMatSim(matSim);
                    classifLPHN.setUse(0);
                    classifLPHN.setMaxNumIterations(parametersLPHN.getMaxNumberIterations());
                    classifiers[rep] = classifLPHN;
                }
                learning(configuration, threads, classifiers, dataOriginal, output + outputFilePar2.toString() + output2, numLabeledInstances, numClasses);    
            }
            if(configuration.isGNetMine()){
                StringBuilder outputFilePar2 = new StringBuilder();
                outputFilePar2.append(outputFilePar);
                outputFilePar2.append("GNetMine_");
                Parameters_GNetMine_DocTerm_TermTerm parametersGNetMine = configuration.getParameters_GNetMine();
                for(int alpha=0;alpha<parametersGNetMine.getAlphasDocs().size();alpha++){
                    for(int lambda=0;lambda<parametersGNetMine.getLambdasDocTermo().size();lambda++){
                        //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                        //System.out.println("Classification Algorithm: GNetMine DTeDD");
                        //System.out.println("Alpha Doc: " + parametersGNetMine.getAlphaDoc(alpha));
                        //System.out.println("Lambda DocTermo: " + parametersGNetMine.getLambdaDocTermo(lambda));
                        StringBuilder outputFilePar3 = new StringBuilder();
                        outputFilePar3.append("_");
                        outputFilePar3.append(parametersGNetMine.getAlphaDoc(alpha));
                        outputFilePar3.append("_");
                        outputFilePar3.append(parametersGNetMine.getLambdaDocTermo(lambda));
                        TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            GNetMine_DocDoc_DocTerm_Transductive classifGNetMine = new GNetMine_DocDoc_DocTerm_Transductive();
                            classifGNetMine.setMatSim(matSim);
                            classifGNetMine.setUse(0);
                            classifiers[rep] = classifGNetMine;
                            classifGNetMine.setMaxNumIterations(parametersGNetMine.getMaxNumberIterations());
                            classifGNetMine.setAlphaDoc(parametersGNetMine.getAlphaDoc(alpha));
                            classifGNetMine.setLambdaDocTermo(parametersGNetMine.getLambdaDocTermo(lambda));
                        }
                        learning(configuration, threads, classifiers, dataOriginal, output + outputFilePar2.toString() + output2 + outputFilePar3.toString(), numLabeledInstances, numClasses);    
                    }
                    
                }    
            }
        }    
    }
    
    //Function to run and evaluate transductive learning
    public static void learning(final TransductiveConfiguration_DocTermAndDocDocRelations configuration, final ExecutorService threads, final TransductiveLearner[] classifiers, final Instances dataOriginal, String outputFile, final double numInstPerClass, final int numClasses){
        
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
