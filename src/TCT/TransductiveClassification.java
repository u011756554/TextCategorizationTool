//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Transductive Learning and Evaluation. The
//              classification algorithms are based on networks with
//              document-document or document-term relations.
//*****************************************************************************

package TCT;

import TCTParameters.Parameters_GNetMine_DocTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_TagBased;
import TCTParameters.SemiSupervisedLearning.Parameters_LPHN;
import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTAlgorithms.Transductive.GNetMine_DocTerm_Transductive;
import TCTAlgorithms.Transductive.GFHF_DocDoc_Transductive;
import TCTAlgorithms.Transductive.LLGC_DocDoc_Transductive;
import TCTAlgorithms.Transductive.LPHN_DocTerm_Transductive;
import TCTAlgorithms.Transductive.NMFE_SSCC_Hybrid_Transductive;
import TCTAlgorithms.Transductive.TCHN_DocTerm_Transductive;
import TCTAlgorithms.Transductive.TagBased_DocTerm_Transductive;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration;
import TCTIO.ListFiles;
import TCTParameters.SemiSupervisedLearning.Parameters_NMFE_SSCC;
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

public class TransductiveClassification {
    
    /*Function to read a document-term matrix, build networks with Document-term or Term-Term relations, and set the parameters of transductive learning algorithms. */
    public static  void learning(TransductiveConfiguration configuration){

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
                StringBuilder outputFilePar = new StringBuilder();
                outputFile.append(configuration.getDirSaida());
                outputFile.append("/");
                outputFile.append(dataOriginal.relationName());
                outputFile.append("_Transductive_");

                for (int numEx = 0; numEx < configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size(); numEx++) {
                    double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                    if (configuration.isTagBased()) {
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
                        Parameters_TagBased parametersTagBased = configuration.getParametersTagBased();
                        for (int beta = 0; beta < parametersTagBased.getBetas().size(); beta++) {
                            for (int gamma = 0; gamma < parametersTagBased.getGammas().size(); gamma++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: TagBased");
                                //System.out.println("Beta: " + parametersTagBased.getBeta(beta));
                                //System.out.println("Gamma: " + parametersTagBased.getGamma(gamma));
                                //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersTagBased.getBeta(beta));
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersTagBased.getGamma(gamma));
                                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    TagBased_DocTerm_Transductive classifTagBased = new TagBased_DocTerm_Transductive();
                                    classifTagBased.setMaxNumIterations(parametersTagBased.getMaxNumberIterations());
                                    classifTagBased.setBeta(parametersTagBased.getBeta(beta));
                                    classifTagBased.setGamma(parametersTagBased.getGamma(gamma));
                                    classifiers[rep] = classifTagBased;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
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
                                for (int sigma = 0; sigma < configuration.getParametersExpNetwork().getSigmas().size(); sigma++) {
                                    //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                    //System.out.println("Classification Algorithm: LLGC");
                                    //System.out.println("Exp Network");
                                    //System.out.println("Alpha: " + parametersLLGC.getAlpha(alpha));
                                    //System.out.println("Sigma: " + configuration.getParametersExpNetwork().getSigma(sigma));
                                    //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                    StringBuilder outputFilePar2 = new StringBuilder();
                                    outputFilePar2.append(outputFilePar.toString());
                                    outputFilePar2.append("_");
                                    outputFilePar2.append("Exp");
                                    outputFilePar2.append("_");
                                    outputFilePar2.append(configuration.getParametersExpNetwork().getSigma(sigma));
                                    outputFilePar2.append("_");
                                    outputFilePar2.append(parametersLLGC.getAlpha(alpha));
                                    TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        LLGC_DocDoc_Transductive classifLLGC = new LLGC_DocDoc_Transductive();
                                        classifLLGC.setUse(0);
                                        if (configuration.getParametersExpNetwork().isCosseno()) {
                                            classifLLGC.setCosine(true);
                                        } else {
                                            classifLLGC.setCosine(false);
                                        }
                                        classifLLGC.setMaxNumIterations(parametersLLGC.getMaxNumberIterations());
                                        classifLLGC.setAlpha(parametersLLGC.getAlpha(alpha));
                                        classifLLGC.setSigma(configuration.getParametersExpNetwork().getSigma(sigma));
                                        classifLLGC.setGaussian(true);
                                        classifLLGC.setMutualKnn(false);
                                        classifiers[rep] = classifLLGC;
                                    }
                                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                                }
                            }
                        }
                        if (configuration.isNetworkKnn() == true) {

                            for (int alpha = 0; alpha < parametersLLGC.getAlphas().size(); alpha++) {
                                for (int k = 0; k < configuration.getParametersKnnNetwork().getKs().size(); k++) {
                                    //System.out.println("Classification Algorithm: LLGC");
                                    //System.out.println("Exp Network");
                                    //System.out.println("Alpha: " + parametersLLGC.getAlpha(alpha));
                                    //System.out.println("K: " + configuration.getParametersKnnNetwork().getK(k));
                                    //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                    StringBuilder outputFilePar2 = new StringBuilder();
                                    outputFilePar2.append(outputFilePar.toString());
                                    outputFilePar2.append("_");
                                    outputFilePar2.append("MutualKnn");
                                    outputFilePar2.append("_");
                                    outputFilePar2.append(configuration.getParametersKnnNetwork().getK(k));
                                    outputFilePar2.append("_");
                                    outputFilePar2.append(parametersLLGC.getAlpha(alpha));
                                    TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        LLGC_DocDoc_Transductive classifLLGC = new LLGC_DocDoc_Transductive();
                                        classifLLGC.setUse(0);
                                        if (configuration.getParametersKnnNetwork().isCosseno()) {
                                            classifLLGC.setCosine(true);
                                        } else {
                                            classifLLGC.setCosine(false);
                                        }
                                        classifLLGC.setMaxNumIterations(parametersLLGC.getMaxNumberIterations());
                                        classifLLGC.setAlpha(parametersLLGC.getAlpha(alpha));
                                        classifLLGC.setK(configuration.getParametersKnnNetwork().getK(k));
                                        classifLLGC.setGaussian(false);
                                        classifLLGC.setMutualKnn(true);
                                        classifiers[rep] = classifLLGC;
                                    }
                                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
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
                            for (int sigma = 0; sigma < configuration.getParametersExpNetwork().getSigmas().size(); sigma++) {
                                //System.out.println("Classification Algorithm: GFHF");
                                //System.out.println("Exp Network");
                                //System.out.println("Sigma: " + configuration.getParametersExpNetwork().getSigma(sigma));
                                //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");
                                outputFilePar2.append("Exp");
                                outputFilePar2.append("_");
                                outputFilePar2.append(configuration.getParametersExpNetwork().getSigma(sigma));
                                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    GFHF_DocDoc_Transductive classifGFHF = new GFHF_DocDoc_Transductive();
                                    classifGFHF.setUse(0);
                                    if (configuration.getParametersExpNetwork().isCosseno()) {
                                        classifGFHF.setCosine(true);
                                    } else {
                                        classifGFHF.setCosine(false);
                                    }
                                    classifGFHF.setMaxNumIterations(parametersGFHF.getMaxNumberIterations());
                                    classifGFHF.setSigma(configuration.getParametersExpNetwork().getSigma(sigma));
                                    classifGFHF.setGaussian(true);
                                    classifGFHF.setMutualKnn(false);
                                    classifiers[rep] = classifGFHF;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                            }
                        }
                        if (configuration.isNetworkKnn() == true) {
                            for (int k = 0; k < configuration.getParametersKnnNetwork().getKs().size(); k++) {
                                //System.out.println("Classification Algorithm: GFHF");
                                //System.out.println("Doc-Doc Relations: Mutual Knn");
                                //System.out.println("K: " + configuration.getParametersKnnNetwork().getK(k));
                                //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");
                                outputFilePar2.append("MutualKnn");
                                outputFilePar2.append("_");
                                outputFilePar2.append(configuration.getParametersKnnNetwork().getK(k));
                                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    GFHF_DocDoc_Transductive classifGFHF = new GFHF_DocDoc_Transductive();
                                    classifGFHF.setUse(0);
                                    if (configuration.getParametersKnnNetwork().isCosseno() == true) {
                                        classifGFHF.setCosine(true);
                                    } else {
                                        classifGFHF.setCosine(false);
                                    }
                                    classifGFHF.setMaxNumIterations(parametersGFHF.getMaxNumberIterations());
                                    classifGFHF.setK(configuration.getParametersKnnNetwork().getK(k));
                                    classifGFHF.setGaussian(false);
                                    classifGFHF.setMutualKnn(true);
                                    classifiers[rep] = classifGFHF;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                            }
                        }

                    }

                    if (configuration.isLPBHN()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("LPBHN_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        Parameters_LPHN parametersLPBHN = configuration.getParametersLPBHN();
                        //System.out.println("Classification Algorithm: LPBHN");
                        //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            LPHN_DocTerm_Transductive classifLPBHN = new LPHN_DocTerm_Transductive();
                            classifLPBHN.setMaxNumIterations(parametersLPBHN.getMaxNumberIterations());
                            classifiers[rep] = classifLPBHN;
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                    }

                    if (configuration.isGNetMine()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("GNetMine_");
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
                            //System.out.println("Classification Algorithm: GNetMine");
                            //System.out.println("Alpha Doc: " + parametersGNetMine.getAlphaDoc(alphaDoc));
                            //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append("_");
                            outputFilePar2.append(parametersGNetMine.getAlphaDoc(alphaDoc));
                            TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                GNetMine_DocTerm_Transductive classifGNetMine = new GNetMine_DocTerm_Transductive();
                                classifGNetMine.setMaxNumIterations(parametersGNetMine.getMaxNumberIterations());
                                classifGNetMine.setAlphaDoc(parametersGNetMine.getAlphaDoc(alphaDoc));
                                classifiers[rep] = classifGNetMine;
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);

                        }
                    }

                    if (configuration.isTCBHN()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("TCBHN_");
                        outputFilePar.append(numLabeledInstances);
                        outputFilePar.append("_");
                        if (configuration.isPorcentage() == true) {
                            outputFilePar.append("percentage");
                            outputFilePar.append("_");
                        } else {
                            outputFilePar.append("real");
                            outputFilePar.append("_");
                        }
                        Parameters_IMHN parametersTCBHN = configuration.getParametersTCBHN();
                        outputFilePar.append(parametersTCBHN.getMaxNumberIterationsGlobal() + "_");
                        outputFilePar.append(parametersTCBHN.getMaxNumberIterationsLocal() + "_");
                        for (int errorCorrectionRate = 0; errorCorrectionRate < parametersTCBHN.getErrorCorrectionRates().size(); errorCorrectionRate++) {
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append(parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate) + "_");
                            for (int error = 0; error < parametersTCBHN.getErrors().size(); error++) {
                                //System.out.println("Classification Algorithm: TCBHN");
                                //System.out.println("Error correction rate: " + parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate));
                                //System.out.println("Minimum mean squared error: " + parametersTCBHN.getError(error));
                                StringBuilder outputFilePar3 = new StringBuilder();
                                outputFilePar3.append(outputFilePar2.toString());
                                outputFilePar3.append(parametersTCBHN.getError(error));
                                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    TCHN_DocTerm_Transductive classifTCBHN = new TCHN_DocTerm_Transductive();
                                    classifTCBHN.setmaxNumberGlobalIterations(parametersTCBHN.getMaxNumberIterationsGlobal());
                                    classifTCBHN.setmaxNumberLocalIterations(parametersTCBHN.getMaxNumberIterationsLocal());
                                    classifTCBHN.setErrorCorrectionRate(parametersTCBHN.getErrorCorrectionRate(errorCorrectionRate));
                                    classifTCBHN.setMinError(parametersTCBHN.getError(error));
                                    classifiers[rep] = classifTCBHN;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numLabeledInstances, numClasses);
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
                                        for (int sigma = 0; sigma < configuration.getParametersExpNetwork().getSigmas().size(); sigma++) {
                                            //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                            //System.out.println("Classification Algorithm: NMFE");
                                            //System.out.println("Alpha: " + valueAlpha);
                                            //System.out.println("Beta: " + valueBeta);
                                            //System.out.println("Omega: " + valueOmega);
                                            //System.out.println("Exp Network");
                                            //System.out.println("Sigma: " + configuration.getParametersExpNetwork().getSigma(sigma));
                                            //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                            StringBuilder outputFilePar2 = new StringBuilder();
                                            outputFilePar2.append(outputFilePar.toString());
                                            outputFilePar2.append(valueAlpha + "_" + valueBeta + "_" + valueOmega + "_");
                                            outputFilePar2.append("Exp");
                                            outputFilePar2.append("_");
                                            outputFilePar2.append(configuration.getParametersExpNetwork().getSigma(sigma));
                                            TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                                NMFE_SSCC_Hybrid_Transductive classifNMFE = new NMFE_SSCC_Hybrid_Transductive();
                                                classifNMFE.setMinDifference(parameters_NMFE.getMinDifference());
                                                classifNMFE.setNumIterations(parameters_NMFE.getNumMaxIterations());
                                                classifNMFE.setAlpha(valueAlpha);
                                                classifNMFE.setBeta(valueBeta);
                                                classifNMFE.setOmega(valueOmega);
                                                if (configuration.getParametersExpNetwork().isCosseno()) {
                                                    classifNMFE.setCosine(true);
                                                } else {
                                                    classifNMFE.setCosine(false);
                                                }
                                                classifNMFE.setSigma(configuration.getParametersExpNetwork().getSigma(sigma));
                                                classifNMFE.setGaussian(true);
                                                classifiers[rep] = classifNMFE;
                                            }
                                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
                                        }

                                    }
                                    if (configuration.isNetworkKnn() == true) {
                                        for (int k = 0; k < configuration.getParametersKnnNetwork().getKs().size(); k++) {
                                            //System.out.println("Classification Algorithm: NMFE");
                                            //System.out.println("Alpha: " + valueAlpha);
                                            //System.out.println("Beta: " + valueBeta);
                                            //System.out.println("Omega: " + valueOmega);
                                            //System.out.println("KNN Network");
                                            //System.out.println("K: " + configuration.getParametersKnnNetwork().getK(k));
                                            //System.out.println("Number of Labeled Instances for Each Class:" + numLabeledInstances);
                                            StringBuilder outputFilePar2 = new StringBuilder();
                                            outputFilePar2.append(outputFilePar.toString());
                                            outputFilePar2.append(valueAlpha + "_" + valueBeta + "_" + valueOmega + "_");
                                            outputFilePar2.append("MutualKnn");
                                            outputFilePar2.append("_");
                                            outputFilePar2.append(configuration.getParametersKnnNetwork().getK(k));
                                            TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                                NMFE_SSCC_Hybrid_Transductive classifNMFE = new NMFE_SSCC_Hybrid_Transductive();
                                                classifNMFE.setMinDifference(parameters_NMFE.getMinDifference());
                                                classifNMFE.setNumIterations(parameters_NMFE.getNumMaxIterations());
                                                classifNMFE.setAlpha(valueAlpha);
                                                classifNMFE.setBeta(valueBeta);
                                                classifNMFE.setOmega(valueOmega);
                                                if (configuration.getParametersExpNetwork().isCosseno()) {
                                                    classifNMFE.setCosine(true);
                                                } else {
                                                    classifNMFE.setCosine(false);
                                                }
                                                classifNMFE.setK(configuration.getParametersKnnNetwork().getK(k));
                                                classifNMFE.setGaussian(false);
                                                classifiers[rep] = classifNMFE;
                                            }
                                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numLabeledInstances, numClasses);
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

    //Function to run and evaluate transductive learning learning
    public static void learning(final TransductiveConfiguration configuration, final ExecutorService threads, final TransductiveLearner[] classifiers, final Instances dataOriginal, String outputFile, final double numInstPerClass, final int numClasses){
        
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