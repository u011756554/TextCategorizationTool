//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: February 27, 2015
// Description: Class to perform Inctive Semi-Supervised Lerning and Evaluation.
//              The semi-supervised learning algorithms are based on vector-
//              space model or networks wit document-term relations. 
//*****************************************************************************

package TCT;

import TCTAlgorithms.InductiveSemiSupervised.InductiveSemiSupervisedClassifier;
import TCTAlgorithms.InductiveSemiSupervised.ExpectationMaximizationMultipleComponents_InductiveSemiSupervised;
import TCTAlgorithms.InductiveSemiSupervised.GFHF_DocDoc_InductiveSemiSupervised;
import TCTAlgorithms.InductiveSemiSupervised.GNetMine_DocTermRelations_InductiveSemiSupervised;
import TCTAlgorithms.InductiveSemiSupervised.LLGC_DocDoc_InductiveSemiSupervised;
import TCTAlgorithms.InductiveSemiSupervised.LPHN_DocTermRelations_InductiveSemiSupervised;
import TCTAlgorithms.InductiveSemiSupervised.TCHN_DocTermRelations_InductiveSemiSupervised;
import TCTAlgorithms.InductiveSemiSupervised.TSVM_Balanced_InductiveSemiSupervised;
import TCTAlgorithms.InductiveSemiSupervised.TSVM_Unbalanced_InductiveSemiSupervised;
import TCTAlgorithms.InductiveSemiSupervised.TagBased_DocTermRelation_InductiveSemiSupervised;
import TCTAlgorithms.InductiveSemiSupervised.NMFE_SSCC_Hybrid_InductiveSemiSupervised;
import TCTIO.ListFiles;
import TCTConfigurations.SemiSupervisedLearning.SemiSupervisedInductiveConfiguration;
import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.Parameters_GNetMine_DocTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import TCTParameters.SemiSupervisedLearning.Parameters_LPHN;
import TCTParameters.SemiSupervisedLearning.Parameters_NMFE_SSCC;
import TCTParameters.SemiSupervisedLearning.Parameters_TagBased;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class SemiSupervisedInductiveClassification_VSM_DocTerm_DocDoc {
    
    /*Function to read a document-term matrix and set parameters and algorithms to induce a classification model through semi-supervised learning */
    public static  void learning(SemiSupervisedInductiveConfiguration configuration){
        
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
        
        ArrayList<String> filesIn = ListFiles.listFilesStr(new File(configuration.getDirEntrada()));
        
        try {

            for (int i = 0; i < filesIn.size(); i++) {

                if (!filesIn.get(i).endsWith(".arff")) {
                    continue;
                }
                System.out.println(filesIn.get(i));
                System.out.println("Loading ARFF file");

                DataSource trainSource = new DataSource(filesIn.get(i)); //Carregando arquivo de Dados
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
                outputFile.append("_SemiSupervisedInductive_");

                for (int numEx = 0; numEx < configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size(); numEx++) {
                    double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                    if (configuration.isExpectationMaximization()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("_");
                        outputFilePar.append("EM_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        for (int weight = 0; weight < configuration.getParametersEM().getWeightsUnlabeled().size(); weight++) {
                            for (int numComp = 0; numComp < configuration.getParametersEM().getNumCompClasses().size(); numComp++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: Expectation Maximization");
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append(configuration.getParametersEM().getWeightUnlabeledInstances(weight));
                                outputFilePar2.append("_");
                                //System.out.println("Peso Objetos Não Rotulados: " + configuration.getParametersEM().getWeightUnlabeledInstances(weight));
                                outputFilePar2.append(configuration.getParametersEM().getNumCompClasse(numComp));
                                //System.out.println("Número de components por classe: " + configuration.getParametersEM().getNumCompClasse(numComp));
                                InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        ExpectationMaximizationMultipleComponents_InductiveSemiSupervised classifEM = new ExpectationMaximizationMultipleComponents_InductiveSemiSupervised();
                                        classifEM.setMaxNumIterations(configuration.getParametersEM().getMaxNumberIterations());
                                        classifEM.setMinLogLikelihood(configuration.getParametersEM().getMinLogLikelihood());
                                        classifEM.setWeightUnlabeledDocs(configuration.getParametersEM().getWeightUnlabeledInstances(weight));
                                        classifEM.setNumCompClass(configuration.getParametersEM().getNumCompClasse(numComp));
                                        classifiers[rep][fold] = classifEM;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                            }
                        }
                    }
                    if (configuration.isTB()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("TagBased_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        Parameters_TagBased parametersTagBased = configuration.getParametersTB();
                        for (int beta = 0; beta < parametersTagBased.getBetas().size(); beta++) {
                            for (int gamma = 0; gamma < parametersTagBased.getGammas().size(); gamma++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: TagBased");
                                //System.out.println("Beta: " + parametersTagBased.getBeta(beta));
                                //System.out.println("Gamma: " + parametersTagBased.getGamma(gamma));
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersTagBased.getBeta(beta));
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersTagBased.getGamma(gamma));
                                InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        TagBased_DocTermRelation_InductiveSemiSupervised classifTagBased = new TagBased_DocTermRelation_InductiveSemiSupervised();
                                        classifTagBased.setMaxNumIterations(parametersTagBased.getMaxNumberIterations());
                                        classifTagBased.setBeta(parametersTagBased.getBeta(beta));
                                        classifTagBased.setGamma(parametersTagBased.getGamma(gamma));
                                        classifiers[rep][fold] = classifTagBased;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                            }
                        }
                    }
                    if (configuration.isGNetMine()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("GNetMine_DT_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        Parameters_GNetMine_DocTerm parametersGNetMine = configuration.getParametersGNetMine();
                        for (int alphaDoc = 0; alphaDoc < parametersGNetMine.getAlphasDocs().size(); alphaDoc++) {
                            //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                            //System.out.println("Classification Algorithm: GNetMine");
                            //System.out.println("Alpha Doc: " + parametersGNetMine.getAlphaDoc(alphaDoc));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append("_");
                            outputFilePar2.append(parametersGNetMine.getAlphaDoc(alphaDoc));
                            outputFilePar2.append("_");
                            InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    GNetMine_DocTermRelations_InductiveSemiSupervised classifGNetMine = new GNetMine_DocTermRelations_InductiveSemiSupervised();
                                    classifGNetMine.setMaxNumIterations(parametersGNetMine.getMaxNumberIterations());
                                    classifGNetMine.setAlphaDoc(parametersGNetMine.getAlphaDoc(alphaDoc));
                                    classifiers[rep][fold] = classifGNetMine;
                                }
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                        }
                    }
                    if (configuration.isLPHN()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("LPHN_DT_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        Parameters_LPHN parametersLPBHN = configuration.getParametersLPHN();
                        //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                        //System.out.println("Classification Algorithm: LPBHN");
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                        for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                LPHN_DocTermRelations_InductiveSemiSupervised classifLPBHN = new LPHN_DocTermRelations_InductiveSemiSupervised();
                                classifLPBHN.setMaxNumIterations(parametersLPBHN.getMaxNumberIterations());
                                classifiers[rep][fold] = classifLPBHN;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                    }
                    if (configuration.isTCHN()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("TCHN-R_DT_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        Parameters_IMHN parametersTCBHN = configuration.getParametersTCHN();
                        outputFilePar.append(parametersTCBHN.getMaxNumberIterationsGlobal() + "_");
                        outputFilePar.append(parametersTCBHN.getMaxNumberIterationsLocal() + "_");
                        for (int errorCorrectionRate = 0; errorCorrectionRate < parametersTCBHN.getErrorCorrectionRates().size(); errorCorrectionRate++) {
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append(parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate) + "_");
                            for (int error = 0; error < parametersTCBHN.getErrors().size(); error++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: TCHN");
                                //System.out.println("Error correction rate: " + parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate));
                                //System.out.println("Minimum mean squared error: " + parametersTCBHN.getError(error));
                                StringBuilder outputFilePar3 = new StringBuilder();
                                outputFilePar3.append(outputFilePar2.toString());
                                outputFilePar3.append(parametersTCBHN.getError(error));
                                InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        TCHN_DocTermRelations_InductiveSemiSupervised classifTCBHN = new TCHN_DocTermRelations_InductiveSemiSupervised();
                                        classifTCBHN.setmaxNumberGlobalIterations(parametersTCBHN.getMaxNumberIterationsGlobal());
                                        classifTCBHN.setmaxNumberLocalIterations(parametersTCBHN.getMaxNumberIterationsLocal());
                                        classifTCBHN.setErrorCorrectionRate(parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate));
                                        classifTCBHN.setMinError(parametersTCBHN.getError(error));
                                        classifiers[rep][fold] = classifTCBHN;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), (int) numLabeledInstances, numClasses);
                            }
                        }
                    }
                    if (configuration.isTSVM()) {
                        if (configuration.getParametersTSVM().getBalanced()) {
                            for (int c = 0; c < configuration.getParametersTSVM().getCs().size(); c++) {
                                double valueC = configuration.getParametersTSVM().getC(c);
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);                        
                                //System.out.println("Classification Algorithm: TSVM - Same Proportion of Labeled Examples");
                                //System.out.println("C: " + valueC);
                                outputFilePar = new StringBuilder();
                                outputFilePar.append(outputFile.toString());
                                outputFilePar.append("_");
                                outputFilePar.append(numLabeledInstances);
                                outputFilePar.append("_");
                                outputFilePar.append("TSVM_SameProportionLabeledExamples_");
                                if (configuration.isPorcentage() == true) {
                                    outputFilePar.append("percentage");
                                    outputFilePar.append("_");
                                } else {
                                    outputFilePar.append("real");
                                    outputFilePar.append("_");
                                }
                                outputFilePar.append(valueC);
                                InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        TSVM_Balanced_InductiveSemiSupervised classifTSVM = new TSVM_Balanced_InductiveSemiSupervised();
                                        classifTSVM.setC(valueC);
                                        classifTSVM.setMaxNumIterations(configuration.getParametersTSVM().getMaxNumberIterations());
                                        classifiers[rep][fold] = classifTSVM;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), (int) numLabeledInstances, numClasses);
                            }
                        }
                        if (configuration.getParametersTSVM().getUnbalanced()) {
                            for (int c = 0; c < configuration.getParametersTSVM().getCs().size(); c++) {
                                double valueC = configuration.getParametersTSVM().getC(c);
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: TSVM");
                                //System.out.println("C: " + valueC);
                                outputFilePar = new StringBuilder();
                                outputFilePar.append(outputFile.toString());
                                outputFilePar.append("_");
                                outputFilePar.append(numLabeledInstances);
                                outputFilePar.append("_");
                                outputFilePar.append("TSVM_");
                                if (configuration.isPorcentage() == true) {
                                    outputFilePar.append("percentage");
                                    outputFilePar.append("_");
                                } else {
                                    outputFilePar.append("real");
                                    outputFilePar.append("_");
                                }
                                outputFilePar.append(valueC);
                                InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        TSVM_Unbalanced_InductiveSemiSupervised classifTSVM = new TSVM_Unbalanced_InductiveSemiSupervised();
                                        classifTSVM.setC(valueC);
                                        classifTSVM.setMaxNumIterations(configuration.getParametersTSVM().getMaxNumberIterations());
                                        classifiers[rep][fold] = classifTSVM;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), (int) numLabeledInstances, numClasses);
                            }
                        }
                    }
                    if (configuration.isLLGC()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("LLGC_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        Parameters_LLGC parametersLLGC = configuration.getParametersLLGC();
                        if (configuration.isNetworkExp() == true) {

                            for (int alpha = 0; alpha < parametersLLGC.getAlphas().size(); alpha++) {
                                for (int sigma = 0; sigma < configuration.getParametersNetworkExp().getSigmas().size(); sigma++) {
                                    //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                    //System.out.println("Classification Algorithm: LLGC");
                                    //System.out.println("Exp Network");
                                    //System.out.println("Alpha: " + parametersLLGC.getAlpha(alpha));
                                    //System.out.println("Sigma: " + configuration.getParametersNetworkExp().getSigma(sigma));
                                    //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                    StringBuilder outputFilePar2 = new StringBuilder();
                                    outputFilePar2.append(outputFilePar.toString());
                                    outputFilePar2.append("_");
                                    outputFilePar2.append("Exp");
                                    outputFilePar2.append("_");
                                    outputFilePar2.append(configuration.getParametersNetworkExp().getSigma(sigma));
                                    outputFilePar2.append("_");
                                    outputFilePar2.append(parametersLLGC.getAlpha(alpha));
                                    InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                    for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                            LLGC_DocDoc_InductiveSemiSupervised classifLLGC = new LLGC_DocDoc_InductiveSemiSupervised();
                                            classifLLGC.setUse(0);
                                            if (configuration.getParametersNetworkExp().isCosseno()) {
                                                classifLLGC.setCosine(true);
                                            } else {
                                                classifLLGC.setCosine(false);
                                            }
                                            classifLLGC.setMaxNumIterations(parametersLLGC.getMaxNumberIterations());
                                            classifLLGC.setAlpha(parametersLLGC.getAlpha(alpha));
                                            classifLLGC.setSigma(configuration.getParametersNetworkExp().getSigma(sigma));
                                            classifLLGC.setGaussian(true);
                                            classifLLGC.setMutualKnn(false);
                                            classifiers[rep][fold] = classifLLGC;
                                        }
                                    }
                                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                                }
                            }
                        }
                        if (configuration.isNetworkKnn() == true) {

                            for (int alpha = 0; alpha < parametersLLGC.getAlphas().size(); alpha++) {
                                for (int k = 0; k < configuration.getParametersNetworkKnn().getKs().size(); k++) {
                                    //System.out.println("Classification Algorithm: LLGC");
                                    //System.out.println("Exp Network");
                                    //System.out.println("Alpha: " + parametersLLGC.getAlpha(alpha));
                                    //System.out.println("K: " + configuration.getParametersNetworkKnn().getK(k));
                                    //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                    StringBuilder outputFilePar2 = new StringBuilder();
                                    outputFilePar2.append(outputFilePar.toString());
                                    outputFilePar2.append("_");
                                    outputFilePar2.append("MutualKnn");
                                    outputFilePar2.append("_");
                                    outputFilePar2.append(configuration.getParametersNetworkKnn().getK(k));
                                    outputFilePar2.append("_");
                                    outputFilePar2.append(parametersLLGC.getAlpha(alpha));
                                    InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                    for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                            LLGC_DocDoc_InductiveSemiSupervised classifLLGC = new LLGC_DocDoc_InductiveSemiSupervised();
                                            classifLLGC.setUse(0);
                                            if (configuration.getParametersNetworkKnn().isCosseno()) {
                                                classifLLGC.setCosine(true);
                                            } else {
                                                classifLLGC.setCosine(false);
                                            }
                                            classifLLGC.setMaxNumIterations(parametersLLGC.getMaxNumberIterations());
                                            classifLLGC.setAlpha(parametersLLGC.getAlpha(alpha));
                                            classifLLGC.setK(configuration.getParametersNetworkKnn().getK(k));
                                            classifLLGC.setGaussian(false);
                                            classifLLGC.setMutualKnn(true);
                                            classifiers[rep][fold] = classifLLGC;
                                        }
                                    }
                                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                                }
                            }
                        }

                    }

                    if (configuration.isGFHF()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("GFHF_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        Parameters_GFHF parametersGFHF = configuration.getParametersGFHF();
                        if (configuration.isNetworkExp() == true) {
                            for (int sigma = 0; sigma < configuration.getParametersNetworkExp().getSigmas().size(); sigma++) {
                                //System.out.println("Classification Algorithm: GFHF");
                                //System.out.println("Exp Network");
                                //System.out.println("Sigma: " + configuration.getParametersNetworkExp().getSigma(sigma));
                                //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");
                                outputFilePar2.append("Exp");
                                outputFilePar2.append("_");
                                outputFilePar2.append(configuration.getParametersNetworkExp().getSigma(sigma));
                                InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        GFHF_DocDoc_InductiveSemiSupervised classifGFHF = new GFHF_DocDoc_InductiveSemiSupervised();
                                        classifGFHF.setUse(0);
                                        if (configuration.getParametersNetworkExp().isCosseno()) {
                                            classifGFHF.setCosine(true);
                                        } else {
                                            classifGFHF.setCosine(false);
                                        }
                                        classifGFHF.setMaxNumIterations(parametersGFHF.getMaxNumberIterations());
                                        classifGFHF.setSigma(configuration.getParametersNetworkExp().getSigma(sigma));
                                        classifGFHF.setGaussian(true);
                                        classifGFHF.setMutualKnn(false);
                                        classifiers[rep][fold] = classifGFHF;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                            }
                        }
                        if (configuration.isNetworkKnn() == true) {
                            for (int k = 0; k < configuration.getParametersNetworkKnn().getKs().size(); k++) {
                                //System.out.println("Classification Algorithm: GFHF");
                                //System.out.println("Doc-Doc Relations: Mutual Knn");
                                //System.out.println("K: " + configuration.getParametersNetworkKnn().getK(k));
                                //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");
                                outputFilePar2.append("MutualKnn");
                                outputFilePar2.append("_");
                                outputFilePar2.append(configuration.getParametersNetworkKnn().getK(k));
                                InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        GFHF_DocDoc_InductiveSemiSupervised classifGFHF = new GFHF_DocDoc_InductiveSemiSupervised();
                                        classifGFHF.setUse(0);
                                        if (configuration.getParametersNetworkKnn().isCosseno() == true) {
                                            classifGFHF.setCosine(true);
                                        } else {
                                            classifGFHF.setCosine(false);
                                        }
                                        classifGFHF.setMaxNumIterations(parametersGFHF.getMaxNumberIterations());
                                        classifGFHF.setK(configuration.getParametersNetworkKnn().getK(k));
                                        classifGFHF.setGaussian(false);
                                        classifGFHF.setMutualKnn(true);
                                        classifiers[rep][fold] = classifGFHF;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                            }
                        }
                    }

                    if (configuration.isNMFE()) {
                        Parameters_NMFE_SSCC parameters_NMFE = configuration.getParametersNMFE();
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("NMFE_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        outputFilePar.append(parameters_NMFE.getMinDifference());
                        outputFilePar.append("_");
                        outputFilePar.append(parameters_NMFE.getNumMaxIterations());
                        outputFilePar.append("_");
                        for (int alpha = 0; alpha < parameters_NMFE.getAlphas().size(); alpha++) {
                            double valueAlpha = parameters_NMFE.getAlpha(alpha);
                            for (int beta = 0; beta < parameters_NMFE.getBetas().size(); beta++) {
                                double valueBeta = parameters_NMFE.getBeta(beta);
                                for (int omega = 0; omega < parameters_NMFE.getOmegas().size(); omega++) {
                                    double valueOmega = parameters_NMFE.getOmega(omega);
                                    if (configuration.isNetworkExp() == true) {
                                        for (int sigma = 0; sigma < configuration.getParametersNetworkExp().getSigmas().size(); sigma++) {
                                            //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                            //System.out.println("Classification Algorithm: NMFE");
                                            //System.out.println("Alpha: " + valueAlpha);
                                            //System.out.println("Beta: " + valueBeta);
                                            //System.out.println("Omega: " + valueOmega);
                                            //System.out.println("Exp Network");
                                            //System.out.println("Sigma: " + configuration.getParametersNetworkExp().getSigma(sigma));
                                            //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                            StringBuilder outputFilePar2 = new StringBuilder();
                                            outputFilePar2.append(outputFilePar.toString());
                                            outputFilePar2.append("_");
                                            outputFilePar2.append(valueAlpha + "_" + valueBeta + "_" + valueOmega + "_");
                                            outputFilePar2.append("Exp");
                                            outputFilePar2.append("_");
                                            outputFilePar2.append(configuration.getParametersNetworkExp().getSigma(sigma));
                                            InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                                    NMFE_SSCC_Hybrid_InductiveSemiSupervised classifNMFE = new NMFE_SSCC_Hybrid_InductiveSemiSupervised();
                                                    classifNMFE.setMinDifference(parameters_NMFE.getMinDifference());
                                                    classifNMFE.setNumIterations(parameters_NMFE.getNumMaxIterations());
                                                    classifNMFE.setAlpha(valueAlpha);
                                                    classifNMFE.setBeta(valueBeta);
                                                    classifNMFE.setOmega(valueOmega);
                                                    if (configuration.getParametersNetworkExp().isCosseno()) {
                                                        classifNMFE.setCosine(true);
                                                    } else {
                                                        classifNMFE.setCosine(false);
                                                    }
                                                    classifNMFE.setSigma(configuration.getParametersNetworkExp().getSigma(sigma));
                                                    classifNMFE.setGaussian(true);
                                                    classifiers[rep][fold] = classifNMFE;
                                                }
                                            }
                                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                                        }
                                    }
                                    if (configuration.isNetworkKnn() == true) {
                                        for (int k = 0; k < configuration.getParametersNetworkKnn().getKs().size(); k++) {
                                            //System.out.println("Classification Algorithm: NMFE");
                                            //System.out.println("Alpha: " + valueAlpha);
                                            //System.out.println("Beta: " + valueBeta);
                                            //System.out.println("Omega: " + valueOmega);
                                            //System.out.println("KNN Network");
                                            //System.out.println("K: " + configuration.getParametersNetworkKnn().getK(k));
                                            //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                            StringBuilder outputFilePar2 = new StringBuilder();
                                            outputFilePar2.append(outputFilePar.toString());
                                            outputFilePar2.append("_");
                                            outputFilePar2.append(valueAlpha + "_" + valueBeta + "_" + valueOmega + "_");
                                            outputFilePar2.append("MutualKnn");
                                            outputFilePar2.append("_");
                                            outputFilePar2.append(configuration.getParametersNetworkKnn().getK(k));
                                            InductiveSemiSupervisedClassifier[][] classifiers = new InductiveSemiSupervisedClassifier[configuration.getNumReps()][configuration.getNumFolds()];
                                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                                    NMFE_SSCC_Hybrid_InductiveSemiSupervised classifNMFE = new NMFE_SSCC_Hybrid_InductiveSemiSupervised();
                                                    classifNMFE.setMinDifference(parameters_NMFE.getMinDifference());
                                                    classifNMFE.setNumIterations(parameters_NMFE.getNumMaxIterations());
                                                    classifNMFE.setAlpha(valueAlpha);
                                                    classifNMFE.setBeta(valueBeta);
                                                    classifNMFE.setOmega(valueOmega);
                                                    classifNMFE.setK(configuration.getParametersNetworkKnn().getK(k));
                                                    classifNMFE.setGaussian(false);
                                                    if (configuration.getParametersNetworkKnn().isCosseno()) {
                                                        classifNMFE.setCosine(true);
                                                    } else {
                                                        classifNMFE.setCosine(false);
                                                    }
                                                    classifNMFE.setK(configuration.getParametersNetworkKnn().getK(k));
                                                    classifNMFE.setGaussian(false);
                                                    classifiers[rep][fold] = classifNMFE;
                                                }
                                            }
                                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), (int) numLabeledInstances, numClasses);
                                        }
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

    //Function to run and evaluate semi-supervised learning
    public static void learning(final SemiSupervisedInductiveConfiguration configuration, final ExecutorService threads, final InductiveSemiSupervisedClassifier[][] classifiers, Instances dataOriginal, String outputFile, final int numInstPerClass, final int numClasses){
        
        try{
            final Results results;
            
            final File output = new File(outputFile + "_" + configuration.getNumReps() + "_" + configuration.getNumFolds());
            
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
                results = new Results(output, configuration.getNumReps(), configuration.getNumFolds(), "InductiveSemiSupervised");
            }
            
            //System.out.println("Output: " + output.getAbsolutePath());

            final Instances data = new Instances(dataOriginal);
            data.setClass(dataOriginal.attribute(dataOriginal.numAttributes()-1));
            data.randomize(new Random(0));
            for(int fold=0; fold<configuration.getNumFolds();fold++){
                
                final int numFold = fold;
                
                for(int rep=0;rep<configuration.getNumReps();rep++){
                    
                    final int numRep = rep;
                    
                    if(results.getComplete(rep, fold) == true){
                        continue;
                    }
                    
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Integer[][] confusionMatrix = new Integer[numClasses][numClasses]; //confusion matriz
                            for(int class1=0;class1<numClasses;class1++){
                                for(int class2=0;class2<numClasses;class2++){
                                    confusionMatrix[class1][class2] = 0;
                                }
                            }
                            
                            Instances dataTrain = data.trainCV(configuration.getNumFolds(), numFold);
                            Instances dataTest = data.testCV(configuration.getNumFolds(),numFold);

                            dataTrain.randomize(new Random(numRep));

                            Instances dataTrainLabeled = new Instances(dataTrain,0);
                            Instances dataTrainUnlabeled = new Instances(dataTrain,0);

                            SplitTrainTest(configuration, dataTrain, dataTrainLabeled, dataTrainUnlabeled, numInstPerClass);
                            
                            long begin = System.currentTimeMillis();
                            try{
                                classifiers[numRep][numFold].buildClassifier(dataTrainLabeled,dataTrainUnlabeled);
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
                            results.setBuildingTime(numRep, numFold, end - begin);

                            begin = System.currentTimeMillis();
                            Evaluations.SemiSupervisedInductiveEvaluation(classifiers[numRep][numFold], dataTest, confusionMatrix);
                            end = System.currentTimeMillis();
                            results.setClassificationTime(numRep, numFold, end - begin);
                            
                            results.computeEvaluationMeasures(confusionMatrix, numClasses, numRep, numFold);
                            
                            results.setComplete(numRep, numFold, true);
                            
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
    
    /*Function to split text collection into labeled and unlabled sets. numInstPerClass instances for each class are selected as labeled examples.*/
    private static void SplitTrainTest(SemiSupervisedInductiveConfiguration configuration, Instances data, Instances dataTrain, Instances dataTest, double numInstPerClass){
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
    
}
