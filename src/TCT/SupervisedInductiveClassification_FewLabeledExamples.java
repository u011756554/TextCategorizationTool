//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Inductive Supervised Learning and Evalutaion.
//              The supervised learning algorithms are based on vector-space
//              model or networks with document-term relations. The difference
//              between this class and TCT SupervisedInductiveClassification is
//              the evaluation. Here we selected randonly few labeled examples
//              and test classification model in the remaining examples. This
//              type of evaluation allow a comparison between inductive
//              supervised learning and transductive learning.
//*****************************************************************************

package TCT;

import TCTAlgorithms.InductiveSupervised.IMBHN_C_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.IMBHN_R_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.KNN_InductiveSupervised;
import TCTIO.ListFiles;
import TCTParameters.SemiSupervisedLearning.Parameters_KNN;
import TCTParameters.SupervisedLearning.Parameters_MLP;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SupervisedLearning.Parameters_SMO;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_FewLabeledExamples;
import TCTKernels.MinNKernelSVM;
import TCTParameters.SupervisedLearning.Parameters_J48;
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
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

public class SupervisedInductiveClassification_FewLabeledExamples {
    
    /*Function to read a and build networks (in case of network-based algorithms) and set the parameters of supervised inductive learning algorithms*/
    public static  void learning(SupervisedInductiveConfiguration_FewLabeledExamples configuration){
        
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
        
        ArrayList<String> filesIn = ListFiles.listFilesStr(new File(configuration.getDirEntrada()));
        
        try {

            for (int i = 0; i < filesIn.size(); i++) { // criando vetores contendo os atributos e suas frquências em cada documento da coleção

                if (!filesIn.get(i).endsWith(".arff")) {
                    continue;
                }
                System.out.println(filesIn.get(i));
                System.out.println("Loading ARFF file");

                DataSource trainSource = new DataSource(filesIn.get(i).toString()); //Carregando arquivo de Dados
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
                outputFile.append("_InductiveSupervised_");

                for (int numEx = 0; numEx < configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size(); numEx++) {
                    double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
                    if (configuration.isNB()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                        //System.out.println("Classification Algorithm: Naive Bayes");
                        outputFilePar.append("NB_");
                        outputFilePar.append(numLabeledInstances);
                        Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            NaiveBayes classifNB = new NaiveBayes();
                            classifiers[rep] = classifNB;
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numClasses, numLabeledInstances);
                    }
                    if (configuration.isMNB()) {

                        //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                        //System.out.println("Classification Algorithm: Multinomial Naive Bayes");
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("MNB_");
                        outputFilePar.append(numLabeledInstances);
                        Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                        for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                            NaiveBayesMultinomial classifMNB = new NaiveBayesMultinomial();
                            classifiers[rep] = classifMNB;
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numClasses, numLabeledInstances);

                    }
                    if (configuration.isJ48()) {
                        //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                        //System.out.println("Classification Algorithm: J48");
                        Parameters_J48 parametersJ48 = configuration.getParametersJ48();
                        for (int conf = 0; conf < parametersJ48.getConfidences().size(); conf++) {
                            double confidence = parametersJ48.getConfidence(conf);
                            //System.out.println("Classification Algorithm: J48");
                            //System.out.println("Confidence: " + confidence);
                            outputFilePar = new StringBuilder();
                            outputFilePar.append(outputFile.toString());
                            outputFilePar.append("J48_");
                            outputFilePar.append(confidence + "_");
                            outputFilePar.append(numLabeledInstances);
                            Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                            for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                J48 classifJ48 = new J48();
                                classifJ48.setConfidenceFactor((float) confidence);
                                classifiers[rep] = classifJ48;
                            }
                            learning(configuration, threads, classifiers, dataOriginal, outputFilePar.toString(), numClasses, numLabeledInstances);
                        }

                    }
                    if (configuration.isSMO()) {

                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("SMO_");
                        outputFilePar.append(numLabeledInstances + "_");
                        Parameters_SMO parametersSMO = configuration.getParametersSMO();
                        if (parametersSMO.isLinearKernel()) {
                            PolyKernel polyKernel = new PolyKernel();
                            polyKernel.setExponent(1);
                            for (int c = 0; c < parametersSMO.getvalueesC().size(); c++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: SMO");
                                //System.out.println("Kernel: " + classifSMO.getKernel().toString());
                                //System.out.println("C: " + classifSMO.getC());
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("Linear_");
                                outputFilePar2.append(parametersSMO.getvalueC(c));
                                Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    SMO classifSMO = new SMO();
                                    classifSMO.setKernel(polyKernel);
                                    classifSMO.setC(parametersSMO.getvalueC(c));
                                    classifiers[rep] = classifSMO;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses, numLabeledInstances);
                            }
                        }
                        if (parametersSMO.isPolyKernel()) {
                            PolyKernel polyKernel = new PolyKernel();
                            polyKernel.setExponent(2);
                            for (int c = 0; c < parametersSMO.getvalueesC().size(); c++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: SMO");
                                //System.out.println("Kernel: " + classifSMO.getKernel().toString());
                                //System.out.println("C: " + classifSMO.getC());
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("PolyKernel_");
                                outputFilePar2.append(parametersSMO.getvalueC(c));
                                Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    SMO classifSMO = new SMO();
                                    classifSMO.setKernel(polyKernel);
                                    classifSMO.setC(parametersSMO.getvalueC(c));
                                    classifiers[rep] = classifSMO;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses, numLabeledInstances);
                            }
                        }
                        if (parametersSMO.isRbfKernel()) {
                            for (int c = 0; c < parametersSMO.getvalueesC().size(); c++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: SMO");
                                //System.out.println("Kernel: " + classifSMO.getKernel().toString());
                                //System.out.println("C: " + classifSMO.getC());
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("RBFKernel_");
                                outputFilePar2.append(parametersSMO.getvalueC(c));
                                Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    SMO classifSMO = new SMO();
                                    classifSMO.setKernel(new RBFKernel());
                                    classifSMO.setC(parametersSMO.getvalueC(c));
                                    classifiers[rep] = classifSMO;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses, numLabeledInstances);
                            }
                        }
                        if (parametersSMO.isMinKernel()) {
                            for (int c = 0; c < parametersSMO.getvalueesC().size(); c++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);                            
                                //System.out.println("Classification Algorithm: SMO");
                                //System.out.println("Kernel: " + classifSMO.getKernel().toString());
                                //System.out.println("C: " + classifSMO.getC());
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("MinNKernel_");
                                outputFilePar2.append(parametersSMO.getvalueC(c));
                                Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    SMO classifSMO = new SMO();
                                    classifSMO.setKernel(new MinNKernelSVM());
                                    classifSMO.setC(parametersSMO.getvalueC(c));
                                    classifiers[rep] = classifSMO;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses, numLabeledInstances);
                            }
                        }
                    }
                    if (configuration.isKNN()) {
                        Parameters_KNN parametersKNN = configuration.getParametersKNN();
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("KNN_");
                        outputFilePar.append(numLabeledInstances + "_");
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
                                    Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        KNN_InductiveSupervised classifKnn = new KNN_InductiveSupervised();
                                        classifKnn.setK(parametersKNN.getNeighbor(viz));
                                        classifKnn.setWeightedVote(true);
                                        classifiers[rep] = classifKnn;
                                    }
                                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar4.toString(), numClasses, numLabeledInstances);
                                }
                                if (parametersKNN.isUnweighted()) {
                                    Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        KNN_InductiveSupervised classifKnn = new KNN_InductiveSupervised();
                                        classifKnn.setK(parametersKNN.getNeighbor(viz));
                                        classifKnn.setWeightedVote(false);
                                        classifiers[rep] = classifKnn;
                                    }
                                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numClasses, numLabeledInstances);
                                }
                            }
                            if (parametersKNN.getEuclidean()) {
                                StringBuilder outputFilePar3 = new StringBuilder();
                                outputFilePar3.append(outputFilePar2.toString());
                                outputFilePar3.append("Euclidean_");
                                EuclideanDistance df = new EuclideanDistance();
                                if (parametersKNN.isWeighted()) {
                                    SelectedTag dist = new SelectedTag(IBk.WEIGHT_INVERSE, IBk.TAGS_WEIGHTING);
                                    StringBuilder outputFilePar4 = new StringBuilder();
                                    outputFilePar4.append(outputFilePar3.toString());
                                    outputFilePar4.append("W_");
                                    Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        IBk classifKnn = new IBk();
                                        classifKnn.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df);
                                        classifKnn.setKNN(parametersKNN.getNeighbor(viz));
                                        classifKnn.setDistanceWeighting(dist);
                                        classifiers[rep] = classifKnn;
                                    }
                                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar4.toString(), numClasses, numLabeledInstances);
                                }
                                if (parametersKNN.isUnweighted()) {
                                    SelectedTag dist = new SelectedTag(IBk.WEIGHT_NONE, IBk.TAGS_WEIGHTING);
                                    Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        IBk classifKnn = new IBk();
                                        classifKnn.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df);
                                        classifKnn.setKNN(parametersKNN.getNeighbor(viz));
                                        classifKnn.setDistanceWeighting(dist);
                                        classifiers[rep] = classifKnn;
                                    }
                                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar3.toString(), numClasses, numLabeledInstances);
                                }
                            }
                        }
                    }
                    if (configuration.isMLP()) {
                        Parameters_MLP parametersMLP = configuration.getParametersMLP();
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("MLP_");
                        outputFilePar.append(numLabeledInstances + "_");
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
                                    //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                    //System.out.println("Classification Algorithm: MLP");
                                    //System.out.println("Number of Neurons" + classifMLP.getHiddenLayers());
                                    //System.out.println("Momentum: " + classifMLP.getMomentum());
                                    //System.out.println("Learning Rate: " + classifMLP.getLearningRate());
                                    StringBuilder outputFilePar4 = new StringBuilder();
                                    outputFilePar4.append(outputFilePar3.toString());
                                    outputFilePar4.append("_");
                                    outputFilePar4.append(parametersMLP.getErrorCorrectionRate(apr));
                                    Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                    for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                        MultilayerPerceptron classifMLP = new MultilayerPerceptron();
                                        classifMLP.setAutoBuild(false);
                                        classifMLP.setNormalizeAttributes(true);
                                        classifMLP.setTrainingTime(parametersMLP.getMaxNumberIterations());
                                        classifMLP.setHiddenLayers(new String(parametersMLP.getNumeroNeuronios(num).toString()));
                                        classifMLP.setMomentum(parametersMLP.getConstanteMomentum(mom));
                                        classifMLP.setLearningRate(parametersMLP.getErrorCorrectionRate(apr));
                                        classifiers[rep] = classifMLP;
                                    }
                                    learning(configuration, threads, classifiers, dataOriginal, outputFilePar4.toString(), numClasses, numLabeledInstances);
                                }
                            }
                        }
                    }
                    if (configuration.isIMBHN()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("IMBHN_");
                        outputFilePar.append(numLabeledInstances + "_");
                        Parameters_IMHN parametersIMBHN = configuration.getParametersIMBHN();
                        for (int error = 0; error < parametersIMBHN.getErrors().size(); error++) {
                            for (int apr = 0; apr < parametersIMBHN.getErrorCorrectionRates().size(); apr++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: IMBHN");
                                //System.err.println("Error Mínimo: " + parametersIMBHN.getError(error));
                                //System.out.println("Learning Rate: " + parametersIMBHN.getErrorCorrectionRate(apr));
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersIMBHN.getErrorCorrectionRate(apr));
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersIMBHN.getError(error));
                                Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    IMBHN_C_InductiveSupervised classifIMBHN = new IMBHN_C_InductiveSupervised();
                                    classifIMBHN.setErrorCorrectionRate(parametersIMBHN.getErrorCorrectionRate(apr));
                                    classifIMBHN.setMinError(parametersIMBHN.getError(error));
                                    classifIMBHN.setMaxNumIterations(parametersIMBHN.getMaxNumberIterations());
                                    classifiers[rep] = classifIMBHN;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses, numLabeledInstances);
                            }
                        }
                    }
                    if (configuration.isIMBHN2()) {
                        outputFilePar = new StringBuilder();
                        outputFilePar.append(outputFile.toString());
                        outputFilePar.append("IMBHN2_");
                        outputFilePar.append(numLabeledInstances);
                        Parameters_IMHN parametersIMBHN2 = configuration.getParametersIMBHN2();
                        for (int error = 0; error < parametersIMBHN2.getErrors().size(); error++) {
                            for (int apr = 0; apr < parametersIMBHN2.getErrorCorrectionRates().size(); apr++) {
                                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                                //System.out.println("Classification Algorithm: IMBHN2");
                                //System.err.println("Error Mínimo: " + parametersIMBHN2.getError(error));
                                //System.out.println("Learning Rate: " + parametersIMBHN2.getErrorCorrectionRate(apr));
                                StringBuilder outputFilePar2 = new StringBuilder();
                                outputFilePar2.append(outputFilePar.toString());
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersIMBHN2.getErrorCorrectionRate(apr));
                                outputFilePar2.append("_");
                                outputFilePar2.append(parametersIMBHN2.getError(error));
                                Classifier[] classifiers = new Classifier[configuration.getNumReps()];
                                for (int rep = 0; rep < configuration.getNumReps(); rep++) {
                                    IMBHN_R_InductiveSupervised classifIMBHN2 = new IMBHN_R_InductiveSupervised();
                                    classifIMBHN2.setErrorCorrectionRate(parametersIMBHN2.getErrorCorrectionRate(apr));
                                    classifIMBHN2.setMinError(parametersIMBHN2.getError(error));
                                    classifIMBHN2.setMaxNumIterations(parametersIMBHN2.getMaxNumberIterations());
                                    classifiers[rep] = classifIMBHN2;
                                }
                                learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses, numLabeledInstances);
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

    //Function to run and evaluate supervised learning
    public static void learning(final SupervisedInductiveConfiguration_FewLabeledExamples configuration, final ExecutorService threads, final Classifier[] classifiers, final Instances dataOriginal, String outputFile, final int numClasses, final double numInstPerClass){
        
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
            
            
            Integer[][] confusionMatrix = new Integer[numClasses][numClasses]; //matriz de confusão
            
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

                        SplitTrainTest(configuration, data, dataTrain, dataTest, numInstPerClass);
                        
                        long begin = System.currentTimeMillis();
                        try{
                            classifiers[numRep].buildClassifier(dataTrain);
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
                        begin = System.currentTimeMillis();
                        Evaluations.SupervisedInductiveEvaluation(classifiers[numRep], dataTest, confusionMatrix);
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
    
    
    
    /*Function to split text collection into labeled and unlabled sets. numInstPerClass instances for each class are selected as labeled examples.*/
    private static void SplitTrainTest(SupervisedInductiveConfiguration_FewLabeledExamples configuration, Instances data, Instances dataTrain, Instances dataTest, double numInstPerClass){
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
