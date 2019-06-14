//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Transductive Learning and Evaluations.
//              The algoritms are based on networks composed by document-term
//              and term-term relations.
//*****************************************************************************

package TCT;

import TCTNetworkGeneration.TermNetworkGeneration;
import TCTParameters.Parameters_GNetMine_DocTerm_TermTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_LPHN;
import TCTParameters.Parameters_TermNetwork;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTAlgorithms.Transductive.GNetMine_DocTerm_TermTerm_Transductive_ID;
import TCTAlgorithms.Transductive.LPHN_DocTerm_TermTerm_Transductive_ID;
import TCTAlgorithms.Transductive.TCHN_DocTerm_TermTerm_Transductive_ID;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_DocTermAndTermTermRelations;
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

/*Function to read the data (document-term matrix and term probabilities) and build relations among terms. */
public class TransductiveClassification_DocTerm_TermTerm {
        
    public static void learning(TransductiveConfiguration_DocTermAndTermTermRelations configuration){
        
        File fileArff = new File(configuration.getArff());
        File fileProb = new File(configuration.getArqProb());
        File dirSaida = new File(configuration.getDirSaida());
        
        int numTerms = 0;
        
        if(!fileArff.getAbsolutePath().endsWith(".arff")){
            System.out.println("Invalid ARF file");
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
        try{
            ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(fileArff.getAbsolutePath().toString()); //Carregando arquivo de Dados
            dataOriginal = trainSource.getDataSet();

            Attribute classAtt = null;
            numTerms = dataOriginal.numAttributes()-1;
            classAtt = dataOriginal.attribute(numTerms); //Setting the last feature as class
            dataOriginal.setClass(classAtt);
            numClasses = classAtt.numValues();

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
        outputFile.append("_Transductive_DTandTT_");
        
        try{
            
        
            //Gerando as networks de Termos
            if(configuration.isSupportNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("Support");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersSupportNetwork = configuration.getParametersSupportNetwork();
                if(parametersSupportNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersSupportNetwork.getThresholds().size();lim++){
                        final double threshold = parametersSupportNetwork.getThreshold(lim);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateSupportNetworkThreshold(fileProb,numTerms,threshold,parametersSupportNetwork.getRelative());
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" + threshold + "_",dataOriginal,numClasses);
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
                if(parametersSupportNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersSupportNetwork.getKs().size();topK++){
                        final int k = parametersSupportNetwork.getK(topK);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateSupportNetworkTopK(fileProb,numTerms,k);
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
            if(configuration.isMutualInformationNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("MutualInformation");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersMutualInformationNetwork = configuration.getParametersMutualInformationNetwork();
                if(parametersMutualInformationNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersMutualInformationNetwork.getThresholds().size();lim++){
                        final double threshold = parametersMutualInformationNetwork.getThreshold(lim);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateMutualInformationNetworkThreshold(fileProb,numTerms,threshold,parametersMutualInformationNetwork.getRelative());
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" +threshold + "_",dataOriginal,numClasses);
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
                if(parametersMutualInformationNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersMutualInformationNetwork.getKs().size();topK++){
                        final int k = parametersMutualInformationNetwork.getK(topK);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateMutualInformationNetworkTopK(fileProb,numTerms,k);
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
            if(configuration.isKappaNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("Kappa");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersKappaNetwork = configuration.getParametersKappaNetwork();
                if(parametersKappaNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersKappaNetwork.getThresholds().size();lim++){
                        final double threshold = parametersKappaNetwork.getThreshold(lim);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateKappaNetworkThreshold(fileProb,numTerms,threshold,parametersKappaNetwork.getRelative());
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" +threshold + "_",dataOriginal,numClasses);
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
                if(parametersKappaNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersKappaNetwork.getKs().size();topK++){
                        final int k = parametersKappaNetwork.getK(topK);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateKappaNetworkTopK(fileProb,numTerms,k);
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
            if(configuration.isShapiroNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("Shapiro");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersShapiroNetwork = configuration.getParametersShapiroNetwork();
                if(parametersShapiroNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersShapiroNetwork.getThresholds().size();lim++){
                        final double threshold = parametersShapiroNetwork.getThreshold(lim);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateShapiroNetworkThreshold(fileProb,numTerms,threshold,parametersShapiroNetwork.getRelative());
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" + threshold + "_",dataOriginal,numClasses);
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
                if(parametersShapiroNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersShapiroNetwork.getKs().size();topK++){
                        final int k = parametersShapiroNetwork.getK(topK);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateShapiroNetworkTopK(fileProb,numTerms,k);
                        learning(configuration,threads, adjacencyListTerms, outputFile.toString(), outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
            if(configuration.isYulesQNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("Yule's Q");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersYulesQNetwork = configuration.getParametersYulesQNetwork();
                if(parametersYulesQNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersYulesQNetwork.getThresholds().size();lim++){
                        final double threshold = parametersYulesQNetwork.getThreshold(lim);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateYulesQNetworkThreshold(fileProb,numTerms,threshold,parametersYulesQNetwork.getRelative());
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" + threshold + "_",dataOriginal,numClasses);
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
                if(parametersYulesQNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersYulesQNetwork.getKs().size();topK++){
                        final int k = parametersYulesQNetwork.getK(topK);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateYulesQNetworkTopK(fileProb,numTerms,k);
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
    public static void learning(TransductiveConfiguration_DocTermAndTermTermRelations configuration, ExecutorService threads, Neighbor[] adjacencyListTerms, String output, String output2, Instances dataOriginal,int numClasses){
        
        
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
                        //System.out.println("Classification Algorithm: TCHN DDeTT");
                        //System.err.println("Error Mínimo: " + parametersIMHN.getError(error));
                        //System.out.println("Learning Rate: " + parametersIMHN.getErrorCorrectionRate(apr));
                        StringBuilder outputFilePar3 = new StringBuilder();
                        outputFilePar3.append(parametersIMHN.getErrorCorrectionRate(apr));
                        outputFilePar3.append("_");
                        outputFilePar3.append(parametersIMHN.getError(error));
                        outputFilePar3.append("_");
                        TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            TCHN_DocTerm_TermTerm_Transductive_ID classifIMHN = new TCHN_DocTerm_TermTerm_Transductive_ID();
                            classifIMHN.setAdjacencyListTerms(adjacencyListTerms);
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
                StringBuilder outputFilePar2 = new StringBuilder();
                outputFilePar2.append(outputFilePar);
                outputFilePar2.append("LPHN_");
                Parameters_LPHN parametersLPHN = configuration.getParameters_LPHN();
                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                //System.out.println("Classification Algorithm: LPHNDDeTT");
                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                for(int rep=0;rep<configuration.getNumReps();rep++){
                    LPHN_DocTerm_TermTerm_Transductive_ID classifLPHN = new LPHN_DocTerm_TermTerm_Transductive_ID();
                    classifLPHN.setUse(0);
                    classifLPHN.setAdjacencyListTerms(adjacencyListTerms);
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
                        //System.out.println("Classification Algorithm: GNetMine");
                        //System.out.println("Alpha Doc: " + parametersGNetMine.getAlphaDoc(alpha));
                        //System.out.println("Lambda DocTermo: " + parametersGNetMine.getLambdaDocTermo(lambda));
                        StringBuilder outputFilePar3 = new StringBuilder();
                        outputFilePar3.append("_");
                        outputFilePar3.append(parametersGNetMine.getAlphaDoc(alpha));
                        outputFilePar3.append("_");
                        outputFilePar3.append(parametersGNetMine.getLambdaDocTermo(lambda));
                        TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            GNetMine_DocTerm_TermTerm_Transductive_ID classifGNetMine = new GNetMine_DocTerm_TermTerm_Transductive_ID();
                            classifGNetMine.setUse(0);
                            classifGNetMine.setMaxNumIterations(parametersGNetMine.getMaxNumberIterations());
                            classifGNetMine.setAlphaDoc(parametersGNetMine.getAlphaDoc(alpha));
                            classifGNetMine.setLambdaDocTermo(parametersGNetMine.getLambdaDocTermo(lambda));
                            classifGNetMine.setAdjacencyListTerms(adjacencyListTerms);
                            classifiers[rep] = classifGNetMine;
                        }
                        learning(configuration, threads, classifiers, dataOriginal, output + outputFilePar2.toString() + output2 + outputFilePar3.toString(), numLabeledInstances, numClasses);    
                    }
                    
                }    
            }
        }
    }
    
    //Function to run and evaluate transductive learning
    public static void learning(final TransductiveConfiguration_DocTermAndTermTermRelations configuration, final ExecutorService threads, final TransductiveLearner[] classifiers, final Instances dataOriginal, final String outputFile, final double numInstPerClass, final int numClasses){
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
