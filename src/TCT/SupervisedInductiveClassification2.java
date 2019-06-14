//******************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: February 27, 2015
// Description: Class to perform Inductive Supervised Learning and Evalutaion.
//              The supervised learning algorithms are based on vector-space
//              model or networks with single type relations (document-document,
//              document-term, or term-term relations.
//******************************************************************************

package TCT;

import TCTAlgorithms.InductiveSupervised.BayesianLogisticRegressionTCT_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.IMBHN_C_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.IMBHN_R_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.IterativeRidgeRegression;
import TCTAlgorithms.InductiveSupervised.KAC_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.KACOG_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.KNN_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.LinearRegressionTCT_InductiveSupervised;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration;
import TCTIO.ListFiles;
import TCTParameters.SemiSupervisedLearning.Parameters_KNN;
import TCTParameters.SupervisedLearning.Parameters_MLP;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SupervisedLearning.Parameters_SMO;
import TCTAlgorithms.InductiveSupervised.Rocchio_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.TGM_InductiveSupervised;
import TCTKernels.MinNKernelSVM;
import TCTParameters.SupervisedLearning.Parameters_RidgeRegression;
import TCTParameters.SupervisedLearning.Parameters_J48;
import TCTParameters.SemiSupervisedLearning.Parameters_KAG;
import TCTParameters.SupervisedLearning.Parameters_LogisticRegression;
import TCTParameters.SupervisedLearning.Parameters_RIPPER;
import TCTParameters.SemiSupervisedLearning.Parameters_Rocchio;
import TCTParameters.SemiSupervisedLearning.Parameters_TGM;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;
import wlsvm.WLSVM;



public class SupervisedInductiveClassification2 {
    
    /*Function to read a and build networks (in case of network-based algorithms) and set the parameters of supervised inductive learning algorithms*/
    public static void learning(SupervisedInductiveConfiguration configuration){
        
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
        
        ArrayList<String> filesIn = ListFiles.listFilesStr(new File(configuration.getDirEntrada()));
        
        try {
            for (int i = 0; i < filesIn.size(); i++) {

                if (!filesIn.get(i).endsWith(".arff")) {
                    continue;
                }
                System.out.println(filesIn.get(i));
                System.out.println("Loading ARFF file");

                DataSource trainSource = new DataSource(filesIn.get(i).toString()); //Carregando arquivo de Dados
                Instances dataOriginal = trainSource.getDataSet();

                Attribute classAtt = null;

                classAtt = dataOriginal.attribute(dataOriginal.numAttributes() - 1); //Setting the last feature as class
                dataOriginal.setClass(classAtt);
                final int numClasses = classAtt.numValues();

                for (int j = 0; j < numClasses; j++) {
                    System.out.println(j + ": " + classAtt.value(j));
                }

                StringBuilder outputFile = new StringBuilder();
                StringBuilder outputFilePar = new StringBuilder();
                outputFile.append(configuration.getDirSaida());
                outputFile.append("/");
                outputFile.append(dataOriginal.relationName());
                outputFile.append("_InductiveSupervised_");

                

                if (configuration.isNB()) {
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("NB_");
                    Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                        for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                            classifiers[rep][fold] = new NaiveBayes();
                        }
                    }
                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numClasses);
                }
                if (configuration.isMNB()) {
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("MNB_");
                    Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                        for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                            classifiers[rep][fold] = new NaiveBayesMultinomial();
                        }
                    }
                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numClasses);

                }
                if (configuration.isJ48()) {
                    Parameters_J48 parametersJ48 = configuration.getParametersJ48();
                    for (int conf = 0; conf < parametersJ48.getConfidences().size(); conf++) {
                        double confidence = parametersJ48.getConfidence(conf);
                        //System.out.println("Classification Algorithm: J48");
                        //System.out.println("Confidence: " + confidence);
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("J48_");
                        outputFilePar.append(confidence + "_");
                        Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                J48 classifJ48 = new J48();
                                classifJ48.setConfidenceFactor((float) confidence);
                                classifiers[rep][fold] = classifJ48;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numClasses);
                    }
                }

                if (configuration.isSMO()) {
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("SMO_");
                    Parameters_SMO parametersSMO = configuration.getParametersSMO();
                    if (parametersSMO.isLinearKernel()) {
                        PolyKernel polyKernel = new PolyKernel();
                        polyKernel.setExponent(1);
                        for (int c = 0; c < parametersSMO.getvalueesC().size(); c++) {
                            //System.out.println("Classification Algorithm: SMO");
                            //System.out.println("Kernel: LinearKernel");
                            //System.out.println("C: " + parametersSMO.getvalueC(c));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append("Linear_");
                            outputFilePar2.append(parametersSMO.getvalueC(c) + "_");
                            Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    SMO classifSMO = new SMO();
                                    classifSMO.setKernel(polyKernel);
                                    classifSMO.setC(parametersSMO.getvalueC(c));
                                    classifiers[rep][fold] = classifSMO;
                                }
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                        }
                    }
                    if (parametersSMO.isPolyKernel()) {
                        PolyKernel polyKernel = new PolyKernel();
                        polyKernel.setExponent(2);
                        for (int c = 0; c < parametersSMO.getvalueesC().size(); c++) {
                            //System.out.println("Classification Algorithm: SMO");
                            //System.out.println("Kernel: PolyKernel");
                            //System.out.println("C: " + parametersSMO.getvalueC(c));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append("PolyKernel_");
                            outputFilePar2.append(parametersSMO.getvalueC(c) + "_");
                            Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    SMO classifSMO = new SMO();
                                    classifSMO.setKernel(polyKernel);
                                    classifSMO.setC(parametersSMO.getvalueC(c));
                                    classifiers[rep][fold] = classifSMO;
                                }
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                        }
                    }
                    if (parametersSMO.isRbfKernel()) {
                        for (int c = 0; c < parametersSMO.getvalueesC().size(); c++) {
                            //System.out.println("Classification Algorithm: SMO");
                            //System.out.println("Kernel: RBFKernel");
                            //System.out.println("C: " + parametersSMO.getvalueC(c));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append("RBFKernel_");
                            outputFilePar2.append(parametersSMO.getvalueC(c) + "_");
                            Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    SMO classifSMO = new SMO();
                                    classifSMO.setKernel(new RBFKernel());
                                    classifSMO.setC(parametersSMO.getvalueC(c));
                                    classifiers[rep][fold] = classifSMO;
                                }
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                        }
                    }
                    if (parametersSMO.isMinKernel()) {
                        for (int c = 0; c < parametersSMO.getvalueesC().size(); c++) {
                            //System.out.println("Classification Algorithm: SMO");
                            //System.out.println("Kernel: MinKernel");
                            //System.out.println("C: " + parametersSMO.getvalueC(c));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append("MinNKernel_");
                            outputFilePar2.append(parametersSMO.getvalueC(c) + "_");
                            Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    SMO classifSMO = new SMO();
                                    classifSMO.setKernel(new MinNKernelSVM());
                                    classifSMO.setC(parametersSMO.getvalueC(c));
                                    classifiers[rep][fold] = classifSMO;
                                }
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                        }
                    }
                }
                if (configuration.isKNN()) {
                    Parameters_KNN parametersKNN = configuration.getParametersKNN();
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("KNN_");
                    for (int viz = 0; viz < parametersKNN.getNeighbors().size(); viz++) {
                        //System.out.println("Classification Algorithm: KNN");
                        //System.out.println("Number of Neighbors: " + parametersKNN.getNeighbor(viz));
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append("_");
                        outputFilePar2.append(parametersKNN.getNeighbor(viz));
                        outputFilePar2.append("_");
                        if (parametersKNN.getCosine()) {
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            outputFilePar3.append("Cosine_");
                            if (parametersKNN.isWeighted()) {
                                StringBuilder outputFilePar4 = new StringBuilder();
                                outputFilePar4.append(outputFilePar3.toString());
                                outputFilePar4.append("W_");
                                Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                        KNN_InductiveSupervised classifKnn = new KNN_InductiveSupervised();
                                        classifKnn.setK(parametersKNN.getNeighbor(viz));
                                        classifKnn.setWeightedVote(true);
                                        classifiers[rep][fold] = classifKnn;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar4.toString(), numClasses);
                            }
                            if (parametersKNN.isUnweighted()) {
                                Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                        KNN_InductiveSupervised classifKnn = new KNN_InductiveSupervised();
                                        classifKnn.setK(parametersKNN.getNeighbor(viz));
                                        classifKnn.setWeightedVote(false);
                                        classifiers[rep][fold] = classifKnn;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numClasses);
                            }
                        }
                        if (parametersKNN.getEuclidean()) {
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            outputFilePar3.append("Euclidean_");
                            if (parametersKNN.isWeighted()) {
                                Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                        IBk classifKnn = new IBk();
                                        EuclideanDistance df = new EuclideanDistance();
                                        classifKnn.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df);
                                        classifKnn.setKNN(parametersKNN.getNeighbor(viz));
                                        SelectedTag dist = new SelectedTag(IBk.WEIGHT_INVERSE, IBk.TAGS_WEIGHTING);
                                        classifKnn.setDistanceWeighting(dist);
                                        classifiers[rep][fold] = classifKnn;
                                    }
                                }
                                StringBuilder outputFilePar4 = new StringBuilder();
                                outputFilePar4.append(outputFilePar3.toString());
                                outputFilePar4.append("W_");
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar4.toString(), numClasses);
                            }

                            if (parametersKNN.isUnweighted()) {
                                Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                        IBk classifKnn = new IBk();
                                        EuclideanDistance df = new EuclideanDistance();
                                        classifKnn.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df);
                                        classifKnn.setKNN(parametersKNN.getNeighbor(viz));
                                        SelectedTag dist = new SelectedTag(IBk.WEIGHT_NONE, IBk.TAGS_WEIGHTING);
                                        classifKnn.setDistanceWeighting(dist);
                                        classifiers[rep][fold] = classifKnn;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numClasses);
                            }
                        }
                    }
                }
                if (configuration.isMLP()) {

                    Parameters_MLP parametersMLP = configuration.getParametersMLP();
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("MLP_");
                    for (int num = 0; num < parametersMLP.getNumerosNeuronios().size(); num++) {
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append(parametersMLP.getNumeroNeuronios(num));
                        outputFilePar2.append("_");
                        for (int mom = 0; mom < parametersMLP.getConstantesMomentum().size(); mom++) {
                            StringBuilder outputFilePar3 = new StringBuilder();
                            outputFilePar3.append(outputFilePar2.toString());
                            outputFilePar3.append(parametersMLP.getConstanteMomentum(mom));
                            outputFilePar3.append("_");
                            for (int apr = 0; apr < parametersMLP.getErrorCorrectionRates().size(); apr++) {
                                //System.out.println("Classification Algorithm: MLP");
                                //System.out.println("Number of Neurons: " + parametersMLP.getNumeroNeuronios(num));
                                //System.out.println("Momentum: " + parametersMLP.getConstanteMomentum(mom));
                                //System.out.println("Learning Rate: " + parametersMLP.getErrorCorrectionRate(apr));
                                StringBuilder outputFilePar4 = new StringBuilder();
                                outputFilePar4.append(outputFilePar3.toString() + "_");
                                outputFilePar4.append(parametersMLP.getErrorCorrectionRate(apr) + "_");
                                Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                        MultilayerPerceptron classifMLP = new MultilayerPerceptron();
                                        classifMLP.setAutoBuild(false);
                                        classifMLP.setNormalizeAttributes(true);
                                        classifMLP.setTrainingTime(parametersMLP.getMaxNumberIterations());
                                        classifMLP.setHiddenLayers(new String(parametersMLP.getNumeroNeuronios(num).toString()));
                                        classifMLP.setMomentum(parametersMLP.getConstanteMomentum(mom));
                                        classifMLP.setLearningRate(parametersMLP.getErrorCorrectionRate(apr));
                                        classifiers[rep][fold] = classifMLP;
                                    }
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar4.toString(), numClasses);
                            }
                        }
                    }
                }
                if (configuration.isIMBHN()) {
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("IMBHN_");
                    Parameters_IMHN parametersIMBHN = configuration.getParametersIMBHN();
                    for (int error = 0; error < parametersIMBHN.getErrors().size(); error++) {
                        for (int apr = 0; apr < parametersIMBHN.getErrorCorrectionRates().size(); apr++) {
                            //System.out.println("Classification Algorithm: IMBHN");
                            //System.out.println("Minimum Mean Squared Error: " + parametersIMBHN.getError(error));
                            //System.out.println("Error Correction Rate: " + parametersIMBHN.getErrorCorrectionRate(apr));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString() + "_");
                            outputFilePar2.append(parametersIMBHN.getErrorCorrectionRate(apr) + "_");
                            outputFilePar2.append(parametersIMBHN.getError(error) + "_");
                            Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    IMBHN_C_InductiveSupervised classifIMBHN = new IMBHN_C_InductiveSupervised();
                                    classifIMBHN.setErrorCorrectionRate(parametersIMBHN.getErrorCorrectionRate(apr));
                                    classifIMBHN.setMinError(parametersIMBHN.getError(error));
                                    classifIMBHN.setMaxNumIterations(parametersIMBHN.getMaxNumberIterations());
                                    classifiers[rep][fold] = classifIMBHN;
                                }
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                        }
                    }
                }
                if (configuration.isIMBHN2()) {
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("IMBHN2_");
                    Parameters_IMHN parametersIMBHN2 = configuration.getParametersIMBHN2();
                    for (int error = 0; error < parametersIMBHN2.getErrors().size(); error++) {
                        for (int apr = 0; apr < parametersIMBHN2.getErrorCorrectionRates().size(); apr++) {
                            //System.out.println("Classification Algorithm: IMBHN2");
                            //System.out.println("Minimum Mean Squared Error: " + parametersIMBHN2.getError(error));
                            //System.out.println("Error Correction Rate: " + parametersIMBHN2.getErrorCorrectionRate(apr));
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString() + "_");
                            outputFilePar2.append(parametersIMBHN2.getErrorCorrectionRate(apr) + "_");
                            outputFilePar2.append(parametersIMBHN2.getError(error) + "_");
                            Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    IMBHN_R_InductiveSupervised classifIMBHN2 = new IMBHN_R_InductiveSupervised();
                                    classifIMBHN2.setErrorCorrectionRate(parametersIMBHN2.getErrorCorrectionRate(apr));
                                    classifIMBHN2.setMinError(parametersIMBHN2.getError(error));
                                    classifIMBHN2.setMaxNumIterations(parametersIMBHN2.getMaxNumberIterations());
                                    classifiers[rep][fold] = classifIMBHN2;
                                }
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                        }
                    }
                }
                if (configuration.isTGM()) {
                    //System.out.println("Classification Algorithm: TGM");
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("TGM_");
                    Parameters_TGM parametersTGM = configuration.getParametersTGM();
                    outputFilePar.append(parametersTGM.getDampingFactor() + "_");
                    outputFilePar.append(parametersTGM.getMaxNumberIterations() + "_");
                    outputFilePar.append(parametersTGM.getAvgMinDifference() + "_");
                    outputFilePar.append("Support");
                    for (int threshold = 0; threshold < parametersTGM.getMinSups().size(); threshold++) {
                        double minSup = parametersTGM.getMinSup(threshold);
                        //System.out.println("MinSup: " + minSup);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append("_");
                        outputFilePar2.append(minSup);
                        outputFilePar2.append("_");
                        StringBuilder outputFilePar3 = new StringBuilder();
                        outputFilePar3.append(outputFilePar2.toString());
                        Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                TGM_InductiveSupervised classifTGM = new TGM_InductiveSupervised();
                                classifTGM.setDampingFactor(parametersTGM.getDampingFactor());
                                classifTGM.setMaxNumIterations(parametersTGM.getMaxNumberIterations());
                                classifTGM.setDifMediaMinima(parametersTGM.getAvgMinDifference());
                                classifTGM.setMinSup(minSup);
                                classifiers[rep][fold] = classifTGM;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numClasses);
                    }
                }
                if (configuration.isRocchio()) {
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("Rocchio_");
                    Parameters_Rocchio parametersRocchio = configuration.getParametersRocchio();
                    for (int beta = 0; beta < parametersRocchio.getBetas().size(); beta++) {
                        //System.out.println("Classification Algorithm: Rocchio");
                        //System.out.println("Beta: " + parametersRocchio.getBeta(beta));
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append("_" + parametersRocchio.getBeta(beta) + "_");
                        Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                Rocchio_InductiveSupervised classifRocchio = new Rocchio_InductiveSupervised();
                                classifRocchio.setBeta(parametersRocchio.getBeta(beta));
                                classifiers[rep][fold] = classifRocchio;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                    }
                }
                if (configuration.isRIPPER()) {
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("RIPPER_");
                    Parameters_RIPPER parametersRIPPER = configuration.getParametersRIPPER();
                    for (int opt = 0; opt < parametersRIPPER.getOpts().size(); opt++) {
                        int numOptimizations = parametersRIPPER.getOpt(opt);
                        for (int foldRIPPER = 0; foldRIPPER < parametersRIPPER.getFolds().size(); foldRIPPER++) {
                            //System.out.println("Classification Algorithm: RIPPER");
                            int numFolds = parametersRIPPER.getFold(foldRIPPER);
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append("_");
                            //System.out.println("Number of Optimizations" + numOptimizations);
                            outputFilePar2.append(numOptimizations);
                            outputFilePar2.append("_");
                            //System.out.println("Number of Folds: " + numFolds);
                            outputFilePar2.append(numFolds + "_");
                            Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    JRip classifRIPPER = new JRip();
                                    classifRIPPER.setOptimizations(numOptimizations);
                                    classifRIPPER.setFolds(numFolds);
                                    classifiers[rep][fold] = classifRIPPER;
                                }
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                        }
                    }
                }
                if (configuration.isBLR()) {
                    Parameters_LogisticRegression parametersBLR = configuration.getParametersBLR();
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    //System.out.println("Classification Algorithm: Bayesian Logistic Regression");
                    outputFilePar.append("BLR_");
                    //System.out.println("Number of Folds: " + parametersBLR.getNumFolds());
                    outputFilePar.append(parametersBLR.getNumFolds());
                    outputFilePar.append("_");
                    //System.out.println("Maximum Number of Iterations" + parametersBLR.getMaxIterations());
                    outputFilePar.append(parametersBLR.getMaxIterations());
                    outputFilePar.append("_");
                    //System.out.println("Tolerance" + parametersBLR.getTolerance());
                    outputFilePar.append(parametersBLR.getTolerance());
                    outputFilePar.append("_");
                    if (parametersBLR.getPriors().contains(1)) {
                        //System.out.println("Priors: Gaussian");
                        Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                BayesianLogisticRegressionTCT_InductiveSupervised classifBLR = new BayesianLogisticRegressionTCT_InductiveSupervised();
                                classifBLR.setPrior(1);
                                classifBLR.setNumFolds(parametersBLR.getNumFolds());
                                classifBLR.setMaxNumIterations(parametersBLR.getMaxIterations());
                                classifBLR.setTolerance(parametersBLR.getTolerance());
                                classifiers[rep][fold] = classifBLR;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString() + 1 + "_Gaussian_", numClasses);
                    }
                    if (parametersBLR.getPriors().contains(2)) {
                        //System.out.println("Priors: Laplacian");
                        Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                BayesianLogisticRegressionTCT_InductiveSupervised classifBLR = new BayesianLogisticRegressionTCT_InductiveSupervised();
                                classifBLR.setPrior(2);
                                classifBLR.setNumFolds(parametersBLR.getNumFolds());
                                classifBLR.setMaxNumIterations(parametersBLR.getMaxIterations());
                                classifBLR.setTolerance(parametersBLR.getTolerance());
                                classifiers[rep][fold] = classifBLR;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString() + 2 + "_Laplacian_", numClasses);
                    }
                }
                if (configuration.isLLSF()) {
                    Parameters_RidgeRegression parameters = configuration.getParametersRidgeRegression();
                    outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("LLSF_");
                    for (int r = 0; r < parameters.getRidges().size(); r++) {
                        double ridge = parameters.getRidge(r);
                        //System.out.println("Classification Algorithm: LLSF");
                        //System.out.println("Ridge value: " + ridge);
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append(ridge + "_");
                        Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                LinearRegressionTCT_InductiveSupervised classifLLSF = new LinearRegressionTCT_InductiveSupervised();
                                classifLLSF.setRidge(ridge);
                                classifiers[rep][fold] = classifLLSF;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                    }
                }
                if (configuration.isKAG()) {
                    Parameters_KAG parameters = configuration.getParametersKAG();
                    if (parameters.getKAG()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("KAG_");
                        for (int k = 0; k < parameters.getNeighbors().size(); k++) {
                            int valueK = parameters.getNeighbor(k);
                            StringBuilder outputFilePar2 = new StringBuilder();
                            outputFilePar2.append(outputFilePar.toString());
                            outputFilePar2.append(valueK + "_");
                            //System.out.println("Classification Algorithm: KAG");
                            //System.out.println("Number of Neighbors: " + valueK);
                            Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                    KAC_InductiveSupervised classifKAG = new KAC_InductiveSupervised();
                                    classifKAG.setK(valueK);
                                    classifiers[rep][fold] = classifKAG;
                                }
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);
                        }
                    }
                    if (parameters.getKAOG()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("KAOG_");
                        //System.out.println("Classification Algorithm: KAOG");
                        Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                                KACOG_InductiveSupervised classifKAOG = new KACOG_InductiveSupervised();
                                classifiers[rep][fold] = classifKAOG;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numClasses);
                    }

                }

                if (configuration.isIRR()) {
                    Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                        for (int fold = 0; fold < configuration.getNumFolds(); fold++) {
                            IterativeRidgeRegression classifIRR = new IterativeRidgeRegression();
                            classifIRR.setLambda(100);
                            classifIRR.setMinDifference(0.000001);
                            classifIRR.setMaxNumberIterations(100);
                            classifiers[rep][fold] = classifIRR;
                        }
                    }
                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString() + "IRR", numClasses);
                }

            }
            
            threads.shutdown();

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

    
    
    //Function to run and evaluate supervised learning
    public static void learning(final SupervisedInductiveConfiguration configuration, final ExecutorService threads, final Classifier[][] classifiers, Instances dataOriginal, String outputFile, final int numClasses){
        
        try{
            
            final Results results;
            
            final File output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds());
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
                results = new Results(output, configuration.getNumReps(), configuration.getNumFolds(), "InductiveSupervised");
            }
            
            //System.out.println("Output: " + output.getAbsolutePath());
            
            for(int rep=0;rep<configuration.getNumReps();rep++){
                
                final int numRep = rep;
                
                final Instances data = new Instances(dataOriginal);
                data.setClass(dataOriginal.attribute(dataOriginal.numAttributes()-1));
                data.randomize(new Random(rep));
                
                for(int fold=0; fold<configuration.getNumFolds();fold++){
                    
                    if(results.getComplete(rep, fold) == true){
                        continue;
                    }
                    
                    final int numFold = fold;
                    
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Integer[][] confusionMatrix = new Integer[numClasses][numClasses]; //matriz de confusão
                            for(int class1=0;class1<numClasses;class1++){//inicializando a matriz
                                for(int class2=0;class2<numClasses;class2++){
                                    confusionMatrix[class1][class2] = 0;
                                }
                            }
                            
                            Instances dataTrain = data.trainCV(configuration.getNumFolds(), numFold);
                            Instances dataTest = data.testCV(configuration.getNumFolds(),numFold);

                            long begin = System.currentTimeMillis();
                            try{
                                classifiers[numRep][numFold].buildClassifier(dataTrain);
                            }catch(OutOfMemoryError e){
                                configuration.getEmail().getContent().append("OutOfMemory!!!!");
                                configuration.getEmail().getContent().append(configuration.toString());
                                configuration.getEmail().send();
                                e.printStackTrace();
                                System.exit(0);
                            }catch(Exception e){
                                configuration.getEmail().getContent().append(e.getMessage());
                                configuration.getEmail().getContent().append(configuration.toString());
                                configuration.getEmail().send();
                                e.printStackTrace();
                                System.exit(0);
                            }
                            
                            long end  = System.currentTimeMillis();
                            results.setBuildingTime(numRep, numFold, end - begin);

                            begin = System.currentTimeMillis();
                            Evaluations.SupervisedInductiveEvaluation(classifiers[numRep][numFold], dataTest, confusionMatrix);
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
    
}
