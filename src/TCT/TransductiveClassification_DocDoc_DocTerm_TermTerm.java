//*******************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Transductive Learning and Evaluations.
//              The algoritms are based on networks composed by document-document,
//              document-term, and term-term relations.
//*******************************************************************************

package TCT;

import TCTNetworkGeneration.TermNetworkGeneration;
import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTAlgorithms.Transductive.GNetMine_DocDoc_DocTerm_TermTerm_Transductive;
import TCTAlgorithms.Transductive.LPHN_DocDoc_DocTerm_TermTerm_Transductive_ID;
import TCTAlgorithms.Transductive.TCHN_DocDoc_DocTerm_TermTerm_Transductive_ID;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateGaussianNetworkMatrix;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateKnnNetworkCosineMatrix;
import static TCTNetworkGeneration.DocumentNetworkGeneration_ID.GenerateKnnNetworkEuclideanMatrix;
import TCTParameters.Parameters_GNetMine_DocDoc_DocTerm_TermTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_LPHN;
import TCTParameters.Parameters_DocumentNetwork_Exp;
import TCTParameters.Parameters_TermNetwork;
import TCTParameters.Parameters_DocumentNetwork_Knn;
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

public class TransductiveClassification_DocDoc_DocTerm_TermTerm {
    
    /*Function to read the data (document-term matrix and document proximities), and build document-document and term-term relations. */
    public static  void learning(TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations configuration){
        File fileArff = new File(configuration.getArff());
        File fileDist = new File(configuration.getArqDist());
        File fileProb = new File(configuration.getArqProb());
        File dirSaida = new File(configuration.getDirSaida());
        
        int numTerms = 0;
        
        if(!fileArff.getAbsolutePath().endsWith(".arff")){
            System.out.println("Invalid ARF file");
            return;
        }
        if(!fileDist.getAbsolutePath().endsWith(".dist")){
            System.out.println("Invalid Proximity File");
            return;
        }
        if(!fileProb.getAbsolutePath().endsWith(".prb")){
            System.out.println("Invalid probability file");
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
            ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(fileArff.getAbsolutePath().toString()); //Carregando arquivo de Dados
            dataOriginal = trainSource.getDataSet();

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
        outputFile.append("_Transductive_DD-DT-TT_");
        
        //Generating term-term relations
        try{ 
            if(configuration.isSupportNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("TT-Support-");
                Parameters_TermNetwork parametersSupportNetwork = configuration.getParametersSupportNetwork();
                if(parametersSupportNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersSupportNetwork.getThresholds().size();lim++){
                        double threshold = parametersSupportNetwork.getThreshold(lim);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("Threshold-" + threshold + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateSupportNetworkThreshold(fileProb,numTerms,threshold,parametersSupportNetwork.getRelative());
                        //Generating document-document relations
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN_");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int k=0;k<parametersNetworkKnn.getKs().size();k++){
                                int valueK = parametersNetworkKnn.getK(k);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                if(parametersSupportNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersSupportNetwork.getKs().size();topK++){
                        int k = parametersSupportNetwork.getK(topK);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("TopK-" + k + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateSupportNetworkTopK(fileProb,numTerms,k);
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN-");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int indK=0;indK<parametersNetworkKnn.getKs().size();indK++){
                                int valueK = parametersNetworkKnn.getK(indK);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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

            }
            if(configuration.isMutualInformationNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("TT-MutualInformation-");
                Parameters_TermNetwork parametersMutualInformationNetwork = configuration.getParametersMutualInformationNetwork();
                if(parametersMutualInformationNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersMutualInformationNetwork.getThresholds().size();lim++){
                        double threshold = parametersMutualInformationNetwork.getThreshold(lim);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("Threshold-" + threshold + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateMutualInformationNetworkThreshold(fileProb,numTerms,threshold,parametersMutualInformationNetwork.getRelative());
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN-");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int k=0;k<parametersNetworkKnn.getKs().size();k++){
                                int valueK = parametersNetworkKnn.getK(k);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + "_TopK_" + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                if(parametersMutualInformationNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersMutualInformationNetwork.getKs().size();topK++){
                        int k = parametersMutualInformationNetwork.getK(topK);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("TopK-" + k + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateMutualInformationNetworkTopK(fileProb,numTerms,k);
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN-");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int indK=0;indK<parametersNetworkKnn.getKs().size();indK++){
                                int valueK = parametersNetworkKnn.getK(indK);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);                    
                                boolean exit = false;
                                threads.shutdown();
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
            }
            if(configuration.isKappaNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("TT-Kappa-");
                Parameters_TermNetwork parametersKappaNetwork = configuration.getParametersKappaNetwork();
                if(parametersKappaNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersKappaNetwork.getThresholds().size();lim++){
                        double threshold = parametersKappaNetwork.getThreshold(lim);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("Threshold-" + threshold + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateKappaNetworkThreshold(fileProb,numTerms,threshold,parametersKappaNetwork.getRelative());
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN-");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int k=0;k<parametersNetworkKnn.getKs().size();k++){
                                int valueK = parametersNetworkKnn.getK(k);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                if(parametersKappaNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersKappaNetwork.getKs().size();topK++){
                        int k = parametersKappaNetwork.getK(topK);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("TopK-" + k + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateKappaNetworkTopK(fileProb,numTerms,k);
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN-");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int indK=0;indK<parametersNetworkKnn.getKs().size();indK++){
                                int valueK = parametersNetworkKnn.getK(indK);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
            }
            if(configuration.isShapiroNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("TT-Shapiro-");
                Parameters_TermNetwork parametersShapiroNetwork = configuration.getParametersShapiroNetwork();
                if(parametersShapiroNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersShapiroNetwork.getThresholds().size();lim++){
                        double threshold = parametersShapiroNetwork.getThreshold(lim);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("Threshold-" + threshold + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateShapiroNetworkThreshold(fileProb,numTerms,threshold,parametersShapiroNetwork.getRelative());
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN-");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int k=0;k<parametersNetworkKnn.getKs().size();k++){
                                int valueK = parametersNetworkKnn.getK(k);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);                 
                                boolean exit = false;
                                threads.shutdown();
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
                if(parametersShapiroNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersShapiroNetwork.getKs().size();topK++){
                        int k = parametersShapiroNetwork.getK(topK);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("TopK-" + k + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateShapiroNetworkTopK(fileProb,numTerms,k);
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN-");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int indK=0;indK<parametersNetworkKnn.getKs().size();indK++){
                                int valueK = parametersNetworkKnn.getK(indK);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);                 
                                boolean exit = false;
                                threads.shutdown();
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
            }
            if(configuration.isYulesQNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(outputFilePar.toString());
                outputFilePar.append("TT-YulesQ-");
                Parameters_TermNetwork parametersYulesQNetwork = configuration.getParametersYulesQNetwork();
                if(parametersYulesQNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersYulesQNetwork.getThresholds().size();lim++){
                        double threshold = parametersYulesQNetwork.getThreshold(lim);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("Threshold-" + threshold + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateYulesQNetworkThreshold(fileProb,numTerms,threshold,parametersYulesQNetwork.getRelative());
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());                        
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN-");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int k=0;k<parametersNetworkKnn.getKs().size();k++){
                                int valueK = parametersNetworkKnn.getK(k);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                if(parametersYulesQNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersYulesQNetwork.getKs().size();topK++){
                        int k = parametersYulesQNetwork.getK(topK);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar);
                        outputFilePar2.append("TopK-" + k + "_");
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateYulesQNetworkTopK(fileProb,numTerms,k);
                        if(configuration.isNetworkKnn()){
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: KNN Network");
                            outputFilePar3.append("DD-KNN-");
                            Parameters_DocumentNetwork_Knn parametersNetworkKnn = configuration.getParametersKnnNetwork();
                            for(int indK=0;indK<parametersNetworkKnn.getKs().size();indK++){
                                int valueK = parametersNetworkKnn.getK(indK);
                                double[][] matSim;
                                if(configuration.getCosine()){
                                    matSim = GenerateKnnNetworkCosineMatrix(fileDist, valueK, numInsts);
                                }else{
                                    matSim = GenerateKnnNetworkEuclideanMatrix(fileDist, valueK, numInsts);
                                }
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueK + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            System.out.println("Doc-Doc Relations: Exp Network");
                            outputFilePar3.append("DD-Exp-");
                            Parameters_DocumentNetwork_Exp parametersNetworkExp = configuration.getParametersExpNetwork();
                            for(int sigma=0;sigma<parametersNetworkExp.getSigmas().size();sigma++){
                                double valueSigma = parametersNetworkExp.getSigma(sigma);
                                double[][] matSim;
                                matSim = GenerateGaussianNetworkMatrix(fileDist, valueSigma, numInsts, configuration.getCosine()); 
                                ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                                learning(configuration,threads,matSim,adjacencyListTerms,outputFile.toString(),outputFilePar3.toString() + valueSigma + "_",dataOriginal,numClasses);
                                boolean exit = false;
                                threads.shutdown();
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
    
    //Function to set the parameters of transductive learning algorithms
    public static void learning(TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations configuration, ExecutorService threads, double[][] matSim, Neighbor[] adjacencyListTerms, String output, String output2, Instances dataOriginal,int numClasses){
        
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
                Parameters_IMHN parametersIMHN = configuration.getParameters_IMHN();
                for(int error=0;error<parametersIMHN.getErrors().size();error++){
                    for(int apr=0;apr<parametersIMHN.getErrorCorrectionRates().size();apr++){
                        //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                        //System.out.println("Classification Algorithm: TCHN DDeTT");
                        //System.err.println("Error Mínimo: " + parametersIMHN.getError(error));
                        //System.out.println("Learning Rate: " + parametersIMHN.getErrorCorrectionRate(apr));
                        StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                        outputFilePar2.append("TCHN_DD-DT-TT_");
                        outputFilePar2.append(output2);
                        outputFilePar2.append(parametersIMHN.getErrorCorrectionRate(apr));
                        outputFilePar2.append("_");
                        outputFilePar2.append(parametersIMHN.getError(error));
                        TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            TCHN_DocDoc_DocTerm_TermTerm_Transductive_ID classifIMHN = new TCHN_DocDoc_DocTerm_TermTerm_Transductive_ID();
                            classifIMHN.setUse(0);
                            classifIMHN.setAdjacencyListTerms(adjacencyListTerms);
                            classifIMHN.setMatSim(matSim);
                            classifIMHN.setErrorCorrectionRate(parametersIMHN.getErrorCorrectionRate(apr));
                            classifIMHN.setMinError(parametersIMHN.getError(error));
                            classifIMHN.setmaxNumberLocalIterations(parametersIMHN.getMaxNumberIterationsLocal());
                            classifIMHN.setmaxNumberGlobalIterations(parametersIMHN.getMaxNumberIterationsGlobal());
                            classifiers[rep] = classifIMHN;
                        }
                        learning(configuration, threads, classifiers, dataOriginal, output + outputFilePar2.toString(), numLabeledInstances, numClasses);    
                    }
                }    
            }
            if(configuration.isLPHN()){
                StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                outputFilePar2.append("LPHN_DD-DT-TT_");
                outputFilePar2.append(output2);
                Parameters_LPHN parametersLPHN = configuration.getParameters_LPHN();
                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                //System.out.println("Classification Algorithm: LPHNDDeTT");
                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                for(int rep=0;rep<configuration.getNumReps();rep++){
                    LPHN_DocDoc_DocTerm_TermTerm_Transductive_ID classifLPHN = new LPHN_DocDoc_DocTerm_TermTerm_Transductive_ID();
                    classifLPHN.setUse(0);
                    classifLPHN.setAdjacencyListTerms(adjacencyListTerms);
                    classifLPHN.setMatSim(matSim);
                    classifLPHN.setMaxNumIterations(parametersLPHN.getMaxNumberIterations());
                    classifiers[rep] = classifLPHN;
                }
                learning(configuration, threads, classifiers, dataOriginal, output + outputFilePar2.toString(), numLabeledInstances, numClasses);    
            }
            if(configuration.isGNetMine()){
                outputFilePar.append("GNetMine_DT-DT-TT");
                Parameters_GNetMine_DocDoc_DocTerm_TermTerm parametersGNetMine = configuration.getParameters_GNetMine();
                for(int alpha=0;alpha<parametersGNetMine.getAlphasDocs().size();alpha++){
                    for(int lambda=0;lambda<parametersGNetMine.getLambdas().size();lambda++){
                        //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                        //System.out.println("Classification Algorithm: GNetMine");
                        //System.out.println("Alpha Doc: " + parametersGNetMine.getAlphaDoc(alpha));
                        //System.out.println("Lambda DocDoc: " + parametersGNetMine.getLambda(lambda).lambdaDocDoc);
                        //System.out.println("Lambda DocTermo: " + parametersGNetMine.getLambda(lambda).lambdaDocTerm);
                        //System.out.println("Lambda TermoTermo: " + parametersGNetMine.getLambda(lambda).lambdaTermTerm);
                        StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                        outputFilePar2.append("GNetMine_DT-DT-TT_");
                        outputFilePar2.append(output2);
                        outputFilePar2.append("_");
                        outputFilePar2.append(parametersGNetMine.getAlphaDoc(alpha));
                        outputFilePar2.append("_");
                        outputFilePar2.append(parametersGNetMine.getLambda(lambda).lambdaDocDoc);
                        outputFilePar2.append("_");
                        outputFilePar2.append(parametersGNetMine.getLambda(lambda).lambdaDocTerm);
                        outputFilePar2.append("_");
                        outputFilePar2.append(parametersGNetMine.getLambda(lambda).lambdaTermTerm);
                        TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            GNetMine_DocDoc_DocTerm_TermTerm_Transductive classifGNetMine = new GNetMine_DocDoc_DocTerm_TermTerm_Transductive();
                            classifGNetMine.setAdjacencyListTerms(adjacencyListTerms);
                            classifGNetMine.setMatSim(matSim);
                            classifGNetMine.setMaxNumIterations(parametersGNetMine.getMaxNumberIterations());
                            classifGNetMine.setAlphaDoc(parametersGNetMine.getAlphaDoc(alpha));
                            classifGNetMine.setLambdaDocDoc(parametersGNetMine.getLambda(lambda).lambdaDocDoc);
                            classifGNetMine.setLambdaDocTermo(parametersGNetMine.getLambda(lambda).lambdaDocTerm);
                            classifGNetMine.setLambdaTermoTermo(parametersGNetMine.getLambda(lambda).lambdaTermTerm);
                            classifiers[rep] = classifGNetMine;
                        }
                        learning(configuration, threads, classifiers, dataOriginal, output + outputFilePar2.toString(), numLabeledInstances, numClasses);    
                    }
                }    
            }
        }    
    }
    
    //Function to run and evaluate transductive learning
    public static void learning(final TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations configuration, final ExecutorService threads, final TransductiveLearner[] classifiers, final Instances dataOriginal, final String outputFile, final double numInstPerClass, final int numClasses){
        
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
