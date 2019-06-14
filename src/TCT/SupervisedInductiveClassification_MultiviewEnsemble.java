//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Inductive Supervised Learning considering
//              ensemble of classifiers. The evaluation considers three ways
//              to combine the classifications: (i) sum of classification 
//              confidences, (ii) vote of each classifier weighted by its 
//              classification accuracy, and (iii) just highest classification 
//              confidence of a classifier.
//*****************************************************************************

package TCT;

import TCTAlgorithms.InductiveSupervised.IMBHN_C_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.IMBHN_R_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.KNN_InductiveSupervised;
import TCTParameters.SemiSupervisedLearning.Parameters_KNN;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SupervisedLearning.Parameters_SMO;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_EnsembleMultiview;
import TCTKernels.MinNKernelSVM;
import TCTParameters.SupervisedLearning.Parameters_J48;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;


public class SupervisedInductiveClassification_MultiviewEnsemble {
    
    
    /*Function to read a document-term matrix and set the parameters of supervised inductive learning algorithms*/
    public static  void learning(final SupervisedInductiveConfiguration_EnsembleMultiview configuration){
        
        System.out.println("Carregando arquivos Arquivos...");
        
        File arqArffView1 = new File(configuration.getArffVisao1());
        if(!arqArffView1.getAbsolutePath().endsWith(".arff")){
            System.out.println("Arff de Entrada1 não é válido");
            System.exit(0);
        }
        File arqArffView2 = new File(configuration.getArffVisao2());
        if(!arqArffView2.getAbsolutePath().endsWith(".arff")){
            System.out.println("Arff de Entrada2 não é válido");
            System.exit(0);
        }
        
        System.out.println("Loading ARFF file");
        
        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
        
        
        try{
            DataSource trainSourceView1 = new DataSource(configuration.getArffVisao1()); //Carregando arquivo de Dados
            final Instances dataOriginalView1 = trainSourceView1.getDataSet();

            DataSource trainSourceView2 = new DataSource(configuration.getArffVisao2()); //Carregando arquivo de Dados
            final Instances dataOriginalView2 = trainSourceView2.getDataSet();
            
            Attribute classAttView1 = dataOriginalView1.attribute(dataOriginalView1.numAttributes()-1); //Setting the last feature as class
            dataOriginalView1.setClass(classAttView1);
            
            Attribute classAttView2 = dataOriginalView2.attribute(dataOriginalView2.numAttributes()-1); //Setting the last feature as class
            dataOriginalView2.setClass(classAttView2);
            
            final int numClasses = classAttView1.numValues();

            for(int j=0;j<numClasses;j++){
                System.out.println(j + ": " + classAttView1.value(j));
            }

            StringBuilder outputFile = new StringBuilder();
            outputFile.append(configuration.getDirSaida());
            outputFile.append("/");
            outputFile.append(dataOriginalView1.relationName());
            outputFile.append("_InductiveSupervised_Multiview_");
            
            
            
            if(configuration.isNB()){
                //System.out.println("Classification Algorithm: Naive Bayes");
                final StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(outputFile.toString());
                outputFilePar.append("NB_");
                final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            for(int fold=0;fold<configuration.getNumFolds();fold++){
                                NaiveBayes classifNBView1 = new NaiveBayes();
                                NaiveBayes classifNBView2 = new NaiveBayes();
                                classifiers1[rep][fold] = classifNBView1;
                                classifiers2[rep][fold] = classifNBView2;
                            }
                        }
                        learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar.toString(), numClasses);
                    }
                });
                threads.execute(thread);    
            }
            if(configuration.isMNB()){
                //System.out.println("Classification Algorithm: Multinomial Naive Bayes");
                final StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(outputFile.toString());
                outputFilePar.append("MNB_");
                final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            for(int fold=0;fold<configuration.getNumFolds();fold++){
                                NaiveBayesMultinomial classifMNBView1 = new NaiveBayesMultinomial();
                                NaiveBayesMultinomial classifMNBView2 = new NaiveBayesMultinomial();
                                classifiers1[rep][fold] = classifMNBView1;
                                classifiers2[rep][fold] = classifMNBView2;
                            }
                        }
                        learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar.toString(), numClasses);
                    }
                });
                threads.execute(thread);    
            }
            if(configuration.isJ48()){
                Parameters_J48 parametersJ48 = configuration.getParametersJ48();
                for(int conf=0;conf<parametersJ48.getConfidences().size();conf++){
                    //System.out.println("Classification Algorithm: J48");
                    final double confidence = parametersJ48.getConfidence(conf);
                    //System.out.println("Confidence: " + confidence);
                    final StringBuilder outputFilePar = new StringBuilder();
                    outputFilePar.append(outputFile.toString());
                    outputFilePar.append("J48_");
                    outputFilePar.append(confidence + "_");
                    final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                    final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(int rep=0;rep<configuration.getNumReps();rep++){
                                for(int fold=0;fold<configuration.getNumFolds();fold++){
                                    J48 classifJ48View1 = new J48();
                                    J48 classifJ48View2 = new J48();
                                    classifJ48View1.setConfidenceFactor((float)confidence);
                                    classifJ48View2.setConfidenceFactor((float)confidence); 
                                    classifiers1[rep][fold] = classifJ48View1;
                                    classifiers2[rep][fold] = classifJ48View2;
                                }
                            }
                            learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar.toString(), numClasses);
                        }
                    });
                    threads.execute(thread);
                }
            }
            if(configuration.isSMO()){
                final StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(outputFile.toString());
                outputFilePar.append("SMO_");
                final Parameters_SMO parametersSMO = configuration.getParametersSMO();
                if(parametersSMO.isLinearKernel()){
                    final PolyKernel polyKernel = new PolyKernel();
                    polyKernel.setExponent(1);
                    for(int c=0;c<parametersSMO.getvalueesC().size();c++){
                        //System.out.println("Classification Algorithm: SMO");
                        //System.out.println("Kernel: " + classifSMOView1.getKernel().toString());
                        //System.out.println("C: " + classifSMOView1.getC());
                        final int idC = c;
                        final StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append("Linear_");
                        outputFilePar2.append(parametersSMO.getvalueC(c));
                        outputFilePar2.append("_");
                        final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    for(int fold=0;fold<configuration.getNumFolds();fold++){
                                        SMO classifSMOView1 = new SMO();
                                        SMO classifSMOView2 = new SMO();
                                        classifSMOView1.setKernel(polyKernel);
                                        classifSMOView2.setKernel(polyKernel);
                                        classifSMOView1.setC(parametersSMO.getvalueC(idC));
                                        classifSMOView2.setC(parametersSMO.getvalueC(idC));
                                        classifiers1[rep][fold] = classifSMOView1;
                                        classifiers2[rep][fold] = classifSMOView2;
                                    }
                                }
                            learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar2.toString(), numClasses);
                            }
                        });
                        threads.execute(thread);
                    }    
                }
                if(parametersSMO.isPolyKernel()){
                    for(int c=0;c<parametersSMO.getvalueesC().size();c++){
                        final double cValue = parametersSMO.getvalueC(c);                        
                        //System.out.println("Classification Algorithm: SMO");
                        //System.out.println("Kernel: " + classifSMOView1.getKernel().toString());
                        //System.out.println("C: " + classifSMOView1.getC());
                        final StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append("PolyKernel_");
                        outputFilePar2.append(parametersSMO.getvalueC(c));
                        outputFilePar2.append("_");
                        final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    for(int fold=0;fold<configuration.getNumFolds();fold++){
                                        SMO classifSMOView1 = new SMO();
                                        SMO classifSMOView2 = new SMO();
                                        PolyKernel polyKernel = new PolyKernel();
                                        polyKernel.setExponent(2);
                                        classifSMOView1.setKernel(polyKernel);
                                        classifSMOView2.setKernel(polyKernel);
                                        classifSMOView1.setC(cValue);
                                        classifSMOView2.setC(cValue);
                                        classifiers1[rep][fold] = classifSMOView1;
                                        classifiers2[rep][fold] = classifSMOView2;
                                    }
                                }
                                learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar2.toString(), numClasses);
                            }
                        });
                        threads.execute(thread);    
                    }
                }
                if(parametersSMO.isRbfKernel()){
                    for(int c=0;c<parametersSMO.getvalueesC().size();c++){
                        final double cValue = parametersSMO.getvalueC(c);
                        //System.out.println("Classification Algorithm: SMO");
                        //System.out.println("Kernel: " + classifSMOView1.getKernel().toString());
                        //System.out.println("C: " + classifSMOView1.getC());
                        final StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append("RBFKernel_");
                        outputFilePar2.append(parametersSMO.getvalueC(c));
                        outputFilePar2.append("_");
                        final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    for(int fold=0;fold<configuration.getNumFolds();fold++){
                                        SMO classifSMOView1 = new SMO();
                                        SMO classifSMOView2 = new SMO();
                                        classifSMOView1.setKernel(new RBFKernel());
                                        classifSMOView2.setKernel(new RBFKernel());
                                        classifSMOView1.setC(cValue);
                                        classifSMOView2.setC(cValue);
                                        classifiers1[rep][fold] = classifSMOView1;
                                        classifiers2[rep][fold] = classifSMOView2;
                                    }
                                }
                                learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar2.toString(), numClasses);
                            }
                        });
                        threads.execute(thread);
                    }
                }
                if(parametersSMO.isMinKernel()){
                    for(int c=0;c<parametersSMO.getvalueesC().size();c++){
                        final double cValue = parametersSMO.getvalueC(c);
                        //System.out.println("Classification Algorithm: SMO");
                        //System.out.println("Kernel: " + classifSMOView1.getKernel().toString());
                        //System.out.println("C: " + classifSMOView1.getC());
                        final StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append("MinNKernel_");
                        outputFilePar2.append(parametersSMO.getvalueC(c));
                        outputFilePar2.append("_");
                        final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    for(int fold=0;fold<configuration.getNumFolds();fold++){
                                        SMO classifSMOView1 = new SMO();
                                        SMO classifSMOView2 = new SMO();
                                        classifSMOView1.setKernel(new MinNKernelSVM());
                                        classifSMOView2.setKernel(new MinNKernelSVM());
                                        classifSMOView1.setC(cValue);
                                        classifSMOView2.setC(cValue);
                                        classifiers1[rep][fold] = classifSMOView1;
                                        classifiers2[rep][fold] = classifSMOView2;
                                    }
                                }
                                learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar2.toString(), numClasses);
                            }
                        });
                        threads.execute(thread);
                    }
                }
            }
            
            if(configuration.isKNN()){
                final Parameters_KNN parametersKNN = configuration.getParametersKNN();
                final StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(outputFile.toString());
                outputFilePar.append("KNN_");
                for(int viz=0;viz<parametersKNN.getNeighbors().size();viz++){
                    final int numViz = parametersKNN.getNeighbor(viz);
                    //System.out.println("Classification Algorithm: KNN");
                    //System.out.println("Number of Neighbors: " + parametersKNN.getNeighbor(viz));
                    StringBuilder outputFilePar2 = new StringBuilder();
                    outputFilePar2.append(outputFilePar.toString());
                    outputFilePar2.append("_");        
                    outputFilePar2.append(parametersKNN.getNeighbor(viz));
                    outputFilePar2.append("_");        
                    if(parametersKNN.getCosine()){
                        final StringBuilder outputFilePar3 = new StringBuilder();
                        outputFilePar3.append(outputFilePar2.toString());
                        outputFilePar3.append("Cosine_");
                        if(parametersKNN.isWeighted()){
                            final StringBuilder outputFilePar4 = new StringBuilder();
                            outputFilePar4.append(outputFilePar3.toString());
                            outputFilePar4.append("W_");
                            final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int rep=0;rep<configuration.getNumReps();rep++){
                                        for(int fold=0;fold<configuration.getNumFolds();fold++){
                                            KNN_InductiveSupervised classifKnnView1 = new KNN_InductiveSupervised();
                                            KNN_InductiveSupervised classifKnnView2 = new KNN_InductiveSupervised();
                                            classifKnnView1.setK(numViz);
                                            classifKnnView2.setK(numViz);
                                            classifKnnView1.setWeightedVote(true);
                                            classifKnnView2.setWeightedVote(true);
                                            classifiers1[rep][fold] = classifKnnView1;
                                            classifiers2[rep][fold] = classifKnnView2;
                                        }
                                    }
                                    learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar4.toString(), numClasses);
                                }
                            });
                            threads.execute(thread);
                        }
                        if(parametersKNN.isUnweighted()){
                            final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int rep=0;rep<configuration.getNumReps();rep++){
                                        for(int fold=0;fold<configuration.getNumFolds();fold++){
                                            KNN_InductiveSupervised classifKnnView1 = new KNN_InductiveSupervised();
                                            KNN_InductiveSupervised classifKnnView2 = new KNN_InductiveSupervised();
                                            classifKnnView1.setK(numViz);
                                            classifKnnView2.setK(numViz);
                                            classifKnnView1.setWeightedVote(false);
                                            classifKnnView2.setWeightedVote(false);
                                            classifiers1[rep][fold] = classifKnnView1;
                                            classifiers2[rep][fold] = classifKnnView2;
                                        }
                                    }
                                    learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar3.toString(), numClasses);
                                }
                            });
                            threads.execute(thread);
                        }
                    }
                    if(parametersKNN.getEuclidean()){
                        final StringBuilder outputFilePar3 = new StringBuilder();
                        outputFilePar3.append(outputFilePar2.toString());
                        outputFilePar3.append("Euclidean_");
                        
                        if(parametersKNN.isWeighted()){
                            final StringBuilder outputFilePar4 = new StringBuilder();
                            outputFilePar4.append(outputFilePar3.toString());
                            outputFilePar4.append("W_");
                            final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int rep=0;rep<configuration.getNumReps();rep++){
                                        for(int fold=0;fold<configuration.getNumFolds();fold++){
                                            IBk classifKnnView1 = new IBk();
                                            IBk classifKnnView2 = new IBk();
                                            EuclideanDistance df1 = new EuclideanDistance();
                                            EuclideanDistance df2 = new EuclideanDistance();
                                            SelectedTag dist1 = new SelectedTag(IBk.WEIGHT_INVERSE, IBk.TAGS_WEIGHTING);
                                            SelectedTag dist2 = new SelectedTag(IBk.WEIGHT_INVERSE, IBk.TAGS_WEIGHTING);
                                            try{
                                                classifKnnView1.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df1);
                                                classifKnnView2.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df2);
                                            }catch(Exception e){
                                                System.err.println("Error when generating a classifier.");
                                                configuration.getEmail().getContent().append(e.getMessage());
                                                configuration.getEmail().getContent().append(configuration.toString());
                                                configuration.getEmail().send();
                                                e.printStackTrace();
                                                System.exit(0);
                                            }
                                            classifKnnView1.setKNN(numViz);
                                            classifKnnView2.setKNN(numViz);
                                            classifKnnView1.setDistanceWeighting(dist1);
                                            classifKnnView2.setDistanceWeighting(dist2);
                                            classifiers1[rep][fold] = classifKnnView1;
                                            classifiers2[rep][fold] = classifKnnView2;
                                        }
                                    }
                                    learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar4.toString(), numClasses);
                                }
                            });
                            threads.execute(thread);    
                        }
                        if(parametersKNN.isUnweighted()){
                            final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int rep=0;rep<configuration.getNumReps();rep++){
                                        for(int fold=0;fold<configuration.getNumFolds();fold++){
                                            IBk classifKnnView1 = new IBk();
                                            IBk classifKnnView2 = new IBk();
                                            //KNN_Euclidean_InductiveSupervised classifKnnView1 = new KNN_Euclidean_InductiveSupervised();
                                            //KNN_Euclidean_InductiveSupervised classifKnnView2 = new KNN_Euclidean_InductiveSupervised();
                                            final EuclideanDistance df1 = new EuclideanDistance();
                                            final EuclideanDistance df2 = new EuclideanDistance();
                                            final SelectedTag dist1 = new SelectedTag(IBk.WEIGHT_NONE, IBk.TAGS_WEIGHTING);
                                            final SelectedTag dist2 = new SelectedTag(IBk.WEIGHT_NONE, IBk.TAGS_WEIGHTING);
                                            try{
                                                classifKnnView1.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df1);
                                                classifKnnView2.getNearestNeighbourSearchAlgorithm().setDistanceFunction(df2);
                                            }catch(Exception e){
                                                System.err.println("Error when generating a classifier.");
                                                configuration.getEmail().getContent().append(e.getMessage());
                                                configuration.getEmail().getContent().append(configuration.toString());
                                                configuration.getEmail().send();
                                                e.printStackTrace();
                                                System.exit(0);
                                            }
                                            //classifKnnView1.setK(numViz);
                                            //classifKnnView2.setK(numViz);
                                            classifKnnView1.setKNN(numViz);
                                            classifKnnView2.setKNN(numViz);
                                            classifKnnView1.setDistanceWeighting(dist1);
                                            classifKnnView2.setDistanceWeighting(dist2);
                                            classifiers1[rep][fold] = classifKnnView1;
                                            classifiers2[rep][fold] = classifKnnView2;
                                        }
                                    }
                                    learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar3.toString(), numClasses);
                                }
                            });
                            threads.execute(thread);    
                        }
                    }
                }
            }
            
            if(configuration.isIMBHN()){
                final StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(outputFile.toString());
                outputFilePar.append("IMBHN_");
                final Parameters_IMHN parametersIMBHN = configuration.getParametersIMBHN();
                for(int error=0;error<parametersIMBHN.getErrors().size();error++){
                    final double errorValue = parametersIMBHN.getError(error);
                    for(int apr=0;apr<parametersIMBHN.getErrorCorrectionRates().size();apr++){
                        final double aprValue = parametersIMBHN.getErrorCorrectionRate(apr);
                        //System.out.println("Classification Algorithm: IMBHN");
                        //System.err.println("Minimum Mean Squared Error: " + parametersIMBHN.getError(error));
                        //System.out.println("Error Correction Rate: " + parametersIMBHN.getErrorCorrectionRate(apr));
                        final StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append("_");        
                        outputFilePar2.append(parametersIMBHN.getErrorCorrectionRate(apr));
                        outputFilePar2.append("_");
                        outputFilePar2.append(parametersIMBHN.getError(error));
                        outputFilePar2.append("_");
                        final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    for(int fold=0;fold<configuration.getNumFolds();fold++){
                                        IMBHN_C_InductiveSupervised classifIMBHNView1 = new IMBHN_C_InductiveSupervised();
                                        IMBHN_C_InductiveSupervised classifIMBHNView2 = new IMBHN_C_InductiveSupervised();
                                        classifIMBHNView1.setErrorCorrectionRate(aprValue);
                                        classifIMBHNView2.setErrorCorrectionRate(aprValue);
                                        classifIMBHNView1.setMinError(errorValue);
                                        classifIMBHNView2.setMinError(errorValue);
                                        classifIMBHNView1.setMaxNumIterations(parametersIMBHN.getMaxNumberIterations());
                                        classifIMBHNView2.setMaxNumIterations(parametersIMBHN.getMaxNumberIterations());
                                        classifiers1[rep][fold] = classifIMBHNView1;
                                        classifiers2[rep][fold] = classifIMBHNView2;
                                    }
                                }
                                learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar2.toString(), numClasses);    
                            }
                        });
                        threads.execute(thread);
                    }    
                }
            }
            if(configuration.isIMBHN2()){
                final StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(outputFile.toString());
                outputFilePar.append("IMBHN2_");
                final Parameters_IMHN parametersIMBHN2 = configuration.getParametersIMBHN2();
                for(int error=0;error<parametersIMBHN2.getErrors().size();error++){
                    final double errorValue = parametersIMBHN2.getError(error);
                    for(int apr=0;apr<parametersIMBHN2.getErrorCorrectionRates().size();apr++){
                        final double aprValue = parametersIMBHN2.getErrorCorrectionRate(apr);
                        //System.out.println("Classification Algorithm: IMBHN2");
                        //System.err.println("Minimum Mean Squared Error: " + parametersIMBHN2.getError(error));
                        //System.out.println("Error Correction Rate: " + parametersIMBHN2.getErrorCorrectionRate(apr));
                        final StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString());
                        outputFilePar2.append("_");        
                        outputFilePar2.append(parametersIMBHN2.getErrorCorrectionRate(apr));
                        outputFilePar2.append("_");
                        outputFilePar2.append(parametersIMBHN2.getError(error));
                        outputFilePar2.append("_");
                        final Classifier[][] classifiers1 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        final Classifier[][] classifiers2 = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(int rep=0;rep<configuration.getNumReps();rep++){
                                    for(int fold=0;fold<configuration.getNumFolds();fold++){
                                        IMBHN_R_InductiveSupervised classifIMBHN2View1 = new IMBHN_R_InductiveSupervised();
                                        IMBHN_R_InductiveSupervised classifIMBHN2View2 = new IMBHN_R_InductiveSupervised();
                                        classifIMBHN2View1.setErrorCorrectionRate(aprValue);
                                        classifIMBHN2View2.setErrorCorrectionRate(aprValue);
                                        classifIMBHN2View1.setMinError(errorValue);
                                        classifIMBHN2View2.setMinError(errorValue);
                                        classifIMBHN2View1.setMaxNumIterations(parametersIMBHN2.getMaxNumberIterations());
                                        classifIMBHN2View2.setMaxNumIterations(parametersIMBHN2.getMaxNumberIterations());
                                        classifiers1[rep][fold] = classifIMBHN2View1;
                                        classifiers2[rep][fold] = classifIMBHN2View2;
                                    }
                                }
                                learning(configuration, threads, classifiers1, classifiers2, dataOriginalView1, dataOriginalView2, outputFilePar2.toString(), numClasses);    
                            }
                        });
                        threads.execute(thread);
                    }    
                }
            }
            
            threads.shutdown();
            
            boolean exit = false;
            while(exit == false){
                if(threads.isTerminated()){
                    System.out.println("Process concluded successfully");
                    configuration.getEmail().getContent().append(configuration.toString());
                   configuration.getEmail().send();
                    exit = true;
                }else{
                    Thread.sleep(1000);
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

    //Function to run and evaluate an ensemble of classifiers
    public static void learning(final SupervisedInductiveConfiguration_EnsembleMultiview configuration, final ExecutorService threads, final Classifier[][] classifiers1, final Classifier[][] classifiers2, final Instances dataOriginalView1, final Instances dataOriginalView2, String outputFile, final int numClasses){
        
        String comp_output = "";
        if(configuration.isHighestConfidence()){
            comp_output = "HighestConficence";
        }else if(configuration.isSumOfConfidences()){
            comp_output = "SumOfConfidences";
        }else{
            comp_output = "SumOfWeightedConfidences";
        }
        
        try{
            
            for(int conf=0;conf<configuration.getConfiancas().size();conf++){
                
                final double confidence = configuration.getConfianca(conf);
                
                final Results results;
            
                final File output = new File(outputFile + "ENSEMBLE_" + comp_output + "_" + confidence + "_" + configuration.getNumReps() + "_" + configuration.getNumFolds());
                final File outputResult = new File(output.getAbsolutePath() + ".txt");
                if(outputResult.exists()){
                    continue;
                }
                final File outputTemp = new File(output.getAbsolutePath() + ".tmp");

                if(outputTemp.exists()){
                    ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(output.getAbsolutePath() + ".tmp"));
                    results = (Results)objInput.readObject();
                    objInput.close();
                }else{
                    results = new Results(output, configuration.getNumReps(), configuration.getNumFolds(), "InductiveSupervised_MultiView");
                }
                
               
                    for(int rep=0;rep<configuration.getNumReps();rep++){

                        final int numRep = rep;

                        final Instances dataView1 = new Instances(dataOriginalView1);
                        final Instances dataView2 = new Instances(dataOriginalView2);

                        dataView1.setClass(dataOriginalView1.attribute(dataOriginalView1.numAttributes()-1));
                        dataView2.setClass(dataOriginalView2.attribute(dataOriginalView2.numAttributes()-1));

                        dataView1.randomize(new Random(numRep));
                        dataView2.randomize(new Random(numRep));

                        for(int fold=0; fold<configuration.getNumFolds();fold++){

                            if(results.getComplete(rep, fold) == true){
                                continue;
                            }

                            final int numFold = fold;
                                
                            Integer[][] confusionMatrix = new Integer[numClasses][numClasses]; //matriz de confusão
                            for(int class1=0;class1<numClasses;class1++){//inicializando a matriz
                                for(int class2=0;class2<numClasses;class2++){
                                    confusionMatrix[class1][class2] = 0;
                                }
                            }


                            final Instances dataTrainView1 = dataView1.trainCV(configuration.getNumFolds(), numFold);
                            final Instances dataTestView1 = dataView1.testCV(configuration.getNumFolds(),numFold);

                            final Instances dataTrainView2 = dataView2.trainCV(configuration.getNumFolds(), numFold);
                            final Instances dataTestView2 = dataView2.testCV(configuration.getNumFolds(), numFold);

                            long begin = System.currentTimeMillis();
                            try{
                                classifiers1[numRep][numFold].buildClassifier(dataTrainView1);
                                classifiers2[numRep][numFold].buildClassifier(dataTrainView2);
                                long end  = System.currentTimeMillis();
                                results.setBuildingTime(numRep, numFold, end - begin);
                                begin = System.currentTimeMillis();

                                if(configuration.isSumOfConfidences()){
                                    Evaluations.SupervisedInductiveEvaluationEnsembleMultiview(classifiers1[numRep][numFold], classifiers2[numRep][numFold], dataTestView1, dataTestView2, confusionMatrix, confidence);
                                }else if(configuration.isSumOfWeightedConfidences()){
                                    Evaluations.SupervisedInductiveEvaluationEnsembleMultiview2(classifiers1[numRep][numFold], classifiers2[numRep][numFold], dataTrainView1, dataTestView1, dataTrainView2, dataTestView2, confusionMatrix, confidence);
                                }else if(configuration.isHighestConfidence()){
                                    Evaluations.SupervisedInductiveEvaluationEnsembleMultiview3(classifiers1[numRep][numFold], classifiers2[numRep][numFold], dataTestView1, dataTestView2, confusionMatrix, confidence);
                                }
                                end = System.currentTimeMillis();
                                results.setClassificationTime(numRep, numFold, end - begin);

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

                            results.computeEvaluationMeasures(confusionMatrix, numClasses, numRep, numFold);
                            results.setComplete(numRep, numFold, true);

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

