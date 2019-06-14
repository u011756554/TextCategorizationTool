//*********************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Transductive Learning and Evaluations. The 
//              algorithms are based on document-document relations. The difference
//              between this class and TCT.TransductiveClassification is the
//              document-term matrix and the proximity file. The document-term
//              matrix used in this class must have and ID as first feature and 
//              a proximity file generated considering this matrix
//              (TCTInterface.Interface_Utilities_DocDocProximities.java). 
//              With this there is no need to compute similarities among documents
//              and generate document-document relation for different repetitions.
//**********************************************************************************

package TCT;

import TCTAlgorithms.Transductive.GFHF_DocDoc_ID;
import TCTAlgorithms.Transductive.GFHF_DocDoc_ID_AdjList_Transductive;
import TCTAlgorithms.Transductive.GFHF_DocDoc_ID_DiagMatrix_Transductive;
import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTAlgorithms.Transductive.LLGC_DocDoc_ID;
import TCTAlgorithms.Transductive.LLGC_DocDoc_ID_AdjList_Transductive;
import TCTAlgorithms.Transductive.LLGC_DocDoc_ID_DiagMatrix_Transductive;
import TCTAlgorithms.Transductive.TCHN_DocDoc_ID_Transductive;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocRelations_ID;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateExpNetworkAdjacencyList;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateGaussianNetworkMatrix;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateKnnNetworkCosineAdjList;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateKnnNetworkCosineMatrix;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateKnnNetworkEuclideanList;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateKnnNetworkEuclideanMatrix;
import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import TCTStructures.Neighbor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class TransductiveClassification_DocDoc_ID {
    
    /*Function to read the data (document-term matrix and document proximities) and build relations among documents. */
    public static void learning(TransductiveConfiguration_DocDocRelations_ID configuration){
    
        File arqMatrizDados = new File(configuration.getMatrizDados());
        if(!arqMatrizDados.exists()){
            System.out.println("Invalid ARFF file.");
            return;
        }
        System.out.println("ARFF file: " + arqMatrizDados.getAbsolutePath());
        File proximityFile = new File(configuration.getMatrizProx());
        if(!proximityFile.exists()){
            System.out.println("Invalid proximity file.");
            return;
        }
        System.out.println("ProximityFile: " + proximityFile.getAbsolutePath());
        File dirResultados = new File(configuration.getDirResultados());
        if(!dirResultados.exists()){
            System.out.println("Invalid result directory.");
            return;
        }
        System.out.println(arqMatrizDados.getAbsolutePath());
        System.out.println("Loading ARFF file");
        try{
            ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(arqMatrizDados.getAbsolutePath()); //Carregando arquivo de Dados
            Instances dataOriginal = trainSource.getDataSet();
            
            int numInsts = dataOriginal.numInstances();
            int numTerms = dataOriginal.numAttributes();
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
            outputFile.append(configuration.getDirResultados());
            outputFile.append("/");
            outputFile.append(dataOriginal.relationName());
            outputFile.append("_Transductive_DD_");
            
            if(configuration.isLLGC()){
                outputFilePar = new StringBuilder();
                outputFilePar.append(outputFile.toString());
                outputFilePar.append("LLGC_");
                Parameters_LLGC parametersLLGC = configuration.getParametersLLGC();
                if(configuration.isNetworkExp() == true){
                    for(int alpha=0;alpha<parametersLLGC.getAlphas().size();alpha++){
                        for(int sigma=0;sigma<configuration.getParametersExpNetwork().getSigmas().size();sigma++){
                            double valueSigma = configuration.getParametersExpNetwork().getSigma(sigma);
                            double[][] matSim = null;
                            Neighbor[] adjListDocs = null;
                            if(configuration.getAdjList() == true){
                                adjListDocs = GenerateExpNetworkAdjacencyList(proximityFile, valueSigma, numInsts, configuration.getParametersExpNetwork().isCosseno());
                            }else{
                                matSim = GenerateGaussianNetworkMatrix(proximityFile, valueSigma, numInsts, configuration.getParametersExpNetwork().isCosseno());
                            }
                            //System.out.println("Classification Algorithm: LLGC");
                            //System.out.println("Exp Network");
                            //System.out.println("Alpha: " + parametersLLGC.getAlpha(alpha));
                            //System.out.println("Sigma: " + configuration.getParametersExpNetwork().getSigma(sigma));
                            ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                            for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
                                double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append(numLabeledInstances);
                                outputFilePar2.append("_");
                                if(configuration.isPorcentage() == true){
                                    outputFilePar2.append("percentage");
                                    outputFilePar2.append("_");
                                }else{
                                    outputFilePar2.append("real");
                                    outputFilePar2.append("_");
                                }
                                outputFilePar2.append("_");
                                outputFilePar2.append("Exp");
                                outputFilePar2.append("_");
                                outputFilePar2.append(configuration.getParametersExpNetwork().getSigma(sigma));
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersLLGC.getAlpha(alpha));
                                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    LLGC_DocDoc_ID classifLLGC = null;
                                    if(configuration.getAdjList()){
                                        classifLLGC = new LLGC_DocDoc_ID_AdjList_Transductive();
                                        classifLLGC.setAdjListDocs(adjListDocs);
                                    }else{
                                        classifLLGC = new LLGC_DocDoc_ID_DiagMatrix_Transductive();
                                        classifLLGC.setMatSim(matSim);
                                    }
                                    classifLLGC.setUse(0);
                                    classifLLGC.setMaxNumIterations(parametersLLGC.getMaxNumberIterations());
                                    classifLLGC.setAlpha(parametersLLGC.getAlpha(alpha));
                                    classifiers[rep] = classifLLGC;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);    
                            }
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
                
                }
                if(configuration.isNetworkKnn() == true){
                    for(int alpha=0;alpha<parametersLLGC.getAlphas().size();alpha++){
                        for(int k=0;k<configuration.getParametersKnnNetwork().getKs().size();k++){
                            int valueK = configuration.getParametersKnnNetwork().getK(k);
                            double[][] matSim = null;
                            Neighbor[] adjListDocs = null;
                            if(configuration.getAdjList() == true){
                                if(configuration.getParametersKnnNetwork().isCosseno()){
                                    adjListDocs = GenerateKnnNetworkCosineAdjList(proximityFile, valueK, numInsts);
                                }else{
                                    adjListDocs = GenerateKnnNetworkEuclideanList(proximityFile, valueK, numInsts);
                                }
                            }else{
                                if(configuration.getParametersExpNetwork().isCosseno()){
                                    matSim = GenerateKnnNetworkCosineMatrix(proximityFile, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(proximityFile, valueK, numInsts);
                                }
                            }
                            //System.out.println("Classification Algorithm: LLGC");
                            //System.out.println("Doc-Doc Relations: Mutual Knn");
                            //System.out.println("Alpha: " + parametersLLGC.getAlpha(alpha));
                            //System.out.println("K: " + configuration.getParametersKnnNetwork().getK(k));
                            ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                            for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
                                double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append(numLabeledInstances);
                                outputFilePar2.append("_");
                                if(configuration.isPorcentage() == true){
                                    outputFilePar2.append("percentage");
                                    outputFilePar2.append("_");
                                }else{
                                    outputFilePar2.append("real");
                                    outputFilePar2.append("_");
                                }
                                outputFilePar2.append("_");
                                outputFilePar2.append("MutualKnn");
                                outputFilePar2.append("_");
                                outputFilePar2.append(configuration.getParametersKnnNetwork().getK(k));
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersLLGC.getAlpha(alpha));
                                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    LLGC_DocDoc_ID classifLLGC = null;
                                    if(configuration.getAdjList()){
                                        classifLLGC = new LLGC_DocDoc_ID_AdjList_Transductive();
                                        classifLLGC.setAdjListDocs(adjListDocs);
                                    }else{
                                        classifLLGC = new LLGC_DocDoc_ID_DiagMatrix_Transductive();
                                        classifLLGC.setMatSim(matSim);
                                    }
                                    classifLLGC.setUse(0);
                                    classifLLGC.setMaxNumIterations(parametersLLGC.getMaxNumberIterations());
                                    classifLLGC.setAlpha(parametersLLGC.getAlpha(alpha));
                                    classifiers[rep] = classifLLGC;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);    
                            }
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
                }
            }

            if(configuration.isGFHF()){
                outputFilePar = new StringBuilder();
                outputFilePar.append(outputFile.toString());
                outputFilePar.append("GFHF_");
                if(configuration.isPorcentage() == true){
                    outputFilePar.append("percentage");
                    outputFilePar.append("_");
                }else{
                    outputFilePar.append("real");
                    outputFilePar.append("_");
                }
                Parameters_GFHF parametersGFHF = configuration.getParametersGFHF();
                if(configuration.isNetworkExp() == true){
                    for(int sigma=0;sigma<configuration.getParametersExpNetwork().getSigmas().size();sigma++){
                        double valueSigma = configuration.getParametersExpNetwork().getSigma(sigma);
                        double[][] matSim = null;
                        Neighbor[] adjListDocs = null;
                        if(configuration.getAdjList() == true){
                           adjListDocs = GenerateExpNetworkAdjacencyList(proximityFile, valueSigma, numInsts, configuration.getParametersExpNetwork().isCosseno());
                        }else{
                            matSim = GenerateGaussianNetworkMatrix(proximityFile, valueSigma, numInsts, configuration.getParametersExpNetwork().isCosseno());
                        }
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
                            double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                            //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                            //System.out.println("Classification Algorithm: GFHF");
                            //System.out.println("Exp Network");
                            //System.out.println("Sigma: " + configuration.getParametersExpNetwork().getSigma(sigma));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append(numLabeledInstances);
                            outputFilePar2.append("_");
                            if(configuration.isPorcentage() == true){
                                outputFilePar2.append("percentage");
                                outputFilePar2.append("_");
                            }else{
                                outputFilePar2.append("real");
                                outputFilePar2.append("_");
                            }
                            outputFilePar2.append("_");
                            outputFilePar2.append("Exp");
                            outputFilePar2.append("_");
                            outputFilePar2.append(configuration.getParametersExpNetwork().getSigma(sigma));
                            TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                            for(int rep=0;rep<configuration.getNumReps();rep++){
                                GFHF_DocDoc_ID classifGFHF = null;
                                if(configuration.getAdjList() == true){
                                    classifGFHF = new GFHF_DocDoc_ID_AdjList_Transductive();
                                }else{
                                    classifGFHF = new GFHF_DocDoc_ID_DiagMatrix_Transductive();
                                }
                                classifGFHF.setUse(0);
                                if(configuration.getAdjList() == true){
                                    classifGFHF.setAdjListDocs(adjListDocs);
                                }else{
                                    classifGFHF.setMatSim(matSim);
                                }
                                classifGFHF.setMaxNumIterations(parametersGFHF.getMaxNumberIterations());
                                classifiers[rep] = classifGFHF;
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);    
                        }
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
                if(configuration.isNetworkKnn() == true){
                    for(int k=0;k<configuration.getParametersKnnNetwork().getKs().size();k++){
                        int valueK = configuration.getParametersKnnNetwork().getK(k);
                        double[][] matSim = null;
                        Neighbor[] adjListDocs = null;
                        if(configuration.getAdjList() == true){
                            if(configuration.getParametersKnnNetwork().isCosseno()){
                                adjListDocs = GenerateKnnNetworkCosineAdjList(proximityFile, valueK, numInsts);
                            }else{
                                adjListDocs = GenerateKnnNetworkEuclideanList(proximityFile, valueK, numInsts);
                            }
                        }else{
                            if(configuration.getParametersExpNetwork().isCosseno()){
                                matSim = GenerateKnnNetworkCosineMatrix(proximityFile, valueK, numInsts);
                            }else{
                                matSim = GenerateKnnNetworkEuclideanMatrix(proximityFile, valueK, numInsts);
                            }
                        }
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
                            double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                            //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                            //System.out.println("Classification Algorithm: GFHF");
                            //System.out.println("Doc-Doc Relations: Mutual Knn");
                            //System.out.println("K: " + configuration.getParametersKnnNetwork().getK(k));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append(numLabeledInstances);
                            outputFilePar2.append("_");
                            if(configuration.isPorcentage() == true){
                                outputFilePar2.append("percentage");
                                outputFilePar2.append("_");
                            }else{
                                outputFilePar2.append("real");
                                outputFilePar2.append("_");
                            }
                            outputFilePar2.append("_");
                            outputFilePar2.append("MutualKnn");
                            outputFilePar2.append("_");
                            outputFilePar2.append(configuration.getParametersKnnNetwork().getK(k));
                            TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                            for(int rep=0;rep<configuration.getNumReps();rep++){
                                GFHF_DocDoc_ID classifGFHF = null;
                                if(configuration.getAdjList() == true){
                                    classifGFHF = new GFHF_DocDoc_ID_AdjList_Transductive();
                                }else{
                                    classifGFHF = new GFHF_DocDoc_ID_DiagMatrix_Transductive();
                                }
                                classifGFHF.setUse(0);
                                if(configuration.getAdjList() == true){
                                    classifGFHF.setAdjListDocs(adjListDocs);
                                }else{
                                    classifGFHF.setMatSim(matSim);
                                }
                                classifGFHF.setMaxNumIterations(parametersGFHF.getMaxNumberIterations());
                                classifiers[rep] = classifGFHF;
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);    
                        }   
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
            }
            
            if(configuration.isTCHN()){
                outputFilePar = new StringBuilder();
                outputFilePar.append(outputFile.toString());
                outputFilePar.append("TCHN_DD_ID");
                if(configuration.isPorcentage() == true){
                    outputFilePar.append("percentage");
                    outputFilePar.append("_");
                }else{
                    outputFilePar.append("real");
                    outputFilePar.append("_");
                }
                Parameters_IMHN parametersTCHN = configuration.getParametersTCHN();
                if(configuration.isNetworkExp() == true){
                    for(int sigma=0;sigma<configuration.getParametersExpNetwork().getSigmas().size();sigma++){
                        double valueSigma = configuration.getParametersExpNetwork().getSigma(sigma);
                        double[][] matSim = GenerateGaussianNetworkMatrix(proximityFile, valueSigma, numInsts, configuration.getParametersExpNetwork().isCosseno());
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
                            double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                            //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                            //System.out.println("Classification Algorithm: GFHF");
                            //System.out.println("Exp Network");
                            //System.out.println("Sigma: " + configuration.getParametersExpNetwork().getSigma(sigma));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append(numLabeledInstances);
                            outputFilePar2.append("_");
                            if(configuration.isPorcentage() == true){
                                outputFilePar2.append("percentage");
                                outputFilePar2.append("_");
                            }else{
                                outputFilePar2.append("real");
                                outputFilePar2.append("_");
                            }
                            outputFilePar2.append("_");
                            outputFilePar2.append("Exp");
                            outputFilePar2.append("_");
                            outputFilePar2.append(configuration.getParametersExpNetwork().getSigma(sigma));
                            TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                            for(int rep=0;rep<configuration.getNumReps();rep++){
                                TCHN_DocDoc_ID_Transductive classifTCHN = new TCHN_DocDoc_ID_Transductive();
                                classifTCHN.setUse(0);
                                classifTCHN.setMatSim(matSim);
                                classifTCHN.setMaxNumIterationsLocal(parametersTCHN.getMaxNumberIterationsLocal());
                                classifTCHN.setMaxNumIterationsGlobal(parametersTCHN.getMaxNumberIterationsGlobal());
                                classifTCHN.setErrorCorrectionRate(0.1);
                                classifiers[rep] = classifTCHN;
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);    
                        }
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
                if(configuration.isNetworkKnn() == true){
                    for(int k=0;k<configuration.getParametersKnnNetwork().getKs().size();k++){
                        int valueK = configuration.getParametersKnnNetwork().getK(k);
                        double[][] matSim;
                        if(configuration.getParametersKnnNetwork().isCosseno()){
                            matSim = GenerateKnnNetworkCosineMatrix(proximityFile, valueK, numInsts);
                        }else{
                            matSim = GenerateKnnNetworkEuclideanMatrix(proximityFile, valueK, numInsts);
                        }
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
                            double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                            //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                            //System.out.println("Classification Algorithm: GFHF");
                            //System.out.println("Doc-Doc Relations: Mutual Knn");
                            //System.out.println("K: " + configuration.getParametersKnnNetwork().getK(k));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append(numLabeledInstances);
                            outputFilePar2.append("_");
                            if(configuration.isPorcentage() == true){
                                outputFilePar2.append("percentage");
                                outputFilePar2.append("_");
                            }else{
                                outputFilePar2.append("real");
                                outputFilePar2.append("_");
                            }
                            outputFilePar2.append("_");
                            outputFilePar2.append("MutualKnn");
                            outputFilePar2.append("_");
                            outputFilePar2.append(configuration.getParametersKnnNetwork().getK(k));
                            TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                            for(int rep=0;rep<configuration.getNumReps();rep++){
                                TCHN_DocDoc_ID_Transductive classifTCHN = new TCHN_DocDoc_ID_Transductive();
                                classifTCHN.setUse(0);
                                classifTCHN.setMatSim(matSim);
                                classifTCHN.setMaxNumIterationsLocal(parametersTCHN.getMaxNumberIterationsLocal());
                                classifTCHN.setMaxNumIterationsGlobal(parametersTCHN.getMaxNumberIterationsGlobal());
                                classifTCHN.setErrorCorrectionRate(0.1);
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);    
                        } 
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
            }
            
            System.out.println("Process concluded successfully");
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
    
    //Function to run and evaluate transductive learning
    public static void learning(final TransductiveConfiguration_DocDocRelations_ID configuration, final ExecutorService threads, final TransductiveLearner[] classifiers, final Instances dataOriginal, final String outputFile, final double numInstPerClass, final int numClasses){
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
    
    //Function to recover experiments
    public static void CheckExperiment(TransductiveConfiguration_DocDocRelations_ID configuration, File file, ArrayList<Double> accuracies, ArrayList<Double> microPrecisions, ArrayList<Double> microRecalls, ArrayList<Double> macroPrecisions, ArrayList<Double> macroRecalls, ArrayList<Long> buildingTimes, ArrayList<Long> classificationTimes, ArrayList<Integer> iterations){
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
            int numIterations = -1;
            
            while((line = arqResult.readLine()) != null){
                if(line.contains("Average") || line.contains("Media")){
                    accuracies.add(accuracy);
                    microPrecisions.add(microPrecision);
                    microRecalls.add(microRecall);
                    macroPrecisions.add(macroPrecision);
                    macroRecalls.add(macroRecall);
                    buildingTimes.add(buildingTime);
                    classificationTimes.add(classificationTime);
                    iterations.add(numIterations);
                    return;
                }
                if (line.contains("Repetition") || line.contains("Repetition")){
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
                        iterations.add(numIterations);
                    }
                    indRepetition = -1;
                    accuracy = -1;
                    microPrecision = -1;
                    microRecall = -1;
                    macroPrecision = -1;
                    macroRecall = -1;    
                    buildingTime = -1;
                    classificationTime = -1;
                    numIterations = -1;
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
                if((line.contains("Number of Iterations") || line.contains("Itera")) && (!line.contains("Average"))){
                    String[] parts = line.split(":");
                    if(parts.length == 2){
                        numIterations = Integer.parseInt(parts[1].trim());
                    }else{
                        numIterations = -1;
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
    
    
}
