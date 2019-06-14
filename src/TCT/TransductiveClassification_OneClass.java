//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Transductive Learning and Evaluation. The
//              classification algorithms are based on networks with
//              document-document or document-term relations.
//*****************************************************************************

package TCT;

import TCTAlgorithms.InductiveSupervisedOneClass.IMBHNR_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.KMeans_Parametric_SupervisedOneClass;
import TCTAlgorithms.InductiveSupervisedOneClass.KNNDensity_SupervisedOneClass;
import TCTAlgorithms.Transductive.ExpectationMaximization_Transductive;
import TCTAlgorithms.Transductive.GFHF_DocDoc_Transductive;
import TCTAlgorithms.Transductive.GNetMine_DocTerm_Transductive;
import TCTAlgorithms.Transductive.LLGC_DocDoc_Transductive;
import TCTAlgorithms.Transductive.TCHN_DocTerm_Transductive;
import TCTAlgorithms.Transductive.TSVM_Balanced_Transductive;
import TCTAlgorithms.Transductive.TSVM_Unbalanced_Transductive;
import TCTAlgorithms.TransductiveOneClass.SelfTraining;
import TCTAlgorithms.TransductiveOneClass.TransductiveLearnerOneClass;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_OneClass;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_SelfTraining;
import TCTIO.ListFiles;
import TCTParameters.Parameters_GNetMine_DocTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_EM;
import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import TCTParameters.SemiSupervisedLearning.Parameters_TSVM;
import TCTParameters.SupervisedOneClass.ParametersIMHN;
import TCTParameters.SupervisedOneClass.ParametersKNNDensity;
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
import weka.core.converters.ConverterUtils.DataSource;

public class TransductiveClassification_OneClass {
    
    /*Function to read a document-term matrix, build networks with Document-term or Term-Term relations, and set the parameters of transductive learning algorithms. */
    public static  void learning(TransductiveConfiguration_OneClass configuration){
        
        
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());

        ArrayList<String> filesIn = ListFiles.listFilesStr(new File(configuration.getDirEntrada()));

        try {

            for (int i = 0; i < filesIn.size(); i++) { // criando vetores contendo os atributos e suas frquências em cada documento da coleção

                if (!filesIn.get(i).endsWith(".arff")) {
                    continue;
                }
                System.out.println(filesIn.get(i));
                System.out.println("Loading ARFF file");

                long begin = System.currentTimeMillis();
                DataSource trainSource = new DataSource(filesIn.get(i).toString()); //Carregando arquivo de Dados
                Instances dataOriginal = trainSource.getDataSet();

                long end = System.currentTimeMillis();
                System.out.println("Loading time: " + ((end - begin) / (double) 1000));

                int numClasses = 0;
                Attribute classAtt = null;

                classAtt = dataOriginal.attribute(dataOriginal.numAttributes() - 1); //Setting the last feature as class
                dataOriginal.setClass(classAtt);
                numClasses = classAtt.numValues();

                for (int j = 0; j < numClasses; j++) {
                    System.out.println(j + ": " + classAtt.value(j));
                }

                StringBuilder outputFile = new StringBuilder();
                outputFile.append(configuration.getDirSaida());
                outputFile.append("/");
                outputFile.append(dataOriginal.relationName());
                outputFile.append("_Transductive_");

                for (int numEx = 0; numEx < configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size(); numEx++) {
                    double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                    if (configuration.isSelfTraining()) {
                        learningSelfTrainigSupervised(configuration, dataOriginal, threads, outputFile, numLabeledInstances, numClasses);
                    }

                    /*if(true){
                        Parameters_IMHN parametersTCBHN = configuration.getParametersTCBHNDirect();
                        
                        for(int errorCorrectionRate=0;errorCorrectionRate<parametersTCBHN.getErrorCorrectionRates().size();errorCorrectionRate++){
                            StringBuilder outputFilePar2 = new StringBuilder(outputFile.toString());
                            outputFilePar2.append("_TCBHNDirect_" + numLabeledInstances);
                            outputFilePar2.append(parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate) + "_");
                            for(int error=0;error<parametersTCBHN.getErrors().size();error++){
                                //System.out.println("Classification Algorithm: TCBHN");
                                //System.out.println("Error correction rate: " + parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate));
                                //System.out.println("Minimum mean squared error: " + parametersTCBHN.getError(error));
                                StringBuilder outputFilePar3 = new StringBuilder();
                                outputFilePar3.append(outputFilePar2.toString());
                                outputFilePar3.append(parametersTCBHN.getError(error));
                                TransductiveLearnerOneClass[][] classifiers = new TransductiveLearnerOneClass[configuration.getNumReps()][numClasses];
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    for (int classe = 0; classe < numClasses; classe++) {
                                        TCBHNDirect classifTCBHN = new TCBHNDirect();
                                        classifTCBHN.setMaxNumberGlobalIterations(parametersTCBHN.getMaxNumberIterationsGlobal());
                                        classifTCBHN.setMaxNumberLocalIterations(parametersTCBHN.getMaxNumberIterationsLocal());
                                        classifTCBHN.setErrorCorrectionRate(parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate));
                                        classifTCBHN.setMinError(parametersTCBHN.getError(error));
                                        classifiers[rep][classe] = classifTCBHN;    
                                    }
                                    
                                }
                                learningAndValidation(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses);
                                
                            }
                        }
                        
                    }*/
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

    public static void learningSelfTrainigSupervised(TransductiveConfiguration_OneClass configuration, Instances dataOriginal, ExecutorService threads, StringBuilder outputFile, double numLabeledInstances, int numClasses){
        StringBuilder outputFilePar = new StringBuilder();
        outputFilePar.append(outputFile.toString());
        outputFilePar.append("SelfTraining_");
        outputFilePar.append(numLabeledInstances);
        outputFilePar.append("_");
        if (configuration.isPorcentage() == true) {
            outputFilePar.append("percentage");
        } else {
            outputFilePar.append("real");
        }
        outputFilePar.append("_");
        TransductiveConfiguration_SelfTraining parameters = configuration.getParametersSelfTrainig();

        //Loop in the parameters of the Self-Training
        for (int numAdd = 0; numAdd < parameters.getNumInstPerClassAddTraining().size(); numAdd++) {
            int numAddExamples = parameters.getNumInstPerClassAddTraining().get(numAdd);
            for (int numIt = 0; numIt < parameters.getNumIterations().size(); numIt++) {
                int numIterations = parameters.getNumIterations().get(numIt);
                StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                outputFilePar2.append(numAddExamples + "_" + numIterations + "_");
                //k-NN Density Algorithms 
                if (parameters.isKnnDensity()) {
                    for (int idK = 0; idK < parameters.getParametersKNNDensity().getKs().size(); idK++) {
                        ParametersKNNDensity parametersKNN = parameters.getParametersKNNDensity();
                        StringBuilder outputFilePar3 = new StringBuilder();
                        outputFilePar3.append(outputFilePar2.toString());
                        outputFilePar3.append("KNNDensity_");
                        outputFilePar3.append(parametersKNN.getK(idK) + "_");
                        SelfTraining[][] classifiers = new SelfTraining[configuration.getNumReps()][numClasses];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            for (int classe = 0; classe < numClasses; classe++) {
                                KNNDensity_SupervisedOneClass classifierKNN = new KNNDensity_SupervisedOneClass();
                                classifierKNN.setK(parametersKNN.getK(idK));
                                SelfTraining classifier = new SelfTraining(numIterations, numAddExamples);
                                classifier.setSupervisedClassifier(classifierKNN);
                                classifiers[rep][classe] = classifier;
                            }
                        }
                        learningSelfTrainigTransductive(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses);
                    }
                }
                //k-Means Algorithm
                if(parameters.iskMeans()){
                    ParametersPrototypeBasedClustering parametersKME = parameters.getParametersKME();
                    //Old Implementation
                    for(int idK=0;idK<parametersKME.getKs().size();idK++){
                        StringBuilder outputFilePar3 = new StringBuilder(outputFilePar2.toString());
                        outputFilePar3.append("KME_");
                        outputFilePar3.append(parametersKME.getK(idK) + "_");
                        outputFilePar3.append(parametersKME.getNumMaxIterations() + "_");
                        outputFilePar3.append(parametersKME.getMinChangeRate() + "_");
                        outputFilePar3.append(parametersKME.getMinDiffObjective() + "_");
                        outputFilePar3.append(parametersKME.getNumTrials() + "_");
                        SelfTraining[][] classifiers = new SelfTraining[configuration.getNumReps()][numClasses];
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            for (int classe = 0; classe < numClasses; classe++) {
                                KMeans_Parametric_SupervisedOneClass classifierKME = new KMeans_Parametric_SupervisedOneClass();
                                classifierKME.setK(parametersKME.getK(idK));
                                classifierKME.setNumMaxIterations(parametersKME.getNumMaxIterations());
                                classifierKME.setChangeRate(parametersKME.getMinChangeRate());
                                classifierKME.setConvergence(parametersKME.getMinDiffObjective());
                                classifierKME.setNumTrials(parametersKME.getNumTrials());
                                classifierKME.setEuclidean(parametersKME.isEuclidean());
                                classifierKME.setCosine(parametersKME.isCosine());
                                classifierKME.setPearson(parametersKME.isPearson());
                                SelfTraining classifier = new SelfTraining(numIterations, numAddExamples);
                                classifier.setSupervisedClassifier(classifierKME);
                                classifiers[rep][classe] = classifier;
                            }
                        }
                        learningSelfTrainigTransductive(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses);
                    }
                }
                
                if(parameters.isImbhn()){
                    ParametersIMHN parametersIMBHN = parameters.getParametersIMBHNR();
                    for (int error = 0; error < parametersIMBHN.getErrors().size(); error++) {
                        for (int apr = 0; apr < parametersIMBHN.getErrorCorrectionRates().size(); apr++) {
                            //System.out.println("Classification Algorithm: IMBHN2");
                            //System.out.println("Minimum Mean Squared Error: " + parametersIMBHN2.getError(error));
                            //System.out.println("Error Correction Rate: " + parametersIMBHN2.getErrorCorrectionRate(apr));
                            StringBuilder outputFilePar3 = new StringBuilder(outputFilePar2.toString());
                            outputFilePar3.append("IMBHNR_");
                            outputFilePar3.append(parametersIMBHN.getErrorCorrectionRate(apr) + "_");
                            outputFilePar3.append(parametersIMBHN.getError(error) + "_");
                            SelfTraining[][] classifiers = new SelfTraining[configuration.getNumReps()][numClasses];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                for (int classe = 0; classe < numClasses; classe++) {
                                    IMBHNR_SupervisedOneClass classifierIMBHNR = new IMBHNR_SupervisedOneClass();
                                    classifierIMBHNR.setErrorCorrectionRate(parametersIMBHN.getErrorCorrectionRate(apr));
                                    classifierIMBHNR.setMinError(parametersIMBHN.getError(error));
                                    classifierIMBHNR.setMaxNumIterations(parametersIMBHN.getMaxNumberIterations());
                                    SelfTraining classifier = new SelfTraining(numIterations, numAddExamples);
                                    classifier.setSupervisedClassifier(classifierIMBHNR);
                                    classifiers[rep][classe] = classifier;
                                }
                            }
                            learningSelfTrainigTransductive(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses);
                        }
                    }
                }
            }
        }
        
    }
    
    public static void learningSelfTrainigTransductive(final TransductiveConfiguration_OneClass configuration, final ExecutorService threads, final SelfTraining[][] classifiers, final Instances dataOriginal, String outputFilePar, final double numLabeledInstances, final int numClasses){
        TransductiveConfiguration_SelfTraining parameters = configuration.getParametersSelfTrainig();
        
        if (parameters.isTcbhn()) {
            Parameters_IMHN parametersTCBHN = parameters.getParametersTCBHN();
            for (int errorCorrectionRate = 0; errorCorrectionRate < parametersTCBHN.getErrorCorrectionRates().size(); errorCorrectionRate++) {
                for (int error = 0; error < parametersTCBHN.getErrors().size(); error++) {
                    //System.out.println("Classification Algorithm: TCBHN");
                    //System.out.println("Error correction rate: " + parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate));
                    //System.out.println("Minimum mean squared error: " + parametersTCBHN.getError(error));
                    StringBuilder outputFilePar2 = new StringBuilder();
                    outputFilePar2.append(outputFilePar);
                    outputFilePar2.append("TCBHN_");
                    outputFilePar2.append(parametersTCBHN.getMaxNumberIterationsGlobal() + "_");
                    outputFilePar2.append(parametersTCBHN.getMaxNumberIterationsLocal() + "_");        
                    outputFilePar2.append(parametersTCBHN.getError(error));
                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                        for (int classe = 0; classe < numClasses; classe++) {
                            TCHN_DocTerm_Transductive classifTCBHN = new TCHN_DocTerm_Transductive();
                            classifTCBHN.setmaxNumberGlobalIterations(parametersTCBHN.getMaxNumberIterationsGlobal());
                            classifTCBHN.setmaxNumberLocalIterations(parametersTCBHN.getMaxNumberIterationsLocal());
                            classifTCBHN.setErrorCorrectionRate(parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate));
                            classifTCBHN.setMinError(parametersTCBHN.getError(error));
                            classifiers[rep][classe].setTransductiveClassifier(classifTCBHN);;
                        }
                    }
                    learningAndValidation(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                }
            }

        }
        
        if(parameters.isGnetMine()){
            Parameters_GNetMine_DocTerm parametersGNetMine = parameters.getParametersGNetMine();
            for(int alphaDoc=0;alphaDoc<parametersGNetMine.getAlphasDocs().size();alphaDoc++){
                StringBuilder outputFilePar2 = new StringBuilder();
                outputFilePar2.append(outputFilePar.toString());
                outputFilePar2.append("GNetMine_");
                outputFilePar2.append("_");
                outputFilePar2.append(parametersGNetMine.getAlphaDoc(alphaDoc));
                for(int rep=0;rep<configuration.getNumReps();rep++){
                    for (int classe = 0; classe < numClasses; classe++) {
                        GNetMine_DocTerm_Transductive classifGNetMine = new GNetMine_DocTerm_Transductive();
                        classifGNetMine.setMaxNumIterations(parametersGNetMine.getMaxNumberIterations());
                        classifGNetMine.setAlphaDoc(parametersGNetMine.getAlphaDoc(alphaDoc));
                        classifiers[rep][classe].setTransductiveClassifier(classifGNetMine);
                    }
                }
                learningAndValidation(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
            }  
        }
        
        if(parameters.isTsvm()){
            Parameters_TSVM parametersTSVM = parameters.getParametersTSVM();
            if (parametersTSVM.getBalanced()) {
                for (int c = 0; c < parametersTSVM.getCs().size(); c++) {
                    double valueC = parametersTSVM.getC(c);
                    //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                    //System.out.println("Classification Algorithm: TSVM - Same Proportion of Labeled Examples");
                    //System.out.println("C: " + valueC);
                    StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                    outputFilePar2.append("_");
                    outputFilePar2.append("TSVM_SameProportinoLabeledExamples_");
                    outputFilePar2.append(valueC);
                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                        for (int classe = 0; classe < numClasses; classe++) {
                            TSVM_Balanced_Transductive classifTSVM = new TSVM_Balanced_Transductive();
                            classifTSVM.setC(valueC);
                            classifTSVM.setMaxNumIterations(parametersTSVM.getMaxNumberIterations());
                            classifiers[rep][classe].setTransductiveClassifier(classifTSVM);
                        }
                        
                    }
                    learningAndValidation(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                }
            }
            if (parametersTSVM.getUnbalanced()) {
                for (int c = 0; c < parametersTSVM.getCs().size(); c++) {
                    double valueC = parametersTSVM.getC(c);
                    StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
                    outputFilePar2.append("TSVM_");
                    outputFilePar2.append(valueC);
                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                        for (int classe = 0; classe < numClasses; classe++) {
                            TSVM_Unbalanced_Transductive classifTSVM = new TSVM_Unbalanced_Transductive();
                            classifTSVM.setC(valueC);
                            classifTSVM.setMaxNumIterations(parametersTSVM.getMaxNumberIterations());
                            classifiers[rep][classe].setTransductiveClassifier(classifTSVM);    
                        }
                    }
                    learningAndValidation(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                }
            }
            
            
        }
                          
        if (parameters.isEm()) {
            Parameters_EM parametersEM = parameters.getParametersEM();
            StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
            outputFilePar2.append("EM_");
            for (int weight = 0; weight < parametersEM.getWeightsUnlabeled().size(); weight++) {
                for (int numComp = 0; numComp < parametersEM.getNumCompClasses().size(); numComp++) {
                    //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                    //System.out.println("Classification Algorithm: Expectation Maximization");
                    StringBuilder outputFilePar3 = new StringBuilder(outputFilePar2);
                    outputFilePar3.append(parametersEM.getWeightUnlabeledInstances(weight));
                    outputFilePar3.append("_");
                    outputFilePar3.append(parametersEM.getNumCompClasse(numComp));
                    outputFilePar3.append("_");
                    outputFilePar3.append(parametersEM.getMinLogLikelihood());
                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                        for (int classe = 0; classe < numClasses; classe++) {
                            ExpectationMaximization_Transductive classifEM = new ExpectationMaximization_Transductive();
                            classifEM.setMaxNumIterations(parametersEM.getMaxNumberIterations());
                            classifEM.setMinLogLikelihood(parametersEM.getMinLogLikelihood());
                            classifEM.setWeightUnlabeled(parametersEM.getWeightUnlabeledInstances(weight));
                            classifEM.setNumCompClass(parametersEM.getNumCompClasse(numComp));
                            classifiers[rep][classe].setTransductiveClassifier(classifEM);       
                        }
                    }
                    learningAndValidation(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses);
                }

            }

        }
        
        if (parameters.isLlgc()) {
            StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
            outputFilePar2.append("LLGC_");
            Parameters_LLGC parametersLLGC = parameters.getParametersLLGC();
            for (int alpha = 0; alpha < parametersLLGC.getAlphas().size(); alpha++) {
                for (int k = 0; k < parameters.getParametersKNNNetwork().getKs().size(); k++) {
                    StringBuilder outputFilePar3 = new StringBuilder(outputFilePar2.toString());
                    outputFilePar3.append("_");
                    outputFilePar3.append("MutualKnn");
                    outputFilePar3.append("_");
                    outputFilePar3.append(parameters.getParametersKNNNetwork().getK(k));
                    outputFilePar3.append("_");
                    outputFilePar3.append(parametersLLGC.getAlpha(alpha));
                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                        for (int classe = 0; classe < numClasses; classe++) {
                            LLGC_DocDoc_Transductive classifLLGC = new LLGC_DocDoc_Transductive();
                            classifLLGC.setUse(0);
                            if (parameters.getParametersKNNNetwork().isCosseno()) {
                                classifLLGC.setCosine(true);
                            } else {
                                classifLLGC.setCosine(false);
                            }
                            classifLLGC.setMaxNumIterations(parametersLLGC.getMaxNumberIterations());
                            classifLLGC.setAlpha(parametersLLGC.getAlpha(alpha));
                            classifLLGC.setK(parameters.getParametersKNNNetwork().getK(k));
                            classifLLGC.setGaussian(false);
                            classifLLGC.setMutualKnn(true);
                            classifiers[rep][classe].setTransductiveClassifier(classifLLGC);
                        }
                        
                    }
                    learningAndValidation(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses);
                }
            }
            
        }
        
        if (parameters.isGfhf()) {
            StringBuilder outputFilePar2 = new StringBuilder(outputFilePar.toString());
            outputFilePar2.append("GFHF_");
            Parameters_GFHF parametersGFHF = parameters.getParametersGFHF();
            for (int k = 0; k < parameters.getParametersKNNNetwork().getKs().size(); k++) {
                StringBuilder outputFilePar3 = new StringBuilder(outputFilePar2.toString());
                    outputFilePar3.append("_");
                    outputFilePar3.append("MutualKnn");
                    outputFilePar3.append("_");
                    outputFilePar3.append(parameters.getParametersKNNNetwork().getK(k));
                    outputFilePar3.append("_");
                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                    for (int classe = 0; classe < numClasses; classe++) {
                        GFHF_DocDoc_Transductive classifGFHF = new GFHF_DocDoc_Transductive();
                        classifGFHF.setUse(0);
                        if (parameters.getParametersKNNNetwork().isCosseno()) {
                            classifGFHF.setCosine(true);
                        } else {
                            classifGFHF.setCosine(false);
                        }
                        classifGFHF.setMaxNumIterations(parametersGFHF.getMaxNumberIterations());
                        classifGFHF.setK(parameters.getParametersKNNNetwork().getK(k));
                        classifGFHF.setGaussian(false);
                        classifGFHF.setMutualKnn(true);
                        classifiers[rep][classe].setTransductiveClassifier(classifGFHF);    
                    }
                }
                learningAndValidation(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses);
            }
        }

    }
    
    
    
    
    //Function to run and evaluate transductive learning learning
    public static void learningAndValidation(final TransductiveConfiguration_OneClass configuration, final ExecutorService threads, final TransductiveLearnerOneClass[][] classifiers, final Instances dataOriginal, String outputFile, final double numInstPerClass, final int numClasses){
        
        try{
            
            final ResultsOneClass results;
            
            final File output = new File(outputFile);
            final File outputResult = new File(output.getAbsolutePath() + ".txt");
            if(outputResult.exists()){
                return;
            }
            final File outputTemp = new File(output.getAbsolutePath() + ".tmp");
            
            if(outputTemp.exists()){
                ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(output.getAbsolutePath() + ".tmp"));
                results = (ResultsOneClass)objInput.readObject();
                objInput.close();
            }else{
                results = new ResultsOneClass(output, numClasses, configuration.getNumReps(), 1, "Transductive");
            }
            
            //System.out.println("Output: " + output.getAbsolutePath());
            for (int classe = 0; classe < numClasses; classe++) {

                final int idClasse = classe;

                for (int rep = 0; rep < configuration.getNumReps(); rep++) {

                    if (results.getComplete(idClasse, rep, 0) == true) {
                        continue;
                    }

                    final int numRep = rep;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Integer[][] confusionMatrix = new Integer[2][2];
                            for (int class1 = 0; class1 < 2; class1++) {
                                for (int class2 = 0; class2 < 2; class2++) {
                                    confusionMatrix[class1][class2] = 0;
                                }
                            }

                            Instances dataTrain = new Instances(dataOriginal, 0);
                            Instances dataTest = new Instances(dataOriginal, 0);

                            Instances data = new Instances(dataOriginal);
                            data.randomize(new Random(numRep));

                            boolean enouthData = SplitData.splitTrainTestTransductiveOneClass(configuration.isPorcentage(), data, dataTrain, dataTest, numInstPerClass, idClasse);

                            long begin = System.currentTimeMillis();
                            try {
                                classifiers[numRep][idClasse].setInterestClass(idClasse);
                                classifiers[numRep][idClasse].buildClassifier(dataTrain, dataTest);
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

                            long end = System.currentTimeMillis();
                            results.setBuildingTime(idClasse, numRep, 0, (end - begin));
                            results.setNumIterations(idClasse, numRep, 0, 0);
                            begin = System.currentTimeMillis();
                            Evaluations.TransductiveEvaluationOneClass(classifiers[numRep][idClasse], dataTest, confusionMatrix,idClasse);
                            end = System.currentTimeMillis();
                            results.setClassificationTime(idClasse, numRep, 0, (end - begin));

                            results.computeEvaluationMeasures(confusionMatrix, idClasse, numRep, 0);
                            results.setComplete(idClasse, numRep, 0, true);

                        }
                    });

                    threads.execute(thread);

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